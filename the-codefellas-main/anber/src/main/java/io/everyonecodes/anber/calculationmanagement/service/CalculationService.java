package io.everyonecodes.anber.calculationmanagement.service;


import io.everyonecodes.anber.calculationmanagement.data.AverageConsumption;
import io.everyonecodes.anber.homemanagement.data.Home;
import io.everyonecodes.anber.providermanagement.data.ProviderType;
import io.everyonecodes.anber.searchmanagement.data.ProviderDTO;
import io.everyonecodes.anber.searchmanagement.repository.ProviderRepository;
import io.everyonecodes.anber.tariffmanagement.data.Tariff;
import io.everyonecodes.anber.tariffmanagement.repository.TariffRepository;
import io.everyonecodes.anber.usermanagement.data.User;
import io.everyonecodes.anber.usermanagement.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

@Service
public class CalculationService {

    private final UserRepository userRepository;
    private final TariffRepository tariffRepository;
    private final ProviderRepository providerRepository;

    public CalculationService(UserRepository userRepository, TariffRepository tariffRepository, ProviderRepository providerRepository) {
        this.userRepository = userRepository;
        this.tariffRepository = tariffRepository;
        this.providerRepository = providerRepository;
    }

    public Optional<Double> getAverageConsumption(String userName, Long homeID) {
        Optional<User> oUser = userRepository.findOneByEmail(userName);
        List<String> data = read("the-codefellas-main/anber/src/main/resources/files/Tabelle.csv");
        List<AverageConsumption> consumptions = parseData(data);

        if (oUser.isPresent()) {
            User user = oUser.get();
            List<Home> homes = user.getSavedHomes();
            var oHome = homes.stream()
                    .filter(x -> x.getId().equals(homeID))
                    .findFirst();

            if (oHome.isPresent()){
                Home home = oHome.get();
                double sizeInSquareMeters = home.getSizeInSquareMeters();
                String country = home.getCountry();
                var oConsumptionOfCountry = consumptions.stream()
                        .filter(x -> x.getCountry().equals(country))
                        .map(AverageConsumption::getAverageConsumption)
                        .findFirst();

                if (oConsumptionOfCountry.isPresent()){
                    double consumptionOfCountry = oConsumptionOfCountry.get();
                    double averageConsumption = sizeInSquareMeters * consumptionOfCountry;
                    return Optional.of(consumptionOfCountry);
                }
            }
        }
        return Optional.empty();
    }

    public List<String> read(String file) {
        Path path = Path.of(file);
        try {
            return Files.readAllLines(path);
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public List<AverageConsumption> parseData(List<String> data) {
        List<AverageConsumption> consumptions = new ArrayList<>(List.of());
        for (String currentElement : data) {
            List<String> consumptionData = new ArrayList<>(List.of(currentElement.split(",", -1)));
            String country = consumptionData.get(0);
            String averageConsumption = consumptionData.get(1);
            AverageConsumption consumption = new AverageConsumption(country, Double.parseDouble(averageConsumption));
            consumptions.add(consumption);
        }
        return consumptions;
    }


    public Optional<Double> getAverageConsumptionForTariff(String username, Long homeID, Long tariffID) {
        Optional<User> oUser = userRepository.findOneByEmail(username);
        if (oUser.isPresent()) {
            User user = oUser.get();
            List<Home> homes = user.getSavedHomes();
            var oHome = homes.stream()
                    .filter(x -> x.getId().equals(homeID))
                    .findFirst();

            if (oHome.isPresent()) {
                Home home = oHome.get();
                double sizeInSquareMeters = home.getSizeInSquareMeters();

                Optional<Tariff> oTariff = tariffRepository.findById(tariffID);
                if (oTariff.isPresent()) {
                    Tariff tariff = oTariff.get();

                    var oDto = providerRepository.findById(tariff.getProviderId());
                    if (oDto.isPresent()){
                        ProviderDTO providerDTO = oDto.get();
                        var providerTape = providerDTO.getProviderType();
                        if (providerTape.equals(ProviderType.INTERNET)){
                            return Optional.of(tariff.getBasicRate());
                        }
                    }
                    double basicRate = tariff.getBasicRate();
                    return Optional.of(sizeInSquareMeters * basicRate);
                }
            }
        }
        return Optional.empty();
    }

    public List<Map<String, Double>> getAverageConsumptionForAllHomes(String username) {
        Optional<User> oUser = userRepository.findOneByEmail(username);
        List<String> data = read("the-codefellas-main/anber/src/main/resources/files/Tabelle.csv");
        List<AverageConsumption> consumptions = parseData(data);
        List<Map<String, Double>> homesConsumptionList = new ArrayList<>();
        if (oUser.isPresent()) {
            User user = oUser.get();
            List<Home> homes = user.getSavedHomes();

            if (!homes.isEmpty()) {
                for (int i = 0; i < homes.size(); i++) {
                    Home home = homes.get(i);
                    double sizeInSquareMeters = home.getSizeInSquareMeters();
                    String country = home.getCountry();
                    var oConsumptionOfCountry = consumptions.stream()
                            .filter(x -> x.getCountry().equals(country))
                            .map(AverageConsumption::getAverageConsumption)
                            .findFirst();

                    if (oConsumptionOfCountry.isPresent()) {
                        double consumptionOfCountry = oConsumptionOfCountry.get();
                        double averageConsumption = sizeInSquareMeters * consumptionOfCountry;
                        Map<String, Double> homeCalculatedConsumption = new HashMap<>();
                        homeCalculatedConsumption.put(home.getHomeName(), averageConsumption);
                        homesConsumptionList.add(homeCalculatedConsumption);
                    }
                }
                return homesConsumptionList;
            }
        }
        return homesConsumptionList;
    }


    public Optional<Double> getAverageConsumptionForData(String username, Long id, String squareMeter) {
        Optional<User> oUser = userRepository.findOneByEmail(username);
        if (oUser.isPresent()) {
            User user = oUser.get();
            Optional<Tariff> oTariff = tariffRepository.findById(id);
            if (oTariff.isPresent()) {
                Tariff tariff = oTariff.get();
                double basicRate = tariff.getBasicRate();
                try {
                    var oDto = providerRepository.findById(tariff.getProviderId());
                    if (oDto.isPresent()){
                        ProviderDTO providerDTO = oDto.get();
                        var providerTape = providerDTO.getProviderType();
                        if (providerTape.equals(ProviderType.INTERNET)){
                            return Optional.of(tariff.getBasicRate());
                        }
                    }
                    double squareMeterDouble = Double.parseDouble(squareMeter);
                    return Optional.of(squareMeterDouble * basicRate);
                } catch (NumberFormatException e) {
                    return Optional.empty();
                }
            }
        }
        return Optional.empty();
    }

}
