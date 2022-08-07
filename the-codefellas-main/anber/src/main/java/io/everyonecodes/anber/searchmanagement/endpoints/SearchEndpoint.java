package io.everyonecodes.anber.searchmanagement.endpoints;

import com.itextpdf.text.DocumentException;
import io.everyonecodes.anber.email.service.EmailService;
import io.everyonecodes.anber.searchmanagement.data.Provider;
import io.everyonecodes.anber.searchmanagement.data.ProviderDTO;
import io.everyonecodes.anber.searchmanagement.service.DataToPDFService;
import io.everyonecodes.anber.searchmanagement.service.SearchService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/provider")
public class SearchEndpoint {

    private final SearchService searchService;
    private final DataToPDFService data;
    private final EmailService emailService;

    public SearchEndpoint(SearchService searchService, DataToPDFService data, EmailService emailService) {
        this.searchService = searchService;
        this.data = data;
        this.emailService = emailService;
    }


    @GetMapping("/getdto")
    @Secured("ROLE_ADMIN")
    List<ProviderDTO> getAllDto() {
        return searchService.getAllDtos();
    }

    @GetMapping("/get")
    List<Provider> getAll() {
        return searchService.getAllProviders();
    }

    @GetMapping("/search/{filters}")
    List<Provider> getProvidersWithOptionalFilters(@PathVariable String filters) {
        return searchService.manageFilters(filters);
    }

    @GetMapping("/search/sorted/basicrate/{operator}/{filters}")
    List<Provider> getSortedProvidersWithOptionalFilters(@PathVariable String operator, @PathVariable String filters) {
        return searchService.sortByBasicRate(operator, filters);
    }

    @GetMapping("/search/sorted/rating/{operator}/{filters}")
    List<Provider> getSortedProvidersByRatingWithOptionalFilters(@PathVariable String operator, @PathVariable String filters) {
        return searchService.sortByRating(operator, filters);
    }

    @GetMapping("/search/{filters}/export")
    String getProvidersWithFiltersAsPDF(Authentication authentication, @PathVariable String filters) throws DocumentException, IOException {
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
        emailService.sendSearchResultAsPdfSorted(authentication.getName(), operator, filters);
        return "Email with PDF has been sent.";
    }
}
