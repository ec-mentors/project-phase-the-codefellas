package io.everyonecodes.anber.searchmanagement.service;

import io.everyonecodes.anber.providermanagement.data.*;
import io.everyonecodes.anber.ratingmanagement.data.Rating;
import io.everyonecodes.anber.searchmanagement.data.Provider;
import io.everyonecodes.anber.searchmanagement.data.ProviderDTO;
import io.everyonecodes.anber.tariffmanagement.data.Tariff;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

class ProviderTranslatorTest {

    private ProviderTranslator providerTranslator = new ProviderTranslator();
    private ProviderDTO dto = new ProviderDTO(1L, "austria", ProviderType.INTERNET, "providerName", List.of(new Tariff("tariffName", 0.5, ContractType.SIX_MONTHS, PriceModelType.PER_CONSUMPTION)), "website.com", "test@email.com", "0123 456789", false, new Rating(Set.of(), "4.5"));

    @Test
    void dtoToProvider() {

        var expected = new Provider("providerName", "tariffName", 0.5, ContractType.SIX_MONTHS, PriceModelType.PER_CONSUMPTION);

        var result = providerTranslator.DtoToProvider(dto);

        Assertions.assertEquals(expected, result);
    }

    @Test
    void DtoToVerifiedAccount() {

        var expected = new VerifiedAccount(1L, "providerName", "website.com", "test@email.com", "0123 456789", true, List.of(new Tariff("tariffName", 0.5, ContractType.SIX_MONTHS, PriceModelType.PER_CONSUMPTION)), new Rating(Set.of(), "4.5"));

        var result = providerTranslator.DtoToVerifiedAccount(dto);

        Assertions.assertEquals(expected, result);
    }

    @Test
    void DtoToUnverifiedAccount() {
        var expected = new UnverifiedAccount(1L, "providerName", "website.com", false, List.of(new Tariff("tariffName", 0.5, ContractType.SIX_MONTHS, PriceModelType.PER_CONSUMPTION)), new Rating(Set.of(), "4.5"));

        var result = providerTranslator.DtoToUnverifiedAccount(dto);

        Assertions.assertEquals(expected, result);
    }


    @Test
    void unverifiedToVerifiedAccount() {
        var unverified = new UnverifiedAccount(1L, "providerName", "website.com", false, List.of(new Tariff("tariffName", 0.5, ContractType.SIX_MONTHS, PriceModelType.PER_CONSUMPTION)), new Rating(Set.of(), "4.5"));

        var expected = new VerifiedAccount(1L, "providerName", "website.com", "test@email.com", "0123 456789", true, List.of(new Tariff("tariffName", 0.5, ContractType.SIX_MONTHS, PriceModelType.PER_CONSUMPTION)), new Rating(Set.of(), "4.5"));

        var result = providerTranslator.unverifiedToVerifiedAccount(unverified, dto);

        Assertions.assertEquals(expected, result);
    }

    @Test
    void verifiedToUnverifiedAccount() {
        var verified = new VerifiedAccount(1L, "providerName", "website.com", "test@email.com", "0123 456789", false, List.of(new Tariff("tariffName", 0.5, ContractType.SIX_MONTHS, PriceModelType.PER_CONSUMPTION)), new Rating(Set.of(), "4.5"));

        var expected = new UnverifiedAccount(1L, "providerName", "website.com", false, List.of(new Tariff("tariffName", 0.5, ContractType.SIX_MONTHS, PriceModelType.PER_CONSUMPTION)), new Rating(Set.of(), "4.5"));

        var result = providerTranslator.verifiedToUnverifiedAccount(verified);

        Assertions.assertEquals(expected, result);
    }

    @Test
    void DtoToPublic() {
        var expected = new ProviderPublic("providerName", "website.com", new Rating(Set.of(), "4.5"), List.of(new Tariff("tariffName", 0.5, ContractType.SIX_MONTHS, PriceModelType.PER_CONSUMPTION)));

        var result = providerTranslator.dtoToPublic(dto);

        Assertions.assertEquals(expected, result);
    }

}