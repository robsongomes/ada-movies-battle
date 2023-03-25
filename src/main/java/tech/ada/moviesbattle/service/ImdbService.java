package tech.ada.moviesbattle.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import tech.ada.moviesbattle.config.MoviePropertiesConfig;
import tech.ada.moviesbattle.dto.MovieImdbDto;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class ImdbService {
    private final RestTemplate restTemplate;

    private final MoviePropertiesConfig moviePropertiesConfig;

    private static final String PREFIX = "tt";

    public List<MovieImdbDto> loadMovies() {
        return IntStream.rangeClosed(1, moviePropertiesConfig.getNumOfMoviesLoaded())
                .mapToObj(this::requestMovie)
                .collect(Collectors.toList());
    }

    private MovieImdbDto requestMovie(int i) {
        String id = generateID(i+100);
        String url = String.format("%s?apiKey=%s&i=%s", "https://www.omdbapi.com", moviePropertiesConfig.getImdbApiKey(), id );
        //tratar erro caso não encontre o filme
        return restTemplate.getForObject(url, MovieImdbDto.class);
    }

    private String generateID(int n) {
        return PREFIX + String.format("%07d", n);
    }
}
