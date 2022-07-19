package io.everyonecodes.anber.email.repository;

import io.everyonecodes.anber.email.data.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    // Notification Repository - future Database queries for Notification will be added here

}