package io.everyonecodes.anber.tariffmanagement.service;

import io.everyonecodes.anber.providermanagement.repository.UnverifiedAccountRepository;
import io.everyonecodes.anber.providermanagement.repository.VerifiedAccountRepository;
import io.everyonecodes.anber.searchmanagement.repository.ProviderRepository;
import io.everyonecodes.anber.tariffmanagement.data.Tariff;
import io.everyonecodes.anber.tariffmanagement.repository.TariffRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TariffService {

    private final TariffRepository tariffRepository;
    private final ProviderRepository providerRepository;
    private final VerifiedAccountRepository verifiedAccountRepository;
    private final UnverifiedAccountRepository unverifiedAccountRepository;

    public TariffService(TariffRepository tariffRepository, ProviderRepository providerRepository, VerifiedAccountRepository verifiedAccountRepository, UnverifiedAccountRepository unverifiedAccountRepository) {
        this.tariffRepository = tariffRepository;
        this.providerRepository = providerRepository;
        this.verifiedAccountRepository = verifiedAccountRepository;
        this.unverifiedAccountRepository = unverifiedAccountRepository;
    }

    private List<Tariff> tariffFileReader(String file) {

        FileReader fileReader = new FileReader();
        TariffParser tariffParser = new TariffParser();

        var tariffs = fileReader.read(file);
        List<Tariff> updatedTariffs = new ArrayList<>();
        for (String line : tariffs) {
            var oTariff = tariffParser.parseLine(line);
            oTariff.ifPresent(updatedTariffs::add);
        }
        return updatedTariffs;
    }

    public String tariffApplier(Long id) {
        if (providerRepository.findById(id).isPresent()) {

            List<Tariff> tariffs = tariffFileReader("the-codefellas-main/anber/src/main/resources/files/" + id + "_updated_tariffs.txt");

            var totalResult = "";
            int i = 1;

            var dto = providerRepository.findById(id).get();
            dto.removeTariffs();
            if (verifiedAccountRepository.findById(id).isPresent()) {
                var vProv = verifiedAccountRepository.findById(id).get();
                vProv.removeTariffs();
            }
            if (unverifiedAccountRepository.findById(id).isPresent()) {
                var uProv = unverifiedAccountRepository.findById(id).get();
                uProv.removeTariffs();
            }


            for (Tariff tariff : tariffs) {

                var result = checkTariffForErrors(tariff, i);

                if (result.isBlank()) {
                    tariff.setProviderId(id);
                    tariffRepository.save(tariff);
                    saveProviderAccount(id, tariff);

                } else {
                    totalResult += result;
                }
                i++;
            }
            if (!totalResult.isBlank()) {
                //write mail with error message
                return (totalResult + "\nTariffs were not updated.");
            } else {
                return "Tariffs were updated successfully.";
            }

        }
        return "Couldn't find provider to update.";
    }


    private String checkTariffForErrors(Tariff tariff, int i) {

        if (tariff.getTariffName() != null
                && String.valueOf(tariff.getBasicRate()) != null
                && tariff.getContractType() != null
                && tariff.getPriceModel() != null) {
            return "";
        }

        String errorMessage = "The following fields of the tariff " + tariff.getTariffName() + " (number " + i + " in provided list) are not filled in correctly: \n";
        if (tariff.getTariffName() == null) {
            errorMessage += "-) TariffName\n";
        }
        if (String.valueOf(tariff.getBasicRate()) == null) {
            errorMessage += "-) BasicRate\n";
        }
        if (tariff.getContractType() == null) {
            errorMessage += "-) ContractType\n";
        }
        if (tariff.getPriceModel() == null) {
            errorMessage += "-) PriceModel\n";
        }
        errorMessage = errorMessage.replace("  ", " <TariffName missing> ");
        return errorMessage + "\n";
    }


    private void saveProviderAccount(Long id, Tariff tariff) {

        if (providerRepository.findById(id).isPresent()) {
            var provider = providerRepository.findById(id).get();

            var list = provider.getTariffs();
            list.add(tariff);
            providerRepository.save(provider);
        }

        if (unverifiedAccountRepository.findById(id).isPresent()) {
            var uProvider = unverifiedAccountRepository.findById(id).get();
            var list = uProvider.getTariffs();
            list.add(tariff);
            unverifiedAccountRepository.save(uProvider);
        }

        if (verifiedAccountRepository.findById(id).isPresent()) {
            var vProvider = verifiedAccountRepository.findById(id).get();
            var list = vProvider.getTariffs();
            list.add(tariff);
            verifiedAccountRepository.save(vProvider);
        }

    }

}
