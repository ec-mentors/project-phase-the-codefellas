package io.everyonecodes.anber.searchmanagement.service;

import io.everyonecodes.anber.searchmanagement.data.Rating;
import io.everyonecodes.anber.searchmanagement.repository.ProviderRepository;
import io.everyonecodes.anber.searchmanagement.repository.RatingRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;

@Service
public class RatingService {

    private final RatingRepository ratingRepository;
    private final ProviderRepository providerRepository;
    private final String noRatings;


    public RatingService(RatingRepository ratingRepository,
                         ProviderRepository providerRepository,
                         @Value("${messages.provider-account.no-ratings}") String noRatings) {
        this.ratingRepository = ratingRepository;
        this.providerRepository = providerRepository;
        this.noRatings = noRatings;
    }

    private String loggedInUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        } else {
            return principal.toString();
        }
    }

    private boolean isInRange(int rating) {
        return rating <= 5 && rating >= 1;
    }

    public Optional<String> rateProvider(int rating, Long id) {
        if (isInRange(rating)) {
            String userName = loggedInUser();

            var oProviderDTO = providerRepository.findById(id);
            if (oProviderDTO.isPresent()) {
                var dto = oProviderDTO.get();
                if (dto.getRating() == null) {
                    dto.setRating(new Rating(dto.getId(), new HashSet<>(), noRatings));
                }
                var users = dto.getRating().getUsersRated();

                if (!users.contains(userName)) {
                    users.add(userName);
                    dto.getRating().setUsersRated(users);
                    int numberOfRatings = users.size();

                    if (dto.getRating().getScore().equals(noRatings)) {
                        dto.getRating().setScore(String.valueOf(rating));
                        ratingRepository.save(new Rating(dto.getId(), users, String.valueOf(rating)));
                        return Optional.of("Current rating for Provider " + dto.getProviderName() + " is: " + (Math.round((double) rating * 100.00)/100.00) );
                    } else {
                        double score = Double.parseDouble(dto.getRating().getScore());
                        score = (score + rating) / numberOfRatings;
                        dto.getRating().setScore(String.valueOf(score));
                        var updatedRating = ratingRepository.findById(dto.getId()).orElse(null);

                        if (updatedRating != null) {
                            updatedRating.setUsersRated(users);
                            updatedRating.setScore(String.valueOf(score));
                            ratingRepository.save(updatedRating);
                            return Optional.of("Current rating for Provider " + dto.getProviderName() + " is: " + (Math.round(score *100.00)/100.00) );

                        }
                    }
                }
                return Optional.of("User " + loggedInUser() + " already gave a rating to Provider " + dto.getProviderName() + "!");
            }
        }
        return Optional.empty();
    }

}