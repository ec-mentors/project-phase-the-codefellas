package io.everyonecodes.anber.searchmanagement.service;

import io.everyonecodes.anber.searchmanagement.data.Rating;
import io.everyonecodes.anber.searchmanagement.repository.ProviderRepository;
import io.everyonecodes.anber.searchmanagement.repository.RatingRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RatingService {
    private final RatingRepository ratingRepository;
    private final ProviderRepository providerRepository;


    public RatingService(RatingRepository ratingRepository, ProviderRepository providerRepository) {
        this.ratingRepository = ratingRepository;
        this.providerRepository = providerRepository;
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

    public void rate(int rating, Long id) {
        if (isInRange(rating)){
            String userName = loggedInUser();
            Map<String, Integer> ratings = new HashMap<>(Map.of(userName, rating));
            var oProviderDTO = providerRepository.findById(id);

            if (oProviderDTO.isPresent()) {
                var dto = oProviderDTO.get();
                Rating dtoRating = dto.getRating();
                var ratingDTO = dtoRating.getRatings();

                var result = ratingDTO.stream().anyMatch(x -> x.containsKey(userName));
                if (!result) {
                    ratingDTO.add(ratings);
                } else {
                    var list = ratingDTO.stream().toList();
                    var value = list.stream().map(x -> x.get(userName)).collect(Collectors.toList());

                    String user = "";

                    for (Map<String, Integer> hashMap : list) {
                        for (Map.Entry<String, Integer> map : ratings.entrySet()) {
                            user = map.getKey();
                        }
                    }

                    String finalUser = user;
                    list.stream().findAny().filter(x -> x.keySet().contains(finalUser))
                            .ifPresent(m -> m.put(finalUser, rating));
                }
            }
        }
    }
}

//Rating:
//private Long id;
//private Set<Map<String,Integer>> ratings = new HashSet<>();
//private String score = "no ratings yet";

//I as a regular user can rate a provider with 1 to 5 stars,
// 1 being poor and 5 being excellent so all users can get a general idea with the services of providers in time.
