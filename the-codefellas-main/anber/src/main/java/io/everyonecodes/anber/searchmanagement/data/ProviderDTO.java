package io.everyonecodes.anber.searchmanagement.data;

import io.everyonecodes.anber.providermanagement.data.ContractType;
import io.everyonecodes.anber.providermanagement.data.PriceModelType;
import io.everyonecodes.anber.providermanagement.data.ProviderType;
import io.everyonecodes.anber.providermanagement.data.Tariff;
import io.everyonecodes.anber.ratingmanagement.data.Rating;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "provider")
public class ProviderDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String countryName;
    @Enumerated(EnumType.STRING)
    private ProviderType providerType;

    private String providerName;

    @OneToMany
    private List<Tariff> tariffs;

    private String tariffName;
    private double basicRate;
    @Enumerated(EnumType.STRING)
    private ContractType contractType;
    @Enumerated(EnumType.STRING)
    private PriceModelType priceModel;

    private String website;
    private String email;
    private String phoneNumber;
    private boolean verified;
    @OneToOne
    private Rating rating;



    public ProviderDTO() {
    }

    public ProviderDTO(Long id, String countryName, ProviderType providerType, String providerName, String tariffName, double basicRate, ContractType contractType, PriceModelType priceModel, Rating rating) {
        this.id = id;
        this.countryName = countryName;
        this.providerType = providerType;
        this.providerName = providerName;
        this.tariffName = tariffName;
        this.basicRate = basicRate;
        this.contractType = contractType;
        this.priceModel = priceModel;
        this.rating = rating;
    }

    public ProviderDTO(String countryName, ProviderType providerType, String providerName, String tariffName, double basicRate, ContractType contractType, PriceModelType priceModel, Rating rating) {
        this.countryName = countryName;
        this.providerType = providerType;
        this.providerName = providerName;
        this.tariffName = tariffName;
        this.basicRate = basicRate;
        this.contractType = contractType;
        this.priceModel = priceModel;
        this.rating = rating;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public ProviderType getProviderType() {
        return providerType;
    }

    public void setProviderType(ProviderType providerType) {
        this.providerType = providerType;
    }

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    public String getTariffName() {
        return tariffName;
    }

    public void setTariffName(String tariffName) {
        this.tariffName = tariffName;
    }

    public double getBasicRate() {
        return basicRate;
    }

    public void setBasicRate(double basicRate) {
        this.basicRate = basicRate;
    }

    public ContractType getContractType() {
        return contractType;
    }

    public void setContractType(ContractType contractType) {
        this.contractType = contractType;
    }

    public PriceModelType getPriceModel() {
        return priceModel;
    }

    public void setPriceModel(PriceModelType priceModel) {
        this.priceModel = priceModel;
    }

    public Rating getRating() {
        return rating;
    }

    public void setRating(Rating rating) {
        this.rating = rating;
    }

    public List<Tariff> getTariffs() {
        return tariffs;
    }

    public void setTariffs(List<Tariff> tariffs) {
        this.tariffs = tariffs;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProviderDTO dto = (ProviderDTO) o;
        return Double.compare(dto.basicRate, basicRate) == 0 && verified == dto.verified && Objects.equals(id, dto.id) && Objects.equals(countryName, dto.countryName) && providerType == dto.providerType && Objects.equals(providerName, dto.providerName) && Objects.equals(tariffs, dto.tariffs) && Objects.equals(tariffName, dto.tariffName) && contractType == dto.contractType && priceModel == dto.priceModel && Objects.equals(website, dto.website) && Objects.equals(email, dto.email) && Objects.equals(phoneNumber, dto.phoneNumber) && Objects.equals(rating, dto.rating);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, countryName, providerType, providerName, tariffs, tariffName, basicRate, contractType, priceModel, website, email, phoneNumber, verified, rating);
    }

    @Override
    public String toString() {
        return "ProviderDTO{" +
                "id=" + id +
                ", countryName='" + countryName + '\'' +
                ", providerType=" + providerType +
                ", providerName='" + providerName + '\'' +
                ", tariffs=" + tariffs +
                ", tariffName='" + tariffName + '\'' +
                ", basicRate=" + basicRate +
                ", contractType=" + contractType +
                ", priceModel=" + priceModel +
                ", website='" + website + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", verified=" + verified +
                ", rating=" + rating +
                '}';
    }
}
