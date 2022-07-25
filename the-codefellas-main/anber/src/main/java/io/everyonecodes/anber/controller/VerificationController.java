package io.everyonecodes.anber.controller;


import io.everyonecodes.anber.data.ConfirmationToken;
import io.everyonecodes.anber.data.User;
import io.everyonecodes.anber.repository.UserRepository;
import io.everyonecodes.anber.service.ConfirmationTokenService;
import io.everyonecodes.anber.service.VerificationEmailSenderService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@Controller
public class VerificationController {
    private final UserRepository userRepository;
    private final ConfirmationTokenService confirmationTokenService;
    private final VerificationEmailSenderService emailSenderService;

    public VerificationController(UserRepository userRepository, ConfirmationTokenService confirmationTokenService, VerificationEmailSenderService emailSenderService) {
        this.userRepository = userRepository;
        this.confirmationTokenService = confirmationTokenService;
        this.emailSenderService = emailSenderService;
    }

    @GetMapping("/verification")
    public ModelAndView displayVerification(ModelAndView modelAndView, User user) {
        modelAndView.addObject("user", user);
        modelAndView.setViewName("verification");
        return modelAndView;
    }

    @PostMapping("/verification")
    public ModelAndView resendEmailverification(ModelAndView modelAndView, User user) {
        Optional<User> existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser.isPresent()) {
            User userE = existingUser.get();

            ConfirmationToken confirmationToken = confirmationTokenService.createToken(userE);
            emailSenderService.sendEmail(userE,confirmationToken);

            modelAndView.addObject("message", "Verification link successfully resent.");
            modelAndView.setViewName("verificationSuccess");

        } else {
            modelAndView.addObject("message", "This email does not exist!");
            modelAndView.setViewName("error");
        }
        return modelAndView;
    }


}
