package io.everyonecodes.anber;



import io.everyonecodes.anber.email.service.LoginAttemptsEmailService;
import io.everyonecodes.anber.usermanagement.data.User;
import io.everyonecodes.anber.usermanagement.repository.UserRepository;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Service
public class CustomLoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private final UserRepository userRepository;
    private final LoginAttemptsEmailService sendFailedLoginEmail;

    public CustomLoginFailureHandler(UserRepository userRepository, LoginAttemptsEmailService sendFailedLoginEmail) {
        this.userRepository = userRepository;
        this.sendFailedLoginEmail = sendFailedLoginEmail;
    }


    public void updateFailedLoginAttempts(User user) {
        user.setLoginAttempts(user.getLoginAttempts() + 1);
        userRepository.save(user);
    }


    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        String email = request.getParameter("email");
        var oUser = userRepository.findOneByEmail(email);
        if (oUser.isPresent()){
            User user = oUser.get();
            if (user.getLoginAttempts() <= 4) {
                updateFailedLoginAttempts(user);
            }
            if (user.getLoginAttempts() == 5) {
                sendFailedLoginEmail.sendEmailLoginFail(user);
                user.setAccountNonLocked(false);
                userRepository.save(user);
            }

        }
        super.setDefaultFailureUrl("/login?error=true");
        super.onAuthenticationFailure(request, response, exception);
    }
}


