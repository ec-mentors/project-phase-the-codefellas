package io.everyonecodes.anber.usermanagement.endpoints;

import io.everyonecodes.anber.usermanagement.data.UserPrivateDTO;
import io.everyonecodes.anber.usermanagement.service.UserService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/signin")
public class LoginEndpoint {

    private final UserService userService;

    public LoginEndpoint(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    UserPrivateDTO viewIndividualProfile(Authentication authentication) {
        return userService.viewIndividualProfileData(authentication.getName()).orElse(null);
    }

}
