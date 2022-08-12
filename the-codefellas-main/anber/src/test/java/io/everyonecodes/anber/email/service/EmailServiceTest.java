package io.everyonecodes.anber.email.service;

import io.everyonecodes.anber.providermanagement.data.ContractType;
import io.everyonecodes.anber.providermanagement.data.PriceModelType;
import io.everyonecodes.anber.providermanagement.data.UnverifiedAccount;
import io.everyonecodes.anber.providermanagement.data.VerifiedAccount;
import io.everyonecodes.anber.providermanagement.repository.UnverifiedAccountRepository;
import io.everyonecodes.anber.providermanagement.repository.VerifiedAccountRepository;
import io.everyonecodes.anber.ratingmanagement.data.Rating;
import io.everyonecodes.anber.tariffmanagement.data.Tariff;
import io.everyonecodes.anber.usermanagement.data.User;
import io.everyonecodes.anber.usermanagement.repository.UserRepository;
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
import java.io.IOException;
import java.util.List;
import java.util.Set;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class EmailServiceTest {

    @Autowired
    EmailService emailService;

    @MockBean
    UnverifiedAccountRepository unverifiedAccountRepository;

    @MockBean
    VerifiedAccountRepository verifiedAccountRepository;

    @MockBean
    UserRepository userRepository;

    @MockBean
    JavaMailSender javaMailSender;

    @MockBean
    PasswordEncoder passwordEncoder;

    @MockBean
    SecurityFilterChain securityFilterChain;

    @Value("${spring.mail.username}")
    String setUsernameValue;

    @Value("${spring.mail.password}")
    String setPasswordValue;

    @Value("${paths.unverifiedListOld-File}")
    String unverifiedListOldLocation;

    @Value("${paths.verifiedListOld-File}")
    String verifiedListOldLocation;

    @Test
    void sendMail() {
    }

    @Test
    void savePassword() {
    }

    @Test
    void addEntryToMap() {
    }

    @Test
    void sendEmailNotificationForNewProviders() {
    }

    @Test
    void sendNewProvidersNotificationHTMLEmail() throws IOException {

        User user = new User("n.teffer@gmail.com", "AAAaaa1#", "ROLE_USER");
        var userList = List.of(user);

        Tariff tariff1 = new Tariff(11L, "tariff1", 0.33, ContractType.ONE_MONTH, PriceModelType.PER_CONSUMPTION);
        Tariff tariff2 = new Tariff(12L, "tariff2", 0.43, ContractType.ONE_MONTH, PriceModelType.PER_CONSUMPTION);
        List<Tariff> tariffList = List.of(tariff1, tariff2);

        Set<String> userSet = Set.of(user.getEmail());
        Rating rating = new Rating(99L, userSet, "3");

        UnverifiedAccount uAccount1 = new UnverifiedAccount(1L, "test1", "website1", false, tariffList, rating);
        List<UnverifiedAccount> unverifiedAccountList = List.of(uAccount1);

        VerifiedAccount vAccount = new VerifiedAccount(200L, "test2", "website2", "n.teffer@chello.at", "+436781212992", true, tariffList, rating);
        List<VerifiedAccount> verifiedAccountList = List.of(vAccount);

        Mockito.when(userRepository.findAllByNotificationsEnabled(true)).thenReturn(userList);
        Mockito.when(unverifiedAccountRepository.findAll()).thenReturn(unverifiedAccountList);
        Mockito.when(verifiedAccountRepository.findAll()).thenReturn(verifiedAccountList);

        emailService.sendNewProvidersNotificationHTMLEmail();

        Mockito.verify(javaMailSender).send(ArgumentMatchers.any(MimeMessage.class));
        Mockito.verify(emailService).sendNewProvidersNotificationHTMLEmail();
        Mockito.verify(userRepository).findAllByNotificationsEnabled(true);
        Mockito.verify(unverifiedAccountRepository).findAll();
        Mockito.verify(verifiedAccountRepository).findAll();
    }

    @Test
    void sendVerificationNotificationHTMLEmail() {
    }

    @Test
    void sendPwResetHTMLEmail() {
    }
}