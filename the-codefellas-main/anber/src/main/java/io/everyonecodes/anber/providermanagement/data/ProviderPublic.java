package io.everyonecodes.anber.providermanagement.data;

import io.everyonecodes.anber.ratingmanagement.data.Rating;
import io.everyonecodes.anber.tariffmanagement.data.Tariff;

import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ProviderPublic {
    private String providerName, website;
    private Rating rating;
    @OneToMany(fetch = FetchType.EAGER)
    private List<Tariff> tariffs = new ArrayList<>();

    public ProviderPublic(String providerName, String website, Rating rating, List<Tariff> tariffs) {
        this.providerName = providerName;
        this.website = website;
        this.rating = rating;
        this.tariffs = tariffs;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProviderPublic that = (ProviderPublic) o;
        return Objects.equals(providerName, that.providerName) && Objects.equals(website, that.website) && Objects.equals(rating, that.rating) && Objects.equals(tariffs, that.tariffs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(providerName, website, rating, tariffs);
    }
}
