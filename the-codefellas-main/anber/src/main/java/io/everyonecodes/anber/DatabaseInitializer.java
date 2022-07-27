package io.everyonecodes.anber;

import io.everyonecodes.anber.searchmanagement.repository.ProviderRepository;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.sql.Connection;
import java.sql.DriverManager;

@Service
public class DatabaseInitializer {

    private final String mysqlUrl;
    private final String mysqlUsername;
    private final String mysqlPassword;
    private final ProviderRepository providerRepository;

    public DatabaseInitializer(@Value("${spring.datasource.url}") String mysqlUrl,
                               @Value("${spring.datasource.username}") String mysqlUsername,
                               @Value("${spring.datasource.password}") String mysqlPassword, ProviderRepository providerRepository) {
        this.mysqlUrl = mysqlUrl;
        this.mysqlUsername = mysqlUsername;
        this.mysqlPassword = mysqlPassword;
        this.providerRepository = providerRepository;
    }

    public void createDummyDatabase() throws Exception {

        if (providerRepository.findAll().isEmpty()) {
            Connection con = DriverManager.getConnection(mysqlUrl, mysqlUsername, mysqlPassword);
            System.out.println("Connection established......");

            ScriptRunner sr = new ScriptRunner(con);

            File f = new File("src/main/resources/static/anber_dummydatabase.sql");
            String absolutePath = f.getAbsolutePath();

            Reader reader = new BufferedReader(new FileReader(absolutePath));
            sr.runScript(reader);
        }
    }

}
