package io.everyonecodes.anber.endpointsOld;

import io.everyonecodes.anber.data.User;
import io.everyonecodes.anber.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserEndpointTest {

    @Autowired
    TestRestTemplate testRestTemplate;

    @MockBean
    UserService userService;

    @Value("${testvalues.user-endpoint-url}")
    String url;

    @Value("${testvalues.username}")
    String username;

    @Value("${testvalues.role}")
    String role;

    @Value("${testvalues.email}")
    String email;

    @Value("${testvalues.password}")
    String password;

    private final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = validatorFactory.getValidator();

    @Test
    void registerUser_Valid() {
        User testUser = new User("name", email, password);
        testRestTemplate.postForObject(url, testUser, User[].class);
        userService.saveUser(testUser);
        Mockito.verify(userService).saveUser(testUser);
        Mockito.verifyNoMoreInteractions(userService);
    }

    @Test
    void registerUser_NotValid() {
        User testUser = new User("name", email, "123");

        Set<ConstraintViolation<User>> violations = validator.validate(testUser);

        Assertions.assertEquals(violations.size(), 1);

        ConstraintViolation<User> violation = violations.iterator().next();

        Assertions.assertEquals("Password needs to be 8 characters in length and must contain at least one lower case letter, one upper case letter, one number and one special character", violation.getMessage());
        Assertions.assertEquals("password", violation.getPropertyPath().toString());

        Assertions.assertEquals("123", violation.getInvalidValue());
    }
}