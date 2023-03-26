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

    public String formatImdbId(int id) {
        return String.format("tt%07d", id);
    }

    public List<MovieImdbDto> loadMovies() {
        return IntStream.rangeClosed(1, moviePropertiesConfig.getNumOfMoviesLoaded())
                .mapToObj(this::requestMovie)
                .collect(Collectors.toList());
    }

    MovieImdbDto requestMovie(int i) {
        String url = String.format("%s?apiKey=%s&i=%s",
                moviePropertiesConfig.getImdbApiUrl(),
                moviePropertiesConfig.getImdbApiKey(),
                formatImdbId(i));
        //tratar erro caso não encontre o filme
        return restTemplate.getForObject(url, MovieImdbDto.class);
    }
}
