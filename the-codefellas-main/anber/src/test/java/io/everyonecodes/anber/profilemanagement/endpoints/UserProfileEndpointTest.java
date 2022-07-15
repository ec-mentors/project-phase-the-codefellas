package io.everyonecodes.anber.profilemanagement.endpoints;

import io.everyonecodes.anber.profilemanagement.data.UserProfile;
import io.everyonecodes.anber.profilemanagement.service.UserProfileService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserProfileEndpointTest {

    @Autowired
    TestRestTemplate testRestTemplate;

    @MockBean
    UserProfileService userProfileService;

    @Value("${testvalues.username}")
    String username;

    @Value("${testvalues.password}")
    String password;

    @Value("${testvalues.email}")
    String email;

    @Test
    void getAllProfiles() {
        testRestTemplate.getForObject("/all", UserProfile[].class);
        userProfileService.viewAll();
        Mockito.verify(userProfileService).viewAll();
    }

    @Test
    void getFullProfile() {
        testRestTemplate.getForObject("/" + username, UserProfile[].class);
        userProfileService.viewProfile(username);
        Mockito.verify(userProfileService).viewProfile(username);
    }

    @Test
    void getFullProfile_returnsNull() {
        Mockito.when(userProfileService.viewProfile(username)).thenReturn(null);
        testRestTemplate.getForObject("/" + username, UserProfile[].class);
        var response = userProfileService.viewProfile(username);
        Assertions.assertNull(response);
        Mockito.verify(userProfileService).viewProfile(username);
    }

    @Test
    void updateProfileOption() {
        testRestTemplate.put("/" + username + "/edit/" + "email", email, String[].class);
        userProfileService.editData(username, "email", email);
        Mockito.verify(userProfileService).editData(username, "email", email);
    }

    @Test
    void updateProfileOption_returnsNull() {
        Mockito.when(userProfileService.editData(username, "email", email)).thenReturn(null);
        testRestTemplate.put("/" + username + "/edit/" + "email", email, String[].class);
        var response = userProfileService.editData(username, "email", email);
        Assertions.assertNull(response);
        Mockito.verify(userProfileService).editData(username, "email", email);
    }

    @Test
    void deleteProfile() {
        testRestTemplate.delete("/" + username + "/delete", Void.class);
        userProfileService.deleteProfile(username);
        Mockito.verify(userProfileService).deleteProfile(username);
    }
}