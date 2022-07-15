package io.everyonecodes.anber.homemanagement.endpoints;

import io.everyonecodes.anber.homemanagement.data.Home;
import io.everyonecodes.anber.homemanagement.data.HomeType;
import io.everyonecodes.anber.homemanagement.service.HomeService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.security.web.SecurityFilterChain;

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

//    @Test
//    void removeHome() {
//        Home testHome1 = new Home(
//                "testCountry1", "testCity1", "2600", HomeType.APARTMENT, 25.5);
//        testRestTemplate.put("/" + username + "/home/remove", testHome1, Home[].class);
//        homeService.removeHome(testHome1, username);
//        Mockito.verify(homeService).removeHome(testHome1, username);
//    }

//    @Test
//    void deleteHomes() {
//        testRestTemplate.delete("/profile/" + username + "/homes/delete", Home[].class);
//        homeService.deleteHomes(username);
//        Mockito.verify(homeService).deleteHomes(username);
//    }
}