package io.everyonecodes.anber.searchmanagement.endpoints;

import io.everyonecodes.anber.searchmanagement.service.RatingService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/provider")
public class RatingEndpoint {

    private final RatingService ratingService;

    public RatingEndpoint(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    @PutMapping("/{id}/rate/{rating}")
    String rateProvider(@PathVariable Long id, @PathVariable int rating) {
        return ratingService.rateProvider(rating, id).orElse(null);
    }
}
