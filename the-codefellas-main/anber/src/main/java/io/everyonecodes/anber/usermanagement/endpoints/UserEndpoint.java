package io.everyonecodes.anber.usermanagement.endpoints;

import io.everyonecodes.anber.usermanagement.data.User;
import io.everyonecodes.anber.usermanagement.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.PreRemove;
import javax.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserEndpoint {

    private final UserService userService;

    public UserEndpoint(UserService userService) {
        this.userService = userService;
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
    void deleteProfile(@PathVariable String username) {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();

        if(!name.equalsIgnoreCase(username)) {
            throw new AuthenticationException("User can only delete himself") {

            };
        }

        userService.deleteUser(username);
    }

    @PutMapping("/{username}/unlock")
    @Secured("ROLE_ADMIN")
    void unlockUser(@PathVariable String username) {
        userService.unlockUser(username);
    }

}
