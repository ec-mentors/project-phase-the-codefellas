package io.everyonecodes.anber.ratingmanagement.service;

import io.everyonecodes.anber.ratingmanagement.data.Rating;
import io.everyonecodes.anber.ratingmanagement.repository.RatingRepository;
import io.everyonecodes.anber.searchmanagement.repository.ProviderRepository;
import io.everyonecodes.anber.usermanagement.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;

@Service
public class RatingService {

    private final RatingRepository ratingRepository;
    private final ProviderRepository providerRepository;
    private final UserService userService;
    private final String noRatings;
    private final String currentRating;
    private final String alreadyRated;
    private final String placeholderProviderName;
    private final String placeholderLoggedInUser;


    public RatingService(RatingRepository ratingRepository,
                         ProviderRepository providerRepository,
                         UserService userService,
                         @Value("${messages.provider-account.no-ratings}") String noRatings,
                         @Value("${messages.ratings.current}") String currentRating,
                         @Value("${messages.ratings.already-rated}") String alreadyRated,
                         @Value("${data.placeholders.providerName}") String placeholderProviderName,
                         @Value("${data.placeholders.loggedInUser}") String placeholderLoggedInUser) {
        this.ratingRepository = ratingRepository;
        this.providerRepository = providerRepository;
        this.userService = userService;
        this.noRatings = noRatings;
        this.currentRating = currentRating;
        this.alreadyRated = alreadyRated;
        this.placeholderProviderName = placeholderProviderName;
        this.placeholderLoggedInUser = placeholderLoggedInUser;
    }

    private boolean isInRange(int rating) {
        return rating <= 5 && rating >= 1;
    }

    public Optional<String> rateProvider(int rating, Long id) {
        if (isInRange(rating)) {
            String userName = userService.loggedInUser();

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
                        ratingRepository.save(new Rating(dto.getId(), users, String.valueOf(Math.round((double) rating * 100.00)/100.00)));
                        return Optional.of(currentRating.replace(placeholderProviderName, dto.getProviderName()) + (Math.round((double) rating * 100.00)/100.00) );
                    } else {
                        double score = Double.parseDouble(dto.getRating().getScore());
                        score = (score + rating) / numberOfRatings;
                        dto.getRating().setScore(String.valueOf(score));
                        var updatedRating = ratingRepository.findById(dto.getId()).orElse(null);

                        if (updatedRating != null) {
                            updatedRating.setUsersRated(users);
                            updatedRating.setScore(String.valueOf(score));
                            ratingRepository.save(updatedRating);
                            return Optional.of(currentRating.replace(placeholderProviderName, dto.getProviderName()) + (Math.round(score *100.00)/100.00) );
                        }
                    }
                }
                return Optional.of(alreadyRated.replace(placeholderProviderName, dto.getProviderName()).replace(placeholderLoggedInUser, userService.loggedInUser()));
            }
        }
        return Optional.empty();
    }

}