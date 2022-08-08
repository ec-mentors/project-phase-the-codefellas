package io.everyonecodes.anber.profilemanagement.service;

import io.everyonecodes.anber.usermanagement.data.User;
import io.everyonecodes.anber.usermanagement.repository.UserRepository;
import io.everyonecodes.anber.usermanagement.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Optional;
import java.util.stream.Stream;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class UserProfileServiceTest {

    @Autowired
    UserProfileService userProfileService;

    @MockBean
    UserRepository userRepository;

    @MockBean
    UserService userService;

    @MockBean
    SecurityFilterChain filterChain;

    @MockBean
    PasswordEncoder encoder;


    private final String username = "test@mail.com";
    private final User user = new User(username, "password");

//    Authentication authentication = Mockito.mock(Authentication.class);
//    SecurityContext securityContext = Mockito.mock(SecurityContext.class);
//
//
//    @BeforeEach
//    public void initSecurityContext() {
//        Mockito.when(authentication.getPrincipal()).thenReturn(user.getEmail());
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//    }

    @Test
    void viewAll() {
        userProfileService.viewAll();
        Mockito.verify(userRepository).findAll();
    }

    @Test
    void viewProfile_Exists() {
//        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
//        SecurityContextHolder.setContext(securityContext);

        Mockito.when(userRepository.findOneByEmail(user.getEmail())).thenReturn(Optional.of(user));
        Mockito.when(userService.loggedInUser()).thenReturn(username);

        var oResult = userProfileService.viewProfile(username);

        var expected = Optional.of(user);

        Assertions.assertEquals(expected, oResult);

        Mockito.verify(userRepository).findOneByEmail(user.getEmail());
        Mockito.verify(userService).loggedInUser();
    }

    @Test
    void viewProfile_UserNotExist() {
        Mockito.when(userRepository.findOneByEmail(user.getEmail())).thenReturn(Optional.empty());

        var oResult = userProfileService.viewProfile(username);

        Assertions.assertEquals(Optional.empty(), oResult);

        Mockito.verify(userRepository).findOneByEmail(user.getEmail());
        Mockito.verify(userService, Mockito.never()).loggedInUser();
    }

    @Test
    void deleteProfile() {

        Mockito.when(userRepository.findOneByEmail(username)).thenReturn(Optional.of(user));

        userProfileService.deleteProfile(username);

        Mockito.verify(userRepository).delete(user);
    }


    @ParameterizedTest
    @MethodSource("parameters")
    void editData_profileExists(String input, String option, String expected) {

        Mockito.when(userRepository.findOneByEmail(username)).thenReturn(Optional.of(user));

        var oResult = userProfileService.editData(username, option, input);

        Assertions.assertEquals(Optional.of(expected), oResult);

        Mockito.verify(userRepository).findOneByEmail(username);
    }
    private static Stream<Arguments> parameters() {
        return Stream.of(
                Arguments.of("somewhere", "country", "somewhere"),
                Arguments.of("someone", "username", "someone"),
                Arguments.of("new@email.com", "email", "new@email.com"),
                Arguments.of("123456", "password", "123456"),
                Arguments.of("true", "notificationsEnabled", "true")
        );
    }
}