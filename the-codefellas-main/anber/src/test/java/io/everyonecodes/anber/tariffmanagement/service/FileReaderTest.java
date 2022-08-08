package io.everyonecodes.anber.tariffmanagement.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

class FileReaderTest {

    private final FileReader fileReader = new FileReader();

    @Test
    void read() {

        List<String> result = fileReader.read("src/main/resources/files/1_updated_tariffs.txt");

        List<String> expected = List.of(
                "name1; 0.55; six months; per consumption",
                "name2; 0.42; twelve months; fixed",
                "name3; 0.31; one month; fixed"
        );

        Assertions.assertEquals(expected, result);
    }
}