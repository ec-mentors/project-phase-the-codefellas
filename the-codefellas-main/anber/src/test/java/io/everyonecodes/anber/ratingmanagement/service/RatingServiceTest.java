package io.everyonecodes.anber.ratingmanagement.service;

import io.everyonecodes.anber.InitializationRunner;
import io.everyonecodes.anber.providermanagement.data.ContractType;
import io.everyonecodes.anber.providermanagement.data.PriceModelType;
import io.everyonecodes.anber.providermanagement.data.ProviderType;
import io.everyonecodes.anber.ratingmanagement.data.Rating;
import io.everyonecodes.anber.ratingmanagement.repository.RatingRepository;
import io.everyonecodes.anber.searchmanagement.data.ProviderDTO;
import io.everyonecodes.anber.searchmanagement.repository.ProviderRepository;
import io.everyonecodes.anber.tariffmanagement.data.Tariff;
import io.everyonecodes.anber.usermanagement.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.web.SecurityFilterChain;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class RatingServiceTest {

    @Autowired
    RatingService ratingService;

    @MockBean
    RatingRepository ratingRepository;

    @MockBean
    ProviderRepository providerRepository;

    @MockBean
    UserService userService;

    @Value("${messages.provider-account.no-ratings}")
    String noRatings;

    @MockBean
    SecurityFilterChain securityFilterChain;

    @MockBean
    InitializationRunner initializationRunner;

    private final ProviderDTO dto = new ProviderDTO(1L, "united kingdom", ProviderType.GAS, "providerName", List.of( new Tariff("tariffName", 0.5, ContractType.SIX_MONTHS, PriceModelType.FIXED)), "website.com", "test@email.com", "0123 456789", false, new Rating(new HashSet<>(), "No Ratings"));

    @Test
    void rateProvider_noRatings() {

        int rating = 4;
        Long id = dto.getId();
        String username = "user1@email.com";

        Mockito.when(userService.loggedInUser()).thenReturn(username);
        Mockito.when(providerRepository.findById(id)).thenReturn(Optional.of(dto));

        var result = ratingService.rateProvider(rating, id);
        var expected = Optional.of("Current rating for Provider providerName is: 4.0");
        Assertions.assertEquals(expected, result);

        Mockito.verify(userService).loggedInUser();
        Mockito.verify(providerRepository).findById(id);
    }

    @Test
    void rateProvider_alreadyRated() {
        dto.setRating(new Rating(new HashSet<>(Set.of("user1@email.com")), "4.0"));

        int rating = 4;
        Long id = dto.getId();
        String username = "user1@email.com";

        Mockito.when(userService.loggedInUser()).thenReturn(username);
        Mockito.when(providerRepository.findById(id)).thenReturn(Optional.of(dto));

        var result = ratingService.rateProvider(rating, id);
        var expected = Optional.of("User user1@email.com already gave a rating to Provider providerName!");
        Assertions.assertEquals(expected, result);

        Mockito.verify(userService).loggedInUser();
        Mockito.verify(providerRepository).findById(id);
    }

    @Test
    void rateProvider_existingRatings() {

        dto.setRating(new Rating(new HashSet<>(Set.of("user1@email.com")), "4.0"));

        int rating = 3;
        Long id = dto.getId();
        String username = "user2@email.com";

        Mockito.when(userService.loggedInUser()).thenReturn(username);
        Mockito.when(providerRepository.findById(id)).thenReturn(Optional.of(dto));
        Mockito.when(ratingRepository.findById(id)).thenReturn(Optional.of(dto.getRating()));

        var result = ratingService.rateProvider(rating, id);
        var expected = Optional.of("Current rating for Provider providerName is: 3.5");
        Assertions.assertEquals(expected, result);

        Mockito.verify(userService).loggedInUser();
        Mockito.verify(providerRepository).findById(id);
    }

    @Test
    void rateProvider_outOfRange() {
        int rating = 42;
        Long id = dto.getId();

        var result = ratingService.rateProvider(rating, id);
        var expected = Optional.empty();
        Assertions.assertEquals(expected, result);

        Mockito.verify(userService, Mockito.never()).loggedInUser();
        Mockito.verify(providerRepository, Mockito.never()).findById(id);
    }
}