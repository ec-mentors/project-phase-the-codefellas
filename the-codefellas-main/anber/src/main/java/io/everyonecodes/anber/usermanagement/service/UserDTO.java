package io.everyonecodes.anber.usermanagement.service;

import io.everyonecodes.anber.usermanagement.data.User;
import io.everyonecodes.anber.usermanagement.data.UserPrivateDTO;
import io.everyonecodes.anber.usermanagement.data.UserPublicDTO;
import org.springframework.stereotype.Service;

@Service
public class UserDTO {


    public UserPrivateDTO toUserPrivateDTO(User user) {
        return new UserPrivateDTO(
                user.getUsername(),
                user.getFirstName(),
                user.getLastName(),
                user.getSavedHomes(),
                user.getRole(),
                user.getEmail(),
                user.getSubscriptions()
        );
    }

    public UserPublicDTO toUserPublicDTO(User user) {
        if (user == null) {
            return null;
        }
        return new UserPublicDTO(user.getEmail());
    }

}
