package io.everyonecodes.anber.tariffmanagement.repository;

import io.everyonecodes.anber.tariffmanagement.data.Tariff;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TariffRepository
        extends JpaRepository<Tariff, Long> {
    List<Tariff> findAllByProviderId(Long providerId);
}
