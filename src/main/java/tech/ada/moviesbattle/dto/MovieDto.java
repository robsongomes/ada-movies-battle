package tech.ada.moviesbattle.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import tech.ada.moviesbattle.entity.Movie;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovieDto {

    private String id;

    private String title;

    private String year;

    private String genre;

    private String country;

    public MovieDto(Movie movie) {
        this.id = movie.getId();
        this.title = movie.getTitle();
        this.year = movie.getYear();
        this.genre = movie.getGenre();
        this.country = movie.getCountry();
    }
}
