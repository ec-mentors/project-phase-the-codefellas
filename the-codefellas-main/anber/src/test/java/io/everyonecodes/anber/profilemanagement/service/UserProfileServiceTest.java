package io.everyonecodes.anber.profilemanagement.service;

import io.everyonecodes.anber.profilemanagement.data.UserProfile;
import io.everyonecodes.anber.profilemanagement.repository.UserProfileRepository;
import io.everyonecodes.anber.usermanagement.data.User;
import io.everyonecodes.anber.usermanagement.repository.UserRepository;
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


    private final String username = "test";
    private final User user = new User("test", "password", "test@mail.com", "ROLE_USER");
    private final UserProfile profile = new UserProfile(user.getEmail(), user.getPassword(), user.getUsername(), "country", List.of(), false);

    @Test
    void viewProfile() {
        Mockito.when(userRepository.findOneByUsername(username)).thenReturn(Optional.of(user));
        Mockito.when(userProfileRepository.findOneByUsername(user.getUsername())).thenReturn(Optional.of(profile));

        var oResult = userProfileService.viewProfile(username);

        var expected = Optional.of(profile);

        Assertions.assertEquals(expected, oResult);

        Mockito.verify(userRepository).findOneByUsername(username);
        Mockito.verify(userProfileRepository).findOneByUsername(user.getUsername());
    }

    @Test
    void deleteProfile() {

        Mockito.when(userProfileRepository.findOneByUsername(username)).thenReturn(Optional.of(profile));

        userProfileService.deleteProfile(username);

        Mockito.verify(userProfileRepository).delete(profile);
    }

    @Test
    void addData() {
        Mockito.when(userProfileRepository.findOneByUsername(username)).thenReturn(Optional.of(profile));
        Mockito.when(userRepository.findOneByUsername(username)).thenReturn(Optional.of(user));

        var oResult = userProfileService.addData(username, profile);

        Assertions.assertEquals(Optional.of(profile), oResult);

        Mockito.verify(userProfileRepository).save(profile);
    }


    @ParameterizedTest
    @MethodSource("parameters")
    void test(String input, String option, String expected) {

        Mockito.when(userProfileRepository.findOneByUsername(username)).thenReturn(Optional.of(profile));

        var oResult = userProfileService.editData(username, option, input);

        Assertions.assertEquals(Optional.of(expected), oResult);

        Mockito.verify(userProfileRepository).findOneByUsername(username);
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