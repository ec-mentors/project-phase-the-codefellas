package io.everyonecodes.anber.profilemanagement.service;

import io.everyonecodes.anber.profilemanagement.data.UserProfile;
import io.everyonecodes.anber.profilemanagement.repository.UserProfileRepository;
import io.everyonecodes.anber.usermanagement.data.User;
import io.everyonecodes.anber.usermanagement.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class UserProfileServiceTest {

    @Autowired
    UserProfileService userProfileService;

    @MockBean
    UserProfileRepository userProfileRepository;

    @MockBean
    UserRepository userRepository;

    @Value("${data.boolean.true}")
    String boolTrue;

    @MockBean
    SecurityFilterChain filterChain;

    @MockBean
    PasswordEncoder encoder;


    private final String username = "test@mail.com";
    private final User user = new User("test", "password", "test@mail.com", "ROLE_USER");
    private final UserProfile profile = new UserProfile(user.getEmail(), user.getPassword(), user.getUsername(), "country", List.of(), false);

    Authentication authentication = Mockito.mock(Authentication.class);
    SecurityContext securityContext = Mockito.mock(SecurityContext.class);


    @BeforeEach
    public void initSecurityContext() {
        Mockito.when(authentication.getPrincipal()).thenReturn(user.getEmail());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Test
    void viewAll() {
        userProfileService.viewAll();
        Mockito.verify(userProfileRepository).findAll();
    }

    @Test
    void viewProfile_Exists() {
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        Mockito.when(userRepository.findOneByEmail(user.getEmail())).thenReturn(Optional.of(user));
        Mockito.when(userProfileRepository.findOneByEmail(profile.getEmail())).thenReturn(Optional.of(profile));

        var oResult = userProfileService.viewProfile(username);

        var expected = Optional.of(profile);

        Assertions.assertEquals(expected, oResult);

        Mockito.verify(userRepository).findOneByEmail(user.getEmail());
        Mockito.verify(userProfileRepository).findOneByEmail(profile.getEmail());
    }

    @Test
    void viewProfile_UserNotExist() {
        Mockito.when(userRepository.findOneByEmail(user.getEmail())).thenReturn(Optional.empty());
        Mockito.when(userProfileRepository.findOneByEmail(profile.getEmail())).thenReturn(Optional.empty());

        var oResult = userProfileService.viewProfile(username);

        Assertions.assertEquals(Optional.empty(), oResult);

        Mockito.verify(userRepository).findOneByEmail(user.getEmail());
        Mockito.verify(userProfileRepository, Mockito.never()).findOneByEmail(profile.getEmail());
    }

    @Test
    void viewProfile_ProfileNotExist() {
        UserProfile newProfile = new UserProfile(user.getEmail(), encoder.encode(user.getPassword()));

        Mockito.when(userRepository.findOneByEmail(user.getEmail())).thenReturn(Optional.of(user));
        Mockito.when(userProfileRepository.findOneByEmail(profile.getEmail())).thenReturn(Optional.empty());
        Mockito.when(userProfileRepository.save(newProfile)).thenReturn((newProfile));

        var oResult = userProfileService.viewProfile(username);

        var expected = Optional.of(newProfile);

        Assertions.assertEquals(expected, oResult);

        Mockito.verify(userRepository).findOneByEmail(user.getEmail());
        Mockito.verify(userProfileRepository).findOneByEmail(profile.getEmail());
        Mockito.verify(userProfileRepository).save(newProfile);
    }

    @Test
    void deleteProfile() {

        Mockito.when(userProfileRepository.findOneByEmail(username)).thenReturn(Optional.of(profile));

        userProfileService.deleteProfile(username);

        Mockito.verify(userProfileRepository).delete(profile);
    }


    @ParameterizedTest
    @MethodSource("parameters")
    void editData_profileExists(String input, String option, String expected) {

        Mockito.when(userProfileRepository.findOneByEmail(username)).thenReturn(Optional.of(profile));

        var oResult = userProfileService.editData(username, option, input);

        Assertions.assertEquals(Optional.of(expected), oResult);

        Mockito.verify(userProfileRepository).findOneByEmail(username);
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