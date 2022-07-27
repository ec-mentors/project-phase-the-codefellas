package io.everyonecodes.anber.searchmanagement.service;

import io.everyonecodes.anber.homemanagement.data.Home;
import io.everyonecodes.anber.searchmanagement.data.Provider;
import io.everyonecodes.anber.searchmanagement.data.ProviderDTO;
import io.everyonecodes.anber.searchmanagement.data.Tariff;
import io.everyonecodes.anber.searchmanagement.data.VerifiedAccount;
import io.everyonecodes.anber.searchmanagement.repository.ProviderRepository;
import io.everyonecodes.anber.usermanagement.data.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProviderService {
    private final ProviderRepository providerRepository;
    private final ProviderTranslator translator;


    public ProviderService(ProviderRepository providerRepository, ProviderTranslator providerTranslator) {
        this.providerRepository = providerRepository;
        this.translator = providerTranslator;
    }

//    public List<Provider> getProviders() {
//        List<ProviderDTO> providers = providerRepository.findAll();
//        return providers.stream()
//                .map(translator::DtoToProvider)
//                .collect(Collectors.toList());
//    }

    public List<Provider> getVerifiedProviders() {
        List<ProviderDTO> providersDTO = providerRepository.findAll();
        var providers  =  providersDTO.stream()
                .filter(ProviderDTO::isVerified)
                .map(translator::DtoToProvider)
                .collect(Collectors.toList());
        return providers;
    }

    public List<Provider> getUnVerifiedProviders() {
        List<ProviderDTO> providersDTO = providerRepository.findAll();
        var providers  =  providersDTO.stream()
                .filter(x -> !x.isVerified())
                .map(translator::DtoToProvider)
                .collect(Collectors.toList());
        return providers;
    }

//    public List<VerifiedAccount> getVerifiedAccounts(){
//        List<VerifiedAccount> verifiedAccounts = new ArrayList<>(List.of());
//        List<ProviderDTO> providerDTOS = providerRepository.findAll();
//        var providerDTOSFiltered = providerDTOS.stream()
//                .filter(ProviderDTO::isVerified)
//                .collect(Collectors.toList());
//        for (ProviderDTO DTO: providerDTOSFiltered){
//            verifiedAccounts.add(DTOtoVarrifiedAccountTranslater(DTO));
//        }
//        return verifiedAccounts;
//    }
//
//    //String providerName, String website, String email, String phoneNumber, List<Tariff> tariffs, Rating rating)
//
//    public VerifiedAccount DTOtoVarrifiedAccountTranslater(ProviderDTO DTO){
//        return new VerifiedAccount(new VerifiedAccount(DTO.getProviderName(), DTO.getWebsite(), DTO.getEmail(), DTO.getPhoneNumber(), DTO.getTariffs(), DTO.getRating()));
//    }


    public Provider save(ProviderDTO providerDTO){
        return translator.DtoToProvider(providerRepository.save(providerDTO));
    }

//    public List<Provider> addProvider(ProviderDTO providerDTO) {
//        List<Provider> providers = new ArrayList<>();
//
//        return  ;
//
//    }
}



