package io.everyonecodes.anber.usermanagement.authentication;

import io.everyonecodes.anber.usermanagement.data.User;
import io.everyonecodes.anber.usermanagement.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final UserRepository userRepository;
    private final LoginAttemptsEmailService sendFailedLoginEmail;
    private final PasswordEncoder encoder;

    public AuthenticationService(UserRepository userRepository, LoginAttemptsEmailService sendFailedLoginEmail, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.sendFailedLoginEmail = sendFailedLoginEmail;
        this.encoder = encoder;
    }


    public boolean onAuthentication(User user) {
        String email = user.getEmail();
        var oUser = userRepository.findOneByEmail(email);
        if (oUser.isPresent()) {
            User userLogin = oUser.get();
            if (userLogin.isAccountNonLocked()) {
                if (encoder.matches(user.getPassword(), oUser.get().getPassword())) {
                    resetFailedLoginAttempts(userLogin);
                    return true;
                }

                if (userLogin.getLoginAttempts() <= 4) {
                        updateFailedLoginAttempts(userLogin);
                        return false;

                }

                if (userLogin.getLoginAttempts() == 5) {
                        sendFailedLoginEmail.sendEmailLoginFail(userLogin);
                        userLogin.setAccountNonLocked(false);
                        userRepository.save(userLogin);
                        return false;
                }
            }
        }

        return false;
    }

    public void updateFailedLoginAttempts(User user) {
        user.setLoginAttempts(user.getLoginAttempts() + 1);
        userRepository.save(user);
    }

    private void resetFailedLoginAttempts(User user) {
        user.setLoginAttempts(0);
        userRepository.save(user);
    }
}
