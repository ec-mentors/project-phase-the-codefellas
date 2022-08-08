package io.everyonecodes.anber.usermanagement.data;

import io.everyonecodes.anber.homemanagement.data.Home;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
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

    @NotBlank(message = "first name must not be empty")
    @Column(name = "firstName")
    private String firstName;

    @NotBlank(message = "last name must not be empty")
    @Column(name = "lastName")
    private String lastName;

    @NotEmpty
    @Email(message = "must be a valid Email")
    @Column(unique = true)
    private String email;

    @NotEmpty
    @Size(min = 6, max = 60, message = "must be at least 6 characters long")
    private String password;

    private String role;

    @Column(unique = true)
    private String username;

    private String country;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Home> savedHomes = new ArrayList<>();

    private boolean notificationsEnabled = false;
    private int loginAttempts;
    private boolean accountNonLocked;


    public User() {
    }
    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public User(String firstName, String lastName, String email, String password, String role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public User(String firstName, String lastName, String email, boolean accountNonLocked, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.accountNonLocked = accountNonLocked;
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

    public User(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    public User(String firstName, String lastName, String email, String password, String role, String username) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.role = role;
        this.username = username;
    }

    public User(Long id, String email, String password, String role, String username, String country, List<Home> savedHomes, boolean notificationsEnabled) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.role = role;
        this.username = username;
        this.country = country;
        this.savedHomes = savedHomes;
        this.notificationsEnabled = notificationsEnabled;
    }


    public int getLoginAttempts() {
        return loginAttempts;
    }

    public void setLoginAttempts(int loginAttempts) {
        this.loginAttempts = loginAttempts;
    }

    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    public void setAccountNonLocked(boolean accountNonLocked) {
        this.accountNonLocked = accountNonLocked;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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
        return notificationsEnabled == user.notificationsEnabled && loginAttempts == user.loginAttempts && accountNonLocked == user.accountNonLocked && Objects.equals(id, user.id) && Objects.equals(firstName, user.firstName) && Objects.equals(lastName, user.lastName) && Objects.equals(email, user.email) && Objects.equals(password, user.password) && Objects.equals(role, user.role) && Objects.equals(username, user.username) && Objects.equals(country, user.country) && Objects.equals(savedHomes, user.savedHomes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, email, password, role, username, country, savedHomes, notificationsEnabled, loginAttempts, accountNonLocked);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                ", username='" + username + '\'' +
                ", country='" + country + '\'' +
                ", savedHomes=" + savedHomes +
                ", notificationsEnabled=" + notificationsEnabled +
                ", loginAttempts=" + loginAttempts +
                ", accountNonLocked=" + accountNonLocked +
                '}';
    }
}
