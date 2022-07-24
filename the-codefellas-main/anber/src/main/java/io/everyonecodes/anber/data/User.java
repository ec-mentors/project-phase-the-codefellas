package io.everyonecodes.anber.data;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.*;

@Entity
@Table(name = "auth_user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "auth_user_id")
    private Long id;

    @NotBlank(message = "Name must not be empty")
    @Column(name = "name")
    private String name;

    @NotBlank(message = "Email must not be empty")
    @Email(message = "Must be a valid Email!")
    @Column(name = "email", unique = true)
    private String email;

    private String resetToken;

    private int loginAttempts;
    private boolean accountNonLocked;
    private Date lockTime;

    @Pattern(regexp = "^(?=.*?[a-z])(?=.*?[A-Z])(?=.*?\\d)(?=.*?[!@#\\]\\[:()\\\"`;+\\-'|_?,.</\\\\>=$%}{^&*~]).{8,}$",
    message = "Password needs to be 8 characters in length and must contain at least one lower case letter, one upper case letter, one number and one special character")
    @Column(name = "password")
    private String password;

    @Column(unique = true)
    private String username;

    private String country;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Role> roles;

    @OneToMany
    private List<Home> savedHomes = new ArrayList<>();

    private boolean notificationsEnabled = false;

    @Column(name = "verified")
    private boolean isVerified;

    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    public void setAccountNonLocked(boolean accountNonLocked) {
        this.accountNonLocked = accountNonLocked;
    }

    public Date getLockTime() {
        return lockTime;
    }

    public void setLockTime(Date lockTime) {
        this.lockTime = lockTime;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public void setVerified(boolean verified) {
        isVerified = verified;
    }

    public User() {
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public User(String name, String email, boolean accountNonLocked, String password) {
        this.name = name;
        this.email = email;
        this.accountNonLocked = accountNonLocked;
        this.password = password;
    }

    public User(String name, String email, String password, String username, String country, Set<Role> roles, List<Home> savedHomes, boolean notificationsEnabled) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.username = username;
        this.country = country;
        this.roles = roles;
        this.savedHomes = savedHomes;
        this.notificationsEnabled = notificationsEnabled;
    }

    public User(String email, String password, Set<Role> roles, String username, String country, List<Home> savedHomes, boolean notificationsEnabled) {
        this.email = email;
        this.password = password;
        this.roles = roles;
        this.username = username;
        this.country = country;
        this.savedHomes = savedHomes;
        this.notificationsEnabled = notificationsEnabled;
    }

    public User(Long id, String email, String password, Set<Role> roles, String username, String country, List<Home> savedHomes, boolean notificationsEnabled) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.roles = roles;
        this.username = username;
        this.country = country;
        this.savedHomes = savedHomes;
        this.notificationsEnabled = notificationsEnabled;
    }


    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public User(String email, String password, Set<Role> roles) {
        this.email = email;
        this.password = password;
        this.roles = roles;
    }

    public User(Long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLoginAttempts() {
        return loginAttempts;
    }

    public void setLoginAttempts(int loginAttempts) {
        this.loginAttempts = loginAttempts;
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

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public String getResetToken() {
        return resetToken;
    }

    public void setResetToken(String resetToken) {
        this.resetToken = resetToken;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return loginAttempts == user.loginAttempts && accountNonLocked == user.accountNonLocked && notificationsEnabled == user.notificationsEnabled && isVerified == user.isVerified && Objects.equals(id, user.id) && Objects.equals(name, user.name) && Objects.equals(email, user.email) && Objects.equals(resetToken, user.resetToken) && Objects.equals(lockTime, user.lockTime) && Objects.equals(password, user.password) && Objects.equals(username, user.username) && Objects.equals(country, user.country) && Objects.equals(roles, user.roles) && Objects.equals(savedHomes, user.savedHomes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, email, resetToken, loginAttempts, accountNonLocked, lockTime, password, username, country, roles, savedHomes, notificationsEnabled, isVerified);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", resetToken='" + resetToken + '\'' +
                ", loginAttempts=" + loginAttempts +
                ", accountNonLocked=" + accountNonLocked +
                ", lockTime=" + lockTime +
                ", password='" + password + '\'' +
                ", username='" + username + '\'' +
                ", country='" + country + '\'' +
                ", roles=" + roles +
                ", savedHomes=" + savedHomes +
                ", notificationsEnabled=" + notificationsEnabled +
                ", isVerified=" + isVerified +
                '}';
    }
}
