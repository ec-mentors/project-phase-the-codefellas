package io.everyonecodes.anber;


import io.everyonecodes.anber.data.User;
import io.everyonecodes.anber.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomLoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final UserRepository userRepository;
    private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    public CustomLoginSuccessHandler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    private void resetFailedLoginAttempts(User user) {
        user.setLoginAttempts(0);
        userRepository.save(user);
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        String email = request.getParameter("email");
        var oUser = userRepository.findByEmail(email);
        if (oUser.isPresent()) {
            User user = oUser.get();
            if (user.getLoginAttempts() > 0) {
                resetFailedLoginAttempts(user);
            }

        }
        redirectStrategy.sendRedirect(request,response,"/home");
    }


}