package io.everyonecodes.anber.usermanagement.endpoints;

import io.everyonecodes.anber.usermanagement.data.UserPrivateDTO;
import io.everyonecodes.anber.usermanagement.service.UserDTO;
import io.everyonecodes.anber.usermanagement.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.SecurityFilterChain;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LoginEndpointTest {

    @Autowired
    TestRestTemplate testRestTemplate;

    @MockBean
    UserService userService;

    @MockBean
    UserDTO userDTO;

    @Value("${testvalues.login-endpoint-url}")
    String url;

    @Value("${testvalues.username}")
    String username;

    @Value("${testvalues.role}")
    String role;

    @Value("${testvalues.email}")
    String email;

    @MockBean
    SecurityFilterChain filterChain;


    @Test
    @WithMockUser(username = "ADMIN", password = "admin", authorities = "ROLE_ADMIN")
    void viewIndividualProfile_returnsProfileData() {
        testRestTemplate.getForObject(url, UserPrivateDTO.class);

        Mockito.verify(userService).viewIndividualProfileData("ADMIN");
    }

    @Test
    void viewIndividualProfile_returnsNull() {
        testRestTemplate.getForObject(url, UserPrivateDTO.class);
        Mockito.verify(userService, Mockito.never()).viewIndividualProfileData(Mockito.any());
    }

}