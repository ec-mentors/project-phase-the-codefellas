package io.everyonecodes.anber.searchmanagement.repository;

import io.everyonecodes.anber.providermanagement.data.ProviderType;
import io.everyonecodes.anber.searchmanagement.data.ProviderDTO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProviderRepository
        extends JpaRepository<ProviderDTO, Long> {
    List<ProviderDTO> findByCountryName(String countryName);
    List<ProviderDTO> findByProviderType(ProviderType providerType);
    List<ProviderDTO> findByProviderName(String providerName);
}

