package io.everyonecodes.anber.tariffmanagement.service;

import io.everyonecodes.anber.providermanagement.data.ContractType;
import io.everyonecodes.anber.providermanagement.data.PriceModelType;
import io.everyonecodes.anber.tariffmanagement.data.Tariff;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TariffParser {

    public Optional<Tariff> parseLine(String tariff) {
        Tariff initialTariff = new Tariff(null, 0.0, null, null);

        if (!tariff.isEmpty()) {
            try {
                List<String> tariffData = new ArrayList<>(List.of(tariff.split(";", 4)));

                if (tariffData.size() >= 1) {

                    try {
                        double tn = Double.parseDouble(tariffData.get(0));

                    } catch (NumberFormatException n) {
                        if (!getEnumNames(ContractType.class).contains(tariffData.get(0))
                                || !getEnumNames(PriceModelType.class).contains(tariffData.get(0))) {
                            initialTariff.setTariffName(tariffData.get(0).trim());
                        }
                    }

                    try {
                        double br = Double.parseDouble(tariffData.get(1));
                        initialTariff.setBasicRate(br);

                    } catch (NumberFormatException n) {
                        //do nothing, keep index of basicRate null
                    }

                    try {
                        String ct = tariffData.get(2).trim().toUpperCase().replace(" ", "_");
                        String pm = tariffData.get(3).trim().toUpperCase().replace(" ", "_");

                        if (getEnumNames(ContractType.class).contains(ct)) {
                            initialTariff.setContractType(Enum.valueOf(ContractType.class, ct));
                        }
                        if (getEnumNames(PriceModelType.class).contains(pm)) {
                            initialTariff.setPriceModel(Enum.valueOf(PriceModelType.class, pm));
                        }
                    } catch (IndexOutOfBoundsException i) {
                        //do nothing, keep index of enum(s) null
                    }

                    return Optional.of(initialTariff);
                }
            } catch (IndexOutOfBoundsException | NoSuchElementException e) {
                System.out.println("Could not read data from file correctly.");
                e.printStackTrace();
            }
        }
        return Optional.of(initialTariff);
    }


    private static List<String> getEnumNames(Class<? extends Enum<?>> e) {
        return Stream.of(e.getEnumConstants()).map(Enum::name).collect(Collectors.toList());
    }
}
