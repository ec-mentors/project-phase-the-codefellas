package io.everyonecodes.anber.usermanagement.service;


import io.everyonecodes.anber.usermanagement.data.User;
import io.everyonecodes.anber.usermanagement.data.UserPrivateDTO;
import io.everyonecodes.anber.usermanagement.data.UserPublicDTO;
import io.everyonecodes.anber.usermanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserDTO mapper;
    private final String roleUser;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, UserDTO mapper,
                       @Value("${data.roles.user}") String roleUser) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.mapper = mapper;
        this.roleUser = roleUser;
    }

    public User saveUser(User user) throws IllegalArgumentException {
        if (!user.getPassword().matches("(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!?@#$^&+=/_-])(?=\\S+$).{6,100}") || (!isEmailValid(user.getEmail())))
            throw new IllegalArgumentException();
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        user.setRole(roleUser);
        user.setUsername(user.getEmail());
        user = userRepository.save(user);
        return user;
    }

    public Optional<User> getUserByUsername(String username) {
        return userRepository.findOneByUsername(username);
    }

    // already coded for viewing the profile - tests for it done.
    public Optional<UserPrivateDTO> viewUserPrivateData(String username) {
        return getUserByUsername(username).map(mapper::toUserPrivateDTO);
    }

    public Optional<UserPublicDTO> viewUserPublicData(String username) {
        return userRepository.findOneByUsername(username).map(mapper::toUserPublicDTO);
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

}
