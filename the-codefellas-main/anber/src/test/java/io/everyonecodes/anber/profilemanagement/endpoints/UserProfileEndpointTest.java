package io.everyonecodes.anber.profilemanagement.endpoints;

import io.everyonecodes.anber.profilemanagement.service.UserProfileService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
class UserProfileEndpointTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserProfileService userProfileService;

    @Value("${testvalues.username}")
    String username;

    @Value("${testvalues.password}")
    String password;

    @Value("${testvalues.email}")
    String email;

    @MockBean
    SecurityFilterChain securityFilterChain;


    @Test
    @WithMockUser(username = "admin@email.com", password = "admin", roles = "ADMIN")
    void getAllProfiles() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/profile/all"))
                .andExpect(MockMvcResultMatchers.status().isOk());

        Mockito.verify(userProfileService).viewAll();
    }

    @Test
    @WithMockUser(username = "test@email.com", password = "Password1!", roles = "USER")
    void getFullProfile() throws Exception {
        String url = "/profile/" + email;

        mockMvc.perform(MockMvcRequestBuilders.get(url))
                .andExpect(MockMvcResultMatchers.status().isOk());
        Mockito.verify(userProfileService).viewProfile(email);
    }

    @Test
    @WithMockUser(username = "test@email.com", password = "Password1!", roles = "USER")
    void updateProfileOption() throws Exception {

        String input = "Austria";
        String property = "country";

        String url = "/profile/" + email + "/edit/" + property;

        mockMvc.perform(MockMvcRequestBuilders.put(url, email, property)
                .with(user("test@email.com").password("Password1!").roles("USER"))
                .contentType("application/json")
                .content(input)
                .accept(MediaType.APPLICATION_JSON))
                        .andExpect(MockMvcResultMatchers.status().isOk());

        Mockito.verify(userProfileService).editData(email, property, input);
    }

    @Test
    @WithMockUser(username = "test@email.com", password = "Password1!", roles = "USER")
    void deleteProfile() throws Exception {
        String url = "/profile/" + email + "/delete";

        mockMvc.perform(MockMvcRequestBuilders.delete(url))
                .andExpect(MockMvcResultMatchers.status().isOk());

        Mockito.verify(userProfileService).deleteProfile(email);
    }
}