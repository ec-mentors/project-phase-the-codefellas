package io.everyonecodes.anber.homemanagement.service;

import io.everyonecodes.anber.homemanagement.data.Home;
import io.everyonecodes.anber.homemanagement.data.HomeType;
import io.everyonecodes.anber.homemanagement.repository.HomeRepository;
import io.everyonecodes.anber.usermanagement.data.User;
import io.everyonecodes.anber.usermanagement.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.web.SecurityFilterChain;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class HomeServiceTest {

    @Autowired
    HomeService homeService;

    @MockBean
    UserRepository userRepository;

    @MockBean
    HomeRepository homeRepository;

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
        Home testHome1 = new Home("name", country, city, postalCode, HomeType.GARAGE, sizeInSquareMeters);
        Home testHome2 = new Home("name", country, city, postalCode, HomeType.APARTMENT, sizeInSquareMeters);
        Home testHome3 = new Home("name", country, city, postalCode, HomeType.HOUSE, sizeInSquareMeters);
        List<Home> homes = List.of(testHome1, testHome2, testHome3);

        User profile = new User(email, password, "role", username, country, homes, false);

        Mockito.when(userRepository.findOneByEmail(email)).thenReturn(Optional.of(profile));
        var result = homeService.getHomes(email);
        Assertions.assertEquals(homes, result);
        Mockito.verify(userRepository).findOneByEmail(email);
    }

    @Test
    void getHomes_emptyList() {
        List<Home> homes = List.of();
        Mockito.when(userRepository.findOneByEmail(email)).thenReturn(Optional.empty());
        var result = homeService.getHomes(email);
        Assertions.assertEquals(homes, result);
        Mockito.verify(userRepository).findOneByEmail(email);
    }

    @Test
    void addHome() {
        List<Home> homes = new ArrayList<>();
        User testUserProfile = new User(
                email, password, "role", username, country, homes, false);
        Home testHome = new Home(country, city, postalCode, HomeType.GARAGE, sizeInSquareMeters);
        Mockito.when(userRepository.findOneByEmail(username)).thenReturn(Optional.of(testUserProfile));
        var response = userRepository.findOneByEmail(username);
        Assertions.assertEquals(0, response.get().getSavedHomes().size());
        homes.add(testHome);
        homeRepository.save(testHome);
        testUserProfile.setSavedHomes(homes);
        var result = userRepository.save(testUserProfile);
        Assertions.assertEquals(1, response.get().getSavedHomes().size());
        Mockito.verify(userRepository).findOneByEmail(username);
        Mockito.verify(homeRepository).save(testHome);
        Mockito.verify(userRepository).save(testUserProfile);
        Mockito.verifyNoMoreInteractions(homeRepository);
    }

    @ParameterizedTest
    @MethodSource("parameters")
    void editHome(String property, String input, Optional<Home> expected) {

        Home testHome1 = new Home(1L,"name", country, city, postalCode, HomeType.GARAGE, sizeInSquareMeters);
        Home testHome2 = new Home(2L,"name", country, city, postalCode, HomeType.APARTMENT, sizeInSquareMeters);
        List<Home> homes =  new ArrayList<>(List.of(testHome1,testHome2));

        User testUserProfile = new User(
                1L, email, password, "role", username, country, homes, false);

        Mockito.when(userRepository.findOneByEmail(testUserProfile.getEmail())).thenReturn(Optional.of(testUserProfile));

        Optional<Home> result = homeService.editHome(testUserProfile.getEmail(), testUserProfile.getSavedHomes().get(0).getId(), property,input);

        Assertions.assertEquals(expected,result);

        Mockito.verify(userRepository).findOneByEmail(testUserProfile.getEmail());
    }


    private static Stream<Arguments> parameters() {
                return Stream.of(
                Arguments.of("homeName", "otherName", Optional.of(
                        new Home(1L,"otherName", "testCountry", "testCity", "666", HomeType.GARAGE, 66.66))),
                Arguments.of("country", "France", Optional.of(
                        new Home(1L,"name", "France", "testCity", "666", HomeType.GARAGE, 66.66))),
                Arguments.of("city", "Paris", Optional.of(
                        new Home(1L,"name", "testCountry", "Paris", "666", HomeType.GARAGE, 66.66))),
                Arguments.of("postalCode", "123", Optional.of(
                        new Home(1L,"name", "testCountry", "testCity", "123", HomeType.GARAGE, 66.66))),
                Arguments.of("type", "apartment", Optional.of(
                        new Home(1L,"name", "testCountry", "testCity", "666", HomeType.APARTMENT, 66.66))),
                Arguments.of("type", "APARTMENT", Optional.of(
                        new Home(1L,"name", "testCountry", "testCity", "666", HomeType.APARTMENT, 66.66))),
                Arguments.of("sizeInSquareMeters", "300.5", Optional.of(
                        new Home(1L,"name", "testCountry", "testCity", "666", HomeType.GARAGE, 300.5)))
        );
    }

    @Test
    void removeHome() {
        Home testHome1 = new Home(1L,"name", country, city, postalCode, HomeType.GARAGE, sizeInSquareMeters);
        Home testHome2 = new Home(2L,"name", country, city, postalCode, HomeType.APARTMENT, sizeInSquareMeters);
        List<Home> homes =  new ArrayList<>(List.of(testHome1,testHome2));

        User testUserProfile = new User(
                1L, email, password, "role", username, country, homes, false);

        Mockito.when(userRepository.findOneByEmail(testUserProfile.getEmail())).thenReturn(Optional.of(testUserProfile));

        Assertions.assertEquals(2, testUserProfile.getSavedHomes().size());

        homeService.removeHome(testUserProfile.getEmail(), testHome1.getId());

        Assertions.assertEquals(1, testUserProfile.getSavedHomes().size());

        Mockito.verify(userRepository).save(testUserProfile);
        Mockito.verify(homeRepository).deleteById(testHome1.getId());
    }

    @Test
    void deleteAllHomes() {
        Home testHome1 = new Home(1L,"name", country, city, postalCode, HomeType.GARAGE, sizeInSquareMeters);
        Home testHome2 = new Home(2L,"name", country, city, postalCode, HomeType.APARTMENT, sizeInSquareMeters);
        List<Home> homes =  new ArrayList<>(List.of(testHome1,testHome2));

        User testUserProfile = new User(
                1L, email, password, "role", username, country, homes, false);

        Mockito.when(userRepository.findOneByEmail(testUserProfile.getEmail())).thenReturn(Optional.of(testUserProfile));

        Assertions.assertEquals(2, testUserProfile.getSavedHomes().size());

        homeService.deleteAllHomes(testUserProfile.getEmail());

        Assertions.assertEquals(0, testUserProfile.getSavedHomes().size());

        Mockito.verify(homeRepository).deleteAllByIdInBatch(List.of(testHome1.getId(), testHome2.getId()));
        Mockito.verify(userRepository).save(testUserProfile);
    }


}