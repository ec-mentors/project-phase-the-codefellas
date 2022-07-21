package io.everyonecodes.anber.homemanagement.endpoints;

import io.everyonecodes.anber.homemanagement.data.Home;
import io.everyonecodes.anber.homemanagement.data.HomeType;
import io.everyonecodes.anber.homemanagement.service.HomeService;
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
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.security.web.SecurityFilterChain;

import java.util.List;
import java.util.stream.Stream;

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


}











