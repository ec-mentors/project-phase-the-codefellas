package io.everyonecodes.anber.searchmanagement.data;

import javax.persistence.*;
import java.util.*;

@Entity
public class Rating {

    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ElementCollection(targetClass = Map.class)
    private Set<Map<String,Integer>> ratings = new HashSet<>();
//    @Range(min = 1, max = 5)
    private String score = "no ratings yet";

    public Rating() {
    }

    public Rating(Long id, Set<Map<String, Integer>> ratings, String score) {
        this.id = id;
        this.ratings = ratings;
        this.score = score;
    }

    public Rating(Set<Map<String, Integer>> ratings, String score) {
        this.ratings = ratings;
        this.score = score;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<Map<String, Integer>> getRatings() {
        return ratings;
    }

    public void setRatings(Set<Map<String, Integer>> ratings) {
        this.ratings = ratings;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rating rating = (Rating) o;
        return Objects.equals(id, rating.id) && Objects.equals(ratings, rating.ratings) && Objects.equals(score, rating.score);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, ratings, score);
    }
}
