package io.everyonecodes.anber.tariffmanagement.service;

import io.everyonecodes.anber.providermanagement.repository.UnverifiedAccountRepository;
import io.everyonecodes.anber.providermanagement.repository.VerifiedAccountRepository;
import io.everyonecodes.anber.searchmanagement.repository.ProviderRepository;
import io.everyonecodes.anber.tariffmanagement.data.Tariff;
import io.everyonecodes.anber.tariffmanagement.email.TariffErrorEmailService;
import io.everyonecodes.anber.tariffmanagement.repository.TariffRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TariffService {

    private final TariffRepository tariffRepository;
    private final ProviderRepository providerRepository;
    private final VerifiedAccountRepository verifiedAccountRepository;
    private final UnverifiedAccountRepository unverifiedAccountRepository;
    private final String tariffFilePath;
    private final String placeholderId;
    private final String updateFailure;
    private final String updateSuccess;
    private final String providerNotFound;
    private final String errorMessage1;
    private final String errorMessage2;
    private final String listElementTariffName;
    private final String listElementBasicRate;
    private final String listElementContractType;
    private final String listElementPriceModel;
    private final String missingTariffName;
    private final String placeholderTariffName;
    private final TariffErrorEmailService tariffErrorEmailService;


    public TariffService(TariffRepository tariffRepository,
                         ProviderRepository providerRepository,
                         VerifiedAccountRepository verifiedAccountRepository,
                         UnverifiedAccountRepository unverifiedAccountRepository,
                         @Value("${paths.tariff-file}") String tariffFilePath,
                         @Value("${data.placeholders.id}") String placeholderId,
                         @Value("${messages.tariff.failure}") String updateFailure,
                         @Value("${messages.tariff.success}") String updateSuccess,
                         @Value("${messages.tariff.not-found}") String providerNotFound,
                         @Value("${messages.tariff.error1}") String errorMessage1,
                         @Value("${messages.tariff.error2}") String errorMessage2,
                         @Value("${messages.tariff.tariffName}") String listElementTariffName,
                         @Value("${messages.tariff.basicRate}") String listElementBasicRate,
                         @Value("${messages.tariff.contractType}") String listElementContractType,
                         @Value("${messages.tariff.priceModel}") String listElementPriceModel,
                         @Value("${messages.tariff.missing}") String missingTariffName,
                         @Value("${data.placeholders.tariffName}") String placeholderTariffName,
                         TariffErrorEmailService tariffErrorEmailService) {
        this.tariffRepository = tariffRepository;
        this.providerRepository = providerRepository;
        this.verifiedAccountRepository = verifiedAccountRepository;
        this.unverifiedAccountRepository = unverifiedAccountRepository;
        this.tariffFilePath = tariffFilePath;
        this.placeholderId = placeholderId;
        this.updateFailure = updateFailure;
        this.updateSuccess = updateSuccess;
        this.providerNotFound = providerNotFound;
        this.errorMessage1 = errorMessage1;
        this.errorMessage2 = errorMessage2;
        this.listElementTariffName = listElementTariffName;
        this.listElementBasicRate = listElementBasicRate;
        this.listElementContractType = listElementContractType;
        this.listElementPriceModel = listElementPriceModel;
        this.missingTariffName = missingTariffName;
        this.placeholderTariffName = placeholderTariffName;
        this.tariffErrorEmailService = tariffErrorEmailService;
    }

    private final String newLine = "\n";

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

            List<Tariff> tariffs = tariffFileReader(tariffFilePath.replace(placeholderId, String.valueOf(id)));

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
                tariffErrorEmailService.sendEmailTariffError(dto, totalResult);
                return (totalResult + newLine + updateFailure);
            } else {
                return updateSuccess;
            }

        }
        return providerNotFound;
    }


    private String checkTariffForErrors(Tariff tariff, int i) {

        if (tariff.getTariffName() != null
                && String.valueOf(tariff.getBasicRate()) != null
                && tariff.getContractType() != null
                && tariff.getPriceModel() != null) {
            return "";
        }


        String errorMessage = "";

        if (tariff.getTariffName() == null) {
            errorMessage = errorMessage1.replace(placeholderTariffName, "") + i + errorMessage2 + newLine;
        }
        else {
            errorMessage = errorMessage1.replace(placeholderTariffName, tariff.getTariffName()) + i + errorMessage2 + newLine;
        }

        if (tariff.getTariffName() == null) {
            errorMessage += listElementTariffName + newLine;
        }
        if (String.valueOf(tariff.getBasicRate()) == null) {
            errorMessage += listElementBasicRate + newLine;
        }
        if (tariff.getContractType() == null) {
            errorMessage += listElementContractType + newLine;
        }
        if (tariff.getPriceModel() == null) {
            errorMessage += listElementPriceModel + newLine;
        }
        errorMessage = errorMessage.replace("  ", missingTariffName);
        return errorMessage + newLine;
    }


    private void saveProviderAccount(Long id, Tariff tariff) {

        if (providerRepository.findById(id).isPresent()) {
            var provider = providerRepository.findById(id).get();

            var list = provider.getTariffs();
            list.add(tariff);
            providerRepository.save(provider);
            //send notification mails to subscribers
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
