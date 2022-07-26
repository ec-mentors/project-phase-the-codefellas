package io.everyonecodes.anber.searchmanagement.data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class Rating {

    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ElementCollection(targetClass = String.class)
    private List<String> ratings = new ArrayList<>();
//    @Range(min = 1, max = 5)
    private double score = 0.0;

    public Rating() {
    }

    public Rating(List<String> ratings, double score) {
        this.ratings = ratings;
        this.score = score;
    }

    public Rating(Long id, List<String> ratings, double score) {
        this.id = id;
        this.ratings = ratings;
        this.score = score;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<String> getRatings() {
        return ratings;
    }

    public void setRatings(List<String> ratings) {
        this.ratings = ratings;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rating rating = (Rating) o;
        return Double.compare(rating.score, score) == 0 && Objects.equals(id, rating.id) && Objects.equals(ratings, rating.ratings);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, ratings, score);
    }

    @Override
    public String toString() {
        return "Rating{" +
                "id=" + id +
                ", ratings=" + ratings +
                ", score=" + score +
                '}';
    }
}
