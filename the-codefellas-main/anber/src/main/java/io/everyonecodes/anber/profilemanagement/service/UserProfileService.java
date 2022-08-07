package io.everyonecodes.anber.profilemanagement.service;

import io.everyonecodes.anber.usermanagement.data.User;
import io.everyonecodes.anber.usermanagement.repository.UserRepository;
import io.everyonecodes.anber.usermanagement.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserProfileService {

    private final UserRepository userRepository;
    private final List<String> profileOptions;
    private final String boolTrue;
    private final UserService userService;

    public UserProfileService(UserRepository userRepository,
                              List<String> profileOptions,
                              @Value("${data.boolean.true}") String boolTrue,
                              UserService userService) {
        this.userRepository = userRepository;
        this.profileOptions = profileOptions;
        this.boolTrue = boolTrue;
        this.userService = userService;
    }

    private String loggedInUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        } else {
            return principal.toString();
        }
    }

    public List<User> viewAll() {
        return userRepository.findAll();
    }


    public Optional<User> viewProfile(String username) {

        Optional<User> oUser = userRepository.findOneByEmail(username);
        if (oUser.isPresent()) {
            User user = oUser.get();
            if (user.getEmail().equals(userService.loggedInUser())) {
                return oUser;
            }
        }
        return Optional.empty();
    }


    public void deleteProfile(String username) {
        var oProfile = userRepository.findOneByEmail(username);
        oProfile.ifPresent(userRepository::delete);
    }


    public Optional<String> editData(String username, String option, String input) {

        var oProfile = userRepository.findOneByEmail(username);

        if (oProfile.isPresent()) {

            User profile = oProfile.get();

            overwriteData(option, profile, input);

            return Optional.of(input);
        }
        return Optional.empty();
    }


    private void overwriteData(String option, User userProfile, String input) {

        if (profileOptions.contains(option)) {
            if (option.equals(profileOptions.get(0))) {
                userProfile.setEmail(input);
            }
            if (option.equals(profileOptions.get(1))) {
                userProfile.setPassword(input);
            }
            if (option.equals(profileOptions.get(2))) {
                userProfile.setUsername(input);
            }
            if (option.equals(profileOptions.get(3))) {
                userProfile.setCountry(input);
            }
            if (option.equals(profileOptions.get(5))) {
                userProfile.setNotificationsEnabled(input.equals(boolTrue));
            }
            userRepository.save(userProfile);
        }
    }

}
