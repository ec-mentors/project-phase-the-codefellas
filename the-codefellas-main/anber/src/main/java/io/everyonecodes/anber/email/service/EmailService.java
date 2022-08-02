package io.everyonecodes.anber.email.service;

import io.everyonecodes.anber.email.data.Notification;
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

    public EmailService(JavaMailSender javaMailSender, UserRepository userRepository, PasswordEncoder passwordEncoder,
                        UserDTO userDTO, NotificationService notificationService,
                        @Value("${spring.mail.username}") String setUsernameValue,
                        @Value("${spring.mail.password}") String setPasswordValue
    ) {
        this.javaMailSender = javaMailSender;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userDTO = userDTO;
        this.notificationService = notificationService;
        this.setUsernameValue = setUsernameValue;
        this.setPasswordValue = setPasswordValue;
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
    @Scheduled(cron = "0 0 10 * * ?")
    public void sendEmailNotificationDaily() {
//        var subject = "Daily Notifications from Anber";
//        var message = "Here are your notifications:\n\n";
//        var mailMessage = new SimpleMailMessage();
//        List<User> users = userRepository.findAllByNotificationsEnabled(true);
//        for (User user : users) {
//            if (users.isEmpty()) {
//                return;
//            }
//            mailMessage.setTo(user.getEmail());
//            mailMessage.setSubject(subject);
//            mailMessage.setText(message);
//            mailMessage.setFrom("anber.project@gmail.com");
//            javaMailSender.send(mailMessage);
//        }
    }

    private String toEmailString(Notification notification) {
        return "From: \"" + notification.getCreator() + "\"\n" +
                "Message: \"" + notification.getMessage() + "\"\n";
    }

    //only for review to show test email for notifications
    public void sendTestHTMLEmail(String usernameInput) {
//        Optional<User> oUser = userRepository.findOneByEmail(usernameInput);
//        if (oUser.isEmpty()) return;
//        String to = oUser.get().getEmail();
//        String from = "anber.project@gmail.com";
//        final String username = from;
//        final String password = "cttkgbdglsmgdttf";
//        String host = "smtp.gmail.com";
//        Properties props = new Properties();
//        props.put("mail.smtp.auth", "true");
//        props.put("mail.smtp.starttls.enable", "true");
//        props.put("mail.smtp.host", host);
//        props.put("mail.smtp.port", "587");
//        var uuid = UUID.randomUUID().toString();
//        Session session = Session.getInstance(props,
//                new javax.mail.Authenticator() {
//                    protected PasswordAuthentication getPasswordAuthentication() {
//                        return new PasswordAuthentication(username, password);
//                    }
//                });
//        try {
//            MimeMessage message = new MimeMessage(session);
//            MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");
//            helper.setTo(to);
//            String body = "<h3><font color=black>Dear " + oUser.get().getEmail() + ",\n" +
//                    "<br><br><br><br>Click here to reset your password:\n</font></h3><br>";
//            body += "<font color=black><p><i></i>";
//            /// Notification part of email goes here if needed
////            List<Notification> notifications = new ArrayList<>();
////            String notificationsAsString = notifications.stream()
////                    .map(this::toEmailStringHTML)
////                    .collect(Collectors.joining("<br>"));
////            body += notificationsAsString;
//            body += "<h3>" + "https://localhost:8080/pwreset/passwordreset/" + uuid + "</h3></p></font>"
//                    + "<html><body><img src='cid:identifier1234'></body></html>";
//            helper.setText(body, true);
//            Resource res = new FileSystemResource(new File("C:\\Users\\nteff\\IdeaProjects\\module-backend\\project-phase\\the-codefellas-main\\anber\\src\\main\\resources\\AnberLogoEmail.png"));
//            helper.addInline("identifier1234", res);
//            helper.setSubject("Password Reset Confirmation from Anber-project");
//            helper.setFrom(from);
//            File file = new File("C:\\Users\\nteff\\IdeaProjects\\module-backend\\project-phase\\the-codefellas-main\\anber\\src\\main\\resources\\AnberLogoEmail.png");
////            helper.addAttachment("AnberLogo.png", file); // - leave this here in case we need it for other attachments
//            javaMailSender.send(message);
//        } catch (MessagingException e) {
//            e.printStackTrace();
//        }
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
