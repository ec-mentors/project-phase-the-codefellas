package io.everyonecodes.anber.searchmanagement.data;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
public class Rating {

    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ElementCollection(targetClass = String.class)
    private Set<String> usersRated = new HashSet<>();
//    @Range(min = 1, max = 5)
    private String score = "No Ratings";

    public Rating() {
    }

    public Rating(Long id, Set<String> usersRated, String score) {
        this.id = id;
        this.usersRated = usersRated;
        this.score = score;
    }

    public Rating(Set<String> usersRated, String score) {
        this.usersRated = usersRated;
        this.score = score;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<String> getUsersRated() {
        return usersRated;
    }

    public void setUsersRated(Set<String> usersRated) {
        this.usersRated = usersRated;
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
        return Objects.equals(id, rating.id) && Objects.equals(usersRated, rating.usersRated) && Objects.equals(score, rating.score);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, usersRated, score);
    }
}
