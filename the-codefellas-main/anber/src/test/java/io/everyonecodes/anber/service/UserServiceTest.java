package io.everyonecodes.anber.service;

import io.everyonecodes.anber.data.User;
import io.everyonecodes.anber.data.UserPrivateDTO;
import io.everyonecodes.anber.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class UserServiceTest {

    @Autowired
    UserService userService;

    @MockBean
    PasswordEncoder passwordEncoder;

    @MockBean
    UserRepository userRepository;

    @MockBean
    UserDTO userDTO;

    @MockBean
    SecurityFilterChain filterChain;

    private final String email = "test@email.com";

    private final User userTest = new User(email, "");

    @ParameterizedTest
    @CsvSource({
            "''", //empty
            "'tEst123'", //special char
            "'tesTIng#'", //number
            "'testing1#'", //Uppercase
            "'TESTING1#'", //lowercase
            "'Testing 12#'", // with blank space
            "'tT#1'", //too short
            "'Coding123#0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890'", //too long
            "'testIng12§'" //wrong special char
    })
    void saveUser_invalidPassword(String password) {
        userTest.setPassword(password);
        Assertions.assertThrows(IllegalArgumentException.class, () -> userService.saveUser(userTest));
    }

    @ParameterizedTest
    @CsvSource({
            "'Coding12#'", // simple valid Password
            "'Test0!?@#$^&+=/_-'", // verifying all valid special chars
            "'Coding123#012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789'", //MaxSize 100
            "'Test1#'" //MinSize 6
    })
    void saveUser_validPassword(String password) {
        userTest.setPassword(password);
        Mockito.when(passwordEncoder.encode(userTest.getPassword())).thenReturn(password);
        Assertions.assertDoesNotThrow(() -> userService.saveUser(userTest));
        Mockito.verify(userRepository, Mockito.times(1)).save(userTest);
    }

    @Test
    void viewUserPrivateData_UserFound() {
        User user = new User(email, "password");
        UserPrivateDTO userPrivateDTO = new UserPrivateDTO(email, user.getRoles(), user.getEmail());
        Mockito.when(userRepository.findOneByEmail(email)).thenReturn(Optional.of(user));
        Mockito.when(userDTO.toUserPrivateDTO(user)).thenReturn(userPrivateDTO);
        var oResult = userService.viewUserPrivateData(email);
        Assertions.assertEquals(Optional.of(userPrivateDTO), oResult);
        Mockito.verify(userRepository, Mockito.times(1)).findOneByEmail(email);
        Mockito.verify(userDTO, Mockito.times(1)).toUserPrivateDTO(user);
    }

    @Test
    void viewUserPrivateData_UserNotFound() {
        Mockito.when(userRepository.findOneByEmail(email)).thenReturn(Optional.empty());
        var oResult = userService.viewUserPrivateData(email);
        Assertions.assertEquals(Optional.empty(), oResult);
        Mockito.verify(userRepository, Mockito.times(1)).findOneByEmail(email);
        Mockito.verify(userDTO, Mockito.never()).toUserPrivateDTO(Mockito.any(User.class));
    }

    @Test
    void viewUserPublicData_UserNotFound() {
        Mockito.when(userRepository.findOneByEmail(email)).thenReturn(Optional.empty());
        var oResult = userService.viewUserPublicData(email);
        Assertions.assertEquals(Optional.empty(), oResult);
        Mockito.verify(userRepository, Mockito.times(1)).findOneByEmail(email);
        Mockito.verify(userDTO, Mockito.never()).toUserPublicDTO(Mockito.any(User.class));
    }
}