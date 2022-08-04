package io.everyonecodes.anber.searchmanagement.service;

import io.everyonecodes.anber.providermanagement.data.ContractType;
import io.everyonecodes.anber.providermanagement.data.PriceModelType;
import io.everyonecodes.anber.providermanagement.data.ProviderType;
import io.everyonecodes.anber.ratingmanagement.data.Rating;
import io.everyonecodes.anber.searchmanagement.data.Provider;
import io.everyonecodes.anber.searchmanagement.data.ProviderDTO;
import io.everyonecodes.anber.searchmanagement.repository.ProviderRepository;
import io.everyonecodes.anber.tariffmanagement.data.Tariff;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SearchService {

    private final ProviderRepository providerRepository;
    private final ProviderTranslator translator;
    private final List<String> searchProperties;
    private final String sortAscending;
    private final String sortDescending;
    private final String noRatings;
    private final String comparatorSmallerThan;
    private final String comparatorGreaterThan;
    private final String concatOperatorAnd;
    private final String valueZero;


    public SearchService(ProviderRepository providerRepository, ProviderTranslator translator, List<String> searchProperties,
                         @Value("${data.search-engine.sort.asc}") String sortAscending,
                         @Value("${data.search-engine.sort.desc}") String sortDescending,
                         @Value("${messages.provider-account.no-ratings}") String noRatings,
                         @Value("${data.search-engine.comparator.smaller}") String comparatorSmallerThan,
                         @Value("${data.search-engine.comparator.greater}") String comparatorGreaterThan,
                         @Value("${data.search-engine.and}") String concatOperatorAnd,
                         @Value("${data.search-engine.zero}") String valueZero) {
        this.providerRepository = providerRepository;
        this.translator = translator;
        this.searchProperties = searchProperties;
        this.sortAscending = sortAscending;
        this.sortDescending = sortDescending;
        this.noRatings = noRatings;
        this.comparatorSmallerThan = comparatorSmallerThan;
        this.comparatorGreaterThan = comparatorGreaterThan;
        this.concatOperatorAnd = concatOperatorAnd;
        this.valueZero = valueZero;
    }

    public List<ProviderDTO> getAllDtos() {
        return providerRepository.findAll();
    }

    public List<Provider> getAllProviders() {
        var providerList = providerRepository.findAll();
        for (ProviderDTO dto : providerList) {
            if (dto.getRating() == null) {
                dto.setRating(new Rating(dto.getId(), new HashSet<>(), noRatings));
            }
        }
        return translateList(providerList);
    }

    private List<String> getProperties() {
        return searchProperties;
    }


    public List<Provider> manageFilters(String filters) {

        List<String> filtersList = new ArrayList<>(List.of(filters.split(concatOperatorAnd)));

        var checkedFilters = checkForFilter(filtersList);

        if (checkedFilters.isEmpty()) {
            return List.of();
        }

        List<ProviderDTO> providerList = new ArrayList<>();

        for (int i = 0; i < checkedFilters.size(); i++) {
            String filter = checkedFilters.get(i);

            for (ProviderDTO dto : providerList) {
                if (dto.getRating() == null) {
                    dto.setRating(new Rating(dto.getId(), new HashSet<>(), noRatings));
                }
            }
            providerList.forEach(this::checkRatings);

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

            //rating
            if (i == 3) {
                String operator = String.valueOf(filter.charAt(0));
                double value = Double.parseDouble(filter.substring(1));

                if (operator.equals(comparatorSmallerThan)) {
                    var providerListRating = providerList.stream()
                            .filter(prov -> Double.parseDouble(prov.getRating().getScore()) < (value))
                            .collect(Collectors.toCollection(ArrayList::new));
                    providerList.retainAll(providerListRating);
                } else {
                    var providerListRating =
                            providerList.stream()
                                    .filter(prov -> Double.parseDouble(prov.getRating().getScore()) > (value))
                                    .collect(Collectors.toCollection(ArrayList::new));
                    providerList.retainAll(providerListRating);
                }
            }
            //tariff name
            if (i == 4) {
                var providerListTn = providerRepository.findAll().stream()
                        .filter(prov -> prov.getTariffs().stream()
                                .anyMatch(br -> br.getTariffName().equals(filter)))
                        .collect(Collectors.toCollection(ArrayList::new));

                providerList.retainAll(providerListTn);
            }
            //basic rate
            if (i == 5) {
                String operator = String.valueOf(filter.charAt(0));
                double value = Double.parseDouble(filter.substring(1));


                if (operator.equals(comparatorGreaterThan)) {
                    var providerListBr1 =
                            providerList.stream()
                                    .filter(prov -> prov.getTariffs().stream()
                                            .anyMatch(br -> br.getBasicRate() > value))
                                    .collect(Collectors.toCollection(ArrayList::new));

                    providerList.retainAll(providerListBr1);
                } else {
                    var providerListBr2 =
                            providerList.stream()
                                    .filter(prov -> prov.getTariffs().stream()
                                            .anyMatch(br -> br.getBasicRate() <= value))
                                    .collect(Collectors.toCollection(ArrayList::new));
                    providerList.retainAll(providerListBr2);
                }
            }
            //price model
            if (i == 6) {
                var providerListPm = providerRepository.findAll().stream()
                        .filter(prov -> prov.getTariffs().stream()
                                .anyMatch(br -> br.getPriceModel().equals(PriceModelType.valueOf(filter.toUpperCase())))).collect(Collectors.toCollection(ArrayList::new));

                providerList.retainAll(providerListPm);
            }
        }

        var providers = translateList(providerList);
        for (Provider prov : providers) {
            if (prov.getRating().equals(valueZero)) {
                prov.setRating(noRatings);
            }
        }

        return providers;
    }


    private List<String> checkForFilter(List<String> filters) {

        List<String> sortedFilters = new ArrayList<>();

        for (int i = 0; i < getProperties().size(); i++) {
            sortedFilters.add("");
        }

        for (String filter : filters) {

            //country
            if (filter.startsWith(searchProperties.get(0).substring(0, 3))) {
                String country = filter.substring(3).replace("_", " ");
                sortedFilters.set(0, country);
            }
            //provider type
            if (filter.startsWith(searchProperties.get(1).substring(0, 3))) {
                String type = filter.substring(3).toUpperCase();

                var providerTypes = Arrays.stream(getEnumNames(ProviderType.class)).toList();

                if (!providerTypes.contains(type)) {
                    type = "";
                }

                sortedFilters.set(1, type);
            }
            //provider name
            if (filter.startsWith(searchProperties.get(2).substring(0, 3))) {
                String providerName = filter.substring(3);
                sortedFilters.set(2, providerName);
            }

            //rating < filter
            if (filter.startsWith(searchProperties.get(3).substring(0, 2) + comparatorSmallerThan)) {
                String rating = filter.substring(2);
                sortedFilters.set(3, rating);
            }
            //rating > filter
            if (filter.startsWith(searchProperties.get(3).substring(0, 2) + comparatorGreaterThan)) {
                String rating = filter.substring(2);
                sortedFilters.set(3, rating);
            }
            //rating = filter
            if (filter.startsWith(searchProperties.get(3).substring(0, 3))) {
                String rating = filter.substring(2);
                sortedFilters.set(3, rating);
            }

            //tariff name
            if (filter.startsWith(searchProperties.get(4).substring(0, 3))) {
                String tariffName = filter.substring(3);
                sortedFilters.set(4, tariffName);
            }
            //basic rate < filter
            if (filter.startsWith(searchProperties.get(5).substring(0, 2) + comparatorSmallerThan)) {
                String basicRate = filter.substring(2);
                sortedFilters.set(5, basicRate);
            }
            //basic rate > filter
            if (filter.startsWith(searchProperties.get(5).substring(0, 2) + comparatorGreaterThan)) {
                String basicRate = filter.substring(2);
                sortedFilters.set(5, basicRate);
            }
            //basic rate = filter
            if (filter.startsWith(searchProperties.get(5).substring(0, 3))) {
                String basicRate = filter.substring(2);
                sortedFilters.set(5, basicRate);
            }

            //contract type
            if (filter.startsWith(searchProperties.get(6).substring(0, 3))) {
                String type = filter.substring(3).toUpperCase();

                var contractTypes = Arrays.stream(getEnumNames(ContractType.class)).toList();

                if (!contractTypes.contains(type)) {
                    type = "";
                }

                sortedFilters.set(6, type);
            }
            //price model
            if (filter.startsWith(searchProperties.get(7).substring(0, 3))) {
                String priceModel = filter.substring(3).toUpperCase();

                var priceModelTypes = Arrays.stream(getEnumNames(PriceModelType.class)).toList();

                if (!priceModelTypes.contains(priceModel)) {
                    priceModel = "";
                }

                sortedFilters.set(7, priceModel);
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


        var dtoWithMultipleTariffs = dtoList.stream()
                .filter(prov -> prov.getTariffs().size() > 1)
                .collect(Collectors.toList());

        for (ProviderDTO dto : dtoWithMultipleTariffs) {
            for (int i = 0; i < dto.getTariffs().size(); i++) {
                dtoList.remove(dto);
                dtoList.add(new ProviderDTO(
                                dto.getProviderName(),
                                List.of(new Tariff(
                                        dto.getTariffs().get(i).getTariffName(),
                                        dto.getTariffs().get(i).getBasicRate(),
                                        dto.getTariffs().get(i).getContractType(),
                                        dto.getTariffs().get(i).getPriceModel(),
                                        dto.getId())
                                ),
                                dto.getRating()
                        )
                );
            }
        }

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

    public List<Provider> sortByRating(String operator, String filters) {

        var providers = manageFilters(filters);

        if (operator.equalsIgnoreCase(sortAscending)) {
            providers.sort(Comparator.comparing(Provider::getRating));
        }
        if (operator.equalsIgnoreCase(sortDescending)) {
            providers.sort(Comparator.comparing(Provider::getRating).reversed());
        }

        return providers;
    }

    private Rating checkRatings(ProviderDTO dto) {
        var rating = dto.getRating();

        if (rating == null) {
            dto.setRating(new Rating(dto.getId(), Set.of(), noRatings));
        }

        if (rating.getScore().equals(noRatings)) {
            dto.setRating(new Rating(rating.getId(), rating.getUsersRated(), valueZero));
            return rating;
        }
        return rating;
    }

}
