package io.everyonecodes.anber.ratingmanagement.endpoints;

import io.everyonecodes.anber.ratingmanagement.service.RatingService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/provider")
public class RatingEndpoint {

    private final RatingService ratingService;

    public RatingEndpoint(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    @PutMapping("/{id}/rate")
    @Secured("ROLE_USER")
    String rateProvider(@PathVariable Long id, @RequestBody int rating) {
        return ratingService.rateProvider(rating, id).orElse(null);
    }
}
