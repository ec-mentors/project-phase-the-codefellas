package io.everyonecodes.anber.searchmanagement.service;

import io.everyonecodes.anber.searchmanagement.data.PriceModelType;
import io.everyonecodes.anber.searchmanagement.data.Provider;
import io.everyonecodes.anber.searchmanagement.data.ProviderDTO;
import io.everyonecodes.anber.searchmanagement.data.ProviderType;
import io.everyonecodes.anber.searchmanagement.repository.ProviderRepository;
import org.springframework.beans.factory.annotation.Value;
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
    private final String sortAscending;
    private final String sortDescending;

    public SearchService(ProviderRepository providerRepository, ProviderTranslator translator, List<String> searchProperties,
                         @Value("${data.search-engine.sort.asc}") String sortAscending,
                         @Value("${data.search-engine.sort.desc}") String sortDescending) {
        this.providerRepository = providerRepository;
        this.translator = translator;
        this.searchProperties = searchProperties;
        this.sortAscending = sortAscending;
        this.sortDescending = sortDescending;
    }

    public List<ProviderDTO> getAll() {
        return providerRepository.findAll();
    }

    private List<String> getProperties() {
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
            //country
            if (i == 0) {
                providerList = new ArrayList<>(providerRepository.findByCountryName(filter));
            }
            //provider type
            if (i == 1) {
                var providerListPt = new ArrayList<>(providerRepository.findByProviderType(ProviderType.valueOf(filter.toUpperCase())));
                providerList.retainAll(providerListPt);
            }
            //provider name
            if (i == 2) {
                var providerListPn = new ArrayList<>(providerRepository.findByProviderName(filter));
                providerList.retainAll(providerListPn);
            }
            //tariff name
            if (i == 3) {
                var providerListTn = new ArrayList<>(providerRepository.findByTariffName(filter));
                providerList.retainAll(providerListTn);
            }
            //basic rate
            if (i == 4) {
                String operator = String.valueOf(filter.charAt(0));
                double value = Double.parseDouble(filter.substring(1));

                if (operator.equals(">")) {
                    var providerListBr1 =
                            providerList.stream()
                                    .filter(prov -> prov.getBasicRate() > (value))
                                    .collect(Collectors.toCollection(ArrayList::new));
                    providerList.retainAll(providerListBr1);
                } else {
                    var providerListBr2 =
                            providerList.stream()
                                    .filter(prov -> prov.getBasicRate() < (value))
                                    .collect(Collectors.toCollection(ArrayList::new));
                    providerList.retainAll(providerListBr2);
                }

            }
            //price model
            if (i == 5) {
                var providerListPm = new ArrayList<>(providerRepository.findByPriceModel(PriceModelType.valueOf(filter.toUpperCase())));
                providerList.retainAll(providerListPm);
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

            //country
            if (filter.startsWith(searchProperties.get(0).substring(0,3))) {
                String country = filter.substring(3).replace("_", " ");
                sortedFilters.set(0, country);
            }
            //provider type
            if (filter.startsWith(searchProperties.get(1).substring(0,3))) {
                String type = filter.substring(3).toUpperCase();

                var providerTypes = Arrays.stream(getEnumNames(ProviderType.class)).toList();

                if (!providerTypes.contains(type)) {
                    type = "";
                }

                sortedFilters.set(1, type);
            }
            //provider name
            if (filter.startsWith(searchProperties.get(2).substring(0,3))) {
                String providerName = filter.substring(3);
                sortedFilters.set(2, providerName);
            }
            //tariff name
            if (filter.startsWith(searchProperties.get(3).substring(0,3))) {
                String tariffName = filter.substring(3);
                sortedFilters.set(3, tariffName);
            }
            //basic rate < filter
            if (filter.startsWith(searchProperties.get(4).substring(0,2) +"<")) {
                String basicRate = filter.substring(2);
                sortedFilters.set(4, basicRate);
            }
            //basic rate > filter
            if (filter.startsWith(searchProperties.get(4).substring(0,2) +">")) {
                String basicRate = filter.substring(2);
                sortedFilters.set(4, basicRate);
            }
            //basic rate = filter
            if (filter.startsWith(searchProperties.get(4).substring(0,3))) {
                String basicRate = filter.substring(2);
                sortedFilters.set(4, basicRate);
            }
            //price model
            if (filter.startsWith(searchProperties.get(5).substring(0,3))) {
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


    public List<Provider> sortByBasicRate(String operator, String filters) {

        var providers = manageFilters(filters);

        if (operator.equalsIgnoreCase(sortAscending)) {
            providers.sort(Comparator.comparing(Provider::getBasicRate));
        }
        if (operator.equalsIgnoreCase(sortDescending)) {
            providers.sort(Comparator.comparing(Provider::getBasicRate).reversed());
        }

        return providers;
    }
}
