package io.everyonecodes.anber.usermanagement.endpoints;

import io.everyonecodes.anber.usermanagement.data.User;
import io.everyonecodes.anber.usermanagement.data.UserPrivateDTO;
import io.everyonecodes.anber.usermanagement.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/signin")
public class LoginEndpoint {

    private final UserService userService;

    public LoginEndpoint(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    UserPrivateDTO viewIndividualProfile(@RequestBody User user) {
        if (userService.isUserUnlocked(user)) {
            return userService.viewIndividualProfileDataUser(user)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Bad credentials"));
        }
        else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Account locked");
        }
    }

}
