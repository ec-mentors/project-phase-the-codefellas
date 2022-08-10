package io.everyonecodes.anber.email.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;

public class FileWriterNewProvider {

    public void write(String fileLocation, List<String> lines) {

        Path path = Path.of(fileLocation);

        try {
            Files.write(path, lines);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void append(String fileLocation, List<String> lines) {

        Path path = Path.of(fileLocation);

        try {
            Files.write(path, lines, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
