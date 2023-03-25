package tech.ada.moviesbattle.dto;

import lombok.Getter;
import tech.ada.moviesbattle.entity.Movie;

@Getter
public class MovieDto {

    private final String id;

    private final String title;

    private final String year;

    private final String genre;

    private final String country;

    public MovieDto(Movie movie) {
        this.id = movie.getId();
        this.title = movie.getTitle();
        this.year = movie.getYear();
        this.genre = movie.getGenre();
        this.country = movie.getCountry();
    }
}
