package io.everyonecodes.anber.email.service;

import io.everyonecodes.anber.email.data.ConfirmationToken;
import io.everyonecodes.anber.email.repository.ConfirmationTokenRepository;
import io.everyonecodes.anber.usermanagement.data.User;
import io.everyonecodes.anber.usermanagement.data.UserPrivateDTO;
import io.everyonecodes.anber.usermanagement.repository.UserRepository;
import io.everyonecodes.anber.usermanagement.service.UserDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class ConfirmationTokenService {
    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final UserRepository userRepository;
    private final UserDTO userDTO;
    
    private final String tokenNotFound;
    private final String alreadyConfirmed;
    private final String expired;
    private final String confirmed;

    public ConfirmationTokenService(ConfirmationTokenRepository confirmationTokenRepository, UserRepository userRepository, UserDTO userDTO, @Value("${messages.token.nf}") String tokenNotFound, @Value("${messages.token.ac}") String alreadyConfirmed, @Value("${messages.token.ex}") String expired, @Value("${messages.token.co}") String confirmed) {
        this.confirmationTokenRepository = confirmationTokenRepository;
        this.userRepository = userRepository;
        this.userDTO = userDTO;
        this.tokenNotFound = tokenNotFound;
        this.alreadyConfirmed = alreadyConfirmed;
        this.expired = expired;
        this.confirmed = confirmed;
    }

    public Optional<ConfirmationToken> getToken(String token) {
        return confirmationTokenRepository.findByToken(token);
    }

    public ConfirmationToken createToken(User user) {
        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken(token, user, LocalDateTime.now(), LocalDateTime.now().plusHours(24));
        confirmationTokenRepository.save(confirmationToken);
        return confirmationToken;
    }

    @Transactional
    public UserPrivateDTO confirmToken(String token) {
        ConfirmationToken confirmationToken = getToken(token)
                .orElseThrow(() ->
                        new IllegalStateException("token not found"));

        if (confirmationToken.getConfirmedAt() != null) {
            new IllegalStateException(alreadyConfirmed);
        }

        LocalDateTime expiredAt = confirmationToken.getExpiresAt();

        if (expiredAt.isBefore(LocalDateTime.now())) {

            confirmationTokenRepository.delete(confirmationToken);
            userRepository.delete(confirmationToken.getUser());
            new IllegalStateException(expired);
        }

        confirmationToken.setConfirmedAt(LocalDateTime.now());
        confirmationTokenRepository.save(confirmationToken);
        User user = confirmationToken.getUser();
        userRepository.save(user);
        return userDTO.toUserPrivateDTO(user);

    }
}