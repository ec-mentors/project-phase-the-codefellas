package io.everyonecodes.anber.tariffmanagement.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FileReader {

    public List<String> read(String file) {
        Path path = Path.of(file);
        try {
            return Files.readAllLines(path);
        }
        catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

}
