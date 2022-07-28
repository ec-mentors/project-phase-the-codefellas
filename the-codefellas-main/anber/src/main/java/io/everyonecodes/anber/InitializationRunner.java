package io.everyonecodes.anber;

import io.everyonecodes.anber.usermanagement.data.User;
import io.everyonecodes.anber.usermanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class InitializationRunner {

    private final String adminPassword;
    private final String adminEmail;
    private final String adminRole;
    private final DatabaseInitializer dbInitializer;

    public InitializationRunner(@Value("${data.admin.password}") String adminPassword,
                                @Value("${data.admin.email}") String adminEmail,
                                @Value("${data.roles.admin}") String adminRole,
                                DatabaseInitializer dbInitializer) {
        this.adminPassword = adminPassword;
        this.adminEmail = adminEmail;
        this.adminRole = adminRole;
        this.dbInitializer = dbInitializer;
    }

    @Bean
    ApplicationRunner initializeAdminAndDB(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (!userRepository.existsByEmail(adminEmail)) {
                String password = passwordEncoder.encode(adminPassword);
                User admin = new User("admin", "admin", adminEmail, password, adminRole, adminEmail);
                userRepository.save(admin);
            }

            dbInitializer.createDummyDatabase();
        };
    }

}
