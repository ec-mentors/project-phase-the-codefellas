package io.everyonecodes.anber.homemanagement.endpoints;

import io.everyonecodes.anber.homemanagement.data.Home;
import io.everyonecodes.anber.homemanagement.service.HomeService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/profile")
@Validated
public class HomeEndpoint {

    private final HomeService homeService;

    public HomeEndpoint(HomeService homeService) {
        this.homeService = homeService;
    }

    @GetMapping("/{username}/homes")
    List<Home> getHomes(@PathVariable String username) {
        return homeService.getHomes(username);
    }

    @PutMapping("/{username}/edit/homes/add")
    List<Home> addHome(@PathVariable String username, @Valid @RequestBody Home home) {
        return homeService.addHome(home, username);
    }


    @PutMapping("/{username}/edit/homes/{id}/{property}")
    Home editHome(@PathVariable String username, @PathVariable Long id, @PathVariable String property, @RequestBody String input) {

        return homeService.editHome(username, id, property, input).orElse(null);
    }

    @DeleteMapping("/{username}/edit/homes/{id}")
    void removeHome(@PathVariable String username, @PathVariable Long id) {
        homeService.removeHome(username, id);
    }

    @DeleteMapping("/{username}/edit/homes/delete")
    void deleteAll(@PathVariable String username) {
        homeService.deleteAllHomes(username);
    }
    
}
