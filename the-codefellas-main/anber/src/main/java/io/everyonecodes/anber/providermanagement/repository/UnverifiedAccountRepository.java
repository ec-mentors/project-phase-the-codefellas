package io.everyonecodes.anber.providermanagement.repository;

import io.everyonecodes.anber.providermanagement.data.UnverifiedAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UnverifiedAccountRepository
        extends JpaRepository<UnverifiedAccount, Long> {

    boolean existsByProviderName(String providerName );
    Optional<UnverifiedAccount> findByProviderName(String providerName);
}
