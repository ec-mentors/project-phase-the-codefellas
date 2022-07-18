package io.everyonecodes.anber.usermanagement.service;


import io.everyonecodes.anber.pwrecovery.GenericResponse;
import io.everyonecodes.anber.pwrecovery.PasswordTokenRepository;
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
    private final PasswordTokenRepository passwordTokenRepository;
    private final GenericResponse genericResponse;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, UserDTO mapper,
                       @Value("${data.roles.user}") String roleUser, PasswordTokenRepository passwordTokenRepository, GenericResponse genericResponse) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.mapper = mapper;
        this.roleUser = roleUser;
        this.passwordTokenRepository = passwordTokenRepository;
        this.genericResponse = genericResponse;
    }

    public User saveUser(User user) throws IllegalArgumentException {
        if (!user.getPassword().matches("(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!?@#$^&+=/_-])(?=\\S+$).{6,100}") || (!isEmailValid(user.getEmail())))
            throw new IllegalArgumentException();
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        user.setRole(roleUser);
//        user.setUsername(user.getEmail());
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

    ////////////////////////////////////////////////////////////////////////////////////////
    // PW Recovery Stuff starts here!
//
//    public void createPasswordResetTokenForUser(User user, String token) {
//        PasswordResetToken myToken = new PasswordResetToken(token, user);
//        passwordTokenRepository.save(myToken);
//    }
//
//    private SimpleMailMessage constructResetTokenEmail(
//            String contextPath, Locale locale, String token, User user) {
//        String url = contextPath + "/user/changePassword?token=" + token;
//        String message = messages.getMessage("message.resetPassword",
//                null, locale);
//        return constructEmail("Reset Password", message + " \r\n" + url, user);
//    }
//
//    private SimpleMailMessage constructEmail(String subject, String body,
//                                             User user) {
//        SimpleMailMessage email = new SimpleMailMessage();
//        email.setSubject(subject);
//        email.setText(body);
//        email.setTo(user.getEmail());
//        email.setFrom(env.getProperty("support.email"));
//        return email;
//    }
}
