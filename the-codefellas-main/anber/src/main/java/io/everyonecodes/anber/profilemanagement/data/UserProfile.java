package io.everyonecodes.anber.profilemanagement.data;

import io.everyonecodes.anber.homemanagement.data.Home;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class UserProfile {

    @Id
    @GeneratedValue
    private Long id;

    @NotEmpty
    @Email(message = "must be a valid Email")
    @Column(unique = true)
    private String email;

    @NotEmpty
    @Size(min = 6, max = 40)
    private String password;

    @NotEmpty
    @Column(unique = true)
    private String username;

    private String country;

    @OneToMany
    private List<Home> savedHomes = new ArrayList<>();

    private boolean notificationsEnabled = false;

    public UserProfile() {
    }

    public UserProfile(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public UserProfile(String email, String password, String username, String country, List<Home> savedHomes, boolean notificationsEnabled) {
        this.email = email;
        this.password = password;
        this.username = username;
        this.country = country;
        this.savedHomes = savedHomes;
        this.notificationsEnabled = notificationsEnabled;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public List<Home> getSavedHomes() {
        return savedHomes;
    }

    public void setSavedHomes(List<Home> savedHomes) {
        this.savedHomes = savedHomes;
    }

    public boolean isNotificationsEnabled() {
        return notificationsEnabled;
    }

    public void setNotificationsEnabled(boolean notificationsEnabled) {
        this.notificationsEnabled = notificationsEnabled;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserProfile userProfile = (UserProfile) o;
        return notificationsEnabled == userProfile.notificationsEnabled && Objects.equals(id, userProfile.id) && Objects.equals(email, userProfile.email) && Objects.equals(password, userProfile.password) && Objects.equals(username, userProfile.username) && Objects.equals(country, userProfile.country) && Objects.equals(savedHomes, userProfile.savedHomes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, password, username, country, savedHomes, notificationsEnabled);
    }

    @Override
    public String toString() {
        return "UserProfile{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", name='" + username + '\'' +
                ", country='" + country + '\'' +
                ", savedHomes=" + savedHomes +
                ", notificationsEnabled=" + notificationsEnabled +
                '}';
    }
}
