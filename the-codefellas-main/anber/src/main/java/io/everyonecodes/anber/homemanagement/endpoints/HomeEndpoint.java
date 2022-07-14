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

    @PutMapping("/{username}/edit/homes/remove/{id}")
    List<Home> removeHome(@PathVariable String username, @PathVariable Long id) {
        return homeService.removeHome(id, username);
    }

    @DeleteMapping("/{username}/edit/homes/delete")
    void deleteHomes (@PathVariable String username) {
        homeService.deleteHomes(username);
    }



}
