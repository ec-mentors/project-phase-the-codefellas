package io.everyonecodes.anber.usermanagement.service;

import io.everyonecodes.anber.usermanagement.data.User;
import io.everyonecodes.anber.usermanagement.data.UserPrivateDTO;
import io.everyonecodes.anber.usermanagement.data.UserPublicDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class UserDTOTest {

    @Autowired
    UserDTO userDTO;

    @Test
    void toUserPrivateDTO() {
        User user = new User(
                "username",
                "password",
                "email",
                "role");
        UserPrivateDTO result = userDTO.toUserPrivateDTO(user);
        UserPrivateDTO expected = new UserPrivateDTO("username",
                "role",
                "email");
        Assertions.assertEquals(expected, result);
    }

    @Test
    void testUserToPrivateUserDTO_nullValues() {
        User user = new User(
                "username",
                "password",
                null,
                "role");
        UserPrivateDTO result = userDTO.toUserPrivateDTO(user);
        UserPrivateDTO expected = new UserPrivateDTO("username",
                "role",
                null);
        Assertions.assertEquals(expected, result);
    }

    @Test
    void toUserPublicDTO() {
        String username = "username";
        User user = new User(username, "password", "email", "role");
        UserPublicDTO result = userDTO.toUserPublicDTO(user);
        UserPublicDTO expected = new UserPublicDTO(username);
        Assertions.assertEquals(expected, result);

    }

    @Test
    void toUserPublicDTO_nullValues() {
        String username = "username";
        User user = new User(username, "password", null, "role");
        UserPublicDTO result = userDTO.toUserPublicDTO(user);
        UserPublicDTO expected = new UserPublicDTO(username);
        Assertions.assertEquals(expected, result);
    }

}