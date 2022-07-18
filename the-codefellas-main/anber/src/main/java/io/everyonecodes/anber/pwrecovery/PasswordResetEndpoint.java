package io.everyonecodes.anber.pwrecovery;

import io.everyonecodes.anber.usermanagement.service.UserService;
import org.springframework.mail.MailSender;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pwreset")
public class PasswordResetEndpoint {

    private final UserService userService;

    private final MailSender mailSender;

    public PasswordResetEndpoint(UserService userService, MailSender mailSender) {
        this.userService = userService;
        this.mailSender = mailSender;
    }

//    @PostMapping("/user/resetPassword")
//    public GenericResponse resetPassword(HttpServletRequest request,
//                                         @RequestParam("email") String userEmail) throws UserNotFoundException {
//        Optional<User> user = userService.getUserByUsername(userEmail);
//        if (!user.isPresent()) {
//            throw new UserNotFoundException(userEmail);
//        }
//        String token = UUID.randomUUID().toString();
//        userService.createPasswordResetTokenForUser(user.get(), token);
//        mailSender.send(constructResetTokenEmail(getAppUrl(request),
//                request.getLocale(), token, user));
//        return new GenericResponse(
//                messages.getMessage("message.resetPasswordEmail", null,
//                        request.getLocale()));
//    }
}
