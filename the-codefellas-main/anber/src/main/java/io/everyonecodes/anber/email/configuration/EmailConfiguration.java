package io.everyonecodes.anber.email.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
@ConfigurationProperties
public class EmailConfiguration {

    private final String setHostValue;

    private final int setPortValue;

    private final String setUsernameValue;

    private final String setPasswordValue;

    public EmailConfiguration(
            @Value("${spring.mail.host}") String setHostValue,
            @Value("${spring.mail.port}") int setPortValue,
            @Value("${spring.mail.username}") String setUsernameValue,
            @Value("${spring.mail.password}") String setPasswordValue
    ) {
        this.setHostValue = setHostValue;
        this.setPortValue = setPortValue;
        this.setUsernameValue = setUsernameValue;
        this.setPasswordValue = setPasswordValue;
    }

    // Hardcoded values since conversion of properties -> yaml was kinda hard, should be read via yml though
    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        mailSender.setHost(setHostValue);
        mailSender.setPort(setPortValue);

        mailSender.setUsername(setUsernameValue);
        mailSender.setPassword(setPasswordValue);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", true);
        props.put("mail.smtp.starttls.enable", true);
        props.put("mail.debug", "true");

        return mailSender;
    }

    // Bean for Simple Mail Message - not sure if this is really needed
    @Bean
    public SimpleMailMessage templateSimpleMessage() {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setText("This is the test email template for your email:\n%s\n");
        return message;
    }

}
