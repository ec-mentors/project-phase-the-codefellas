package io.everyonecodes.anber.profilemanagement.endpoints;

import io.everyonecodes.anber.profilemanagement.data.UserProfile;
import io.everyonecodes.anber.profilemanagement.service.UserProfileService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.security.test.context.support.WithMockUser;


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
    @WithMockUser(username = "ADMIN", password = "admin", authorities = {"ROLE_ADMIN"})
    void getAllProfiles() {

        testRestTemplate.getForObject("/profile/all", UserProfile[].class);
//        userProfileService.viewAll();
        Mockito.verify(userProfileService).viewAll();
    }

    @Test
    void getFullProfile() {
        testRestTemplate.getForObject("/profile/" + email, UserProfile.class);
//        userProfileService.viewProfile(username);
        Mockito.verify(userProfileService).viewProfile(email);
    }

    @Test
    void getFullProfile_returnsNull() {
//        Mockito.when(userProfileService.viewProfile(email)).thenReturn(null);
        testRestTemplate.getForObject("/profile/" + email, UserProfile.class);
//        var response = userProfileService.viewProfile(email);
//        Assertions.assertNull(response);
        Mockito.verify(userProfileService).viewProfile(email);
    }

    @Test
    void updateProfileOption() {
        testRestTemplate.put("/profile/" + email + "/edit/" + "email", email, String.class);
//        userProfileService.editData(username, "email", email);
        Mockito.verify(userProfileService).editData(email, "email", email);
    }

    @Test
    void updateProfileOption_returnsNull() {
//        Mockito.when(userProfileService.editData(email, "email", email)).thenReturn(null);
        testRestTemplate.put("/profile/" + email + "/edit/" + "email", email, String.class);
//        var response = userProfileService.editData(email, "email", email);
//        Assertions.assertNull(response);
        Mockito.verify(userProfileService).editData(email, "email", email);
    }

    @Test
    void deleteProfile() {
        testRestTemplate.delete("/profile/" + email + "/delete");
//        userProfileService.deleteProfile(username);
        Mockito.verify(userProfileService).deleteProfile(email);
    }
}