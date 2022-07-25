package io.everyonecodes.anber.service;


import io.everyonecodes.anber.data.Role;
import io.everyonecodes.anber.data.User;
import io.everyonecodes.anber.data.UserPrivateDTO;
import io.everyonecodes.anber.data.UserPublicDTO;
import io.everyonecodes.anber.repository.RoleRepository;
import io.everyonecodes.anber.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImp implements UserService {
    private final PasswordEncoder encoder;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final UserDTO mapper;
    
    private final String roleName;
    private final String roleDescription;

    public UserServiceImp(PasswordEncoder encoder, RoleRepository roleRepository, UserRepository userRepository, UserDTO mapper, @Value("${messages.user.userRole.name}") String roleName, @Value("${messages.user.userRole.description}") String roleDescription) {
        this.encoder = encoder;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.mapper = mapper;
        this.roleName = roleName;
        this.roleDescription = roleDescription;
    }

    @Override
    public void saveUser(User user) {
        if (!isUserAlreadyPresent(user)) {

            prepareUserDetails(user);
            user.setVerified(false);

        }
        userRepository.save(user);
    }
    @Override
    public void saveAdmin(User user) {
        if (!isUserAlreadyPresent(user)) {
 
            prepareUserDetails(user);
            user.setVerified(true);
        }
        
        userRepository.save(user);
    }
    
    private void prepareUserDetails(User user) {
        if (!user.getPassword().matches("(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!?@#$^&+=/_-])(?=\\S+$).{6,100}"))
            throw new IllegalArgumentException();
        user.setPassword(encoder.encode(user.getPassword()));
        user.setAccountNonLocked(true);
        Role userRole = roleRepository.findByRole(roleName);
        if (userRole == null) {
            userRole = new Role(roleName, roleDescription);
        }
        
        user.setRoles(new HashSet<>(List.of(userRole)));
    }

    @Override
    public boolean isUserAlreadyPresent(User user) {
        return userRepository.existsByEmail(user.getEmail());
    }

    @Override
    public Optional<User> findUserByResetToken(String token) {
        return userRepository.findUserByResetToken(token);
    }

    @Override
    public Optional<User> findUserByEmail(String userEmail) {
        return userRepository.findByEmail(userEmail);
    }

    @Override
    public User loadLoggedInUser(Principal principal) {
        String userName = principal.getName();
        return findUserByEmail(userName).orElse(null);
    }

    @Override
    public boolean isUserValid(User user) {
        var oUser =  userRepository.findByEmail(user.getEmail());
        return oUser.map(User::isVerified).orElse(false);
    }

    @Override
    public void deleteUserByEmail(User user) {
        Optional<User> oUser = userRepository.findByEmail(user.getEmail());
        if (oUser.isPresent()){
            userRepository.delete(user);
        }
    }

    public void deleteUserByUsername(Object principal) {
        Optional<User> oUser = userRepository.findByUsername(principal.toString());
        oUser.ifPresent(userRepository::delete);

    }
    public Optional<User> getUserByUsername(String email) {
        return userRepository.findOneByEmail(email);
    }

    // already coded for viewing the profile - tests for it done.
    @Override
    public Optional<UserPrivateDTO> viewUserPrivateData(String username) {
        return getUserByUsername(username).map(mapper::toUserPrivateDTO);
    }

    @Override
    public Optional<UserPublicDTO> viewUserPublicData(String email) {
        return userRepository.findOneByEmail(email).map(mapper::toUserPublicDTO);
    }

    @Override
    public Optional<UserPrivateDTO> viewIndividualProfileData(String username) {
        return getUserByUsername(username).map(mapper::toUserPrivateDTO);
    }

}
