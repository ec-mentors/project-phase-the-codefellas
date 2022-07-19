package io.everyonecodes.anber;

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

    public DatabaseInitializer(@Value("${spring.datasource.url}") String mysqlUrl,
                               @Value("${spring.datasource.username}") String mysqlUsername,
                               @Value("${spring.datasource.password}") String mysqlPassword) {
        this.mysqlUrl = mysqlUrl;
        this.mysqlUsername = mysqlUsername;
        this.mysqlPassword = mysqlPassword;
    }

    public void createDummyDatabase() throws Exception {

        Connection con = DriverManager.getConnection(mysqlUrl, mysqlUsername, mysqlPassword);
        System.out.println("Connection established......");

        ScriptRunner sr = new ScriptRunner(con);

        File f = new File("the-codefellas-main/anber/src/main/resources/static/anber_dummydatabase.sql");
        String absolutePath = f.getAbsolutePath();

        Reader reader = new BufferedReader(new FileReader(absolutePath));
        sr.runScript(reader);
        System.out.println("Database created successfully!");

    }

}
