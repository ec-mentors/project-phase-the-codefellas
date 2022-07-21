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

    @GetMapping
    List<String> getProperties() {
        return searchService.getProperties();
    }

    @GetMapping("/get/all")
    List<ProviderDTO> getAll() {
        return searchService.getAll();
    }

    @GetMapping("/{filters}")
    List<Provider> getProvidersWithOptionalFilters(@PathVariable String filters) {
    return searchService.manageFilters(filters);
    }

    @GetMapping("/sorted/{way}/{filters}")
    List<Provider> getProvidersWithOptionalFilters(@PathVariable String way, @PathVariable String filters) {
        return searchService.sortByRate(way, filters);
    }


}
