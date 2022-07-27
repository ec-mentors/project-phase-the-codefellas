package io.everyonecodes.anber.searchmanagement.service;

import io.everyonecodes.anber.searchmanagement.data.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

class ProviderTranslatorTest {

    ProviderTranslator providerTranslator = new ProviderTranslator();

    @Test
    void dtoToProvider() {
        ProviderDTO dto = new ProviderDTO(1L, "austria", ProviderType.INTERNET, "providerName", "tariffName", 0.5, ContractType.SIX_MONTHS, PriceModelType.PER_CONSUMPTION, new Rating(List.of(), 4.5));

        var expected = new Provider("providerName", "tariffName", 0.5, ContractType.SIX_MONTHS, PriceModelType.PER_CONSUMPTION);

        var result = providerTranslator.DtoToProvider(dto);

        Assertions.assertEquals(expected, result);
    }
}