package io.everyonecodes.anber.tariffmanagement.service;

import io.everyonecodes.anber.InitializationRunner;
import io.everyonecodes.anber.providermanagement.data.*;
import io.everyonecodes.anber.providermanagement.repository.UnverifiedAccountRepository;
import io.everyonecodes.anber.providermanagement.repository.VerifiedAccountRepository;
import io.everyonecodes.anber.ratingmanagement.data.Rating;
import io.everyonecodes.anber.searchmanagement.data.ProviderDTO;
import io.everyonecodes.anber.searchmanagement.repository.ProviderRepository;
import io.everyonecodes.anber.tariffmanagement.data.Tariff;
import io.everyonecodes.anber.tariffmanagement.repository.TariffRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class TariffServiceTest {

    @Autowired
    TariffService tariffService;

    @MockBean
    TariffRepository tariffRepository;

    @MockBean
    ProviderRepository providerRepository;

    @MockBean
    VerifiedAccountRepository verifiedAccountRepository;

    @MockBean
    UnverifiedAccountRepository unverifiedAccountRepository;

    @Value("${paths.tariff-file}")
    String tariffFilePath;

    @Value("${data.placeholders.id}")
    String placeholderId;

    @MockBean
    SecurityFilterChain securityFilterChain;

    @MockBean
    InitializationRunner initializationRunner;


    private final ProviderDTO dto = new ProviderDTO(1L, "united kingdom", ProviderType.GAS, "providerName", new ArrayList<>(List.of(new Tariff("tariffName", 0.5, ContractType.SIX_MONTHS, PriceModelType.FIXED))), "website.com", "test@email.com", "0123 456789", false, new Rating(Set.of(), "4.5"));

    private int linesInFile(Long id, String message) {
        FileReader fileReader = new FileReader();
        var lines = fileReader.read(tariffFilePath.replace(placeholderId, String.valueOf(id)));

        String part = "are not filled in correctly";
        var errors = StringUtils.countOccurrencesOf(message, part);

        return lines.size() - errors;
    }



    @Test
    void tariffApplier_successUnverified() {
        Long id = dto.getId();

        UnverifiedAccount uProvider = new UnverifiedAccount(dto.getId(), dto.getProviderName(), dto.getWebsite(), false, dto.getTariffs(), dto.getRating());
        Mockito.when(providerRepository.findById(id)).thenReturn(Optional.of(dto));
        Mockito.when(unverifiedAccountRepository.findById(id)).thenReturn(Optional.of(uProvider));


        var result = tariffService.tariffApplier(id);
        var expected = "Tariffs were updated successfully.";

        Assertions.assertEquals(expected, result);

        int minInvocations = linesInFile(id, expected);

        Mockito.verify(unverifiedAccountRepository, Mockito.atLeast(minInvocations)).findById(id);
        Mockito.verify(unverifiedAccountRepository, Mockito.times(minInvocations)).save(uProvider);
        Mockito.verify(tariffRepository, Mockito.times(minInvocations)).save(Mockito.any(Tariff.class));
    }

    @Test
    void tariffApplier_errorUnverified() {
        dto.setId(2L);
        Long id = dto.getId();

        UnverifiedAccount uProvider = new UnverifiedAccount(dto.getId(), dto.getProviderName(), dto.getWebsite(), false, dto.getTariffs(), dto.getRating());
        Mockito.when(providerRepository.findById(id)).thenReturn(Optional.of(dto));
        Mockito.when(unverifiedAccountRepository.findById(id)).thenReturn(Optional.of(uProvider));


        var result = tariffService.tariffApplier(id);
        var expected = errorMessageId2();

        Assertions.assertEquals(expected, result);

        int minInvocations = linesInFile(id, expected);

        Mockito.verify(unverifiedAccountRepository, Mockito.atLeast(minInvocations)).findById(id);
        Mockito.verify(unverifiedAccountRepository, Mockito.times(minInvocations)).save(uProvider);
        Mockito.verify(tariffRepository, Mockito.times(minInvocations)).save(Mockito.any(Tariff.class));
    }


    @Test
    void tariffApplier_successVerified() {
        Long id = dto.getId();

        VerifiedAccount vProvider = new VerifiedAccount(dto.getId(), dto.getProviderName(), dto.getWebsite(), dto.getEmail(), dto.getPhoneNumber(), true, dto.getTariffs(), dto.getRating());

        Mockito.when(providerRepository.findById(id)).thenReturn(Optional.of(dto));
        Mockito.when(verifiedAccountRepository.findById(id)).thenReturn(Optional.of(vProvider));

        var result = tariffService.tariffApplier(id);
        var expected = "Tariffs were updated successfully.";

        Assertions.assertEquals(expected, result);

        Mockito.verify(verifiedAccountRepository, Mockito.atLeast(3)).findById(id);
        Mockito.verify(verifiedAccountRepository, Mockito.times(3)).save(vProvider);
        Mockito.verify(tariffRepository, Mockito.times(3)).save(Mockito.any(Tariff.class));
    }


    @Test
    void tariffApplier_errorVerified() {
        dto.setId(2L);
        Long id = dto.getId();

        VerifiedAccount vProvider = new VerifiedAccount(dto.getId(), dto.getProviderName(), dto.getWebsite(), dto.getEmail(), dto.getPhoneNumber(), true, dto.getTariffs(), dto.getRating());

        Mockito.when(providerRepository.findById(id)).thenReturn(Optional.of(dto));
        Mockito.when(verifiedAccountRepository.findById(id)).thenReturn(Optional.of(vProvider));

        var result = tariffService.tariffApplier(id);
        var expected = errorMessageId2();

        Assertions.assertEquals(expected, result);

        int minInvocations = linesInFile(id, expected);

        Mockito.verify(verifiedAccountRepository, Mockito.atLeast(minInvocations)).findById(id);
        Mockito.verify(verifiedAccountRepository, Mockito.times(minInvocations)).save(vProvider);
        Mockito.verify(tariffRepository, Mockito.times(minInvocations)).save(Mockito.any(Tariff.class));
    }



    private String errorMessageId2() {
        return """
                The following fields of the tariff name3 (number 3 in provided list) are not filled in correctly:\s
                -) ContractType
                -) PriceModel

                The following fields of the tariff <TariffName missing> (number 4 in provided list) are not filled in correctly:\s
                -) TariffName
                -) ContractType
                -) PriceModel

                The following fields of the tariff name5 (number 5 in provided list) are not filled in correctly:\s
                -) ContractType
                -) PriceModel

                The following fields of the tariff name6 (number 6 in provided list) are not filled in correctly:\s
                -) ContractType
                -) PriceModel


                Tariffs were not updated.""";
    }


}