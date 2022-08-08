package io.everyonecodes.anber.tariffmanagement.service;

import io.everyonecodes.anber.providermanagement.data.ContractType;
import io.everyonecodes.anber.providermanagement.data.PriceModelType;
import io.everyonecodes.anber.tariffmanagement.data.Tariff;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Optional;
import java.util.stream.Stream;

class TariffParserTest {

    private final TariffParser tariffParser = new TariffParser();


    @ParameterizedTest
    @CsvSource({
            "name1, 0.55, six months, per consumption, name1; 0.55; six months; per consumption",
            "name2, 0.42, twelve months, fixed, name2; 0.42; twelve months; fixed",
            "name3, 0.31, one month, fixed, name3; 0.31; one month; fixed"
    })
    void parseLine_success(String tn, Double br, String ct, String pm, String line) {
        Optional<Tariff> oResult = tariffParser.parseLine(line);

        Assertions.assertTrue(oResult.isPresent());
        Tariff result = oResult.get();

        ct = ct.trim().toUpperCase().replace(" ", "_");
        pm = pm.trim().toUpperCase().replace(" ", "_");

        Assertions.assertEquals(tn, result.getTariffName());
        Assertions.assertEquals(br, result.getBasicRate());
        Assertions.assertEquals(Enum.valueOf(ContractType.class, ct), result.getContractType());
        Assertions.assertEquals(Enum.valueOf(PriceModelType.class, pm), result.getPriceModel());
    }



    @ParameterizedTest
    @MethodSource("parameters")
    void parseLine_error(String input, Tariff expected) {
        var oResult = tariffParser.parseLine(input);

        Assertions.assertTrue(oResult.isPresent());
        Tariff result = oResult.get();

        Assertions.assertEquals(expected, result);
    }
    private static Stream<Arguments> parameters() {
        return Stream.of(
                Arguments.of("name3; 0.31; fixed", new Tariff("name3", 0.31, null, null)),
                Arguments.of("0.22; twelve months; fix", new Tariff(null, 0.00, null, null)),
                Arguments.of("name5; one month; perconsumption", new Tariff("name5", 0.0, null, null)),
                Arguments.of("name6; fixed", new Tariff("name6", 0.0, null, null))
        );
    }

}