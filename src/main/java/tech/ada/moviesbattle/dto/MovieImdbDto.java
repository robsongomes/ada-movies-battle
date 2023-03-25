package tech.ada.moviesbattle.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;
import tech.ada.moviesbattle.entity.Movie;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@ToString
public class MovieImdbDto {
    @JsonProperty("imdbID")
    private String id;

    @JsonProperty("Title")
    private String title;

    @JsonProperty("imdbRating")
    private double rating;

    @JsonProperty("Year")
    private String year;

    @JsonProperty("Genre")
    private String genre;

    @JsonProperty("Country")
    private String country;

    @JsonProperty("imdbVotes")
    private int votes;

    public Movie toMovie() {
        return Movie
                .builder()
                .id(this.id)
                .title(this.title)
                .rating(this.rating)
                .country(this.country)
                .genre(this.genre)
                .year(this.year)
                .votes(this.votes)
                .build();
    }
}
