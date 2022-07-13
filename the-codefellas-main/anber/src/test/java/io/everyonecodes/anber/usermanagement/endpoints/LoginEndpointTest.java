package io.everyonecodes.anber.usermanagement.endpoints;

import io.everyonecodes.anber.usermanagement.data.UserPrivateDTO;
import io.everyonecodes.anber.usermanagement.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;

import java.util.Optional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LoginEndpointTest {

    @Autowired
    TestRestTemplate testRestTemplate;

    @MockBean
    UserService userService;

    @Value("${testvalues.login-endpoint-url}")
    String url;

    @Value("${testvalues.username}")
    String username;

    @Value("${testvalues.role}")
    String role;

    @Value("${testvalues.email}")
    String email;

    @Test
    void viewIndividualProfile_returnsProfileData() {
        Optional<UserPrivateDTO> userPrivateDTO = Optional.of(new UserPrivateDTO(username, role, email));
        testRestTemplate.getForObject(url, UserPrivateDTO[].class);
        Mockito.when(userService.viewIndividualProfileData(username)).thenReturn(userPrivateDTO);
        var response = userService.viewIndividualProfileData(username);
        Assertions.assertEquals(userPrivateDTO, response);
        Mockito.verify(userService).viewIndividualProfileData(username);
    }

    @Test
    void viewIndividualProfile_returnsNull() {
        testRestTemplate.getForObject(url, UserPrivateDTO[].class);
        Mockito.when(userService.viewIndividualProfileData(username)).thenReturn(null);
        var response = userService.viewIndividualProfileData(username);
        Assertions.assertNull(response);
        Mockito.verify(userService).viewIndividualProfileData(username);
    }

}