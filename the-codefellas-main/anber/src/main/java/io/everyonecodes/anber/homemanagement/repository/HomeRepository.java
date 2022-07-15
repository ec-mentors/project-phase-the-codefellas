package io.everyonecodes.anber.homemanagement.repository;

import io.everyonecodes.anber.homemanagement.data.Home;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HomeRepository
        extends JpaRepository<Home, Long> {
    Optional<Home> findOneByHomeName(String homeName);
}
