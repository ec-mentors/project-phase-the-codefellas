package io.everyonecodes.anber.searchmanagement.data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity
@Table(name = "tariff")
public class Tariff {

    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String tariffName;
    @NotNull
    private double basicRate;
    @NotNull
    @Enumerated(EnumType.STRING)
    private ContractType contractType;
    @NotNull
    @Enumerated(EnumType.STRING)
    private PriceModelType priceModel;

    public Tariff() {
    }

    public Tariff(String tariffName, double basicRate, ContractType contractType, PriceModelType priceModel) {
        this.tariffName = tariffName;
        this.basicRate = basicRate;
        this.contractType = contractType;
        this.priceModel = priceModel;
    }

    public Tariff(Long id, String tariffName, double basicRate, ContractType contractType, PriceModelType priceModel) {
        this.id = id;
        this.tariffName = tariffName;
        this.basicRate = basicRate;
        this.contractType = contractType;
        this.priceModel = priceModel;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
        Tariff tariff = (Tariff) o;
        return Double.compare(tariff.basicRate, basicRate) == 0 && Objects.equals(id, tariff.id) && Objects.equals(tariffName, tariff.tariffName) && contractType == tariff.contractType && priceModel == tariff.priceModel;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tariffName, basicRate, contractType, priceModel);
    }

    @Override
    public String toString() {
        return "Tariff{" +
                "id=" + id +
                ", tariffName='" + tariffName + '\'' +
                ", basicRate=" + basicRate +
                ", contractType=" + contractType +
                ", priceModel=" + priceModel +
                '}';
    }
}
