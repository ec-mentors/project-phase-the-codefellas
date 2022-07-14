package io.everyonecodes.anber.homemanagement.service;

import io.everyonecodes.anber.homemanagement.data.Home;
import io.everyonecodes.anber.homemanagement.data.HomeType;
import io.everyonecodes.anber.homemanagement.repository.HomeRepository;
import io.everyonecodes.anber.profilemanagement.data.UserProfile;
import io.everyonecodes.anber.profilemanagement.repository.UserProfileRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.web.SecurityFilterChain;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class HomeServiceTest {

    @Autowired
    HomeService homeService;

    @MockBean
    HomeRepository homeRepository;

    @MockBean
    UserProfileRepository userProfileRepository;

    @MockBean
    SecurityFilterChain securityFilterChain;

    @Value("${testvalues.username}")
    String username;

    @Value("${testvalues.email}")
    String email;

    @Value("${testvalues.password}")
    String password;

    @Value("${testvalues.country}")
    String country;

    @Value("${testvalues.city}")
    String city;

    @Value("${testvalues.postalCode}")
    String postalCode;

    @Value("${testvalues.sizeInSquareMeters}")
    double sizeInSquareMeters;

    @Test
    void getHomes_returnsHomes() {
        Home testHome1 = new Home(country, city, postalCode, HomeType.GARAGE, sizeInSquareMeters);
        Home testHome2 = new Home(country, city, postalCode, HomeType.APARTMENT, sizeInSquareMeters);
        Home testHome3 = new Home(country, city, postalCode, HomeType.HOUSE, sizeInSquareMeters);
        List<Home> homes = List.of(testHome1, testHome2, testHome3);
        Mockito.when(homeRepository.findAll()).thenReturn(homes);
        var result = homeService.getHomes();
        Assertions.assertEquals(homes, result);
        Mockito.verify(homeRepository).findAll();
    }

    @Test
    void getHomes_emptyList() {
        List<Home> homes = List.of();
        Mockito.when(homeRepository.findAll()).thenReturn(homes);
        var result = homeService.getHomes();
        Assertions.assertEquals(homes, result);
        Mockito.verify(homeRepository).findAll();
    }

    @Test
    void addHome() {
        List<Home> homes = new ArrayList<>();
        UserProfile testUserProfile = new UserProfile(
                email, password, username, country, homes, false);
        Home testHome = new Home(country, city, postalCode, HomeType.GARAGE, sizeInSquareMeters);
        Mockito.when(userProfileRepository.findOneByUsername(username)).thenReturn(Optional.of(testUserProfile));
        var response = userProfileRepository.findOneByUsername(username);
        Assertions.assertEquals(0, response.get().getSavedHomes().size());
        homes.add(testHome);
        homeRepository.save(testHome);
        testUserProfile.setSavedHomes(homes);
        var result = userProfileRepository.save(testUserProfile);
        Assertions.assertEquals(1, response.get().getSavedHomes().size());
        Mockito.verify(userProfileRepository).findOneByUsername(username);
        Mockito.verify(homeRepository).save(testHome);
        Mockito.verify(userProfileRepository).save(testUserProfile);
        Mockito.verifyNoMoreInteractions(userProfileRepository);
        Mockito.verifyNoMoreInteractions(homeRepository);
    }

    @Test
    void removeHome() {
        Home testHome = new Home(country, city, postalCode, HomeType.GARAGE, sizeInSquareMeters);
        List<Home> homes = new ArrayList<>(List.of(testHome));
        UserProfile testUserProfile = new UserProfile(
                email, password, username, country, homes, false);
        Mockito.when(userProfileRepository.findOneByUsername(username)).thenReturn(Optional.of(testUserProfile));
        var response = userProfileRepository.findOneByUsername(username);
        Assertions.assertEquals(1, response.get().getSavedHomes().size());
        homes.remove(testHome);
        homeRepository.save(testHome);
        testUserProfile.setSavedHomes(homes);
        var result = userProfileRepository.save(testUserProfile);
        Assertions.assertEquals(0, response.get().getSavedHomes().size());
        Mockito.verify(userProfileRepository).findOneByUsername(username);
        Mockito.verify(homeRepository).save(testHome);
        Mockito.verify(userProfileRepository).save(testUserProfile);
        Mockito.verifyNoMoreInteractions(userProfileRepository);
        Mockito.verifyNoMoreInteractions(homeRepository);
    }

    @Test
    void deleteHome() {
        Home testHome = new Home(country, city, postalCode, HomeType.GARAGE, sizeInSquareMeters);
        List<Home> homes = new ArrayList<>(List.of(testHome));
        UserProfile testUserProfile = new UserProfile(
                email, password, username, country, homes, false);
        Mockito.when(userProfileRepository.findOneByUsername(username)).thenReturn(Optional.of(testUserProfile));
        var response = userProfileRepository.findOneByUsername(username);
        Assertions.assertEquals(1, response.get().getSavedHomes().size());
        homeRepository.deleteAll();
        testUserProfile.setSavedHomes(new ArrayList<>());
        var result = userProfileRepository.save(testUserProfile);
        Assertions.assertEquals(0, response.get().getSavedHomes().size());
        Mockito.verify(userProfileRepository).findOneByUsername(username);
        Mockito.verify(homeRepository).deleteAll();
        Mockito.verify(userProfileRepository).save(testUserProfile);
        Mockito.verifyNoMoreInteractions(userProfileRepository);
        Mockito.verifyNoMoreInteractions(homeRepository);
    }
}