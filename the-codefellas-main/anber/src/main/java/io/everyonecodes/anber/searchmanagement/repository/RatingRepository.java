package io.everyonecodes.anber.searchmanagement.repository;

import io.everyonecodes.anber.searchmanagement.data.Rating;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RatingRepository
        extends JpaRepository<Rating, Long> {
}
