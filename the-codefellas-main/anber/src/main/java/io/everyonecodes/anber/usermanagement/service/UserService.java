package io.everyonecodes.anber.usermanagement.service;


import io.everyonecodes.anber.email.service.EmailService;
import io.everyonecodes.anber.usermanagement.authentication.AuthenticationService;
import io.everyonecodes.anber.usermanagement.data.User;
import io.everyonecodes.anber.usermanagement.data.UserPrivateDTO;
import io.everyonecodes.anber.usermanagement.data.UserPublicDTO;
import io.everyonecodes.anber.usermanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationService authenticationService;
    private final EmailService emailService;
    private final UserDTO mapper;
    private final String roleUser;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationService authenticationService, EmailService emailService, UserDTO mapper,
                       @Value("${data.roles.user}") String roleUser) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationService = authenticationService;
        this.emailService = emailService;
        this.mapper = mapper;
        this.roleUser = roleUser;
    }

    public User saveUser(User user) throws IllegalArgumentException {
        if (!user.getPassword().matches("(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!?@#$^&+=/_-])(?=\\S+$).{6,100}") || (!isEmailValid(user.getEmail()) || user.getFirstName().isBlank() || user.getLastName().isBlank()))
            throw new IllegalArgumentException();
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        user.setRole(roleUser);
        user.setUsername(user.getEmail());
        user.setAccountNonLocked(true);
        user.setLoginAttempts(0);
        user = userRepository.save(user);
        return user;
    }

    public Optional<User> getUserByUsername(String email) {
        return userRepository.findOneByEmail(email);
    }

    // already coded for viewing the profile - tests for it done.
    public Optional<UserPrivateDTO> viewUserPrivateData(String username) {
        return getUserByUsername(username).map(mapper::toUserPrivateDTO);
    }

    public Optional<UserPublicDTO> viewUserPublicData(String email) {
        return userRepository.findOneByEmail(email).map(mapper::toUserPublicDTO);
    }

    public Optional<UserPrivateDTO> viewIndividualProfileData(String username) {
        return getUserByUsername(username).map(mapper::toUserPrivateDTO);
    }

    //additional check for valid email
    private boolean isEmailValid(String email) {
        if (email == null) return false;
        int at = email.indexOf("@");
        if (at < 0) return false;
        int dot = email.lastIndexOf(".");
        return at < dot;
    }

    public void deleteUser(String username) {
        var oProfile = userRepository.findOneByEmail(username);
        emailService.sendDeleteMail(username);
        oProfile.ifPresent(userRepository::delete);
    }

    public String loggedInUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        } else {
            return principal.toString();
        }
    }

    public Optional<UserPrivateDTO> viewIndividualProfileDataUser(User user) {
        if(authenticationService.onAuthentication(user)) {
            return getUserByUsername(user.getUsername()).map(mapper::toUserPrivateDTO);
        }
        return Optional.empty();
    }

    public boolean isUserUnlocked(User user) {
        var oUser = userRepository.findOneByEmail(user.getEmail());
        var foundUser = oUser.get();
        return foundUser.isAccountNonLocked();
    }

    public void unlockUser(String username) {
        var oUser = userRepository.findOneByEmail(username);
        if (oUser.isPresent()) {
            var user = oUser.get();
            user.setLoginAttempts(0);
            user.setAccountNonLocked(true);
            userRepository.save(user);
        }
    }
}
