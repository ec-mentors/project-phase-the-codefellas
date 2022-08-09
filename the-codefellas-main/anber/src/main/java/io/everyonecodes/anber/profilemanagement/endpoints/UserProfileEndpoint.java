package io.everyonecodes.anber.profilemanagement.endpoints;

import io.everyonecodes.anber.profilemanagement.service.UserProfileService;
import io.everyonecodes.anber.usermanagement.data.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import java.util.List;

@RestController
@RequestMapping("/profile")
public class UserProfileEndpoint {

    private final UserProfileService userProfileService;
    private final String userFailedDelete;

    public UserProfileEndpoint(UserProfileService userProfileService,
                               @Value("${messages.user.failed-delete}") String userFailedDelete) {
        this.userProfileService = userProfileService;
        this.userFailedDelete = userFailedDelete;
    }

    @GetMapping("/all")
    @Secured("ROLE_ADMIN")
    List<User> getAllProfiles() {
        return userProfileService.viewAll();
    }

    @GetMapping("/{username}")
    @Secured("ROLE_USER")
    User getFullProfile(@PathVariable String username) {

        String name = SecurityContextHolder.getContext().getAuthentication().getName();

        if(!name.equals(username)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User can only view themselves!") {

            };
        }

        return userProfileService.viewProfile(username).orElse(null);
    }

    @PreUpdate
    @PutMapping("/{username}/edit/{profileOption}")
    @Secured("ROLE_USER")
    String updateProfileOption(@PathVariable String username,
                               @PathVariable String profileOption,
                               @RequestBody String input) {

        String name = SecurityContextHolder.getContext().getAuthentication().getName();

        if(!name.equals(username)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User can only edit properties for themselves!") {

            };
        }
        return userProfileService.editData(username, profileOption, input).orElse(null);
    }

    @PreRemove
    @DeleteMapping("/{username}/delete")
    @Secured("ROLE_USER")
    void deleteProfile(@PathVariable String username) {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();

        if(!name.equalsIgnoreCase(username)) {
            throw new AuthenticationException(userFailedDelete) {

            };
        }

        userProfileService.deleteProfile(username);
    }

}
