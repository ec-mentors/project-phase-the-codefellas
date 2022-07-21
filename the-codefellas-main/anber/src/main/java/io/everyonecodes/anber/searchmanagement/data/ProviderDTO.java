package io.everyonecodes.anber.searchmanagement.data;

import org.hibernate.validator.constraints.Range;

import javax.persistence.*;

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
    private String tariffName;
    private double basicRate;
    @Enumerated(EnumType.STRING)
    private ContractType contractType;
    @Enumerated(EnumType.STRING)
    private PriceModelType priceModel;
    @Range(min = 1, max = 5)
    private double rating;



    public ProviderDTO() {
    }

    public ProviderDTO(Long id, String countryName, ProviderType providerType, String providerName, String tariffName, double basicRate, ContractType contractType, PriceModelType priceModel, double rating) {
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

    public ProviderDTO(String countryName, ProviderType providerType, String providerName, String tariffName, double basicRate, ContractType contractType, PriceModelType priceModel, double rating) {
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

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }
}
