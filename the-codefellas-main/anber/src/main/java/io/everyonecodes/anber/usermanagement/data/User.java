package io.everyonecodes.anber.usermanagement.data;

import io.everyonecodes.anber.homemanagement.data.Home;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    @Email(message = "must be a valid Email")
    @Column(unique = true)
    private String email;

    @NotEmpty
    @Size(min = 6, max = 60, message = "must be at least 6 characters long.")
    private String password;

    private String role;

    @Column(unique = true)
    private String username;

    private String country;

    @OneToMany
    private List<Home> savedHomes = new ArrayList<>();

    private boolean notificationsEnabled = false;

    public User() {
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public User(String email, String password, String role, String username, String country, List<Home> savedHomes, boolean notificationsEnabled) {
        this.email = email;
        this.password = password;
        this.role = role;
        this.username = username;
        this.country = country;
        this.savedHomes = savedHomes;
        this.notificationsEnabled = notificationsEnabled;
    }

    public User(String email, String password, String role) {
        this.email = email;
        this.password = password;
        this.role = role;
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
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
        User user = (User) o;
        return notificationsEnabled == user.notificationsEnabled && Objects.equals(id, user.id) && Objects.equals(email, user.email) && Objects.equals(password, user.password) && Objects.equals(role, user.role) && Objects.equals(username, user.username) && Objects.equals(country, user.country) && Objects.equals(savedHomes, user.savedHomes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, password, role, username, country, savedHomes, notificationsEnabled);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                ", username='" + username + '\'' +
                ", country='" + country + '\'' +
                ", savedHomes=" + savedHomes +
                ", notificationsEnabled=" + notificationsEnabled +
                '}';
    }
}
