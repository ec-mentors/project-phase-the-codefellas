package io.everyonecodes.anber.usermanagement.endpoints;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.everyonecodes.anber.usermanagement.data.User;
import io.everyonecodes.anber.usermanagement.data.UserPrivateDTO;
import io.everyonecodes.anber.usermanagement.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Optional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class LoginEndpointTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    UserService userService;

    @Value("${testvalues.login-endpoint-url}")
    String url;

    @Value("${testvalues.username}")
    String username;

    @Value("${testvalues.role}")
    String role;

    @Value("${testvalues.email}")
    String email;

    @MockBean
    SecurityFilterChain filterChain;

    private final User user = new User("test@email.com", "Password1!");


    private ResultActions mockUserCredentials() throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.post(url)
                .with(user("test@email.com").password("Password1!").roles("USER"))
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(new User(user.getEmail(), user.getPassword())))
                .accept(MediaType.APPLICATION_JSON));
    }

    @Test
    void viewIndividualUser_unlocked() throws Exception {

        Mockito.when(userService.isUserUnlocked(user)).thenReturn(true);
        Mockito.when(userService.viewIndividualProfileDataUser(user)).thenReturn(Optional.of(new UserPrivateDTO()));

        mockUserCredentials().andExpect(MockMvcResultMatchers.status().isOk());

        Mockito.verify(userService).isUserUnlocked(user);
        Mockito.verify(userService).viewIndividualProfileDataUser(user);
    }

    @Test
    void viewIndividualUser_locked() throws Exception {

        Mockito.when(userService.isUserUnlocked(user)).thenReturn(false);

        mockUserCredentials().andExpect(MockMvcResultMatchers.status().isForbidden());

        Mockito.verify(userService).isUserUnlocked(user);
        Mockito.verify(userService, Mockito.never()).viewIndividualProfileDataUser(user);
    }

}