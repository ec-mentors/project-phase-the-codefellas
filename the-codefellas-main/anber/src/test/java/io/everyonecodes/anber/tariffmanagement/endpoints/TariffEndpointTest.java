package io.everyonecodes.anber.tariffmanagement.endpoints;

import io.everyonecodes.anber.tariffmanagement.service.TariffService;
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
class TariffEndpointTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    TariffService tariffService;

    @MockBean
    SecurityFilterChain securityFilterChain;


    @Test
    @WithMockUser(username = "admin@email.com", password = "admin", roles = "ADMIN")
    void updateTariff() throws Exception {

        Long id = 1L;

        mockMvc.perform(MockMvcRequestBuilders.put("/provider/update/tariff/" + id))
                .andExpect(MockMvcResultMatchers.status().isOk());

        Mockito.verify(tariffService).tariffApplier(id);
    }
}