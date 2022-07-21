package io.everyonecodes.anber.searchmanagement.data;

public class Provider {

    private String providerName;
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
}
