package io.everyonecodes.anber.profilemanagement.service;

import io.everyonecodes.anber.profilemanagement.data.UserProfile;
import io.everyonecodes.anber.profilemanagement.repository.UserProfileRepository;
import io.everyonecodes.anber.usermanagement.data.User;
import io.everyonecodes.anber.usermanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserProfileService {

    private final UserProfileRepository userProfileRepository;
    private final UserRepository userRepository;
    private final List<String> profileOptions;
    private final String boolTrue;

    public UserProfileService(UserProfileRepository userProfileRepository, UserRepository userRepository,
                              List<String> profileOptions, @Value("${data.boolean.true}") String boolTrue) {
        this.userProfileRepository = userProfileRepository;
        this.userRepository = userRepository;
        this.profileOptions = profileOptions;
        this.boolTrue = boolTrue;
    }


    public Optional<UserProfile> viewProfile(String username) {
        Optional<User> oUser = userRepository.findOneByUsername(username);
        if (oUser.isPresent()) {
            User user = oUser.get();
            return userProfileRepository.findOneByUsername(user.getUsername());
        }
        return Optional.empty();
    }


    public void deleteProfile(String username) {
        var oProfile = userProfileRepository.findOneByUsername(username);
        oProfile.ifPresent(userProfileRepository::delete);
    }

    public Optional<UserProfile> addData(String username, UserProfile input) {
        var oProfile = userProfileRepository.findOneByUsername(username);
        var oUser = userRepository.findOneByUsername(username);

        var exists = userProfileRepository.findAll().contains(input);
        if (oProfile.isPresent() && oUser.isPresent()) {
            User user = oUser.get();
            UserProfile profile = oProfile.get();
            if (!exists && (user.getPassword().equals(profile.getPassword()))) {

                userProfileRepository.save(input);

                return Optional.of(input);
            }
        }
        return Optional.empty();
    }


    public Optional<String> editData(String username, String option, String input) {

        var oProfile = userProfileRepository.findOneByUsername(username);

        if (oProfile.isPresent()) {

            UserProfile profile = oProfile.get();

            overwriteData(option, profile, input);

            return Optional.of(input);
        }
        return Optional.empty();
    }


    private void overwriteData(String option, UserProfile userProfile, String input) {

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
            userProfileRepository.save(userProfile);
        }
    }

}
