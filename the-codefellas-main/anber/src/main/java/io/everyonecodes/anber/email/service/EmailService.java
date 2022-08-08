package io.everyonecodes.anber.email.service;

import io.everyonecodes.anber.email.data.Notification;
import io.everyonecodes.anber.providermanagement.data.UnverifiedAccount;
import io.everyonecodes.anber.providermanagement.repository.UnverifiedAccountRepository;
import io.everyonecodes.anber.providermanagement.repository.VerifiedAccountRepository;
import io.everyonecodes.anber.searchmanagement.data.ProviderDTO;
import io.everyonecodes.anber.searchmanagement.repository.ProviderRepository;
import io.everyonecodes.anber.tariffmanagement.data.Tariff;
import io.everyonecodes.anber.tariffmanagement.repository.TariffRepository;
import io.everyonecodes.anber.usermanagement.data.User;
import io.everyonecodes.anber.usermanagement.data.UserPrivateDTO;
import io.everyonecodes.anber.usermanagement.repository.UserRepository;
import io.everyonecodes.anber.usermanagement.service.UserDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.*;

@Service
@EnableScheduling
@ConfigurationProperties
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserDTO userDTO;
    private final NotificationService notificationService;
    private final Map<String, String> allowedUsers = new HashMap<>();
    private final String setUsernameValue;
    private final String setPasswordValue;
    private final ProviderRepository providerRepository;
    private final UnverifiedAccountRepository unverifiedAccountRepository;
    private final VerifiedAccountRepository verifiedAccountRepository;
    private final TariffRepository tariffRepository;
    private List<ProviderDTO> currentProviderList;

    public EmailService(JavaMailSender javaMailSender, UserRepository userRepository, PasswordEncoder passwordEncoder,
                        UserDTO userDTO, NotificationService notificationService,
                        @Value("${spring.mail.username}") String setUsernameValue,
                        @Value("${spring.mail.password}") String setPasswordValue,
                        ProviderRepository providerRepository, UnverifiedAccountRepository unverifiedAccountRepository,
                        VerifiedAccountRepository verifiedAccountRepository, TariffRepository tariffRepository,
                        List<ProviderDTO> currentProviderList) {
        this.javaMailSender = javaMailSender;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userDTO = userDTO;
        this.notificationService = notificationService;
        this.setUsernameValue = setUsernameValue;
        this.setPasswordValue = setPasswordValue;
        this.providerRepository = providerRepository;
        this.unverifiedAccountRepository = unverifiedAccountRepository;
        this.verifiedAccountRepository = verifiedAccountRepository;
        this.tariffRepository = tariffRepository;
        this.currentProviderList = currentProviderList;
    }

    // send email with link that allows password change
    public void sendMail(String email) {
        var oUser = userRepository.findOneByEmail(email);
        if (oUser.isEmpty()) throw new IllegalArgumentException();
        var uuid = UUID.randomUUID().toString();

        // add the user and the uuid to map that allows password change
        allowedUsers.put(oUser.get().getUsername(), uuid);

        var subject = "Reset your Password";
        var message = "Please use this dummy link to create a new password:\n https://localhost:8080/pwreset/passwordreset/" + uuid;

        var mailMessage = new SimpleMailMessage();

        mailMessage.setTo(email);
        mailMessage.setSubject(subject);
        mailMessage.setText(message);
        mailMessage.setFrom(setUsernameValue);

        javaMailSender.send(mailMessage);
    }

    // use the link sent by mail to actually set a new password
    public UserPrivateDTO savePassword(String email, String uuid, String newPassword) throws IllegalArgumentException {

        // check if user with that email exists
        var oUser = userRepository.findOneByEmail(email);
        if (oUser.isEmpty()) throw new IllegalArgumentException();
        var userTemp = oUser.get();


        // check if these values have been added to map by sendMail() method
        if (!allowedUsers.containsKey(userTemp.getUsername()) || !allowedUsers.get(userTemp.getUsername()).equals(uuid))
            throw new IllegalArgumentException();

        // validate and save new password
        if (!newPassword.matches("(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!?@#$^&+=/_-])(?=\\S+$).{6,100}"))
            throw new IllegalArgumentException();
        userTemp.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(userTemp);

        var userDto = userDTO.toUserPrivateDTO(userTemp);

        // remove used entry from map
        allowedUsers.remove(userTemp.getUsername());

        return userDto;
    }

    // just for testing
    public void addEntryToMap(String key, String value) {
        allowedUsers.put(key, value);
    }

