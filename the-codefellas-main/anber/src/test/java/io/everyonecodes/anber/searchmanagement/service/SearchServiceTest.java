package io.everyonecodes.anber.searchmanagement.service;

import io.everyonecodes.anber.DatabaseInitializer;
import io.everyonecodes.anber.searchmanagement.data.*;
import io.everyonecodes.anber.searchmanagement.repository.ProviderRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.web.SecurityFilterChain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class SearchServiceTest {

    @Autowired
    SearchService searchService;

    @MockBean
    ProviderRepository providerRepository;

    @MockBean
    ProviderTranslator translator;

    @MockBean
    SecurityFilterChain securityFilterChain;

    @MockBean
    DatabaseInitializer databaseInitializer;



    @Test
    void getAll() {
        Mockito.when(providerRepository.findAll()).thenReturn(List.of(new ProviderDTO()));
        var result = searchService.getAll();
        var expected = List.of(new ProviderDTO());
        Assertions.assertEquals(expected, result);
        Mockito.verify(providerRepository).findAll();
    }


    @ParameterizedTest
    @MethodSource("parametersC")
    void manageFiltersCorrect(String input, List<Provider> expected) {

        ProviderDTO dto = new ProviderDTO(1L, "austria", ProviderType.GAS, "providerName", "tariffName", 0.5, ContractType.SIX_MONTHS, PriceModelType.FIXED, 4.5);

        Provider provider = new Provider(dto.getProviderName(), dto.getTariffName(), dto.getBasicRate(), dto.getContractType(), dto.getPriceModel());

        Mockito.when(providerRepository.findByCountryName(dto.getCountryName())).thenReturn(List.of(dto));
        Mockito.when(translator.DtoToProvider(dto)).thenReturn(provider);


        var result = searchService.manageFilters(input);
        Assertions.assertEquals(expected, result);

        Mockito.verify(providerRepository).findByCountryName(dto.getCountryName());
        Mockito.verify(translator).DtoToProvider(dto);
    }
    private static Stream<Arguments> parametersC() {
        ProviderDTO dto = new ProviderDTO(1L, "austria", ProviderType.GAS, "providerName", "tariffName", 0.5, ContractType.SIX_MONTHS, PriceModelType.FIXED, 4.5);
        Provider provider = new Provider(dto.getProviderName(), dto.getTariffName(), dto.getBasicRate(), dto.getContractType(), dto.getPriceModel());

        return Stream.of(
                Arguments.of("cn=austria&pt=gas", List.of(provider)),
                Arguments.of("cn=austria&pt=gas&pm=fixed", List.of(provider)),
                Arguments.of("cn=austria&pt=gas&pn=providerName", List.of(provider)),
                Arguments.of("cn=austria&pt=gas&tn=tariffName", List.of(provider)),
                Arguments.of("cn=austria&pt=gas&br<1", List.of(provider)),
                Arguments.of("cn=austria&pt=gas&pm=fixed", List.of(provider)),
                Arguments.of("pm=fixed&cn=austria&br<1&pt=gas", List.of(provider))
        );
    }

    @ParameterizedTest
    @MethodSource("parametersI")
    void manageFiltersIncorrect(String input, List<Provider> expected) {

        ProviderDTO dto = new ProviderDTO(1L, "austria", ProviderType.GAS, "providerName", "tariffName", 0.5, ContractType.SIX_MONTHS, PriceModelType.FIXED, 4.5);

        Provider provider = new Provider(dto.getProviderName(), dto.getTariffName(), dto.getBasicRate(), dto.getContractType(), dto.getPriceModel());

        Mockito.when(providerRepository.findByCountryName(dto.getCountryName())).thenReturn(List.of());
        Mockito.when(translator.DtoToProvider(dto)).thenReturn(provider);


        var result = searchService.manageFilters(input);
        Assertions.assertEquals(expected, result);

        Mockito.verify(providerRepository, Mockito.never()).findByCountryName(dto.getCountryName());
        Mockito.verify(translator, Mockito.never()).DtoToProvider(dto);
    }

    private static Stream<Arguments> parametersI() {
        return Stream.of(
                Arguments.of("", List.of()),
                Arguments.of("cn=austria", List.of()),
                Arguments.of("pt=gas", List.of()),
                Arguments.of("pt=interne", List.of()),
                Arguments.of("pm=fixed", List.of())
        );
    }

    @Test
    void sortByRate() {

        ProviderDTO dto1 = new ProviderDTO(1L, "austria", ProviderType.GAS, "providerName", "tariffName", 0.3, ContractType.SIX_MONTHS, PriceModelType.FIXED, 4.5);

        ProviderDTO dto2 = new ProviderDTO(1L, "austria", ProviderType.GAS, "providerName", "tariffName", 0.5, ContractType.SIX_MONTHS, PriceModelType.FIXED, 4.5);

        ProviderDTO dto3 = new ProviderDTO(1L, "austria", ProviderType.GAS, "providerName", "tariffName", 0.4, ContractType.SIX_MONTHS, PriceModelType.FIXED, 4.5);

        Provider prov1 = new Provider(dto1.getProviderName(), dto1.getTariffName(), dto1.getBasicRate(), dto1.getContractType(), dto1.getPriceModel());

        Provider prov2 = new Provider(dto2.getProviderName(), dto2.getTariffName(), dto2.getBasicRate(), dto2.getContractType(), dto2.getPriceModel());

        Provider prov3 = new Provider(dto3.getProviderName(), dto3.getTariffName(), dto3.getBasicRate(), dto3.getContractType(), dto3.getPriceModel());

        var providersAsc = new ArrayList<>(List.of(prov1, prov3, prov2));
        var providersDesc = new ArrayList<>(List.of(prov2, prov3, prov1));


        Mockito.when(providerRepository.findByCountryName(dto1.getCountryName())).thenReturn(new ArrayList<>(List.of(dto1, dto2, dto3)));

        Mockito.when(translator.DtoToProvider(dto1)).thenReturn(prov1);
        Mockito.when(translator.DtoToProvider(dto2)).thenReturn(prov2);
        Mockito.when(translator.DtoToProvider(dto3)).thenReturn(prov3);



        var resultAsc = searchService.sortByRate("asc", "cn=austria&pt=gas");
        Assertions.assertEquals(providersAsc, resultAsc);

        var resultDesc = searchService.sortByRate("desc", "cn=austria&pt=gas");
        Assertions.assertEquals(providersDesc, resultDesc);

        Mockito.verify(providerRepository, Mockito.times(2)).findByCountryName(dto1.getCountryName());
        Mockito.verify(translator, Mockito.times(2)).DtoToProvider(dto1);
    }
}