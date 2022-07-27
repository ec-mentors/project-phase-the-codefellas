package io.everyonecodes.anber.controller;

import io.everyonecodes.anber.service.WebAppTreeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/webapptree")
public class WebAppTreeEndpoint {


    @Autowired
    WebAppTreeService webAppTreeService;

    @GetMapping
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public String getWebAppTree() {
        return webAppTreeService.prepareWebAppTree();
    }
}
