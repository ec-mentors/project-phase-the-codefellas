package io.everyonecodes.anber.email.service;

import io.everyonecodes.anber.email.data.Notification;
import io.everyonecodes.anber.email.repository.NotificationRepository;
import io.everyonecodes.anber.usermanagement.data.User;
import io.everyonecodes.anber.usermanagement.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    public NotificationService(NotificationRepository notificationRepository, UserRepository userRepository) {
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
    }

    // notification service is WIP

    // method for finding all users with notifications enabled - WIP
    public List<Notification> findAllNotificationsByUsername(String username) {
        Optional<User> oUser = userRepository.findOneByEmail(username);
        if (oUser.isEmpty()) {
            return new ArrayList<>();
        }
        List<Notification> result = new ArrayList<>();
        return result;
    }

}