package io.everyonecodes.anber.providermanagement.service;

import io.everyonecodes.anber.providermanagement.data.ProviderPublic;
import io.everyonecodes.anber.providermanagement.data.Tariff;
import io.everyonecodes.anber.providermanagement.data.UnverifiedAccount;
import io.everyonecodes.anber.providermanagement.data.VerifiedAccount;
import io.everyonecodes.anber.providermanagement.repository.TariffRepository;
import io.everyonecodes.anber.providermanagement.repository.UnverifiedAccountRepository;
import io.everyonecodes.anber.providermanagement.repository.VerifiedAccountRepository;
import io.everyonecodes.anber.ratingmanagement.data.Rating;
import io.everyonecodes.anber.ratingmanagement.repository.RatingRepository;
import io.everyonecodes.anber.searchmanagement.data.*;
import io.everyonecodes.anber.searchmanagement.repository.*;
import io.everyonecodes.anber.searchmanagement.service.ProviderTranslator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AccountService {

    private final ProviderRepository providerRepository;
    private final TariffRepository tariffRepository;
    private final VerifiedAccountRepository verifiedAccountRepository;
    private final UnverifiedAccountRepository unverifiedAccountRepository;
    private final RatingRepository ratingRepository;
    private final String verificationMark;
    private final List<String> accountProperties;
    private final ProviderTranslator translator;

    public AccountService(ProviderRepository providerRepository,
                          TariffRepository tariffRepository,
                          VerifiedAccountRepository verifiedAccountRepository,
                          UnverifiedAccountRepository unverifiedAccountRepository,
                          RatingRepository ratingRepository,
                          @Value("${messages.provider-account.verified}") String verificationMark,
                          List<String> accountProperties,
                          ProviderTranslator translator) {
        this.providerRepository = providerRepository;
        this.tariffRepository = tariffRepository;
        this.verifiedAccountRepository = verifiedAccountRepository;
        this.unverifiedAccountRepository = unverifiedAccountRepository;
        this.ratingRepository = ratingRepository;
        this.verificationMark = verificationMark;
        this.accountProperties = accountProperties;
        this.translator = translator;
    }


    public List<UnverifiedAccount> getAllUnverified() {
        return unverifiedAccountRepository.findAll();
    }

    public List<VerifiedAccount> getAllVerified() {
        return verifiedAccountRepository.findAll();
    }

    public Optional<UnverifiedAccount> createAccount(Long dtoId) {

        var oDto = providerRepository.findById(dtoId);
        if (oDto.isPresent()) {
            ProviderDTO dto = oDto.get();
            if (!unverifiedAccountRepository.existsByProviderName(dto.getProviderName())) {
                UnverifiedAccount account = translator.DtoToUnverifiedAccount(dto);

                account.setTariffs(List.of());
                account.setRating(new Rating(account.getId(), Set.of(), "no ratings yet"));
                ratingRepository.save(account.getRating());
                unverifiedAccountRepository.save(account);

                return Optional.of(account);
            }
        }
        return Optional.empty();
    }


    public Optional<VerifiedAccount> verifyAccount(Long id) {

        Optional<UnverifiedAccount> oAccount = unverifiedAccountRepository.findById(id);
        if (oAccount.isPresent()) {
            UnverifiedAccount account = oAccount.get();

            ProviderDTO dto = providerRepository.findById(account.getId()).stream()
                    .findFirst().orElse(null);

            var accountVerified = translator.unverifiedToVerifiedAccount(account, dto);
            accountVerified.setProviderName(accountVerified.getProviderName() + verificationMark);

            if (dto != null) {
                dto.setVerified(true);
                providerRepository.save(dto);
            }

            accountVerified.setRating(account.getRating());
            accountVerified.setId(account.getId());
            verifiedAccountRepository.save(accountVerified);
            unverifiedAccountRepository.deleteById(account.getId());
            return Optional.of(accountVerified);
        }
        return Optional.empty();
    }

    public Optional<UnverifiedAccount> unverifyAccount(Long id) {

        Optional<VerifiedAccount> oAccount = verifiedAccountRepository.findById(id);
        if (oAccount.isPresent()) {
            VerifiedAccount account = oAccount.get();

            ProviderDTO dto = providerRepository.findById(account.getId()).stream()
                    .findFirst().orElse(null);

            var accountUnverified = translator.verifiedToUnverifiedAccount(account);
            accountUnverified.setProviderName(account.getProviderName().replace(verificationMark, ""));

            if (dto != null) {
                dto.setVerified(false);
                providerRepository.save(dto);
            }

            accountUnverified.setRating(account.getRating());
            accountUnverified.setId(account.getId());
            unverifiedAccountRepository.save(accountUnverified);
            verifiedAccountRepository.deleteById(account.getId());
            return Optional.of(accountUnverified);
        }
        return Optional.empty();
    }


    public Optional<VerifiedAccount> editAccountV(Long id, String property, String input) {

        Optional<VerifiedAccount> oAccountVerified = verifiedAccountRepository.findById(id);
        boolean verified;
        if (isVerified(id).isPresent()) {
            verified = isVerified(id).get();

            if (verified && oAccountVerified.isPresent()) {
                VerifiedAccount account = oAccountVerified.get();
                overwriteDataVerified(property, account, input);
                return Optional.of(account);
            }
        }

        return Optional.empty();
    }


    public Optional<UnverifiedAccount> editAccountU(Long id, String property, String input) {

        Optional<UnverifiedAccount> oAccountUnverified = unverifiedAccountRepository.findById(id);
        boolean verified;
        if (isVerified(id).isPresent()) {
            verified = isVerified(id).get();

            if (!verified && oAccountUnverified.isPresent()) {
                UnverifiedAccount account = oAccountUnverified.get();

                overwriteDataUnverified(property, account, input);
                return Optional.of(account);
            }
        }

        return Optional.empty();
    }

    public void deleteAccount(Long id) {

        Optional<VerifiedAccount> oAccountVerified = verifiedAccountRepository.findById(id);
        Optional<UnverifiedAccount> oAccountUnverified = unverifiedAccountRepository.findById(id);
        List<Long> ids = new ArrayList<>();
        boolean verified;
        if (isVerified(id).isPresent()) {
            verified = isVerified(id).get();

            if (verified && oAccountVerified.isPresent()) {

                ids = oAccountVerified.get().getTariffs().stream()
                        .map(Tariff::getId)
                        .collect(Collectors.toList());
                tariffRepository.deleteAllByIdInBatch(ids);
                ratingRepository.deleteById(id);
                verifiedAccountRepository.deleteById(id);

            }
            if (!verified && oAccountUnverified.isPresent()) {

                ids = oAccountUnverified.get().getTariffs().stream()
                        .map(Tariff::getId)
                        .collect(Collectors.toList());
                tariffRepository.deleteAllByIdInBatch(ids);
                unverifiedAccountRepository.deleteById(id);

            }
        }
    }


    private void overwriteDataVerified(String property, VerifiedAccount account, String input) {

        if (accountProperties.contains(property)) {
            //providerName
            if (property.equals(accountProperties.get(0))) {
                account.setProviderName(input + verificationMark);
            }
            //website
            if (property.equals(accountProperties.get(1))) {
                account.setWebsite(input);
            }
            //email
            if (property.equals(accountProperties.get(2))) {
                account.setEmail(input);
            }
            //phoneNumber
            if (property.equals(accountProperties.get(3))) {
                account.setPhoneNumber(input);
            }

            verifiedAccountRepository.save(account);
        }
    }

    private void overwriteDataUnverified(String property, UnverifiedAccount account, String input) {

        if (accountProperties.contains(property)) {
            //providerName
            if (property.equals(accountProperties.get(0))) {
                account.setProviderName(input);
            }
            //website
            if (property.equals(accountProperties.get(1))) {
                account.setWebsite(input);
            }

            unverifiedAccountRepository.save(account);
        }
    }

    private Optional<Boolean> isVerified(Long id) {
        Optional<VerifiedAccount> oAccountVerified = verifiedAccountRepository.findById(id);
        Optional<UnverifiedAccount> oAccountUnverified = unverifiedAccountRepository.findById(id);
        if (oAccountVerified.isPresent()) {
            return Optional.of(true);
        } else if (oAccountUnverified.isPresent()) {
            return Optional.of(false);
        } else {
            return Optional.empty();
        }
    }

    public Optional<ProviderPublic> viewPublicProvider(Long id) {
        Optional<ProviderDTO> oProvider = providerRepository.findById(id);
        if (oProvider.isPresent()) {
            var dto = oProvider.get();
            ProviderPublic provider = translator.dtoToPublic(dto);
            return Optional.of(provider);
        }
        return Optional.empty();
    }
}