//    //-----------email notifications----------------//

    //every day at 10am
    @Scheduled(cron = "0 0/5 * * * ?")
    public void sendEmailNotificationForNewProviders() {

        sendNewProvidersNotificationHTMLEmail(); //Send Notification email for new provider

    }

    private String toEmailString(Notification notification) {
        return "From: \"" + notification.getCreator() + "\"\n" +
                "Message: \"" + notification.getMessage() + "\"\n";
    }

    // Send new provider email
    public void sendNewProvidersNotificationHTMLEmail() {
//        var notificationUserList = userRepository.findAllByNotificationsEnabled(true);
//
//        List<UnverifiedAccount> unverifiedList = unverifiedAccountRepository.findAll();
//        List<VerifiedAccount> verifiedList = verifiedAccountRepository.findAll();
//
//        currentProviderList.addAll(unverifiedList);
//        currentList.addAll(unverifiedList);
//        currentList.addAll(verifiedList);
//        System.out.println(currentList);
//        List<String> testList2 = List.of("1", "2", "5", "7", "8", "9", "10");
//        List<String> newNumbersList = new ArrayList<>();
//        for (String number : testList2) {
//            if (!currentList.contains(number)) {
//                newNumbersList.add(number);
//            }
//        }
//        System.out.println(newNumbersList);
//
//
//        for (User user : notificationUserList) {
//            var mailAddress = user.getEmail();
//
//        }
    }

    // Send verification email
    public void sendVerificationNotificationHTMLEmail(UnverifiedAccount account) {
        var notificationUserList = userRepository.findAllByNotificationsEnabled(true);
        for (User user : notificationUserList) {
            var mailAddress = user.getEmail();
            mailingNotification(mailAddress, account);
        }
    }

    private String extractTariffs(Long id) {
        var tariffsForMail = tariffRepository.findAllByProviderId(id);
        var tariffName = "";
        var basicrate = 0.0;
        var contractType = "";
        var priceModel = "";
        var extractedTariffs = "";
        for (Tariff tariff : tariffsForMail) {
            tariffName = tariff.getTariffName();
            basicrate = tariff.getBasicRate();
            contractType = String.valueOf(tariff.getContractType());
            priceModel = String.valueOf(tariff.getPriceModel());
            extractedTariffs = "Tariff Name: " + tariffName + ", " +
                    "Basic Rate: " + basicrate + ", " +
                    "Contract Type: " + contractType + ", " +
                    "Price Model: " + priceModel;
            return extractedTariffs;
        }
        return "There are no tariffs yet for this provider.";
    }

    // Method for sending a notification mail
    private void mailingNotification(String mailAddress, UnverifiedAccount account) {
        String from = setUsernameValue;
        final String username = from;
        final String password = setPasswordValue;
        String host = "smtp.gmail.com";
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", "587");
        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });
        try {
            MimeMessage message = new MimeMessage(session);
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");
            helper.setTo(mailAddress);

            String body = "<h3><font color=black>Dear " + mailAddress + ",\n" +
                    "<br><br><br><br><u>The provider " + account.getProviderName() + " just got verified!</u>" +
                    "\n</font></h3><br>";
            body += "<font color=black><p><i></i>";
            body += "<h3>" + "<u>Provider Details:</u>" + "<br><br>"
                    + "<u>Provider Name:</u>  " + account.getProviderName() + "<br><br>"
                    + "<u>Provider Website:</u>  " + account.getWebsite() + "<br><br>"
                    + "<u>Provider Score:</u>  " + account.getRating().getScore() + "<br><br>"
                    + "<u>Provider Tariffs:</u>  " + extractTariffs(account.getId()) + "<br><br>" // Tariffs WIP so this is basic for now
                    +
                    "</h3></p></font>"
                    + "<html><body><img src='cid:identifier1234'></body></html>";
            helper.setText(body, true);
            File f = new File("project-phase/the-codefellas-main/anber/src/main/resources/images/AnberLogoEmail.png");
            String absolutePath = f.getAbsolutePath();
            Resource res = new FileSystemResource(new File(absolutePath));
            helper.addInline("identifier1234", res);
            helper.setSubject("A provider just got verified!");
            helper.setFrom(from);
            File file = new File(absolutePath);
//            helper.addAttachment("AnberLogo.png", file); // - leave this here in case we need it for other attachments
            javaMailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public void sendPwResetHTMLEmail(String usernameInput) {
        Optional<User> oUser = userRepository.findOneByEmail(usernameInput);
        if (oUser.isEmpty()) throw new IllegalArgumentException();
        var uuid = UUID.randomUUID().toString();
        // add the user and the uuid to map that allows password change
        allowedUsers.put(oUser.get().getUsername(), uuid);
        String to = oUser.get().getEmail();
        String from = setUsernameValue;
        final String username = from;
        final String password = setPasswordValue;
        String host = "smtp.gmail.com";
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", "587");
        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });
        try {
            MimeMessage message = new MimeMessage(session);
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");
            helper.setTo(to);
            String body = "<h3><font color=black>Dear " + oUser.get().getEmail() + ",\n" +
                    "<br><br><br><br>Click here to reset your password:\n</font></h3><br>";
            body += "<font color=black><p><i></i>";
            /// Notification part of email goes here if needed
//            List<Notification> notifications = new ArrayList<>();
//            String notificationsAsString = notifications.stream()
//                    .map(this::toEmailStringHTML)
//                    .collect(Collectors.joining("<br>"));
//            body += notificationsAsString;
            body += "<h3>" + "https://localhost:8080/pwreset/passwordreset/" + uuid + "</h3></p></font>"
                    + "<html><body><img src='cid:identifier1234'></body></html>";
            helper.setText(body, true);
            File f = new File("project-phase/the-codefellas-main/anber/src/main/resources/images/AnberLogoEmail.png");
            String absolutePath = f.getAbsolutePath();
            Resource res = new FileSystemResource(new File(absolutePath));
            helper.addInline("identifier1234", res);
            helper.setSubject("Password Reset Confirmation from Anber-project");
            helper.setFrom(from);
            File file = new File(absolutePath);
//            helper.addAttachment("AnberLogo.png", file); // - leave this here in case we need it for other attachments
            javaMailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    private String toEmailStringHTML(Notification notification) {
        return "<b>From:</b> \"" + notification.getCreator() + "\"<br>" +
                "<b>Message:</b> \"" + notification.getMessage() + "\"<br>";
    }
}