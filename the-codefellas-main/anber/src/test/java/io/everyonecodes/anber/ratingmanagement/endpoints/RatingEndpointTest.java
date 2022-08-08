package io.everyonecodes.anber.ratingmanagement.endpoints;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.everyonecodes.anber.ratingmanagement.service.RatingService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class RatingEndpointTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    RatingService ratingService;

    @MockBean
    SecurityFilterChain securityFilterChain;


    @Test
    @WithMockUser(username = "test@email.com", password = "Password1!", roles = "USER")
    void rateProvider() throws Exception {
        Long id = 1L;
        int rating = 4;
        String url = "/provider/" + id + "/rate";
        mockMvc.perform(MockMvcRequestBuilders.put(url, id)
                        .with(user("test@email.com").password("Password1!").roles("USER"))
                        .contentType("application/json")
                        .content(String.valueOf(rating))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());

        Mockito.verify(ratingService).rateProvider(rating, id);
    }
}