package io.everyonecodes.anber.searchmanagement.configurations;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties("data.provider")
public class AccountConfiguration {

    private List<String> accountProperties;

    public void setAccountProperties(List<String> accountProperties) {
        this.accountProperties = accountProperties;
    }

    @Bean
    List<String> accountProperties() {
        return accountProperties;
    }
}
