package io.everyonecodes.anber.searchmanagement.service;

import io.everyonecodes.anber.DatabaseInitializer;
import io.everyonecodes.anber.providermanagement.data.ContractType;
import io.everyonecodes.anber.providermanagement.data.PriceModelType;
import io.everyonecodes.anber.providermanagement.data.ProviderType;
import io.everyonecodes.anber.ratingmanagement.data.Rating;
import io.everyonecodes.anber.searchmanagement.data.Provider;
import io.everyonecodes.anber.searchmanagement.data.ProviderDTO;
import io.everyonecodes.anber.searchmanagement.repository.ProviderRepository;
import io.everyonecodes.anber.tariffmanagement.data.Tariff;
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
import java.util.Set;
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
    void getAllDtos() {
        Mockito.when(providerRepository.findAll()).thenReturn(List.of(new ProviderDTO()));
        var result = searchService.getAllDtos();
        var expected = List.of(new ProviderDTO());
        Assertions.assertEquals(expected, result);
        Mockito.verify(providerRepository).findAll();
    }


    @Test
    void getAllProviders() {

        ProviderDTO dto = new ProviderDTO(1L, "united kingdom", ProviderType.GAS, "providerName", List.of( new Tariff("tariffName", 0.5, ContractType.SIX_MONTHS, PriceModelType.FIXED)), "website.com", "test@email.com", "0123 456789", false, new Rating(Set.of(), "4.5"));

        Mockito.when(providerRepository.findAll()).thenReturn(List.of(dto));
        Mockito.when(translator.DtoToProvider(dto)).thenReturn(new Provider(dto.getProviderName(), dto.getRating().getScore(), dto.getTariffs().get(0).getTariffName(), dto.getTariffs().get(0).getBasicRate(), dto.getTariffs().get(0).getContractType(), dto.getTariffs().get(0).getPriceModel()));

        var result = searchService.getAllProviders();
        var expected = List.of(new Provider(dto.getProviderName(), dto.getRating().getScore(), dto.getTariffs().get(0).getTariffName(), dto.getTariffs().get(0).getBasicRate(), dto.getTariffs().get(0).getContractType(), dto.getTariffs().get(0).getPriceModel()));
        Assertions.assertEquals(expected, result);
        Mockito.verify(providerRepository).findAll();
        Mockito.verify(translator).DtoToProvider(dto);
    }


    @ParameterizedTest
    @MethodSource("parametersC")
    void manageFiltersCorrect(String input, List<Provider> expected) {

        ProviderDTO dto = new ProviderDTO(1L, "united kingdom", ProviderType.GAS, "providerName", List.of( new Tariff("tariffName", 0.5, ContractType.SIX_MONTHS, PriceModelType.FIXED)), "website.com", "test@email.com", "0123 456789", false, new Rating(Set.of(), "4.5"));

        ProviderDTO dto1 = new ProviderDTO(2L, "united kingdom", ProviderType.INTERNET, "providerName1", List.of( new Tariff("tariffName6", 0.4, ContractType.SIX_MONTHS, PriceModelType.FIXED)), "website.com", "test@email.com", "0123 456789", false,new Rating(Set.of(), "4.5"));
        ProviderDTO dto2 = new ProviderDTO(3L, "united kingdom", ProviderType.INTERNET, "providerName2", List.of( new Tariff("tariffName5", 0.7, ContractType.SIX_MONTHS, PriceModelType.PER_CONSUMPTION)), "website.com", "test@email.com", "0123 456789", false, new Rating(Set.of(), "4.5"));
        ProviderDTO dto3 = new ProviderDTO(4L, "united kingdom", ProviderType.GAS, "providerName3", List.of( new Tariff("tariffName4", 0.3, ContractType.SIX_MONTHS, PriceModelType.FIXED)), "website.com", "test@email.com", "0123 456789", false, new Rating(Set.of(), "4.5"));
        ProviderDTO dto4 = new ProviderDTO(5L, "united kingdom", ProviderType.ELECTRICITY, "providerName4", List.of( new Tariff("tariffName3", 0.2, ContractType.SIX_MONTHS, PriceModelType.PER_CONSUMPTION)), "website.com", "test@email.com", "0123 456789", false, new Rating(Set.of(), "4.5"));
        ProviderDTO dto5 = new ProviderDTO(6L, "united kingdom", ProviderType.ELECTRICITY, "providerName5", List.of( new Tariff("tariffName2", 0.1, ContractType.SIX_MONTHS, PriceModelType.FIXED)), "website.com", "test@email.com", "0123 456789", false, new Rating(Set.of(), "4.5"));
        ProviderDTO dto6 = new ProviderDTO(7L, "austria", ProviderType.ELECTRICITY, "providerName6", List.of( new Tariff("tariffName1", 0.5, ContractType.SIX_MONTHS, PriceModelType.PER_CONSUMPTION)), "website.com", "test@email.com", "0123 456789", false, new Rating(Set.of(), "4.5"));

        Provider provider = new Provider(dto.getProviderName(), dto.getRating().getScore(), dto.getTariffs().get(0).getTariffName(), dto.getTariffs().get(0).getBasicRate(), dto.getTariffs().get(0).getContractType(), dto.getTariffs().get(0).getPriceModel());

        Provider provider1 = new Provider(dto1.getProviderName(), dto1.getRating().getScore(), dto1.getTariffs().get(0).getTariffName(), dto1.getTariffs().get(0).getBasicRate(), dto1.getTariffs().get(0).getContractType(), dto1.getTariffs().get(0).getPriceModel());
        Provider provider2 = new Provider(dto2.getProviderName(), dto2.getRating().getScore(), dto2.getTariffs().get(0).getTariffName(), dto2.getTariffs().get(0).getBasicRate(), dto2.getTariffs().get(0).getContractType(), dto2.getTariffs().get(0).getPriceModel());
        Provider provider3 = new Provider(dto3.getProviderName(), dto3.getRating().getScore(), dto3.getTariffs().get(0).getTariffName(), dto3.getTariffs().get(0).getBasicRate(), dto3.getTariffs().get(0).getContractType(), dto3.getTariffs().get(0).getPriceModel());
        Provider provider4 = new Provider(dto4.getProviderName(), dto4.getRating().getScore(), dto4.getTariffs().get(0).getTariffName(), dto4.getTariffs().get(0).getBasicRate(), dto4.getTariffs().get(0).getContractType(), dto4.getTariffs().get(0).getPriceModel());
        Provider provider5 = new Provider(dto5.getProviderName(), dto5.getRating().getScore(), dto5.getTariffs().get(0).getTariffName(), dto5.getTariffs().get(0).getBasicRate(), dto5.getTariffs().get(0).getContractType(), dto5.getTariffs().get(0).getPriceModel());
        Provider provider6 = new Provider(dto6.getProviderName(), dto6.getRating().getScore(), dto6.getTariffs().get(0).getTariffName(), dto6.getTariffs().get(0).getBasicRate(), dto6.getTariffs().get(0).getContractType(), dto6.getTariffs().get(0).getPriceModel());

        Mockito.when(providerRepository.findAll()).thenReturn(List.of(dto, dto1, dto2, dto3, dto4, dto5, dto6));

        Mockito.when(providerRepository.findByCountryName(dto.getCountryName())).thenReturn(List.of(dto, dto1, dto2, dto3, dto4, dto5, dto6));
        Mockito.when(providerRepository.findByProviderType(dto.getProviderType())).thenReturn(List.of(dto, dto3));
        Mockito.when(providerRepository.findByProviderName(dto.getProviderName())).thenReturn(List.of(dto));

        Mockito.when(translator.DtoToProvider(dto)).thenReturn(provider);
        Mockito.when(translator.DtoToProvider(dto1)).thenReturn(provider1);
        Mockito.when(translator.DtoToProvider(dto2)).thenReturn(provider2);
        Mockito.when(translator.DtoToProvider(dto3)).thenReturn(provider3);
        Mockito.when(translator.DtoToProvider(dto4)).thenReturn(provider4);
        Mockito.when(translator.DtoToProvider(dto5)).thenReturn(provider5);
        Mockito.when(translator.DtoToProvider(dto6)).thenReturn(provider6);

        var result = searchService.manageFilters(input);
        Assertions.assertEquals(expected, result);

        Mockito.verify(providerRepository).findByCountryName(dto.getCountryName());
        Mockito.verify(translator).DtoToProvider(dto);

    }
    private static Stream<Arguments> parametersC() {
        ProviderDTO dto = new ProviderDTO(1L, "united kingdom", ProviderType.GAS, "providerName", List.of(new Tariff("tariffName", 0.5, ContractType.SIX_MONTHS, PriceModelType.FIXED)), "website.com", "test@email.com", "0123 456789", false, new Rating(Set.of(), "4.5"));

        ProviderDTO dto3 = new ProviderDTO(4L, "united kingdom", ProviderType.GAS, "providerName3", List.of(new Tariff("tariffName4", 0.3, ContractType.SIX_MONTHS, PriceModelType.FIXED)), "website.com", "test@email.com", "0123 456789", false, new Rating(Set.of(), "4.5"));

        Provider provider = new Provider(dto.getProviderName(), dto.getRating().getScore(), dto.getTariffs().get(0).getTariffName(), dto.getTariffs().get(0).getBasicRate(), dto.getTariffs().get(0).getContractType(), dto.getTariffs().get(0).getPriceModel());
        Provider provider3 = new Provider(dto3.getProviderName(), dto3.getRating().getScore(), dto3.getTariffs().get(0).getTariffName(), dto3.getTariffs().get(0).getBasicRate(), dto3.getTariffs().get(0).getContractType(), dto3.getTariffs().get(0).getPriceModel());

        return Stream.of(
                Arguments.of("cn=united_kingdom&pt=gas", List.of(provider, provider3)),
                Arguments.of("cn=united_kingdom&pt=gas&pm=fixed", List.of(provider, provider3)),
                Arguments.of("cn=united_kingdom&pt=gas&pn=providerName", List.of(provider)),
                Arguments.of("cn=united_kingdom&pt=gas&tn=tariffName", List.of(provider)),
                Arguments.of("cn=united_kingdom&pt=gas&br<0.6", List.of(provider, provider3)),
                Arguments.of("pm=fixed&cn=united_kingdom&br<1&pt=gas", List.of(provider, provider3))
        );
    }

    @ParameterizedTest
    @MethodSource("parametersI")
    void manageFiltersIncorrect(String input, List<Provider> expected) {

        ProviderDTO dto = new ProviderDTO(1L, "austria", ProviderType.INTERNET, "providerName", List.of( new Tariff("tariffName", 0.5, ContractType.SIX_MONTHS, PriceModelType.PER_CONSUMPTION)), "website.com", "test@email.com", "0123 456789", false, new Rating(Set.of(), "4.5"));

        Provider provider = new Provider(dto.getProviderName(), dto.getTariffs().get(0).getTariffName(), dto.getTariffs().get(0).getBasicRate(), dto.getTariffs().get(0).getContractType(), dto.getTariffs().get(0).getPriceModel());

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
    void sortByBasicRate() {

        ProviderDTO dto1 = new ProviderDTO(1L, "austria", ProviderType.INTERNET, "providerName", List.of( new Tariff("tariffName", 0.3, ContractType.SIX_MONTHS, PriceModelType.PER_CONSUMPTION)), "website.com", "test@email.com", "0123 456789", false, new Rating(Set.of(), "4.5"));

        ProviderDTO dto2 = new ProviderDTO(1L, "austria", ProviderType.INTERNET, "providerName", List.of( new Tariff("tariffName", 0.5, ContractType.SIX_MONTHS, PriceModelType.PER_CONSUMPTION)), "website.com", "test@email.com", "0123 456789", false, new Rating(Set.of(), "4.5"));

        ProviderDTO dto3 = new ProviderDTO(1L, "austria", ProviderType.INTERNET, "providerName", List.of( new Tariff("tariffName", 0.4, ContractType.SIX_MONTHS, PriceModelType.PER_CONSUMPTION)), "website.com", "test@email.com", "0123 456789", false, new Rating(Set.of(), "4.5"));

        Provider prov1 = new Provider(dto1.getProviderName(), dto1.getRating().getScore(), dto1.getTariffs().get(0).getTariffName(), dto1.getTariffs().get(0).getBasicRate(), dto1.getTariffs().get(0).getContractType(), dto1.getTariffs().get(0).getPriceModel());

        Provider prov2 = new Provider(dto2.getProviderName(), dto2.getRating().getScore(), dto2.getTariffs().get(0).getTariffName(), dto2.getTariffs().get(0).getBasicRate(), dto2.getTariffs().get(0).getContractType(), dto2.getTariffs().get(0).getPriceModel());

        Provider prov3 = new Provider(dto3.getProviderName(), dto3.getRating().getScore(), dto3.getTariffs().get(0).getTariffName(), dto3.getTariffs().get(0).getBasicRate(), dto3.getTariffs().get(0).getContractType(), dto3.getTariffs().get(0).getPriceModel());

        var providersAsc = new ArrayList<>(List.of(prov1, prov3, prov2));
        var providersDesc = new ArrayList<>(List.of(prov2, prov3, prov1));


        Mockito.when(providerRepository.findByCountryName(dto1.getCountryName())).thenReturn(List.of(dto1, dto2, dto3));

        Mockito.when(providerRepository.findByProviderType(dto1.getProviderType())).thenReturn(List.of(dto1, dto2, dto3));
        Mockito.when(providerRepository.findByProviderName(dto1.getProviderName())).thenReturn(List.of(dto1, dto2, dto3));

        Mockito.when(translator.DtoToProvider(dto1)).thenReturn(prov1);
        Mockito.when(translator.DtoToProvider(dto2)).thenReturn(prov2);
        Mockito.when(translator.DtoToProvider(dto3)).thenReturn(prov3);



        var resultAsc = searchService.sortByBasicRate("asc", "cn=austria&pt=internet");
        Assertions.assertEquals(providersAsc, resultAsc);

        var resultDesc = searchService.sortByBasicRate("desc", "cn=austria&pt=internet");
        Assertions.assertEquals(providersDesc, resultDesc);

        Mockito.verify(providerRepository, Mockito.times(2)).findByCountryName(dto1.getCountryName());
        Mockito.verify(translator, Mockito.times(2)).DtoToProvider(dto1);
    }




    @Test
    void sortByRating() {

        ProviderDTO dto1 = new ProviderDTO(1L, "austria", ProviderType.INTERNET, "providerName", List.of( new Tariff("tariffName", 0.3, ContractType.SIX_MONTHS, PriceModelType.PER_CONSUMPTION)), "website.com", "test@email.com", "0123 456789", false, new Rating(Set.of(), "2.5"));

        ProviderDTO dto2 = new ProviderDTO(1L, "austria", ProviderType.INTERNET, "providerName", List.of( new Tariff("tariffName", 0.5, ContractType.SIX_MONTHS, PriceModelType.PER_CONSUMPTION)), "website.com", "test@email.com", "0123 456789", false, new Rating(Set.of(), "4.5"));

        ProviderDTO dto3 = new ProviderDTO(1L, "austria", ProviderType.INTERNET, "providerName", List.of( new Tariff("tariffName", 0.4, ContractType.SIX_MONTHS, PriceModelType.PER_CONSUMPTION)), "website.com", "test@email.com", "0123 456789", false, new Rating(Set.of(), "3.5"));

        Provider prov1 = new Provider(dto1.getProviderName(), dto1.getRating().getScore(), dto1.getTariffs().get(0).getTariffName(), dto1.getTariffs().get(0).getBasicRate(), dto1.getTariffs().get(0).getContractType(), dto1.getTariffs().get(0).getPriceModel());

        Provider prov2 = new Provider(dto2.getProviderName(), dto2.getRating().getScore(), dto2.getTariffs().get(0).getTariffName(), dto2.getTariffs().get(0).getBasicRate(), dto2.getTariffs().get(0).getContractType(), dto2.getTariffs().get(0).getPriceModel());

        Provider prov3 = new Provider(dto3.getProviderName(), dto3.getRating().getScore(), dto3.getTariffs().get(0).getTariffName(), dto3.getTariffs().get(0).getBasicRate(), dto3.getTariffs().get(0).getContractType(), dto3.getTariffs().get(0).getPriceModel());

        var providersAsc = new ArrayList<>(List.of(prov1, prov3, prov2));
        var providersDesc = new ArrayList<>(List.of(prov2, prov3, prov1));


        Mockito.when(providerRepository.findByCountryName(dto1.getCountryName())).thenReturn(List.of(dto1, dto2, dto3));

        Mockito.when(providerRepository.findByProviderType(dto1.getProviderType())).thenReturn(List.of(dto1, dto2, dto3));
        Mockito.when(providerRepository.findByProviderName(dto1.getProviderName())).thenReturn(List.of(dto1, dto2, dto3));

        Mockito.when(translator.DtoToProvider(dto1)).thenReturn(prov1);
        Mockito.when(translator.DtoToProvider(dto2)).thenReturn(prov2);
        Mockito.when(translator.DtoToProvider(dto3)).thenReturn(prov3);



        var resultAsc = searchService.sortByBasicRate("asc", "cn=austria&pt=internet");
        Assertions.assertEquals(providersAsc, resultAsc);

        var resultDesc = searchService.sortByBasicRate("desc", "cn=austria&pt=internet");
        Assertions.assertEquals(providersDesc, resultDesc);

        Mockito.verify(providerRepository, Mockito.times(2)).findByCountryName(dto1.getCountryName());
        Mockito.verify(translator, Mockito.times(2)).DtoToProvider(dto1);
    }
}