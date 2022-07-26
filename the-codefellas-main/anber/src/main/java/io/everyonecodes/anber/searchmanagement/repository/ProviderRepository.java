package io.everyonecodes.anber.searchmanagement.repository;

import io.everyonecodes.anber.searchmanagement.data.PriceModelType;
import io.everyonecodes.anber.searchmanagement.data.ProviderDTO;
import io.everyonecodes.anber.searchmanagement.data.ProviderType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProviderRepository
        extends JpaRepository<ProviderDTO, Long> {

    List<ProviderDTO> findByCountryName(String countryName);
    List<ProviderDTO> findByProviderType(ProviderType providerType);

    List<ProviderDTO> findByProviderName(String providerName);
    List<ProviderDTO> findByTariffName(String tariffName);
    List<ProviderDTO> findByBasicRate(Double basicRate);
    List<ProviderDTO> findByPriceModel(PriceModelType priceModel);
}

