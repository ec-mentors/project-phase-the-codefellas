package io.everyonecodes.anber.endpointsOld;

import io.everyonecodes.anber.data.UserPrivateDTO;
import io.everyonecodes.anber.service.UserDTO;
import io.everyonecodes.anber.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@AutoConfigureMockMvc
//class LoginEndpointTest {
//
//    @Autowired
//    TestRestTemplate testRestTemplate;
//
//    @Autowired
//    MockMvc mockMvc;
//
//    @MockBean
//    UserService userService;
//
//    @MockBean
//    UserDTO userDTO;
//
//
//    @Value("${testvalues.login-endpoint-url}")
//    String url;
//
//    @Value("${testvalues.username}")
//    String username;
//
//    @Value("${testvalues.role}")
//    String role;
//
//    @Value("${testvalues.email}")
//    String email;
//
//    @MockBean
//    SecurityFilterChain filterChain;
//
//
//    @Test
//    @WithMockUser(username = "ADMIN", password = "admin", authorities = "ROLE_ADMIN")
//    void viewIndividualProfile_returnsProfileData() {
//        testRestTemplate.getForObject(url, UserPrivateDTO.class);
//
//        Mockito.verify(userService).viewIndividualProfileData("user");
//    }
//
//    @Test
//    void viewIndividualPrivateData_authorized() throws Exception{
//        String username = "firstUser";
//        Mockito.when(userService.viewIndividualProfileData(username))
//                .thenReturn(Optional.of(new UserPrivateDTO()));
//        mockMvc.perform(MockMvcRequestBuilders.get(url)
//                        .with(user("firstUser").password("Coding12#").roles("USER"))
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(MockMvcResultMatchers.status().isOk());
//        Mockito.verify(userService, Mockito.times(1)).viewIndividualProfileData(username);
//    }
//
//    @Test
//    void viewIndividualProfile_returnsNull() {
//        testRestTemplate.getForObject(url, UserPrivateDTO.class);
//        Mockito.verify(userService, Mockito.never()).viewIndividualProfileData(Mockito.any());
//    }
//
//}