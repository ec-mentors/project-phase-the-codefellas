package io.everyonecodes.anber.email.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FileReaderNewProvider {

    public List<String> read(String fileLocation) {

        Path path = Path.of(fileLocation);

        try {
            return Files.readAllLines(path);
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }

    }

}

