package io.everyonecodes.anber.searchmanagement.service;

import io.everyonecodes.anber.searchmanagement.data.PriceModelType;
import io.everyonecodes.anber.searchmanagement.data.Provider;
import io.everyonecodes.anber.searchmanagement.data.ProviderDTO;
import io.everyonecodes.anber.searchmanagement.data.ProviderType;
import io.everyonecodes.anber.searchmanagement.repository.ProviderRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SearchService {

    private final ProviderRepository providerRepository;
    private final ProviderTranslator translator;
    private final List<String> searchProperties;

    public SearchService(ProviderRepository providerRepository, ProviderTranslator translator, List<String> searchProperties) {
        this.providerRepository = providerRepository;
        this.translator = translator;
        this.searchProperties = searchProperties;
    }

    public List<ProviderDTO> getAll() {
        return providerRepository.findAll();
    }

    public List<String> getProperties() {
        return searchProperties;
    }


    public List<Provider> manageFilters(String filters) {

        List<String> filtersList = new ArrayList<>(List.of(filters.split("&")));

        var checkedFilters = checkForFilter(filtersList);

        if (checkedFilters.isEmpty()) {
            return List.of();
        }

        List<ProviderDTO> providerList = new ArrayList<>();

        for (int i = 0; i < checkedFilters.size(); i++) {
            String filter = checkedFilters.get(i);
            if (filter.isEmpty()) {
                continue;
            }
            if (i == 0) {
                providerList = providerRepository.findByCountryName(filter);
            }
            if (i == 1) {
                providerList = providerList.stream()
                        .filter(prov -> prov.getProviderType().equals(ProviderType.valueOf(filter.toUpperCase())))
                        .collect(Collectors.toList());
            }
            if (i == 2) {
                providerList = providerList.stream()
                        .filter(prov -> prov.getProviderName().equals(filter))
                        .collect(Collectors.toList());
            }
            if (i == 3) {
                providerList = providerList.stream()
                        .filter(prov -> prov.getTariffName().equals(filter))
                        .collect(Collectors.toList());
            }
            if (i == 4) {
                String operator = String.valueOf(filter.charAt(0));
                double value = Double.parseDouble(filter.substring(1));

                if (operator.equals("<")) {
                    providerList = providerList.stream()
                            .filter(prov -> prov.getBasicRate() < (value))
                            .collect(Collectors.toList());
                }
                else {
                    providerList = providerList.stream()
                            .filter(prov -> prov.getBasicRate() > (value))
                            .collect(Collectors.toList());
                }


                providerList = providerList.stream()
                        .filter(prov -> prov.getBasicRate() <= (value))
                        .collect(Collectors.toList());
            }
            if (i == 5) {
                providerList = providerList.stream()
                        .filter(prov -> prov.getPriceModel().equals(PriceModelType.valueOf(filter.toUpperCase())))
                        .collect(Collectors.toList());
            }
        }

        return translateList(providerList);
    }

    private List<String> checkForFilter(List<String> filters) {

        List<String> sortedFilters = new ArrayList<>();

        for (int i = 0; i < getProperties().size(); i++) {
            sortedFilters.add("");
        }

        for (String filter : filters) {
            if (filter.startsWith("cn=")) {
                String country = filter.substring(3);
                sortedFilters.set(0, country);
            }
            if (filter.startsWith("pt=")) {
                String type = filter.substring(3).toUpperCase();

                var providerTypes = Arrays.stream(getEnumNames(ProviderType.class)).toList();

                if (!providerTypes.contains(type)) {
                    type = "";
                }

                sortedFilters.set(1, type);
            }
            if (filter.startsWith("pn=")) {
                String providerName = filter.substring(3);
                sortedFilters.set(2, providerName);
            }
            if (filter.startsWith("tn=")) {
                String tariffName = filter.substring(3);
                sortedFilters.set(3, tariffName);
            }
            if (filter.startsWith("br<")) {
                String basicRate = filter.substring(2);
                sortedFilters.set(4, basicRate);
            }
            if (filter.startsWith("br>")) {
                String basicRate = filter.substring(2);
                sortedFilters.set(4, basicRate);
            }
            if (filter.startsWith("br=")) {
                String basicRate = filter.substring(2);
                sortedFilters.set(4, basicRate);
            }
            if (filter.startsWith("pm=")) {
                String priceModel = filter.substring(3).toUpperCase();

                var priceModelTypes = Arrays.stream(getEnumNames(PriceModelType.class)).toList();

                if (!priceModelTypes.contains(priceModel)) {
                    priceModel = "";
                }

                sortedFilters.set(5, priceModel);
            }
        }

        if (!sortedFilters.get(0).isEmpty()) {
            if (!sortedFilters.get(1).isEmpty()) {
                return sortedFilters;
            }
        }
        return List.of();
    }


    private List<Provider> translateList(List<ProviderDTO> dtoList) {
        return dtoList.stream()
                .map(translator::DtoToProvider)
                .collect(Collectors.toList());
    }

    private static String[] getEnumNames(Class<? extends Enum<?>> e) {
        return Arrays.stream(e.getEnumConstants()).map(Enum::name).toArray(String[]::new);
    }




    public List<Provider> sortByRate(String way, String filters) {

        var providers = manageFilters(filters);

        if (way.equalsIgnoreCase("asc")) {
            providers.sort(Comparator.comparing(Provider::getBasicRate));
        }
        if (way.equalsIgnoreCase("desc")) {
            providers.sort(Comparator.comparing(Provider::getBasicRate).reversed());
        }

        return providers;
    }
}
