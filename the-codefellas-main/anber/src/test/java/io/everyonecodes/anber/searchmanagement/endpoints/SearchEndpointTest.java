package io.everyonecodes.anber.searchmanagement.endpoints;

import io.everyonecodes.anber.searchmanagement.data.Provider;
import io.everyonecodes.anber.searchmanagement.service.SearchService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.security.web.SecurityFilterChain;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SearchEndpointTest {

    @Autowired
    TestRestTemplate testRestTemplate;

    @MockBean
    SearchService searchService;

    @MockBean
    SecurityFilterChain securityFilterChain;

    @Test
    void getAllDtos() {
        testRestTemplate.getForObject("/provider/getdto", Provider[].class);
        Mockito.verify(searchService).getAllDtos();
    }

    @Test
    void getAllProviders() {
        testRestTemplate.getForObject("/provider/get", Provider[].class);
        Mockito.verify(searchService).getAllProviders();
    }

    @Test
    void getProvidersWithOptionalFilters() {
        String filters = "test";
        testRestTemplate.getForObject("/provider/search/" + filters, Provider[].class);
        Mockito.verify(searchService).manageFilters(filters);
    }


    @Test
    void getSortedProvidersWithOptionalFilters() {
        String filters = "test";
        String operator = "foo";
        testRestTemplate.getForObject("/provider/search/sorted/basicrate/" + operator + "/" + filters, Provider[].class);
        Mockito.verify(searchService).sortByBasicRate(operator, filters);
    }

    @Test
    void getSortedByRatingProvidersWithOptionalFilters() {
        String filters = "test";
        String operator = "foo";
        testRestTemplate.getForObject("/provider/search/sorted/rating/" + operator + "/" + filters, Provider[].class);
        Mockito.verify(searchService).sortByRating(operator, filters);
    }
}