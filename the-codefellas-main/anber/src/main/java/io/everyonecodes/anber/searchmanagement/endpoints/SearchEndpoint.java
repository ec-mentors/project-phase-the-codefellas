package io.everyonecodes.anber.searchmanagement.endpoints;

import com.itextpdf.text.DocumentException;
import io.everyonecodes.anber.email.service.EmailService;
import io.everyonecodes.anber.searchmanagement.data.Provider;
import io.everyonecodes.anber.searchmanagement.data.ProviderDTO;
import io.everyonecodes.anber.searchmanagement.service.DataToPDFService;
import io.everyonecodes.anber.searchmanagement.service.SearchService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/provider")
public class SearchEndpoint {

    private final SearchService searchService;
    private final DataToPDFService data;
    private final EmailService emailService;
    private final List<String> searchProperties;

    public SearchEndpoint(SearchService searchService,
                          DataToPDFService data,
                          EmailService emailService,
                          List<String> searchProperties) {
        this.searchService = searchService;
        this.data = data;
        this.emailService = emailService;
        this.searchProperties = searchProperties;
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
        containsCountryAndProviderType(filters);
        return searchService.manageFilters(filters);
    }

    @GetMapping("/search/sorted/basicrate/{operator}/{filters}")
    @Secured("ROLE_USER")
    List<Provider> getSortedProvidersWithOptionalFilters(@PathVariable String operator, @PathVariable String filters) {
        containsCountryAndProviderType(filters);
        return searchService.sortByBasicRate(operator, filters);
    }

    @GetMapping("/search/sorted/rating/{operator}/{filters}")
    @Secured("ROLE_USER")
    List<Provider> getSortedProvidersByRatingWithOptionalFilters(@PathVariable String operator, @PathVariable String filters) {
        containsCountryAndProviderType(filters);
        return searchService.sortByRating(operator, filters);
    }

    @GetMapping("/search/{filters}/export")
    String getProvidersWithFiltersAsPDF(Authentication authentication, @PathVariable String filters) throws DocumentException, IOException {
        containsCountryAndProviderType(filters);
        emailService.sendSearchResultAsPdf(authentication.getName(), filters);
        return "Email with PDF has been sent.";
    }

    // Testing with Dummy PDF

    @GetMapping("/search/pdf")
    String getProvidersWithDummyPDF(Authentication authentication) throws DocumentException, IOException {
        emailService.sendSearchResultAsPdf(authentication.getName(), "filters");
        return "Email with PDF has been sent.";
    }

    @GetMapping("/search/sorted/basicrate/{operator}/{filters}/export")
    String getSortedProvidersWithOptionalFiltersAsPDF(Authentication authentication, @PathVariable String operator, @PathVariable String filters) throws DocumentException {
        containsCountryAndProviderType(filters);
        emailService.sendSearchResultAsPdfSorted(authentication.getName(), operator, filters);
        return "Email with PDF has been sent.";
    }


    private void containsCountryAndProviderType(String input) {
        String countryIdentifier = searchProperties.get(0).substring(0,3);
        String providerTypeIdentifier = searchProperties.get(1).substring(0,3);

        if (!input.contains(countryIdentifier) || !input.contains(providerTypeIdentifier)) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Country (cn) and ProviderType (pt) required for search!") {

            };
        }
    }
}
