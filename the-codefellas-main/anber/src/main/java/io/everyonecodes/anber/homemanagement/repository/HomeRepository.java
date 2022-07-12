package io.everyonecodes.anber.homemanagement.repository;

import io.everyonecodes.anber.homemanagement.data.Home;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HomeRepository
        extends JpaRepository<Home, Long> {
}
