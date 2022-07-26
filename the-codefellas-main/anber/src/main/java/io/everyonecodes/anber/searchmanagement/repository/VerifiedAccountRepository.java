package io.everyonecodes.anber.searchmanagement.repository;

import io.everyonecodes.anber.searchmanagement.data.VerifiedAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VerifiedAccountRepository
        extends JpaRepository<VerifiedAccount, Long> {

    boolean existsByProviderName(String providerName);
    Optional<VerifiedAccount> findByProviderName(String providerName);
}
