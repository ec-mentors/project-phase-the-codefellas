package io.everyonecodes.anber.homemanagement.endpoints;

import io.everyonecodes.anber.homemanagement.data.Home;
import io.everyonecodes.anber.homemanagement.data.HomeType;
import io.everyonecodes.anber.homemanagement.service.HomeService;
import io.everyonecodes.anber.usermanagement.data.User;
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
                1L, email, password, "role", username, country, List.of(testHome), false);

        testRestTemplate.put("/profile/" + testUserProfile.getEmail() + "/edit/homes/" + testHome.getId() + "/country", "Ireland", Home.class);
        Mockito.verify(homeService).editHome(testUserProfile.getEmail(), testHome.getId(), "country", "Ireland");
    }

    @Test
    void removeHome() {
        Home testHome1 = new Home(1L,"name", country, city, postalCode, HomeType.GARAGE, sizeInSquareMeters);
        Home testHome2 = new Home(2L,"name", country, city, postalCode, HomeType.APARTMENT, sizeInSquareMeters);
        List<Home> homes =  new ArrayList<>(List.of(testHome1,testHome2));

        User testUserProfile = new User(
                1L, email, password, "role", username, country, homes, false);

        testRestTemplate.delete("/profile/" + testUserProfile.getEmail() + "/edit/homes/remove/" + testHome1.getId());
        Mockito.verify(homeService).removeHome(testUserProfile.getEmail(), testHome1.getId());

    }

    @Test
    void deleteAllHomes() {
        Home testHome1 = new Home(1L,"name", country, city, postalCode, HomeType.GARAGE, sizeInSquareMeters);
        Home testHome2 = new Home(2L,"name", country, city, postalCode, HomeType.APARTMENT, sizeInSquareMeters);
        List<Home> homes =  new ArrayList<>(List.of(testHome1,testHome2));

        User testUserProfile = new User(
                1L, email, password, "role", username, country, homes, false);

        testRestTemplate.delete("/profile/" + testUserProfile.getEmail() + "/edit/homes/delete");
        Mockito.verify(homeService).deleteAllHomes(testUserProfile.getEmail());

    }

}











