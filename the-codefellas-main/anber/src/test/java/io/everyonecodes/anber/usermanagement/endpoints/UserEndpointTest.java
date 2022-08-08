package io.everyonecodes.anber.usermanagement.endpoints;

import io.everyonecodes.anber.usermanagement.data.User;
import io.everyonecodes.anber.usermanagement.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;
import java.util.stream.Stream;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class UserEndpointTest {

    @Autowired
    TestRestTemplate testRestTemplate;

    @Autowired
    MockMvc mockMvc;

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

    @MockBean
    SecurityFilterChain securityFilterChain;

    private final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = validatorFactory.getValidator();


    @Test
    void registerUser_Valid() {
        User testUser = new User("firstName", "lastName", email, password);
        testRestTemplate.postForObject(url, testUser, User[].class);
        Mockito.verify(userService).saveUser(testUser);
        Mockito.verifyNoMoreInteractions(userService);
    }

    @ParameterizedTest
    @MethodSource("parameters")
    void registerUser_NotValid(User input, String expectedMessage, String expectedPath, String expectedValue, int violationSize) {

        Set<ConstraintViolation<User>> violations = validator.validate(input);

        Assertions.assertEquals(violations.size(), violationSize);

        ConstraintViolation<User> violation = violations.iterator().next();

        Assertions.assertEquals(expectedMessage, violation.getMessage());
        Assertions.assertEquals(expectedPath, violation.getPropertyPath().toString());
        Assertions.assertEquals(expectedValue, violation.getInvalidValue());
    }
    private static Stream<Arguments> parameters() {
        return Stream.of(
                Arguments.of(new User("testo", "testman", "test@email.com", "123"), "must be at least 6 characters long", "password", "123", 1),

                Arguments.of(new User("testo", "testman", "testemail", "Password1!"), "must be a valid Email", "email", "testemail", 1),

                Arguments.of(new User( "", "testman", "test@email.com", "Password1!"), "first name must not be empty", "firstName", "", 1),

                Arguments.of(new User("testo", "", "test@email.com", "Password1!"), "last name must not be empty", "lastName", "", 1)

        );
    }

    @Test
    @WithMockUser(username = "test@email.com", password = "Password1!", roles = "USER")
    void deleteUser_Self() throws Exception {

        String url = "/users/" + email + "/delete";

        mockMvc.perform(MockMvcRequestBuilders.delete(url))
                .andExpect(MockMvcResultMatchers.status().isOk());

        Mockito.verify(userService).deleteUser(email);
    }

    @Test
    @WithMockUser(username = "wrong@email.com", password = "Password1!", roles = "USER")
    void deleteUser_NotSelf() throws Exception {

        String url = "/users/" + email + "/delete";

        mockMvc.perform(MockMvcRequestBuilders.delete(url))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());

        Mockito.verify(userService, Mockito.never()).deleteUser("wrong@email.com");
    }

    @Test
    @WithMockUser(username = "admin@email.com", password = "admin", roles = "ADMIN")
    void getAllProfiles() throws Exception {

        String url = "/users/" + email + "/unlock";

        mockMvc.perform(MockMvcRequestBuilders.put(url))
                .andExpect(MockMvcResultMatchers.status().isOk());

        Mockito.verify(userService).unlockUser(email);
    }
}