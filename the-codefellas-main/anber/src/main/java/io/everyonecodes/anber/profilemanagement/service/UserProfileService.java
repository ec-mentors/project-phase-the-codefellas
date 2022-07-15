package io.everyonecodes.anber.profilemanagement.service;

import io.everyonecodes.anber.profilemanagement.data.UserProfile;
import io.everyonecodes.anber.profilemanagement.repository.UserProfileRepository;
import io.everyonecodes.anber.usermanagement.data.User;
import io.everyonecodes.anber.usermanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserProfileService {

    private final UserProfileRepository userProfileRepository;
    private final UserRepository userRepository;
    private final List<String> profileOptions;
    private final String boolTrue;
    private final PasswordEncoder encoder;



    public UserProfileService(UserProfileRepository userProfileRepository, UserRepository userRepository,
                              List<String> profileOptions, @Value("${data.boolean.true}") String boolTrue, PasswordEncoder encoder) {
        this.userProfileRepository = userProfileRepository;
        this.userRepository = userRepository;
        this.profileOptions = profileOptions;
        this.boolTrue = boolTrue;
        this.encoder = encoder;
    }

    private String loggedInUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            return ((UserDetails)principal).getUsername();
        } else {
            return principal.toString();
        }
    }

    public List<UserProfile> viewAll(){
        return userProfileRepository.findAll();
    }


    public Optional<UserProfile> viewProfile(String username) {


        Optional<User> oUser = userRepository.findOneByEmail(username);
        if (oUser.isPresent()) {
            User user = oUser.get();
            Optional<UserProfile> oProfile = userProfileRepository.findOneByEmail(username);
            if (oProfile.isEmpty()) {
                UserProfile newProfile = new UserProfile();
                newProfile.setEmail(user.getEmail());
                newProfile.setPassword(encoder.encode(user.getPassword()));

                newProfile = userProfileRepository.save(newProfile);
                return Optional.of(newProfile);
            }
            else {
                if (oProfile.get().getEmail().equals(loggedInUser())) {
                    return oProfile;
                }
            }
        }
        return Optional.empty();
    }


    public void deleteProfile(String username) {
        var oProfile = userProfileRepository.findOneByEmail(username);
        oProfile.ifPresent(userProfileRepository::delete);
    }


    public Optional<String> editData(String username, String option, String input) {

        var oProfile = userProfileRepository.findOneByEmail(username);

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
