package io.everyonecodes.anber.searchmanagement.service;

import io.everyonecodes.anber.providermanagement.data.ContractType;
import io.everyonecodes.anber.providermanagement.data.PriceModelType;
import io.everyonecodes.anber.providermanagement.data.ProviderType;
import io.everyonecodes.anber.ratingmanagement.data.Rating;
import io.everyonecodes.anber.searchmanagement.data.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

class ProviderTranslatorTest {

    ProviderTranslator providerTranslator = new ProviderTranslator();

    @Test
    void dtoToProvider() {
        ProviderDTO dto = new ProviderDTO(1L, "austria", ProviderType.INTERNET, "providerName", "tariffName", 0.5, ContractType.SIX_MONTHS, PriceModelType.PER_CONSUMPTION, new Rating(Set.of(), "4.5"));

        var expected = new Provider("providerName", "tariffName", 0.5, ContractType.SIX_MONTHS, PriceModelType.PER_CONSUMPTION);

        var result = providerTranslator.DtoToProvider(dto);

        Assertions.assertEquals(expected, result);
    }
}