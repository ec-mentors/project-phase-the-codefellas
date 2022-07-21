package io.everyonecodes.anber.searchmanagement.service;

import io.everyonecodes.anber.searchmanagement.data.Provider;
import io.everyonecodes.anber.searchmanagement.data.ProviderDTO;
import org.springframework.stereotype.Service;

@Service
public class ProviderTranslator {

    public Provider DtoToProvider(ProviderDTO dto) {
        return new Provider(dto.getProviderName(), dto.getTariffName(), dto.getBasicRate(), dto.getContractType(), dto.getPriceModel());
    }

}
