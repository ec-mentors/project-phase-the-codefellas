package io.everyonecodes.anber.tariffmanagement.email;

import io.everyonecodes.anber.searchmanagement.data.ProviderDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class TariffErrorEmailService {

    private final JavaMailSender mailSender;

    private final String errorTitle;
    private final String errorText;
    private final String errorDetails;
    private final String emailSender;

    public TariffErrorEmailService(JavaMailSender mailSender,
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
    public void sendEmailTariffError(ProviderDTO dto, String message) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(dto.getEmail());
        simpleMailMessage.setSubject(errorTitle);
        simpleMailMessage.setText(errorText + "\n\n" + errorDetails + "\n\n" + message);
        simpleMailMessage.setFrom(emailSender);
        mailSender.send(simpleMailMessage);
    }
}
