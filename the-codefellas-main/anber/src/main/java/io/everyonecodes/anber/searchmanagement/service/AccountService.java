package io.everyonecodes.anber.searchmanagement.service;

import io.everyonecodes.anber.searchmanagement.data.*;
import io.everyonecodes.anber.searchmanagement.repository.UnverifiedAccountRepository;
import io.everyonecodes.anber.searchmanagement.repository.VerifiedAccountRepository;
import io.everyonecodes.anber.searchmanagement.repository.ProviderRepository;
import io.everyonecodes.anber.searchmanagement.repository.TariffRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AccountService {

    private final ProviderRepository providerRepository;
    private final TariffRepository tariffRepository;
    private final VerifiedAccountRepository verifiedAccountRepository;
    private final UnverifiedAccountRepository unverifiedAccountRepository;
    private final String verificationMark;
    private final List<String> accountProperties;
    private final ProviderTranslator translator;

    public AccountService(ProviderRepository providerRepository,
                          TariffRepository tariffRepository,
                          VerifiedAccountRepository verifiedAccountRepository,
                          UnverifiedAccountRepository unverifiedAccountRepository, @Value("${messages.verified}") String verificationMark,
                          List<String> accountProperties,
                          ProviderTranslator translator) {
        this.providerRepository = providerRepository;
        this.tariffRepository = tariffRepository;
        this.verifiedAccountRepository = verifiedAccountRepository;
        this.unverifiedAccountRepository = unverifiedAccountRepository;
        this.verificationMark = verificationMark;
        this.accountProperties = accountProperties;
        this.translator = translator;
    }

    public List<VerifiedAccount> getAll() {
        return verifiedAccountRepository.findAll();
    }

    public Optional<UnverifiedAccount> createAccount(Long dtoId) {

        var oDto = providerRepository.findById(dtoId);
        if (oDto.isPresent()) {
            ProviderDTO dto = oDto.get();
            if (!unverifiedAccountRepository.existsByProviderName(dto.getProviderName())) {
                UnverifiedAccount account = translator.DtoToUnverifiedAccount(dto);

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

            ProviderDTO dto = providerRepository.findByProviderName(account.getProviderName()).stream()
                    .findFirst().orElse(null);

            var accountVerified = translator.unverifiedToVerifiedAccount(account, dto);
            accountVerified.setProviderName(accountVerified.getProviderName() + verificationMark);

            if (dto != null) {
                dto.setVerified(true);
                providerRepository.save(dto);
            }

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

            ProviderDTO dto = providerRepository.findByProviderName(account.getProviderName()).stream()
                    .findFirst().orElse(null);

            var accountUnverified = translator.verifiedToUnverifiedAccount(account, dto);
            accountUnverified.setProviderName(accountUnverified.getProviderName());

            if (dto != null) {
                dto.setVerified(true);
                providerRepository.save(dto);
            }

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
        }
        else if (oAccountUnverified.isPresent()) {
            return Optional.of(false);
        }
        else {
            return Optional.empty();
        }
    }
}
