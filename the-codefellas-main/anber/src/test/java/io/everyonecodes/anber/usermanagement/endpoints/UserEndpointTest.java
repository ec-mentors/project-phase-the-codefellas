package io.everyonecodes.anber.usermanagement.endpoints;

import io.everyonecodes.anber.usermanagement.data.User;
import io.everyonecodes.anber.usermanagement.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.validation.Validator;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserEndpointTest {

    @Autowired
    TestRestTemplate testRestTemplate;

    @MockBean
    UserService userService;

    @Value("${testvalues.user-endpoint-postUser-url}")
    String url;

    @Value("${testvalues.username}")
    String username;

    @Value("${testvalues.role}")
    String role;

    @Value("${testvalues.email}")
    String email;

    @Value("${testvalues.password}")
    String password;

    private Validator validator;

    @Test
    void registerUser_Valid() {
        User testUser = new User(username, password, email, role);
        testRestTemplate.postForObject(url, testUser, User[].class);
        Mockito.when(userService.saveUser(testUser)).thenReturn(testUser);
        var response = userService.saveUser(testUser);
        Assertions.assertEquals(testUser, response);
        Mockito.verify(userService).saveUser(testUser);
        Mockito.verifyNoMoreInteractions(userService);
    }

}