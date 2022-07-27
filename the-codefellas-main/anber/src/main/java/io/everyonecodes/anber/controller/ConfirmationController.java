package io.everyonecodes.anber.controller;

import io.everyonecodes.anber.service.ConfirmationTokenService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ConfirmationController {

    private final ConfirmationTokenService confirmationTokenService;

    public ConfirmationController(ConfirmationTokenService confirmationTokenService) {
        this.confirmationTokenService = confirmationTokenService;
    }

    @GetMapping(path = "confirm")
    public ModelAndView confirm(@RequestParam("token") String token) {
        return confirmationTokenService.confirmToken(token);
    }
}
