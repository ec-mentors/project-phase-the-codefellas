package io.everyonecodes.anber.usermanagement.service;

import io.everyonecodes.anber.usermanagement.data.User;
import io.everyonecodes.anber.usermanagement.data.UserPrivateDTO;
import io.everyonecodes.anber.usermanagement.data.UserPublicDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.web.SecurityFilterChain;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class UserDTOTest {

    @Autowired
    UserDTO userDTO;

    @MockBean
    SecurityFilterChain filterChain;

    private final String email = "test@email.com";

    @Test
    void toUserPrivateDTO() {
        User user = new User(
                "test@email.com",
                "password");
        UserPrivateDTO result = userDTO.toUserPrivateDTO(user);
        UserPrivateDTO expected = new UserPrivateDTO("username",
                "role",
                "email");
        Assertions.assertEquals(expected, result);
    }

    @Test
    void testUserToPrivateUserDTO_nullValues() {
        User user = new User(
                null,
                "password");
        UserPrivateDTO result = userDTO.toUserPrivateDTO(user);
        UserPrivateDTO expected = new UserPrivateDTO("username",
                "role",
                null);
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