package io.everyonecodes.anber.email.endpoints;

import io.everyonecodes.anber.email.service.EmailService;
import io.everyonecodes.anber.usermanagement.data.UserPrivateDTO;
import io.everyonecodes.anber.usermanagement.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pwreset")
public class EmailEndpoint {
    private final UserService userService;
    private final EmailService emailService;

    public EmailEndpoint(UserService userService, EmailService emailService) {
        this.userService = userService;
        this.emailService = emailService;
    }

    // Get Method for resetting Password
    @GetMapping("/passwordreset/{email}")
    void forgotPassword(@PathVariable String email) {
        emailService.sendPwResetHTMLEmail(email);
    }

    // PostMapping for new Password
    @PostMapping("/passwordreset/{email}/{uuid}")
    UserPrivateDTO setPassword(@PathVariable String email, @PathVariable String uuid, @RequestBody String password) {
        return emailService.savePassword(email, uuid, password);
    }

}
