package io.everyonecodes.anber.calculationmanagement.data;


import java.util.Objects;

public class AverageConsumption {

    private String country;
    private double averageConsumption;

    public AverageConsumption(String country, double averageConsumption) {
        this.country = country;
        this.averageConsumption = averageConsumption;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public double getAverageConsumption() {
        return averageConsumption;
    }

    public void setAverageConsumption(double averageConsumption) {
        this.averageConsumption = averageConsumption;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AverageConsumption that = (AverageConsumption) o;
        return Double.compare(that.averageConsumption, averageConsumption) == 0 && Objects.equals(country, that.country);
    }

    @Override
    public int hashCode() {
        return Objects.hash(country, averageConsumption);
    }
}
