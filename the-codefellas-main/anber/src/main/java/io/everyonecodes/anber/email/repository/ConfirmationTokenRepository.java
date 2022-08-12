package io.everyonecodes.anber.email.repository;

import io.everyonecodes.anber.email.data.ConfirmationToken;
import io.everyonecodes.anber.usermanagement.data.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken, Long> {
    Optional<ConfirmationToken> findByToken(String token);
    List<ConfirmationToken> deleteByUser(User user);
}
