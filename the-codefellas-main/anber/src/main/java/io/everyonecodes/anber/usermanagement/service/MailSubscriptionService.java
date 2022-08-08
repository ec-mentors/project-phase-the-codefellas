//package io.everyonecodes.anber.usermanagement.service;
//
//import io.everyonecodes.anber.searchmanagement.data.ProviderDTO;
//import io.everyonecodes.anber.usermanagement.data.User;
//import io.everyonecodes.anber.usermanagement.repository.UserRepository;
//import org.springframework.stereotype.Service;
//
//import java.util.stream.Collectors;
//
//@Service
//public class MailSubscriptionService {
//
//    private final UserRepository userRepository;
//
//    public MailSubscriptionService(UserRepository userRepository) {
//        this.userRepository = userRepository;
//    }
//
//    public void sendPriceChanges(ProviderDTO dto) {
//
//        var listOfSubscribedUsers = userRepository.findAll().stream()
//                .filter(User::isNotificationsEnabled)
//                .filter(user -> user.getSubscriptionIds().contains(dto.getId()))
//                .collect(Collectors.toList());
//
////        listOfSubscribedUsers.forEach(sub -> sub. //send email);
//    }
//
//    public void sixMonthsNewsletter() {
//
//    }
//}
