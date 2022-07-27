package io.everyonecodes.anber.service;



import io.everyonecodes.anber.data.User;
import io.everyonecodes.anber.data.UserPrivateDTO;
import io.everyonecodes.anber.data.UserPublicDTO;

import java.security.Principal;
import java.util.Optional;

public interface UserService {

    void saveUser(User user);

    void saveAdmin(User user);

    boolean isUserAlreadyPresent(User user);

    Optional<User> findUserByResetToken(String token);

    Optional<User> findUserByEmail(String userEmail);

    User loadLoggedInUser(Principal principal);

    boolean isUserValid(User user);

    void deleteUserByEmail(User user);

    void deleteUserByUsername(Object principal);

    // already coded for viewing the profile - tests for it done.
    Optional<UserPrivateDTO> viewUserPrivateData(String username);

    Optional<UserPublicDTO> viewUserPublicData(String email);

    Optional<UserPrivateDTO> viewIndividualProfileData(String username);
}
