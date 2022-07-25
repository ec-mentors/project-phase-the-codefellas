package io.everyonecodes.anber.service;


import io.everyonecodes.anber.data.ConfirmationToken;
import io.everyonecodes.anber.data.User;
import io.everyonecodes.anber.repository.ConfirmationTokenRepository;
import io.everyonecodes.anber.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class ConfirmationTokenService {
    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final UserRepository userRepository;
    
    private final String tokenNotFound;
    private final String alreadyConfirmed;
    private final String expired;
    private final String confirmed;

    public ConfirmationTokenService(ConfirmationTokenRepository confirmationTokenRepository, UserRepository userRepository, @Value("${messages.token.nf}") String tokenNotFound, @Value("${messages.token.ac}") String alreadyConfirmed, @Value("${messages.token.ex}") String expired, @Value("${messages.token.co}") String confirmed) {
        this.confirmationTokenRepository = confirmationTokenRepository;
        this.userRepository = userRepository;
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
    public ModelAndView confirmToken(String token) {
        ModelAndView modelAndView = new ModelAndView();
        ConfirmationToken confirmationToken = getToken(token)
                .orElseThrow(() ->
                        new IllegalStateException(tokenNotFound));

        if (confirmationToken.getConfirmedAt() != null) {
            modelAndView.setViewName(alreadyConfirmed);
            return modelAndView;
        }

        LocalDateTime expiredAt = confirmationToken.getExpiresAt();

        if (expiredAt.isBefore(LocalDateTime.now())) {

            confirmationTokenRepository.delete(confirmationToken);
            userRepository.delete(confirmationToken.getUser());
            modelAndView.setViewName(expired);
            return modelAndView;
        }

        confirmationToken.setConfirmedAt(LocalDateTime.now());
        confirmationTokenRepository.save(confirmationToken);
        User user = confirmationToken.getUser();
        user.setVerified(true);
        userRepository.save(user);
        modelAndView.setViewName(confirmed);
        return modelAndView;
    }
}