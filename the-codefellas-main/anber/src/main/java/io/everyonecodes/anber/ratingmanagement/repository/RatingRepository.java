package io.everyonecodes.anber.ratingmanagement.repository;

import io.everyonecodes.anber.ratingmanagement.data.Rating;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RatingRepository
        extends JpaRepository<Rating, Long> {
}
