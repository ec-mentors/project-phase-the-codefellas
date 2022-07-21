package io.everyonecodes.anber.searchmanagement.configurations;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties("data.search-engine")
public class ProviderSearchConfiguration {

    private List<String> searchProperties;

    public void setSearchProperties(List<String> searchProperties) {
        this.searchProperties = searchProperties;
    }

    @Bean
    List<String> searchProperties() {
        return searchProperties;
    }
}
