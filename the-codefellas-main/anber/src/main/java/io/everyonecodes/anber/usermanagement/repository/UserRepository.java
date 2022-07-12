package io.everyonecodes.anber.usermanagement.repository;

import io.everyonecodes.anber.usermanagement.data.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByUsername(String username);

    Optional<User> findOneByUsername(String username);
    Optional<User> findOneByEmailAddress(String username);

}
