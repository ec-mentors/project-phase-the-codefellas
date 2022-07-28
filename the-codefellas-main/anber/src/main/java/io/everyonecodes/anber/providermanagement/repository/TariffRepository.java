package io.everyonecodes.anber.providermanagement.repository;

import io.everyonecodes.anber.providermanagement.data.Tariff;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TariffRepository
        extends JpaRepository<Tariff, Long> {
}
