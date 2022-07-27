package io.everyonecodes.anber.controller;

import io.everyonecodes.anber.data.User;
import io.everyonecodes.anber.service.UserService;
import org.aspectj.weaver.ast.Var;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Optional;

@Controller
public class LoginController {
   private final UserService userService;

    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    String login(HttpServletRequest request, Model model) {
        var oUser = userService.findUserByEmail("admin@gmail.com");
        if (oUser.isEmpty()){
            userService.saveAdmin(new User("admin","admin@gmail.com",true, "Password1!"));
        }
        HttpSession session = request.getSession(false);
        String errorMessage = null;
        if (session != null) {
            AuthenticationException ex = (AuthenticationException) session
                    .getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
            if (ex != null) {
                errorMessage = ex.getMessage();
            }
        }
        if (errorMessage != null) {
            if (errorMessage.equals("Bad credentials")) {

                model.addAttribute("errorMessage", "Your email or password is incorrect. Please try again");

            } else if (errorMessage.equals("User is disabled")) {

                model.addAttribute("errorMessage", "Your email is not validated! Resend <a href=\"/verification\">Verification Link</a>?");

            } else {
                model.addAttribute("errorMessage", errorMessage);

            }
        }
        return "login";
    }

    @RequestMapping("/delete")
    public String empDelete(Authentication authentication, Model model) {
        Optional<User> user = userService.findUserByEmail(authentication.getPrincipal().toString());

        model.addAttribute("user", user);
        return "redirect:/index";
    }

}
