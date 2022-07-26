package io.everyonecodes.anber.searchmanagement.data;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class VerifiedAccount  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @Column(unique = true)
    private String providerName;
    @NotNull
    private String website;
    @NotNull
    private String email;
    @NotNull
    private String phoneNumber;
    @NotNull
    private boolean verified = true;

    @Valid
    @NotNull
    @OneToMany
    private List<Tariff> tariffs = new ArrayList<>();

    @NotNull
    @Valid
    @OneToOne
    private Rating rating = new Rating();

        public VerifiedAccount() {
    }

    public VerifiedAccount(String providerName, String website, String email, String phoneNumber, boolean verified, List<Tariff> tariffs, Rating rating) {
        this.providerName = providerName;
        this.website = website;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.verified = verified;
        this.tariffs = tariffs;
        this.rating = rating;
    }

    public VerifiedAccount(String providerName, String website, String email, String phoneNumber, List<Tariff> tariffs, Rating rating) {
        this.providerName = providerName;
        this.website = website;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.tariffs = tariffs;
        this.rating = rating;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public List<Tariff> getTariffs() {
        return tariffs;
    }

    public void setTariffs(List<Tariff> tariffs) {
        this.tariffs = tariffs;
    }

    public Rating getRating() {
        return rating;
    }

    public void setRating(Rating rating) {
        this.rating = rating;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VerifiedAccount that = (VerifiedAccount) o;
        return verified == that.verified && Objects.equals(id, that.id) && Objects.equals(providerName, that.providerName) && Objects.equals(website, that.website) && Objects.equals(email, that.email) && Objects.equals(phoneNumber, that.phoneNumber) && Objects.equals(tariffs, that.tariffs) && Objects.equals(rating, that.rating);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, providerName, website, email, phoneNumber, verified, tariffs, rating);
    }

    @Override
    public String toString() {
        return "VerifiedAccount{" +
                "id=" + id +
                ", providerName='" + providerName + '\'' +
                ", website='" + website + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", verified=" + verified +
                ", tariffs=" + tariffs +
                ", rating=" + rating +
                '}';
    }
}
