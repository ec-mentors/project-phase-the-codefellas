package io.everyonecodes.anber.tariffmanagement.service;

import io.everyonecodes.anber.providermanagement.repository.VerifiedAccountRepository;
import io.everyonecodes.anber.searchmanagement.repository.ProviderRepository;
import io.everyonecodes.anber.tariffmanagement.data.Tariff;
import io.everyonecodes.anber.tariffmanagement.email.TariffErrorEmailService;
import io.everyonecodes.anber.tariffmanagement.email.UpdatedVTariffEmailService;
import io.everyonecodes.anber.tariffmanagement.repository.TariffRepository;
import io.everyonecodes.anber.usermanagement.data.User;
import io.everyonecodes.anber.usermanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TariffService {

    private final TariffRepository tariffRepository;
    private final ProviderRepository providerRepository;
    private final VerifiedAccountRepository verifiedAccountRepository;
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
    private final UpdatedVTariffEmailService updatedVTariffEmailService;
    private final UserRepository userRepository;
    private final String verificationMark;


    public TariffService(TariffRepository tariffRepository,
                         ProviderRepository providerRepository,
                         VerifiedAccountRepository verifiedAccountRepository,
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
                         TariffErrorEmailService tariffErrorEmailService,
                         UpdatedVTariffEmailService updatedVTariffEmailService,
                         UserRepository userRepository,
                         @Value("${messages.provider-account.verified}") String verificationMark) {
        this.tariffRepository = tariffRepository;
        this.providerRepository = providerRepository;
        this.verifiedAccountRepository = verifiedAccountRepository;
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
        this.updatedVTariffEmailService = updatedVTariffEmailService;
        this.userRepository = userRepository;
        this.verificationMark = verificationMark;
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
            int i = 0;

            var dto = providerRepository.findById(id).get();
            int maxIterations = dto.getTariffs().size();


            String updateMessage = "";
            for (Tariff tariff : tariffs) {

                var result = checkTariffForErrors(tariff, i);

                if (result.isBlank()) {

                    if (i < maxIterations) {
                        Long existingId = dto.getTariffs().get(i).getId();
                        tariff.setId(existingId);
                    }
                    updateMessage += updateTariff(tariff, i, id, maxIterations);

                    //providerId for identifying which tariff belongs to what provider
                    tariff.setProviderId(id);
                    tariffRepository.save(tariff);

                } else {
                    totalResult += result;
                }
                i++;
            }

            if (!totalResult.isBlank()) {
                tariffErrorEmailService.sendEmailTariffError(dto, totalResult);
                return (totalResult + newLine + updateFailure);
            } else {

                sendUpdatesPerEmail(id, updateMessage);
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
            errorMessage = errorMessage1.replace(placeholderTariffName, "") + (i+1) + errorMessage2 + newLine;
        } else {
            errorMessage = errorMessage1.replace(placeholderTariffName, tariff.getTariffName()) + (i+1) + errorMessage2 + newLine;
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


    private String updateTariff(Tariff tariff, int i, Long id, int maxIterations) {

        var oProv = verifiedAccountRepository.findById(id);
        var oDto = providerRepository.findById(id);

        String message = "";
        if (oProv.isPresent()) {
            var prov = oProv.get();

            if (i < maxIterations) {
                var provTariff = prov.getTariffs().get(i);

                message = collectUpdateMessages(prov.getProviderName(), prov.getTariffs().get(i).getTariffName(),
                        provTariff.getBasicRate(), tariff.getBasicRate());

                provTariff.setTariffName(tariff.getTariffName());
                provTariff.setBasicRate(tariff.getBasicRate());
                provTariff.setContractType(tariff.getContractType());
                provTariff.setPriceModel(tariff.getPriceModel());

            } else {
                var list = prov.getTariffs();
                list.add(tariff);
                prov.setTariffs(list);
            }
            verifiedAccountRepository.save(prov);

        }
        if (oDto.isPresent()) {
            var dto = oDto.get();

            if (i < maxIterations) {
                var dtoTariff = dto.getTariffs().get(i);

                dtoTariff.setTariffName(tariff.getTariffName());
                dtoTariff.setBasicRate(tariff.getBasicRate());
                dtoTariff.setContractType(tariff.getContractType());
                dtoTariff.setPriceModel(tariff.getPriceModel());
            } else {
                var list = dto.getTariffs();
                list.add(tariff);
                dto.setTariffs(list);
            }
            providerRepository.save(dto);
        }
        return message;
    }


    private String collectUpdateMessages(String providerName, String tariffName, double brOld, double brNew) {
        return "Provider " + providerName.replace(verificationMark, "") + " changed price of tariff " + tariffName
                + " from " + brOld + " to " + brNew + "\n";
    }


    private void sendUpdatesPerEmail(Long id, String message) {

        var userList = userRepository.findAll();

        List<User> subscribedUsers = userList.stream()
                .filter(user -> user.getSubscriptions().contains(id))
                .collect(Collectors.toList());

        for (User user : subscribedUsers) {
            updatedVTariffEmailService.sendEmailUpdatedTariffs(user, message);
        }
    }


}
