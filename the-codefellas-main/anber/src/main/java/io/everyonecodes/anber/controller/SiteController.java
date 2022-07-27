package io.everyonecodes.anber.controller;

import io.everyonecodes.anber.data.User;
import io.everyonecodes.anber.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
public class SiteController {

    private final UserService userService;

    public SiteController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    String index() {
        return "index";
    }

    @GetMapping("/calculator")
    String calculator() {
        return "calculator";
    }

    @GetMapping("/about")
    String about() {
        return "about";
    }


    @GetMapping("/home")
    public String filter(Model model,
                         Principal principal) {
        User user = userService.loadLoggedInUser(principal);
        return "home";
    }

    @GetMapping("/profile")
    public String profile(Model model,
                         Principal principal) {

        User user = userService.loadLoggedInUser(principal);
        model.addAttribute("target", "/profile");
        return "profile";
    }

}
