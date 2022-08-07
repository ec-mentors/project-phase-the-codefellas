package io.everyonecodes.anber.searchmanagement.endpoints;

import io.everyonecodes.anber.searchmanagement.data.Provider;
import io.everyonecodes.anber.searchmanagement.data.ProviderDTO;
import io.everyonecodes.anber.searchmanagement.service.SearchService;
import org.springframework.security.access.annotation.Secured;
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


    @GetMapping("/getdto")
    @Secured("ROLE_ADMIN")
    List<ProviderDTO> getAllDto() {
        return searchService.getAllDtos();
    }

    @GetMapping("/get")
    @Secured("ROLE_USER")
    List<Provider> getAll() {
        return searchService.getAllProviders();
    }

    @GetMapping("/search/{filters}")
    @Secured("ROLE_USER")
    List<Provider> getProvidersWithOptionalFilters(@PathVariable String filters) {
        return searchService.manageFilters(filters);
    }

    @GetMapping("/search/sorted/basicrate/{operator}/{filters}")
    @Secured("ROLE_USER")
    List<Provider> getSortedProvidersWithOptionalFilters(@PathVariable String operator, @PathVariable String filters) {
        return searchService.sortByBasicRate(operator, filters);
    }

    @GetMapping("/search/sorted/rating/{operator}/{filters}")
    @Secured("ROLE_USER")
    List<Provider> getSortedProvidersByRatingWithOptionalFilters(@PathVariable String operator, @PathVariable String filters) {
        return searchService.sortByRating(operator, filters);
    }
}
