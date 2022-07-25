package io.everyonecodes.anber.endpointsOld;

import io.everyonecodes.anber.data.Home;
import io.everyonecodes.anber.data.HomeType;
import io.everyonecodes.anber.data.Role;
import io.everyonecodes.anber.data.User;
import io.everyonecodes.anber.service.HomeService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.security.web.SecurityFilterChain;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class HomeEndpointTest {

    @Autowired
    TestRestTemplate testRestTemplate;

    @MockBean
    HomeService homeService;

    @MockBean
    SecurityFilterChain securityFilterChain;

    @Value("${testvalues.email}")
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
    void getHomes() {
        testRestTemplate.getForObject("/profile/" + username + "/homes", Home[].class);
        Mockito.verify(homeService).getHomes(username);
    }

    @Test
    void addHome() {
        Home testHome = new Home("homeName",
                "testCountry", "testCity", "2700", HomeType.APARTMENT, 25.5
        );
        testRestTemplate.put("/profile/" + username + "/edit/homes/add", testHome, Home[].class);
        Mockito.verify(homeService).addHome(testHome, username);
    }

    @Test
    void editHome() {
        Home testHome = new Home(1L, "homeName",
                "testCountry", "testCity", "2700", HomeType.APARTMENT, 25.5
        );
        User testUserProfile = new User(
                1L, email, password, Set.of(new Role("ROLE_USER", "normal User")), username, country, List.of(testHome), false);

        testRestTemplate.put("/profile/" + testUserProfile.getEmail() + "/edit/homes/" + testHome.getId() + "/country", "Ireland", Home.class);
        Mockito.verify(homeService).editHome(testUserProfile.getEmail(), testHome.getId(), "country", "Ireland");
    }

    @Test
    void removeHome() {
        Home testHome1 = new Home(1L,"name", country, city, postalCode, HomeType.GARAGE, sizeInSquareMeters);
        Home testHome2 = new Home(2L,"name", country, city, postalCode, HomeType.APARTMENT, sizeInSquareMeters);
        List<Home> homes =  new ArrayList<>(List.of(testHome1,testHome2));

        User testUserProfile = new User(
                1L, email, password, Set.of(new Role("ROLE_USER", "normal User")), username, country, homes, false);

        testRestTemplate.delete("/profile/" + testUserProfile.getEmail() + "/edit/homes/remove/" + testHome1.getId());
        Mockito.verify(homeService).removeHome(testUserProfile.getEmail(), testHome1.getId());

    }

    @Test
    void deleteAllHomes() {
        Home testHome1 = new Home(1L,"name", country, city, postalCode, HomeType.GARAGE, sizeInSquareMeters);
        Home testHome2 = new Home(2L,"name", country, city, postalCode, HomeType.APARTMENT, sizeInSquareMeters);
        List<Home> homes =  new ArrayList<>(List.of(testHome1,testHome2));

        User testUserProfile = new User(
                1L, email, password, Set.of(new Role("ROLE_USER", "normal User")), username, country, homes, false);

        testRestTemplate.delete("/profile/" + testUserProfile.getEmail() + "/edit/homes/delete");
        Mockito.verify(homeService).deleteAllHomes(testUserProfile.getEmail());

    }
}