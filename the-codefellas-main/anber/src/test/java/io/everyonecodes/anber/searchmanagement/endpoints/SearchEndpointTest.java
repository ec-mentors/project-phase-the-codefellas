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

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SearchEndpointTest {

    @Autowired
    TestRestTemplate testRestTemplate;

    @MockBean
    SearchService searchService;

    @MockBean
    SecurityFilterChain securityFilterChain;

    @Test
    void getAll() {
        testRestTemplate.getForObject("/provider/get/all", Provider[].class);
        Mockito.verify(searchService).getAll();
    }

    @Test
    void getProvidersWithOptionalFilters() {
        String filters = "test";
        testRestTemplate.getForObject("/provider/" + filters, Provider[].class);
        Mockito.verify(searchService).manageFilters(filters);
    }


    @Test
    void getSortedProvidersWithOptionalFilters() {
        String filters = "test";
        String operator = "foo";
        testRestTemplate.getForObject("/provider/sorted/" + operator + "/" + filters, Provider[].class);
        Mockito.verify(searchService).sortByBasicRate(operator, filters);
    }
}