//package io.everyonecodes.anber.searchmanagement.data;
//
//import javax.persistence.*;
//import javax.validation.Valid;
//import javax.validation.constraints.NotNull;
//import java.util.ArrayList;
//import java.util.List;
//
//public abstract class ProviderAccount {
//
////    @Id
////    @GeneratedValue(strategy = GenerationType.IDENTITY)
////    private Long id;
//    @NotNull
//    @Column(unique = true)
//    private String providerName;
//    @NotNull
//    private String website;
////    @NotNull
////    private String email;
////    @NotNull
////    private String phoneNumber;
//    @NotNull
//    private boolean verified = false;
//
//    @Valid
//    @NotNull
//    @OneToMany
//    private List<Tariff> tariffs = new ArrayList<>();
//
////    @NotNull
//    @Valid
//    @OneToOne
//    private Rating rating = new Rating(List.of("No ratings yet"), 0.0);
//
//    public ProviderAccount(String providerName, String website, boolean verified, List<Tariff> tariffs, Rating rating) {
//        this.providerName = providerName;
//        this.website = website;
//        this.verified = verified;
//        this.tariffs = tariffs;
//        this.rating = rating;
//    }
//
//    public ProviderAccount(String providerName, String website, List<Tariff> tariffs, Rating rating) {
//        this.providerName = providerName;
//        this.website = website;
//        this.tariffs = tariffs;
//        this.rating = rating;
//    }
//
////    public Long getId() {
////        return id;
////    }
////
////    public void setId(Long id) {
////        this.id = id;
////    }
//
//    public String getProviderName() {
//        return providerName;
//    }
//
//    public void setProviderName(String providerName) {
//        this.providerName = providerName;
//    }
//
//    public String getWebsite() {
//        return website;
//    }
//
//    public void setWebsite(String website) {
//        this.website = website;
//    }
//
//    public boolean isVerified() {
//        return verified;
//    }
//
//    public void setVerified(boolean verified) {
//        this.verified = verified;
//    }
//
//    public List<Tariff> getTariffs() {
//        return tariffs;
//    }
//
//    public void setTariffs(List<Tariff> tariffs) {
//        this.tariffs = tariffs;
//    }
//
//    public Rating getRating() {
//        return rating;
//    }
//
//    public void setRating(Rating rating) {
//        this.rating = rating;
//    }
//
//    public abstract void verification();
//
//}
