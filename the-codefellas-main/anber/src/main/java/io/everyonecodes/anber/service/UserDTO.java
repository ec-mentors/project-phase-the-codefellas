package io.everyonecodes.anber.service;

import io.everyonecodes.anber.data.User;
import io.everyonecodes.anber.data.UserPrivateDTO;
import io.everyonecodes.anber.data.UserPublicDTO;
import org.springframework.stereotype.Service;

@Service
public class UserDTO {


    public UserPrivateDTO toUserPrivateDTO(User user) {
        return new UserPrivateDTO(
                user.getUsername(),
                user.getRoles(),
                user.getEmail()
        );
    }

    public UserPublicDTO toUserPublicDTO(User user) {
        if (user == null) {
            return null;
        }
        return new UserPublicDTO(user.getEmail());
    }

}
