package tech.ada.moviesbattle.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "movies")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Movie {

    @Id
    private String id;

    private String title;

    @Column(name = "released_year")
    private String year;

    private String genre;

    private String country;

    private double rating;

    private int votes;
}
