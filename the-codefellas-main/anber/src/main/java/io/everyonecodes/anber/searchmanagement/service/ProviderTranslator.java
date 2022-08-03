package io.everyonecodes.anber.searchmanagement.service;

import io.everyonecodes.anber.providermanagement.data.ProviderPublic;
import io.everyonecodes.anber.providermanagement.data.UnverifiedAccount;
import io.everyonecodes.anber.providermanagement.data.VerifiedAccount;
import io.everyonecodes.anber.searchmanagement.data.Provider;
import io.everyonecodes.anber.searchmanagement.data.ProviderDTO;
import io.everyonecodes.anber.searchmanagement.repository.ProviderRepository;
import io.everyonecodes.anber.tariffmanagement.repository.TariffRepository;
import org.springframework.stereotype.Service;

@Service
public class ProviderTranslator {


    private final ProviderRepository providerRepository;
    private final TariffRepository tariffRepository;

    public ProviderTranslator(ProviderRepository providerRepository, TariffRepository tariffRepository) {
        this.providerRepository = providerRepository;
        this.tariffRepository = tariffRepository;
    }

    public Provider DtoToProvider(ProviderDTO dto) {
        return new Provider(dto.getProviderName(), dto.getRating().getScore(),
                dto.getTariffs().get(0).getTariffName(),
                dto.getTariffs().get(0).getBasicRate(),
                dto.getTariffs().get(0).getContractType(),
                dto.getTariffs().get(0).getPriceModel());

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
    }

    public ProviderPublic dtoToPublic(ProviderDTO providerDTO) {
        return new ProviderPublic(providerDTO.getProviderName(), providerDTO.getWebsite(),providerDTO.getRating(), providerDTO.getTariffs());
    }

//    private void addTariffToDto(Long id) {
//        if (providerRepository.findById(id).isPresent()) {
//            var dto = providerRepository.findById(id).get();
//            var tariffs = tariffRepository.findAllByProviderId(id);
//            dto.setTariffs(tariffs);
//            providerRepository.save(dto);
//        }
//    }



}

