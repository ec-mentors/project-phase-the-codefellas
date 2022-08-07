package io.everyonecodes.anber.providermanagement.endpoints;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.everyonecodes.anber.providermanagement.data.ProviderPublic;
import io.everyonecodes.anber.providermanagement.service.AccountService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class AccountEndpointTest {

    @Autowired
    TestRestTemplate testRestTemplate;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    AccountService accountService;

    @MockBean
    SecurityFilterChain securityFilterChain;


    @Test
    @WithMockUser(username = "admin@email.com", password = "admin", roles = "ADMIN")
    void getAllV() throws Exception {
        String url = "/provider/all/v";

        mockMvc.perform(MockMvcRequestBuilders.get(url))
                .andExpect(MockMvcResultMatchers.status().isOk());

        Mockito.verify(accountService).getAllVerified();
    }

    @Test
    @WithMockUser(username = "admin@email.com", password = "admin", roles = "ADMIN")
    void getAllU() throws Exception {
        String url = "/provider/all/u";

        mockMvc.perform(MockMvcRequestBuilders.get(url))
                .andExpect(MockMvcResultMatchers.status().isOk());

        Mockito.verify(accountService).getAllUnverified();
    }

    @Test
    @WithMockUser(username = "admin@email.com", password = "admin", roles = "ADMIN")
    void createAccount() throws Exception {

        Long id = 1L;
        String url = "/provider/" + id + "/create";

        mockMvc.perform(MockMvcRequestBuilders.post(url, id))
                .andExpect(MockMvcResultMatchers.status().isOk());

        Mockito.verify(accountService).createAccount(id);
    }

    @Test
    @WithMockUser(username = "admin@email.com", password = "admin", roles = "ADMIN")
    void verifyAccount() throws Exception {

        Long id = 1L;
        String url = "/provider/" + id + "/verify";

        mockMvc.perform(MockMvcRequestBuilders.put(url, id))
                .andExpect(MockMvcResultMatchers.status().isOk());

        Mockito.verify(accountService).verifyAccount(id);
    }

    @Test
    @WithMockUser(username = "admin@email.com", password = "admin", roles = "ADMIN")
    void unverifyAccount() throws Exception {

        Long id = 1L;
        String url = "/provider/" + id + "/unverify";

        mockMvc.perform(MockMvcRequestBuilders.put(url, id))
                .andExpect(MockMvcResultMatchers.status().isOk());

        Mockito.verify(accountService).unverifyAccount(id);
    }

    @Test
    @WithMockUser(username = "admin@email.com", password = "admin", roles = "ADMIN")
    void editAccountV() throws Exception {

        Long id = 1L;
        String property = "website";
        String input = "newsite.com";
        String url = "/provider/v/" + id + "/edit/" + property;

        mockMvc.perform(MockMvcRequestBuilders.put(url, property)
                        .with(user("admin@email.com").password("admin").roles("USER"))
                        .contentType("application/json")
                        .content(input)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());

        Mockito.verify(accountService).editAccountV(id, property, input);
    }

    @Test
    @WithMockUser(username = "admin@email.com", password = "admin", roles = "ADMIN")
    void editAccountU() throws Exception {

        Long id = 1L;
        String property = "website";
        String input = "newsite.com";
        String url = "/provider/u/" + id + "/edit/" + property;

        mockMvc.perform(MockMvcRequestBuilders.put(url, property)
                        .with(user("admin@email.com").password("admin").roles("USER"))
                        .contentType("application/json")
                        .content(input)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());

        Mockito.verify(accountService).editAccountU(id, property, input);
    }

    @Test
    @WithMockUser(username = "admin@email.com", password = "admin", roles = "ADMIN")
    void deleteAccount() throws Exception {

        Long id = 1L;
        String url = "/provider/" + id + "/delete";
        mockMvc.perform(MockMvcRequestBuilders.delete(url))
                .andExpect(MockMvcResultMatchers.status().isOk());

        Mockito.verify(accountService).deleteAccount(id);
    }

    @Test
    void viewProvider() {
        Long id = 1L;
        String url = "/provider/" + id + "/view";
        testRestTemplate.getForObject(url, ProviderPublic.class);

        Mockito.verify(accountService).viewPublicProvider(id);
    }

    @Test
    @WithMockUser(username = "admin@email.com", password = "admin", roles = "ADMIN")
    void fillInDtoDataTariffs() throws Exception {

        String url = "/provider/db/fill";
        mockMvc.perform(MockMvcRequestBuilders.get(url))
                .andExpect(MockMvcResultMatchers.status().isOk());

        Mockito.verify(accountService).fillInDtoData();
    }
}