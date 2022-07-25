package io.everyonecodes.anber;

import io.everyonecodes.anber.data.Role;
import io.everyonecodes.anber.data.User;
import io.everyonecodes.anber.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Set;

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
                User admin = new User("firstName", "lastName", adminEmail, password, "admin", "country",
                        Set.of(new Role("ROLE_ADMIN", "Site Admin")), List.of(), false);
                userRepository.save(admin);
            }

            dbInitializer.createDummyDatabase();
        };
    }

}
