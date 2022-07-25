package io.everyonecodes.anber.service;

import io.everyonecodes.anber.data.Role;
import io.everyonecodes.anber.data.User;
import io.everyonecodes.anber.data.UserPrivateDTO;
import io.everyonecodes.anber.data.UserPublicDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class UserDTOTest {

    @Autowired
    UserDTO userDTO;

    @MockBean
    SecurityFilterChain filterChain;

    private final String email = "test@email.com";

    @Test
    void toUserPrivateDTO() {
        User user = new User("name", "email@email.com", true, "Coding12#");
        UserPrivateDTO result = userDTO.toUserPrivateDTO(user);
        UserPrivateDTO expected = new UserPrivateDTO(null,
                null,
                "email@email.com");
        Assertions.assertEquals(expected, result);
    }

    @Test
    void testUserToPrivateUserDTO_nullValues() {
        User user = new User(email,
                "password");
        UserPrivateDTO result = userDTO.toUserPrivateDTO(user);
        UserPrivateDTO expected = new UserPrivateDTO(null,
                null,
                email);
        Assertions.assertEquals(expected, result);
    }

    @Test
    void toUserPublicDTO() {
        User user = new User(email, "password");
        UserPublicDTO result = userDTO.toUserPublicDTO(user);
        UserPublicDTO expected = new UserPublicDTO(email);
        Assertions.assertEquals(expected, result);

    }

    @Test
    void toUserPublicDTO_nullValues() {
        User user = new User(email, "password");
        UserPublicDTO result = userDTO.toUserPublicDTO(user);
        UserPublicDTO expected = new UserPublicDTO(email);
        Assertions.assertEquals(expected, result);
    }

}