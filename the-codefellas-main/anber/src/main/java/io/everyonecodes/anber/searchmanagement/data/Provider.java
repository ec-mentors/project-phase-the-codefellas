package io.everyonecodes.anber.searchmanagement.data;

import io.everyonecodes.anber.providermanagement.data.ContractType;
import io.everyonecodes.anber.providermanagement.data.PriceModelType;

import java.util.Objects;

public class Provider {

    private String providerName;
    private String rating;
    private String tariffName;
    private double basicRate;
    private ContractType contractType;
    private PriceModelType priceModel;


    public Provider() {
    }

    public Provider(String providerName, String tariffName, double basicRate, ContractType contractType, PriceModelType priceModel) {
        this.providerName = providerName;
        this.tariffName = tariffName;
        this.basicRate = basicRate;
        this.contractType = contractType;
        this.priceModel = priceModel;
    }



    public Provider(String providerName, String rating, String tariffName, double basicRate, ContractType contractType, PriceModelType priceModel) {
        this.providerName = providerName;
        this.rating = rating;
        this.tariffName = tariffName;
        this.basicRate = basicRate;
        this.contractType = contractType;
        this.priceModel = priceModel;
    }



    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Provider provider = (Provider) o;
        return Double.compare(provider.basicRate, basicRate) == 0 && Objects.equals(providerName, provider.providerName) && Objects.equals(tariffName, provider.tariffName) && contractType == provider.contractType && priceModel == provider.priceModel;
    }

    @Override
    public int hashCode() {
        return Objects.hash(providerName, tariffName, basicRate, contractType, priceModel);
    }

    @Override
    public String toString() {
        return "Provider{" +
                "providerName='" + providerName + '\'' +
                ", tariffName='" + tariffName + '\'' +
                ", basicRate=" + basicRate +
                ", contractType=" + contractType +
                ", priceModel=" + priceModel +
                '}';
    }
}

