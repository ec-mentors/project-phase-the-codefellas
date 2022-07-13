package io.everyonecodes.anber.usermanagement.service;

import io.everyonecodes.anber.usermanagement.data.User;
import io.everyonecodes.anber.usermanagement.data.UserPrivateDTO;
import io.everyonecodes.anber.usermanagement.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;


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

    private final User userTest = new User("name", "", "email", "role");

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
        String username = "username";
        User user = new User(username, "password", "email", "role");
        UserPrivateDTO userPrivateDTO = new UserPrivateDTO(username, user.getEmail(), user.getRole());
        Mockito.when(userRepository.findOneByUsername(username)).thenReturn(Optional.of(user));
        Mockito.when(userDTO.toUserPrivateDTO(user)).thenReturn(userPrivateDTO);
        var oResult = userService.viewUserPrivateData(username);
        Assertions.assertEquals(Optional.of(userPrivateDTO), oResult);
        Mockito.verify(userRepository, Mockito.times(1)).findOneByUsername(username);
        Mockito.verify(userDTO, Mockito.times(1)).toUserPrivateDTO(user);
    }

    @Test
    void viewUserPrivateData_UserNotFound() {
        String username = "username";
        Mockito.when(userRepository.findOneByUsername(username)).thenReturn(Optional.empty());
        var oResult = userService.viewUserPrivateData(username);
        Assertions.assertEquals(Optional.empty(), oResult);
        Mockito.verify(userRepository, Mockito.times(1)).findOneByUsername(username);
        Mockito.verify(userDTO, Mockito.never()).toUserPrivateDTO(Mockito.any(User.class));
    }

    @Test
    void viewUserPublicData_UserNotFound() {
        String username = "username";
        Mockito.when(userRepository.findOneByUsername(username)).thenReturn(Optional.empty());
        var oResult = userService.viewUserPublicData(username);
        Assertions.assertEquals(Optional.empty(), oResult);
        Mockito.verify(userRepository, Mockito.times(1)).findOneByUsername(username);
        Mockito.verify(userDTO, Mockito.never()).toUserPublicDTO(Mockito.any(User.class));
    }
}