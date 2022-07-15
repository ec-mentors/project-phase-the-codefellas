package io.everyonecodes.anber.profilemanagement.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties("data.user-profile")
public class ProfileOptionConfiguration {

    private List<String> profileOptions;

    public void setProfileOptions(List<String> profileOptions) {
        this.profileOptions = profileOptions;
    }

    @Bean
    List<String> profileOptions() {
        return profileOptions;
    }


}
