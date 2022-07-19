package io.everyonecodes.anber.searchmanagement.data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Provider {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String countryName;
    private ProviderType providerType;

    private String providerName;
    private String tariffName;
    private Double basicRate;
    private PriceModelType priceModel;


    public Provider() {
    }

    public Provider(String countryName, ProviderType providerType, String providerName, String tariffName, Double basicRate, PriceModelType priceModel) {
        this.countryName = countryName;
        this.providerType = providerType;
        this.providerName = providerName;
        this.tariffName = tariffName;
        this.basicRate = basicRate;
        this.priceModel = priceModel;
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

    public Double getBasicRate() {
        return basicRate;
    }

    public void setBasicRate(Double basicRate) {
        this.basicRate = basicRate;
    }

    public PriceModelType getPriceModel() {
        return priceModel;
    }

    public void setPriceModel(PriceModelType priceModel) {
        this.priceModel = priceModel;
    }
}
