package io.everyonecodes.anber.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties("data.home")
public class HomeConfiguration {
    private List<String> homeProperties;

    public void setHomeProperties(List<String> homeProperties) {
        this.homeProperties = homeProperties;
    }

    @Bean
    List<String> homeProperties() {
        return homeProperties;
    }
}
