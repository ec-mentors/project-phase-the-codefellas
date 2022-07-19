package io.everyonecodes.anber.searchmanagement.repository;

import io.everyonecodes.anber.searchmanagement.data.Provider;
import io.everyonecodes.anber.searchmanagement.data.ProviderType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProviderRepository
        extends JpaRepository<Provider, Long> {

    List<Provider> findByCountryName(String countryName);
    List<Provider> findByProviderType(ProviderType providerType);

    //Optional filters
//    List<Provider> findByProviderName(String providerName);
//    List<Provider> findByTariffName(String tariffName);
//    List<Provider> findByBasicRate(Double basicRate);
//    List<Provider> findByPriceModel(PriceModelType priceModel);
}
