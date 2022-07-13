package io.everyonecodes.anber.usermanagement.service;


import io.everyonecodes.anber.usermanagement.data.User;
import io.everyonecodes.anber.usermanagement.data.UserPrivateDTO;
import io.everyonecodes.anber.usermanagement.data.UserPublicDTO;
import io.everyonecodes.anber.usermanagement.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserDTO mapper;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, UserDTO mapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.mapper = mapper;
    }

    public User saveUser(User user) throws IllegalArgumentException {
        if (!user.getPassword().matches("(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!?@#$^&+=/_-])(?=\\S+$).{6,100}")) throw new IllegalArgumentException();
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        user.setRole("ROLE_INDIVIDUAL");
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
    
}
