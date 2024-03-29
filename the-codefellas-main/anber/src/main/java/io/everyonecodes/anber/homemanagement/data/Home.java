package io.everyonecodes.anber.homemanagement.data;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Objects;

@Entity
public class Home {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 100)
    private String homeName;

    private String country;
    private String city;
    private String postalCode;

    @Enumerated
    private HomeType homeType;
    private double sizeInSquareMeters;


    public Home() {
    }

    public Home(String country, String city, String postalCode, HomeType homeType, double sizeInSquareMeters) {
        this.country = country;
        this.city = city;
        this.postalCode = postalCode;
        this.homeType = homeType;
        this.sizeInSquareMeters = sizeInSquareMeters;
    }

    public Home(Long id, String homeName, String country, String city, String postalCode, HomeType homeType, double sizeInSquareMeters) {
        this.id = id;
        this.homeName = homeName;
        this.country = country;
        this.city = city;
        this.postalCode = postalCode;
        this.homeType = homeType;
        this.sizeInSquareMeters = sizeInSquareMeters;
    }

    public Home(String homeName, String country, String city, String postalCode, HomeType homeType, double sizeInSquareMeters) {
        this.homeName = homeName;
        this.country = country;
        this.city = city;
        this.postalCode = postalCode;
        this.homeType = homeType;
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

    public HomeType getHomeType() {
        return homeType;
    }

    public void setHomeType(HomeType homeType) {
        this.homeType = homeType;
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
        return Double.compare(home.sizeInSquareMeters, sizeInSquareMeters) == 0 && Objects.equals(id, home.id) && Objects.equals(homeName, home.homeName) && Objects.equals(country, home.country) && Objects.equals(city, home.city) && Objects.equals(postalCode, home.postalCode) && homeType == home.homeType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, homeName, country, city, postalCode, homeType, sizeInSquareMeters);
    }

    @Override
    public String toString() {
        return "Home{" +
                "id=" + id +
                ", homeName='" + homeName + '\'' +
                ", country='" + country + '\'' +
                ", city='" + city + '\'' +
                ", postalCode='" + postalCode + '\'' +
                ", type=" + homeType +
                ", sizeInSquareMeters=" + sizeInSquareMeters +
                '}';
    }
}
