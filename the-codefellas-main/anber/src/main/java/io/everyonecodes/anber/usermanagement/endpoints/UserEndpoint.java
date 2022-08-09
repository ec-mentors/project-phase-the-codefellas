package io.everyonecodes.anber.usermanagement.endpoints;

import io.everyonecodes.anber.email.service.EmailService;
import io.everyonecodes.anber.usermanagement.data.User;
import io.everyonecodes.anber.usermanagement.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserEndpoint {

    private final UserService userService;
    private final EmailService emailService;

    public UserEndpoint(UserService userService, EmailService emailService) {
        this.userService = userService;
        this.emailService = emailService;
    }

    @PostMapping("/register")
    User registerUser(@Valid @RequestBody User user) {
        try {
            return userService.saveUser(user);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Invalid Password (must be at least 6 characters long, and include lower- and uppercase letters, numbers and special characters)", e
            );
        }
    }

    @PreRemove
    @DeleteMapping("/{username}/delete")
    @Secured("ROLE_USER")
    void deleteUser(@PathVariable String username) {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();

        if(!name.equals(username)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User can only delete himself") {

            };
        }


        emailService.deleteConfirmationTokenAndSendDeleteMail(username);
        userService.deleteUser(username);
    }

    @PutMapping("/{username}/unlock")
    @Secured("ROLE_ADMIN")
    void unlockUser(@PathVariable String username) {
        userService.unlockUser(username);
    }

    @PreUpdate
    @PutMapping("/{username}/notifications/toggle")
    String toggleNotifications(@PathVariable String username) {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();

        if(!name.equals(username)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User can only toggle notifications for themselves!") {

            };
        }

        return userService.toggleNotificationStatus(username);
    }

    @PreUpdate
    @PutMapping("/{username}/subscribe/{id}")
    String subscribeToProvider(@PathVariable String username, @PathVariable Long id) {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();

        if(!name.equals(username)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User can only subscribe to providers for themselves!") {

            };
        }

        return userService.subscribeToProvider(username, id);
    }

    @PreUpdate
    @PutMapping("/{username}/unsubscribe/{id}")
    String unsubscribeToProvider(@PathVariable String username, @PathVariable Long id) {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();

        if(!name.equals(username)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User can only unsubscribe to providers for themselves!") {

            };
        }

        return userService.unsubscribeToProvider(username, id);
    }

}
