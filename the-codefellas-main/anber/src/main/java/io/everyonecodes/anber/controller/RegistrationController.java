package io.everyonecodes.anber.controller;


import io.everyonecodes.anber.data.ConfirmationToken;
import io.everyonecodes.anber.data.User;
import io.everyonecodes.anber.service.ConfirmationTokenService;
import io.everyonecodes.anber.service.UserService;
import io.everyonecodes.anber.service.VerificationEmailSenderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class RegistrationController {

    private final UserService userService;
    private final VerificationEmailSenderService emailSenderService;
    private final ConfirmationTokenService confirmationTokenService;

    public RegistrationController(UserService userService,

                                  VerificationEmailSenderService emailSenderService, ConfirmationTokenService confirmationTokenService) {
        this.userService = userService;
        this.emailSenderService = emailSenderService;

        this.confirmationTokenService = confirmationTokenService;
    }


    @GetMapping("/register")
    String register(Model model) {
        User user = new User();
        model.addAttribute(user);
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@Valid User user, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("successMessage", "Please write the fields in the right format!");
            return "register";
        } else if (userService.isUserAlreadyPresent(user)) {
            if (!userService.isUserValid(user)){
                model.addAttribute("successMessage", "Account already registered, Email is not verified!");

            }else{
                model.addAttribute("successMessage", "There is already an account registered with AnBer with the Email: " + user.getEmail() +
                        ". Please proceed to log in.");
            }


        } else {
            userService.saveUser(user);
            model.addAttribute("successMessage", "User is registered successfully, please verify your email.");

            ConfirmationToken confirmationToken = confirmationTokenService.createToken(user);
            emailSenderService.sendEmail2(user, confirmationToken);

        }
        model.addAttribute("user", new User());
        return "register";
    }
}
