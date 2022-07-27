package io.everyonecodes.anber.searchmanagement.repository;

import io.everyonecodes.anber.searchmanagement.data.Tariff;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TariffRepository
        extends JpaRepository<Tariff, Long> {
}
