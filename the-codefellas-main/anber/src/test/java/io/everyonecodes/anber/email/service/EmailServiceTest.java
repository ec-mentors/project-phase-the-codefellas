package io.everyonecodes.anber.email.service;

import io.everyonecodes.anber.usermanagement.data.User;
import io.everyonecodes.anber.usermanagement.repository.UserRepository;
import io.everyonecodes.anber.usermanagement.service.UserDTO;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import javax.mail.internet.MimeMessage;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class EmailServiceTest {

    @Autowired
    EmailService emailService;

    @MockBean
    JavaMailSender javaMailSender;

    @MockBean
    UserRepository userRepository;

    @MockBean
    PasswordEncoder passwordEncoder;

    @MockBean
    UserDTO userDTO;

    @MockBean
    NotificationService notificationService;

    @MockBean
    SecurityFilterChain securityFilterChain;

    private final Map<String, String> allowedUsers = new HashMap<>();

    private final String role = "ROLE_USER";

    @Value("${testvalues.test-mail-email}")
    String testMail;

    @Value("${testvalues.password}")
    String password;

    // Test for outgoing email - success
    @Test
    void sendPwResetHTMLEmail() {
        User user = new User(testMail, password, role);
        Mockito.when(userRepository.findOneByEmail(testMail)).thenReturn(Optional.of(user));
        emailService.sendPwResetHTMLEmail(testMail);
        Mockito.verify(javaMailSender).send(ArgumentMatchers.any(MimeMessage.class));
    }

    // Test for NOT outgoing email - user not found
    @Test
    void sendPwResetHTMLEmail_UserNotFound() {
        Mockito.when(userRepository.findOneByEmail(testMail)).thenReturn(Optional.empty());
        emailService.sendPwResetHTMLEmail(testMail);
        Mockito.verify(javaMailSender, Mockito.never()).send(ArgumentMatchers.any(MimeMessage.class));
    }
}