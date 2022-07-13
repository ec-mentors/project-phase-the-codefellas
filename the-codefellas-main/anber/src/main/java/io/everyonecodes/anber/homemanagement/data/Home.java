package io.everyonecodes.anber.homemanagement.data;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Max;
import java.util.Objects;

@Entity
public class Home {

    @Id
    @GeneratedValue
    private Long id;

    @Max(value = 100)
    private String homeName;

    private String country;
    private String city;
    private String postalCode;

    @Enumerated
    private HomeType type;
    private double sizeInSquareMeters;



    public Home() {
    }

    public Home(String country, String city, String postalCode, HomeType type, double sizeInSquareMeters) {
        this.country = country;
        this.city = city;
        this.postalCode = postalCode;
        this.type = type;
        this.sizeInSquareMeters = sizeInSquareMeters;
    }

    public Home(String homeName, String country, String city, String postalCode, HomeType type, double sizeInSquareMeters) {
        this.homeName = homeName;
        this.country = country;
        this.city = city;
        this.postalCode = postalCode;
        this.type = type;
        this.sizeInSquareMeters = sizeInSquareMeters;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public HomeType getType() {
        return type;
    }

    public void setType(HomeType type) {
        this.type = type;
    }

    public double getSizeInSquareMeters() {
        return sizeInSquareMeters;
    }

    public void setSizeInSquareMeters(double sizeInSquareMeters) {
        this.sizeInSquareMeters = sizeInSquareMeters;
    }

    public String getHomeName() {
        return homeName;
    }

    public void setHomeName(String homeName) {
        this.homeName = homeName;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Home home = (Home) o;
        return Double.compare(home.sizeInSquareMeters, sizeInSquareMeters) == 0 && Objects.equals(id, home.id) && Objects.equals(country, home.country) && Objects.equals(city, home.city) && Objects.equals(postalCode, home.postalCode) && type == home.type && Objects.equals(homeName, home.homeName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, country, city, postalCode, type, sizeInSquareMeters, homeName);
    }

    @Override
    public String toString() {
        return "Home{" +
                "id=" + id +
                ", country='" + country + '\'' +
                ", city='" + city + '\'' +
                ", postalCode='" + postalCode + '\'' +
                ", type=" + type +
                ", sizeInSquareMeters=" + sizeInSquareMeters +
                ", homeName='" + homeName + '\'' +
                '}';
    }
}
