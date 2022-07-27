package io.everyonecodes.anber.endpointsOld;

import io.everyonecodes.anber.data.User;
import io.everyonecodes.anber.service.UserProfileService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.persistence.PreRemove;
import java.util.List;

@RestController
@RequestMapping("/profile")
public class UserProfileEndpoint {

    private final UserProfileService userProfileService;

    public UserProfileEndpoint(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }

    @GetMapping("/all")
    @Secured("ROLE_ADMIN")
    List<User> getAllProfiles() {
        return userProfileService.viewAll();
    }

    @GetMapping("/{username}")
    @Secured("ROLE_USER")
    User getFullProfile(@PathVariable String username) {
        return userProfileService.viewProfile(username).orElse(null);
    }

    @PutMapping("/{username}/edit/{profileOption}")
    @Secured("ROLE_USER")
    String updateProfileOption(@PathVariable String username,
                               @PathVariable String profileOption,
                               @RequestBody String input) {
        return userProfileService.editData(username, profileOption, input).orElse(null);
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

        userProfileService.deleteProfile(username);
    }

}
