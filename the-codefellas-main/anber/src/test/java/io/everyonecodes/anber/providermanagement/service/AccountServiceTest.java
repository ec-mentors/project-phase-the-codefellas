package io.everyonecodes.anber.providermanagement.service;

import io.everyonecodes.anber.DatabaseInitializer;
import io.everyonecodes.anber.providermanagement.data.*;
import io.everyonecodes.anber.providermanagement.repository.UnverifiedAccountRepository;
import io.everyonecodes.anber.providermanagement.repository.VerifiedAccountRepository;
import io.everyonecodes.anber.ratingmanagement.data.Rating;
import io.everyonecodes.anber.ratingmanagement.repository.RatingRepository;
import io.everyonecodes.anber.searchmanagement.data.ProviderDTO;
import io.everyonecodes.anber.searchmanagement.repository.ProviderRepository;
import io.everyonecodes.anber.searchmanagement.service.ProviderTranslator;
import io.everyonecodes.anber.tariffmanagement.data.Tariff;
import io.everyonecodes.anber.tariffmanagement.repository.TariffRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.web.SecurityFilterChain;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class AccountServiceTest {

    @Autowired
    AccountService accountService;

    @MockBean
    ProviderRepository providerRepository;

    @MockBean
    TariffRepository tariffRepository;

    @MockBean
    VerifiedAccountRepository verifiedAccountRepository;

    @MockBean
    UnverifiedAccountRepository unverifiedAccountRepository;

    @MockBean
    RatingRepository ratingRepository;

    @MockBean
    ProviderTranslator translator;

    @MockBean
    SecurityFilterChain securityFilterChain;

    @MockBean
    DatabaseInitializer databaseInitializer;


    private final ProviderDTO dto = new ProviderDTO(1L, "united kingdom", ProviderType.GAS, "providerName", List.of( new Tariff("tariffName", 0.5, ContractType.SIX_MONTHS, PriceModelType.FIXED)), "website.com", "test@email.com", "0123 456789", false, new Rating(Set.of(), "4.5"));
    private final UnverifiedAccount uProv = new UnverifiedAccount(dto.getId(), dto.getProviderName(), dto.getWebsite(), false, dto.getTariffs(), dto.getRating());
    private final VerifiedAccount vProv = new VerifiedAccount(dto.getId(), dto.getProviderName(), dto.getWebsite(), "email", "1234 567890", true, dto.getTariffs(), dto.getRating());

    @Test
    void getAllUnverified() {
        var expected = List.of(new UnverifiedAccount());

        Mockito.when(unverifiedAccountRepository.findAll()).thenReturn(expected);

        var result = accountService.getAllUnverified();

        Assertions.assertEquals(expected, result);

        Mockito.verify(unverifiedAccountRepository).findAll();
    }

    @Test
    void getAllVerified() {
        var expected = List.of(new VerifiedAccount());

        Mockito.when(verifiedAccountRepository.findAll()).thenReturn(expected);

        var result = accountService.getAllVerified();

        Assertions.assertEquals(expected, result);

        Mockito.verify(verifiedAccountRepository).findAll();
    }

    @Test
    void createAccount_notYetExisting() {
        Long id = dto.getId();
        var expected = new UnverifiedAccount(dto.getId(), dto.getProviderName(), dto.getWebsite(), false, dto.getTariffs(), dto.getRating());

        Mockito.when(providerRepository.findById(id)).thenReturn(Optional.of(dto));
        Mockito.when(unverifiedAccountRepository.existsByProviderName(dto.getProviderName())).thenReturn(false);
        Mockito.when(translator.DtoToUnverifiedAccount(dto)).thenReturn(expected);

        var result = accountService.createAccount(id);

        Assertions.assertEquals(Optional.of(expected), result);

        Mockito.verify(providerRepository).findById(id);
        Mockito.verify(unverifiedAccountRepository).existsByProviderName(dto.getProviderName());
        Mockito.verify(translator).DtoToUnverifiedAccount(dto);
        Mockito.verify(ratingRepository).save(expected.getRating());
        Mockito.verify(unverifiedAccountRepository).save(expected);
    }

    @Test
    void createAccount_Existing() {
        Long id = dto.getId();

        Mockito.when(providerRepository.findById(id)).thenReturn(Optional.of(dto));
        Mockito.when(unverifiedAccountRepository.existsByProviderName(dto.getProviderName())).thenReturn(true);

        var result = accountService.createAccount(id);

        Assertions.assertEquals(Optional.empty(), result);

        Mockito.verify(providerRepository).findById(id);
        Mockito.verify(unverifiedAccountRepository).existsByProviderName(dto.getProviderName());
        Mockito.verify(translator, Mockito.never()).DtoToUnverifiedAccount(dto);
        Mockito.verify(ratingRepository, Mockito.never()).save(uProv.getRating());
        Mockito.verify(unverifiedAccountRepository, Mockito.never()).save(uProv);
    }

    @Test
    void verifyAccount() {
        Long id = dto.getId();

        Mockito.when(unverifiedAccountRepository.findById(id)).thenReturn(Optional.of(uProv));
        Mockito.when(providerRepository.findById(id)).thenReturn(Optional.of(dto));
        Mockito.when(translator.unverifiedToVerifiedAccount(uProv, dto)).thenReturn(vProv);


        var result = accountService.verifyAccount(id);
        var expected = Optional.of(vProv);

        Assertions.assertEquals(expected, result);

        Mockito.verify(unverifiedAccountRepository).findById(id);
        Mockito.verify(providerRepository).findById(id);
        Mockito.verify(translator).unverifiedToVerifiedAccount(uProv, dto);
        Mockito.verify(providerRepository).save(dto);
        Mockito.verify(verifiedAccountRepository).save(vProv);
        Mockito.verify(unverifiedAccountRepository).deleteById(id);

    }

    @Test
    void unverifyAccount() {

        Long id = dto.getId();

        Mockito.when(verifiedAccountRepository.findById(id)).thenReturn(Optional.of(vProv));
        Mockito.when(providerRepository.findById(id)).thenReturn(Optional.of(dto));
        Mockito.when(translator.verifiedToUnverifiedAccount(vProv)).thenReturn(uProv);

        var result = accountService.unverifyAccount(id);
        var expected = Optional.of(uProv);

        Assertions.assertEquals(expected, result);


        Mockito.verify(verifiedAccountRepository).findById(id);
        Mockito.verify(providerRepository).findById(id);
        Mockito.verify(translator).verifiedToUnverifiedAccount(vProv);
        Mockito.verify(providerRepository).save(dto);
        Mockito.verify(unverifiedAccountRepository).save(uProv);
        Mockito.verify(verifiedAccountRepository).deleteById(id);
    }

    @Test
    void editAccountV() {
        Long id = vProv.getId();
        String property = "phoneNumber";
        String input = "0987 654321";
        Mockito.when(verifiedAccountRepository.findById(id)).thenReturn(Optional.of(vProv));

        var result = accountService.editAccountV(id, property, input);
        var expected = Optional.of (new VerifiedAccount(dto.getId(), dto.getProviderName(), dto.getWebsite(), "email", "0987 654321", true, dto.getTariffs(), dto.getRating()));

        Assertions.assertEquals(expected, result);

        Mockito.verify(verifiedAccountRepository, Mockito.atLeast(1)).findById(id);
        Mockito.verify(verifiedAccountRepository).save(vProv);
    }

    @Test
    void editAccountU() {
        Long id = uProv.getId();
        String property = "website";
        String input = "newsite.com";
        Mockito.when(unverifiedAccountRepository.findById(id)).thenReturn(Optional.of(uProv));

        var result = accountService.editAccountU(id, property, input);
        var expected = Optional.of (new UnverifiedAccount(dto.getId(), dto.getProviderName(), input, false, dto.getTariffs(), dto.getRating()));

        Assertions.assertEquals(expected, result);

        Mockito.verify(unverifiedAccountRepository, Mockito.atLeast(1)).findById(id);
        Mockito.verify(unverifiedAccountRepository).save(uProv);
    }

    @Test
    void deleteAccount_verified() {

        Long id = dto.getId();

        Mockito.when(verifiedAccountRepository.findById(id)).thenReturn(Optional.of(vProv));

        accountService.deleteAccount(id);

        List<Long> ids = vProv.getTariffs().stream()
                .map(Tariff::getId)
                .collect(Collectors.toList());

        Mockito.verify(verifiedAccountRepository, Mockito.atLeast(1)).findById(id);
        Mockito.verify(tariffRepository).deleteAllByIdInBatch(ids);
        Mockito.verify(ratingRepository).deleteById(id);
        Mockito.verify(verifiedAccountRepository).deleteById(id);
    }


    @Test
    void deleteAccount_unverified() {

        Long id = dto.getId();

        Mockito.when(unverifiedAccountRepository.findById(id)).thenReturn(Optional.of(uProv));

        accountService.deleteAccount(id);

        List<Long> ids = uProv.getTariffs().stream()
                .map(Tariff::getId)
                .collect(Collectors.toList());

        Mockito.verify(unverifiedAccountRepository, Mockito.atLeast(1)).findById(id);
        Mockito.verify(tariffRepository).deleteAllByIdInBatch(ids);
        Mockito.verify(ratingRepository).deleteById(id);
        Mockito.verify(unverifiedAccountRepository).deleteById(id);
    }

    @Test
    void viewPublicProvider_dtoExists() {
        Long id = dto.getId();
        var pProv = new ProviderPublic(dto.getProviderName(), dto.getWebsite(),dto.getRating(), dto.getTariffs());

        Mockito.when(providerRepository.findById(id)).thenReturn(Optional.of(dto));

        Mockito.when(translator.dtoToPublic(dto)).thenReturn(pProv);

        var result = accountService.viewPublicProvider(id);
        var expected = Optional.of(pProv);

        Assertions.assertEquals(expected, result);
        Mockito.verify(providerRepository).findById(id);
        Mockito.verify(translator).dtoToPublic(dto);

    }

    @Test
    void viewPublicProvider_dtoNotExists() {
        Long id = dto.getId();
        var pProv = new ProviderPublic(dto.getProviderName(), dto.getWebsite(),dto.getRating(), dto.getTariffs());

        Mockito.when(providerRepository.findById(id)).thenReturn(Optional.empty());


        var result = accountService.viewPublicProvider(id);

        Assertions.assertEquals(Optional.empty(), result);
        Mockito.verify(providerRepository).findById(id);
        Mockito.verify(translator, Mockito.never()).dtoToPublic(dto);

    }

//    @Test
//    void fillInDtoData() {
//        ProviderDTO dto1 = new ProviderDTO(1L, "united kingdom", ProviderType.GAS, "providerName", List.of(), "website.com", "test@email.com", "0123 456789", false, new Rating());
//
//        ProviderDTO dto2 = new ProviderDTO(500L, "united kingdom", ProviderType.GAS, "providerName", List.of(), "website.com", "test@email.com", "0123 456789", false, new Rating());
//
//        Mockito.when(providerRepository.findById(dto1.getId()).get().getTariffs().isEmpty()).thenReturn(false);
//        Mockito.when(providerRepository.findById(dto1.getId()).get().getRating() == null).thenReturn(false);
//
//        Mockito.when(providerRepository.findById(dto2.getId()).get().getTariffs().isEmpty()).thenReturn(false);
//        Mockito.when(providerRepository.findById(dto2.getId()).get().getRating() == null).thenReturn(false);
//
//
//        Mockito.when(providerRepository.findAll().size()).thenReturn(List.of(dto1, dto2).size());
//
//        Mockito.when(tariffRepository.findAllByProviderId(dto1.getId())).thenReturn(List.of(new Tariff()));
//        Mockito.when(tariffRepository.findAllByProviderId(dto2.getId())).thenReturn(List.of(new Tariff()));
//
//        var result = accountService.fillInDtoData();
//        var expected = List.of(dto1, dto2);
//
//        Assertions.assertEquals(expected, result);
//
//        Mockito.verify(providerRepository, Mockito.times(2)).findById(Mockito.any(Long.class));
//        Mockito.verify(providerRepository).findAll();
//
////        Mockito.verify(tariffRepository, Mockito.times(2)).findAllByProviderId(Mockito.any(Long.class));
//
////        Mockito.verify(tariffRepository, Mockito.times(2)).saveAll(List.of());
////        Mockito.verify(providerRepository, Mockito.times(2)).save(Mockito.any(ProviderDTO.class));
////        Mockito.verify(ratingRepository, Mockito.times(2)).save(new Rating(dto.getId(), Set.of(), "No Ratings"));
////        Mockito.when(providerRepository.findById(dto1.getId())).thenReturn(Optional.of(dto1));
////        Mockito.when(providerRepository.findById(dto2.getId())).thenReturn(Optional.of(dto2));
////
////        Mockito.when(providerRepository.findAll()).thenReturn(List.of(dto1, dto2));
////
////        Mockito.when(tariffRepository.findAllByProviderId(dto1.getId())).thenReturn(List.of());
////        Mockito.when(tariffRepository.findAllByProviderId(dto2.getId())).thenReturn(List.of());
////
////        var result = accountService.fillInDtoData();
////        var expected = List.of(dto1, dto2);
////
//        Assertions.assertEquals(expected, result);
//
//        Mockito.verify(providerRepository, Mockito.times(2)).findById(Mockito.any(Long.class));
//        Mockito.verify(providerRepository).findAll();
//
//        Mockito.verify(tariffRepository, Mockito.times(2)).findAllByProviderId(Mockito.any(Long.class));
//
//        Mockito.verify(tariffRepository, Mockito.times(2)).saveAll(List.of());
//        Mockito.verify(providerRepository, Mockito.times(2)).save(Mockito.any(ProviderDTO.class));
//        Mockito.verify(ratingRepository, Mockito.times(2)).save(new Rating(dto.getId(), Set.of(), "No Ratings"));
//
//    }
}