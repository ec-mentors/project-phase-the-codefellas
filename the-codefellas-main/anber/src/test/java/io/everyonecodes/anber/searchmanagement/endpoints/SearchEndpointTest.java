package io.everyonecodes.anber.searchmanagement.endpoints;

import io.everyonecodes.anber.searchmanagement.service.SearchService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class SearchEndpointTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    SearchService searchService;

    @MockBean
    SecurityFilterChain securityFilterChain;


    @Test
    @WithMockUser(username = "admin@email.com", password = "admin", roles = "ADMIN")
    void getAllDtos() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/provider/getdto"))
                .andExpect(MockMvcResultMatchers.status().isOk());

        Mockito.verify(searchService).getAllDtos();
    }

    @Test
    @WithMockUser(username = "test@email.com", password = "Password1!", roles = "USER")
    void getAllProviders() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/provider/get"))
                        .andExpect(MockMvcResultMatchers.status().isOk());

        Mockito.verify(searchService).getAllProviders();
    }

    @Test
    @WithMockUser(username = "test@email.com", password = "Password1!", roles = "USER")
    void getProvidersWithOptionalFilters() throws Exception {
        String filters = "test";

        mockMvc.perform(MockMvcRequestBuilders.get("/provider/search/" + filters))
                .andExpect(MockMvcResultMatchers.status().isOk());

        Mockito.verify(searchService).manageFilters(filters);
    }


    @Test
    @WithMockUser(username = "test@email.com", password = "Password1!", roles = "USER")
    void getSortedProvidersWithOptionalFilters() throws Exception {
        String filters = "test";
        String operator = "foo";


        mockMvc.perform(MockMvcRequestBuilders.get("/provider/search/sorted/basicrate/" + operator + "/" + filters))
                .andExpect(MockMvcResultMatchers.status().isOk());

        Mockito.verify(searchService).sortByBasicRate(operator, filters);
    }

    @Test
    @WithMockUser(username = "test@email.com", password = "Password1!", roles = "USER")
    void getSortedByRatingProvidersWithOptionalFilters() throws Exception {
        String filters = "test";
        String operator = "foo";

        mockMvc.perform(MockMvcRequestBuilders.get("/provider/search/sorted/rating/" + operator + "/" + filters))
                .andExpect(MockMvcResultMatchers.status().isOk());
        Mockito.verify(searchService).sortByRating(operator, filters);
    }
}