package io.everyonecodes.anber.searchmanagement.endpoints;

import io.everyonecodes.anber.searchmanagement.data.Provider;
import io.everyonecodes.anber.searchmanagement.data.ProviderDTO;
import io.everyonecodes.anber.searchmanagement.service.SearchService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/provider")
public class SearchEndpoint {

    private final SearchService searchService;

    public SearchEndpoint(SearchService searchService) {
        this.searchService = searchService;
    }


    @GetMapping("/get/all")
    List<ProviderDTO> getAll() {
        return searchService.getAll();
    }

    @GetMapping("/{filters}")
    List<Provider> getProvidersWithOptionalFilters(@PathVariable String filters) {
        return searchService.manageFilters(filters);
    }

    @GetMapping("/sorted/basicrate/{operator}/{filters}")
    List<Provider> getSortedProvidersWithOptionalFilters(@PathVariable String operator, @PathVariable String filters) {
        return searchService.sortByBasicRate(operator, filters);
    }

    @GetMapping("/sorted/rating/{operator}/{filters}")
    List<Provider> getSortedProvidersByRatingWithOptionalFilters(@PathVariable String operator, @PathVariable String filters) {
        return searchService.sortByRating(operator, filters);
    }
}
