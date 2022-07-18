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

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

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
        User testUser = new User(email, password);
        testRestTemplate.postForObject(url, testUser, User[].class);
        Mockito.verify(userService).saveUser(testUser);
        Mockito.verifyNoMoreInteractions(userService);
    }

    @Test
    void registerUser_NotValid() {
        User testUser = new User(email, "123");

        Set<ConstraintViolation<User>> violations = validator.validate(testUser);

        Assertions.assertEquals(violations.size(), 1);

        ConstraintViolation<User> violation = violations.iterator().next();

        Assertions.assertEquals("must be at least 6 characters long.", violation.getMessage());
        Assertions.assertEquals("password", violation.getPropertyPath().toString());

        Assertions.assertEquals("123", violation.getInvalidValue());
    }
}