package io.everyonecodes.anber.usermanagement.repository;

import io.everyonecodes.anber.usermanagement.data.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);
    Optional<User> findOneByEmail(String email);
    List<User> findAllByNotificationsEnabled(boolean enabled); // Lists all Users that have notifications = true
}
