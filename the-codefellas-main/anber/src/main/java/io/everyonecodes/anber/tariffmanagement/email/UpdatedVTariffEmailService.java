package io.everyonecodes.anber.tariffmanagement.email;

import io.everyonecodes.anber.usermanagement.data.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class UpdatedVTariffEmailService {

    private final JavaMailSender mailSender;

    private final String errorTitle;
    private final String errorText;
    private final String errorDetails;
    private final String emailSender;

    public UpdatedVTariffEmailService(JavaMailSender mailSender,
                                      @Value("${messages.email.tariffError.errorTitle}") String errorTitle,
                                      @Value("${messages.email.tariffError.errorText}") String errorText,
                                      @Value("${messages.email.tariffError.errorDetails}") String errorDetails,
                                      @Value("${messages.email.sender}") String emailSender) {
        this.mailSender = mailSender;
        this.errorTitle = errorTitle;
        this.errorText = errorText;
        this.errorDetails = errorDetails;
        this.emailSender = emailSender;
    }

    @Async
    public void sendEmailUpdatedTariffs(User user, String message) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(user.getEmail());
        simpleMailMessage.setSubject("AnBer Tariff Updated");
        simpleMailMessage.setText("The following tariff changes were made:\n" + message);
        simpleMailMessage.setFrom(emailSender);
        mailSender.send(simpleMailMessage);
    }
}
