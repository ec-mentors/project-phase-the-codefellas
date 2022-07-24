package io.everyonecodes.anber.controller;


import io.everyonecodes.anber.data.ConfirmationToken;
import io.everyonecodes.anber.data.User;
import io.everyonecodes.anber.repository.ConfirmationTokenRepository;
import io.everyonecodes.anber.repository.UserRepository;
import io.everyonecodes.anber.service.ConfirmationTokenService;
import io.everyonecodes.anber.service.EmailSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping
public class PasswordController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ConfirmationTokenRepository confirmationTokenRepository;

    @Autowired
    private EmailSenderService emailSenderService;

    private final ConfirmationTokenService confirmationTokenService;

    PasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public PasswordController(ConfirmationTokenService confirmationTokenService) {
        this.confirmationTokenService = confirmationTokenService;
    }

    @GetMapping("/forgot-password")
    public ModelAndView displayResetPassword(ModelAndView modelAndView, User user) {
        modelAndView.addObject("user", user);
        modelAndView.setViewName("forgotPassword");
        return modelAndView;
    }

    @PostMapping("/forgot-password")
    public ModelAndView forgotUserPassword(ModelAndView modelAndView, User user) {
        Optional<User> existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser.isPresent()) {

            String token = UUID.randomUUID().toString();
            User userE = existingUser.get();
            ConfirmationToken confirmationToken = confirmationTokenService.createToken(userE);

            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(user.getEmail());
            mailMessage.setSubject("Complete Password Reset!");
            mailMessage.setFrom("anber.project@gmail.com");
            mailMessage.setText("To complete the password reset process, please click here: "
                    + "http://localhost:9200/confirm-reset?token=" + confirmationToken.getToken());

            emailSenderService.sendEmail(mailMessage);

            modelAndView.addObject("message", "Request to reset password received. Check your inbox for the reset link.");
            modelAndView.setViewName("successForgotPassword");

        } else {
            modelAndView.addObject("message", "This email does not exist!");
            modelAndView.setViewName("error");
        }

        return modelAndView;
    }


    @RequestMapping(value = "/confirm-reset", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView validateResetToken(ModelAndView modelAndView, @RequestParam("token") String confirmationToken) {
        Optional<ConfirmationToken> oToken = confirmationTokenRepository.findByToken(confirmationToken);

        if (oToken.isPresent()) {
            ConfirmationToken token = oToken.get();
            Optional<User> existingUser = userRepository.findByEmail(token.getUser().getEmail());
            if (existingUser.isPresent()) {
                User user = existingUser.get();
                user.setVerified(true);
                user.setAccountNonLocked(true);
                user.setLoginAttempts(0);
                userRepository.save(user);
                modelAndView.addObject("user", user);
                modelAndView.addObject("emailId", user.getEmail());
                modelAndView.setViewName("resetPassword2");
            }
        } else {
            modelAndView.addObject("message", "The link is invalid!");
            modelAndView.setViewName("error");
        }
        return modelAndView;
    }

    @PostMapping(value = "/reset-password")
    public ModelAndView resetUserPassword(ModelAndView modelAndView, User user) {

        if (user.getEmail() != null) {
            Optional<User> otTokenUser = userRepository.findByEmail(user.getEmail());
            if (otTokenUser.isPresent()) {

                User tokenUser = otTokenUser.get();
                user.setAccountNonLocked(true);
                user.setLoginAttempts(0);
                tokenUser.setPassword(encoder.encode(user.getPassword()));
                userRepository.save(tokenUser);
                modelAndView.addObject("message", "Password successfully reset. You can now log in.");
                modelAndView.setViewName("successResetPassword");
            }
        } else {
            modelAndView.addObject("message", "The link is invalid!");
            modelAndView.setViewName("error");
        }

        return modelAndView;
    }


    public UserRepository getUserRepository() {
        return userRepository;
    }

    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public ConfirmationTokenRepository getConfirmationTokenRepository() {
        return confirmationTokenRepository;
    }

    public void setConfirmationTokenRepository(ConfirmationTokenRepository confirmationTokenRepository) {
        this.confirmationTokenRepository = confirmationTokenRepository;
    }

    public EmailSenderService getEmailSenderService() {
        return emailSenderService;
    }

    public void setEmailSenderService(EmailSenderService emailSenderService) {
        this.emailSenderService = emailSenderService;
    }

    public void sendLockedEmail(User user) {
        Optional<User> existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser.isPresent()) {

            String token = UUID.randomUUID().toString();
            User userE = existingUser.get();
            ConfirmationToken confirmationToken = confirmationTokenService.createToken(userE);

            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(user.getEmail());
            mailMessage.setSubject("Complete Password Reset!");
            mailMessage.setFrom("anber.project@gmail.com");
            mailMessage.setText("To complete the password reset process, please click here: "
                    + "http://localhost:9200/confirm-reset?token=" + confirmationToken.getToken());

            emailSenderService.sendEmail(mailMessage);
        }
    }
}
