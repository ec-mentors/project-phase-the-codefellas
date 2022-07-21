package io.everyonecodes.anber.email.endpoints;

import io.everyonecodes.anber.email.service.EmailService;
import io.everyonecodes.anber.email.service.NotificationService;
import io.everyonecodes.anber.usermanagement.data.UserPrivateDTO;
import io.everyonecodes.anber.usermanagement.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;

import java.util.UUID;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class EmailEndpointTest {

    @Autowired
    TestRestTemplate testRestTemplate;

    @MockBean
    UserService userService;

    @MockBean
    EmailService emailService;

    @MockBean
    NotificationService notificationService;

    @Value("${testvalues.pw-reset-endpoint-url}")
    String url;

    @Value("${testvalues.test-mail-email}")
    String testMail;

    @Test
    void forgotPassword() {
        testRestTemplate.getForObject(url + "/passwordreset/" + testMail, Void.class);
        Mockito.verify(emailService).sendPwResetHTMLEmail(testMail);
    }

    @Test
    void setPassword() {
        UUID testUuid = UUID.fromString("9133656a-23d1-4105-a7b1-b120f287b6be");
        testRestTemplate.postForObject(
                url + "/passwordreset/" + testMail + "/" + testUuid,
                "AAAaaa1#",
                UserPrivateDTO[].class);
        Mockito.verify(emailService).savePassword(testMail, String.valueOf(testUuid), "AAAaaa1#");
    }

    // endpoint only for review
    @Test
    void sendTestHtmlEmail() {
        testRestTemplate.getForObject(url + "/notifications/email/test/" + testMail, Void.class);
        emailService.sendTestHTMLEmail(testMail);
        Mockito.verify(emailService).sendTestHTMLEmail(testMail);
    }
}