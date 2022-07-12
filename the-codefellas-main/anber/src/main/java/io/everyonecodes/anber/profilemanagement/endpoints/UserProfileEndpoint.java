package io.everyonecodes.anber.profilemanagement.endpoints;

import io.everyonecodes.anber.profilemanagement.data.UserProfile;
import io.everyonecodes.anber.profilemanagement.service.UserProfileService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/profile")
public class UserProfileEndpoint {

    private final UserProfileService userProfileService;

    public UserProfileEndpoint(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }


    @GetMapping("/{username}")
    @Secured("ROLE_USER")
    UserProfile getFullProfile(@PathVariable String username) {
        return userProfileService.viewProfile(username).orElse(null);
    }

    @PostMapping("/{username}/edit")
    @Secured("ROLE_USER")
    UserProfile addProfileOption(@PathVariable String username,
                            @RequestBody UserProfile input) {
        return userProfileService.addData(username, input).orElse(null);
    }

    @PutMapping("/{username}/edit/{profileOption}")
    @Secured("ROLE_USER")
    String updateProfileOption(@PathVariable String username,
                               @PathVariable String profileOption,
                               @RequestBody String input) {
        return userProfileService.editData(username, profileOption, input).orElse(null);
    }

    @DeleteMapping("/{username}/delete")
    @Secured("ROLE_USER")
    void deleteProfile(@PathVariable String username) {
        userProfileService.deleteProfile(username);
    }

}
