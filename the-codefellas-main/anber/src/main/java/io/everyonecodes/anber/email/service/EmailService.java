package io.everyonecodes.anber.email.service;

import io.everyonecodes.anber.providermanagement.data.ProviderType;
import io.everyonecodes.anber.providermanagement.data.UnverifiedAccount;
import io.everyonecodes.anber.providermanagement.data.VerifiedAccount;
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
    private List<VerifiedAccount> verifiedList = new ArrayList<>();
    private List<UnverifiedAccount> unverifiedList = new ArrayList<>();

    public EmailService(JavaMailSender javaMailSender, UserRepository userRepository, PasswordEncoder passwordEncoder,
                        UserDTO userDTO, NotificationService notificationService,
                        @Value("${spring.mail.username}") String setUsernameValue,
                        @Value("${spring.mail.password}") String setPasswordValue,
                        ProviderRepository providerRepository, UnverifiedAccountRepository unverifiedAccountRepository,
                        VerifiedAccountRepository verifiedAccountRepository, TariffRepository tariffRepository) {
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

    //every day at 10am
    @Scheduled(cron = "0 0/1 * * * ?")
    public void sendEmailNotificationForNewProviders() {


        sendNewProvidersNotificationHTMLEmail(unverifiedList, verifiedList); // Send Notification email for new provider

    }

    // Send new provider email
    public void sendNewProvidersNotificationHTMLEmail(List<UnverifiedAccount> ProvidersAddedUnverified,
                                                      List<VerifiedAccount> ProvidersAddedVerified) {
        var notificationUserList = userRepository.findAllByNotificationsEnabled(true);


        System.out.println(unverifiedList.stream().toList());

        // Data Fill for the first Run
        if (unverifiedList.isEmpty()) {
            var update = unverifiedAccountRepository.findAll();
            unverifiedList.addAll(update);
        }

        System.out.println(verifiedList.stream().toList());

        if (verifiedList.isEmpty()) {
            var update = verifiedAccountRepository.findAll();
            verifiedList.addAll(update);
        }


        // Up-to-date Providers
        var up2DateProvidersListUnverified = unverifiedAccountRepository.findAll();
        var up2DateProvidersListVerified = verifiedAccountRepository.findAll();

        // Lists for new added Provider Entries
        List<UnverifiedAccount> newProvidersAddedUnverified = new ArrayList<>();
        List<VerifiedAccount> newProvidersAddedVerified = new ArrayList<>();

        // New Entries check
        for (UnverifiedAccount unverifiedAccount : up2DateProvidersListUnverified) {
            if (!unverifiedList.contains(unverifiedAccount)) {
                newProvidersAddedUnverified.add(unverifiedAccount);
            }
        }

        for (VerifiedAccount verifiedAccount : up2DateProvidersListVerified) {
            if (!verifiedList.contains(verifiedAccount)) {
                newProvidersAddedVerified.add(verifiedAccount);
            }
        }

        // String Building for Mail
        StringBuilder uAccountNames = new StringBuilder();
        StringBuilder vAccountNames = new StringBuilder();

        for (UnverifiedAccount unverifiedAccount : newProvidersAddedUnverified) {
            var uAccountName = unverifiedAccount.getProviderName();
            uAccountNames.append(uAccountName).append("<br>");
        }

        for (VerifiedAccount verifiedAccount : newProvidersAddedVerified) {
            var vAccountName = verifiedAccount.getProviderName();
            vAccountNames.append(vAccountName).append("<br>");
        }

        // Sending the actual email to enabled users
        for (User user : notificationUserList) {
            var mailAddress = user.getEmail();
            mailingNewProviders(mailAddress, uAccountNames.toString(), vAccountNames.toString());
        }

        saveListValues(newProvidersAddedUnverified, newProvidersAddedVerified);
    }

    // Adding the new entries to the persistent List
    private void saveListValues(List<UnverifiedAccount> newProvidersAddedUnverified, List<VerifiedAccount> newProvidersAddedVerified) {
        unverifiedList = unverifiedAccountRepository.findAll();
        verifiedList = verifiedAccountRepository.findAll();
    }

    private void mailingNewProviders(String mailAddress, String uAccountNames, String vAccountNames) {
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
                    "<br><br><br><br><u>Here is your semi-annual provider update!</u>" +
                    "\n</font></h3><br>";
            body += "<font color=black><p><i></i>";
            body += "<h3>" + "<u>Unverified Providers added:</u>" + "<br><br>"
                    + uAccountNames + "<br><br>"
                    + "<u>Verified Providers added:</u>" + "<br><br>"
                    + vAccountNames +
                    "</h3></p></font>"
                    + "<html><body><img src='cid:identifier1234'></body></html>";
            helper.setText(body, true);
            File f = new File("project-phase/the-codefellas-main/anber/src/main/resources/images/AnberLogoEmail.png");
            String absolutePath = f.getAbsolutePath();
            Resource res = new FileSystemResource(new File(absolutePath));
            helper.addInline("identifier1234", res);
            helper.setSubject("Your semi-annual provider update is here.");
            helper.setFrom(from);
            javaMailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
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
        var uAccount = unverifiedAccountRepository.findById(id);
        var oProviderAccount = providerRepository.findById(id);
        ProviderDTO providerAccount;
        ProviderType providerType = null;
        if (oProviderAccount.isPresent()) {
            providerAccount = oProviderAccount.get();
            providerType = providerAccount.getProviderType();
        }
        if (uAccount.isPresent()) {
            var tariffsForMail = uAccount.get();
            var tariffsList = tariffsForMail.getTariffs();
            var tariffName = "";
            var basicrate = 0.0;
            var contractType = "";
            var priceModel = "";
            var extractedTariffs = "";
            assert providerType != null;
            StringBuilder stringBuilder = new StringBuilder();
            if (tariffsList.isEmpty()) {
                extractedTariffs = "There are no tariffs yet for this provider.";
                stringBuilder.append(extractedTariffs);
            }
            for (Tariff tariff : tariffsList) {
                tariffName = tariff.getTariffName();
                basicrate = tariff.getBasicRate();
                contractType = String.valueOf(tariff.getContractType());
                priceModel = String.valueOf(tariff.getPriceModel());
                extractedTariffs = "<br>Tariff Name: " + tariffName + ", " +
                        "Provider Type: " + providerType + ", " +
                        "Basic Rate: " + basicrate + ", " +
                        "Contract Type: " + contractType + ", " +
                        "Price Model: " + priceModel;
                stringBuilder.append(extractedTariffs);
            }
            return stringBuilder.toString();
        }
        return "Provider was not found.";
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
                    + "<u>Provider Tariffs:</u>  " + extractTariffs(account.getId()) + "<br><br>"
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
            body += "<h3>" + "https://localhost:8080/pwreset/passwordreset/" + uuid + "</h3></p></font>"
                    + "<html><body><img src='cid:identifier1234'></body></html>";
            helper.setText(body, true);
            File f = new File("project-phase/the-codefellas-main/anber/src/main/resources/images/AnberLogoEmail.png");
            String absolutePath = f.getAbsolutePath();
            Resource res = new FileSystemResource(new File(absolutePath));
            helper.addInline("identifier1234", res);
            helper.setSubject("Password Reset Confirmation from Anber-project");
            helper.setFrom(from);
            javaMailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

}
