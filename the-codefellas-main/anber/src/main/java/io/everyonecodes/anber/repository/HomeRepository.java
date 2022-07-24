package io.everyonecodes.anber.repository;

import io.everyonecodes.anber.data.Home;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HomeRepository
        extends JpaRepository<Home, Long> {
    Optional<Home> findOneByHomeName(String homeName);
}
