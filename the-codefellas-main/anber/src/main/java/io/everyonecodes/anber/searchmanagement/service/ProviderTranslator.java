package io.everyonecodes.anber.searchmanagement.service;

import io.everyonecodes.anber.searchmanagement.data.*;
import org.springframework.stereotype.Service;

@Service
public class ProviderTranslator {

    public Provider DtoToProvider(ProviderDTO dto) {
        return new Provider(dto.getProviderName(), dto.getRating().getScore(), dto.getTariffName(), dto.getBasicRate(), dto.getContractType(), dto.getPriceModel());
    }

    public VerifiedAccount DtoToVerifiedAccount(ProviderDTO dto) {
        return new VerifiedAccount(dto.getId(), dto.getProviderName(), dto.getWebsite(), dto.getEmail(), dto.getPhoneNumber(), true, dto.getTariffs(), dto.getRating());
    }

    public UnverifiedAccount DtoToUnverifiedAccount(ProviderDTO dto) {
        return new UnverifiedAccount(dto.getId(), dto.getProviderName(), dto.getWebsite(), false, dto.getTariffs(), dto.getRating());
    }

    public VerifiedAccount unverifiedToVerifiedAccount(UnverifiedAccount privateAccount, ProviderDTO dto) {
        var publicAccount = DtoToVerifiedAccount(dto);
        publicAccount.setId(privateAccount.getId());
        return publicAccount;
    }

    public UnverifiedAccount verifiedToUnverifiedAccount(VerifiedAccount account) {
        return new UnverifiedAccount(account.getId(), account.getProviderName(), account.getWebsite(), false, account.getTariffs(), account.getRating());
//        var privateAccount = DtoToUnverifiedAccount(dto);
//        privateAccount.setId(publicAccount.getId());
//        return privateAccount;
    }

    public Tariff DtoToTariff(ProviderDTO dto) {
        return new Tariff(dto.getTariffName(), dto.getBasicRate(), dto.getContractType(), dto.getPriceModel());
    }

    public ProviderPublic dtoToPublic(ProviderDTO providerDTO) {
        return new ProviderPublic(providerDTO.getProviderName(), providerDTO.getWebsite(),providerDTO.getRating(), providerDTO.getTariffs());
    }



}

