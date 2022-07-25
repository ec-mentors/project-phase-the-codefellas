package io.everyonecodes.anber.repository;


import io.everyonecodes.anber.data.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
    Optional<User> findOneByEmail(String email);
    Optional<User> findByUsername(String username);

    boolean existsByEmail(String email);

    Optional<User> findUserByResetToken(String token);

    void deleteByEmail(String email);

}
