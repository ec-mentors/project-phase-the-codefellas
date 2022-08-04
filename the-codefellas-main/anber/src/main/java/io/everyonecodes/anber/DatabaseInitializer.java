package io.everyonecodes.anber;

import io.everyonecodes.anber.searchmanagement.repository.ProviderRepository;
import io.everyonecodes.anber.tariffmanagement.repository.TariffRepository;
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
    private final String sqlPath;
    private final ProviderRepository providerRepository;
    private final TariffRepository tariffRepository;

    public DatabaseInitializer(@Value("${spring.datasource.url}") String mysqlUrl,
                               @Value("${spring.datasource.username}") String mysqlUsername,
                               @Value("${spring.datasource.password}") String mysqlPassword,
                               @Value("${paths.sql-file}") String sqlPath,
                               ProviderRepository providerRepository,
                               TariffRepository tariffRepository) {
        this.mysqlUrl = mysqlUrl;
        this.mysqlUsername = mysqlUsername;
        this.mysqlPassword = mysqlPassword;
        this.sqlPath = sqlPath;
        this.providerRepository = providerRepository;
        this.tariffRepository = tariffRepository;
    }

    public void createDummyDatabase() throws Exception {

        if (providerRepository.findAll().isEmpty() || tariffRepository.findAll().isEmpty()) {
            Connection con = DriverManager.getConnection(mysqlUrl, mysqlUsername, mysqlPassword);

            ScriptRunner sr = new ScriptRunner(con);

            File f = new File(sqlPath);
            String absolutePath = f.getCanonicalPath();

            Reader reader = new BufferedReader(new FileReader(absolutePath));
            sr.runScript(reader);
        }
    }

}
