package io.everyonecodes.anber.homemanagement.endpoints;

import io.everyonecodes.anber.homemanagement.data.Home;
import io.everyonecodes.anber.homemanagement.service.HomeService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/profile")
public class HomeEndpoint {

    private final HomeService homeService;

    public HomeEndpoint(HomeService homeService) {
        this.homeService = homeService;
    }

    @GetMapping("/{username}/homes")
    List<Home> getHomes(@PathVariable String username) {
        return homeService.getHomes();
    }

    @PutMapping("/{username}/home/add")
    List<Home> addHome(@PathVariable String username, @RequestBody Home home) {
        return homeService.addHome(home, username);
    }

    @PutMapping("/{username}/home/remove")
    List<Home> removeHome(@PathVariable String username, @RequestBody Home home) {
        return homeService.removeHome(home, username);
    }

    @DeleteMapping("/{username}/homes/delete")
    void deleteHomes (@PathVariable String username) {
        homeService.deleteHome(username);
    }



}
