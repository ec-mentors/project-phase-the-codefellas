package io.everyonecodes.anber.email.endpoints;

import io.everyonecodes.anber.email.service.EmailService;
import io.everyonecodes.anber.email.service.NotificationService;
import io.everyonecodes.anber.usermanagement.data.UserPrivateDTO;
import io.everyonecodes.anber.usermanagement.service.UserService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pwreset")
public class EmailEndpoint {

    ////////////////////// Email Endpoint ////////////////////////////////

    private final UserService userService;
    private final EmailService emailService;
    private final NotificationService notificationService;

    public EmailEndpoint(UserService userService, EmailService emailService, NotificationService notificationService) {
        this.userService = userService;
        this.emailService = emailService;
        this.notificationService = notificationService;
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

    // endpoint only for review
    @Secured({"ROLE_ADMIN"})
    @GetMapping("/notifications/email/test/{username}")
    void sendTestHtmlEmail(@PathVariable String username) {
        emailService.sendTestHTMLEmail(username);
    }
}
