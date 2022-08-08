package io.everyonecodes.anber.homemanagement.endpoints;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.everyonecodes.anber.homemanagement.data.Home;
import io.everyonecodes.anber.homemanagement.data.HomeType;
import io.everyonecodes.anber.homemanagement.service.HomeService;
import io.everyonecodes.anber.usermanagement.data.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class HomeEndpointTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

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
    @WithMockUser(username = "test@email.com", password = "Password1!", roles = "USER")
    void getHomes() throws Exception {
        String url = "/profile/" + username + "/homes";

        mockMvc.perform(MockMvcRequestBuilders.get(url))
                .andExpect(MockMvcResultMatchers.status().isOk());

        Mockito.verify(homeService).getHomes(username);
    }

    @Test
    @WithMockUser(username = "test@email.com", password = "Password1!", roles = "USER")
    void addHome() throws Exception {

        String url = "/profile/" + username + "/edit/homes/add";

        var input = new Home("homeName", "testCountry", "testCity", "2700", HomeType.APARTMENT, 25.5);

        mockMvc.perform(MockMvcRequestBuilders.put(url, email)
                        .with(user("test@email.com").password("Password1!").roles("USER"))
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(input))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());

        Mockito.verify(homeService).addHome(input, username);
    }

    @ParameterizedTest
    @CsvSource({
            "homeName, Test",
            "country, Ireland",
            "city, Dublin",
            "postalCode, 4321",
            "homeType, house",
            "sizeInSquareMeters, 33.3"
    })
    @WithMockUser(username = "test@email.com", password = "Password1!", roles = "USER")
    void editHome(String property, String input) throws Exception {
        Home testHome = new Home(1L, "homeName", "testCountry", "testCity", "123", HomeType.APARTMENT, 25.5);

        User testUserProfile = new User(email, password);

        String url = "/profile/" + testUserProfile.getEmail() + "/edit/homes/" + testHome.getId() + "/" + property;

        mockMvc.perform(MockMvcRequestBuilders.put(url, email, testHome.getId(), property)
                        .with(user("test@email.com").password("Password1!").roles("USER"))
                        .contentType("application/json")
                        .content(input)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());

        Mockito.verify(homeService).editHome(testUserProfile.getEmail(), testHome.getId(), property, input);
    }



    @Test
    @WithMockUser(username = "test@email.com", password = "Password1!", roles = "USER")
    void removeHome() throws Exception {
        Home testHome1 = new Home(1L,"name", country, city, postalCode, HomeType.GARAGE, sizeInSquareMeters);
        Home testHome2 = new Home(2L,"name", country, city, postalCode, HomeType.APARTMENT, sizeInSquareMeters);
        List<Home> homes =  new ArrayList<>(List.of(testHome1,testHome2));

        User testUserProfile = new User(
                1L, email, password, "role", username, country, homes, false);

       String url = "/profile/" + testUserProfile.getEmail() + "/edit/homes/remove/" + testHome1.getId();


        mockMvc.perform(MockMvcRequestBuilders.delete(url))
                .andExpect(MockMvcResultMatchers.status().isOk());

        Mockito.verify(homeService).removeHome(testUserProfile.getEmail(), testHome1.getId());

    }

    @Test
    @WithMockUser(username = "test@email.com", password = "Password1!", roles = "USER")
    void deleteAllHomes() throws Exception {
        Home testHome1 = new Home(1L,"name", country, city, postalCode, HomeType.GARAGE, sizeInSquareMeters);
        Home testHome2 = new Home(2L,"name", country, city, postalCode, HomeType.APARTMENT, sizeInSquareMeters);
        List<Home> homes =  new ArrayList<>(List.of(testHome1,testHome2));

        User testUserProfile = new User(
                1L, email, password, "role", username, country, homes, false);

       String url ="/profile/" + testUserProfile.getEmail() + "/edit/homes/delete";

        mockMvc.perform(MockMvcRequestBuilders.delete(url))
                .andExpect(MockMvcResultMatchers.status().isOk());

        Mockito.verify(homeService).deleteAllHomes(testUserProfile.getEmail());

    }
}











