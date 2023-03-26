package tech.ada.moviesbattle.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import tech.ada.moviesbattle.config.MoviePropertiesConfig;
import tech.ada.moviesbattle.dto.MovieImdbDto;

import java.util.ArrayList;
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
        List<MovieImdbDto> list = new ArrayList<>();
        for (int i = 100; i <= moviePropertiesConfig.getNumOfMoviesLoaded()+100; i++) {
            MovieImdbDto dto = requestMovie(i);
            if (dto != null) {
                list.add(dto);
            }
        }
        return list;
    }

    MovieImdbDto requestMovie(int i) {
        String url = String.format("%s?apiKey=%s&i=%s",
                moviePropertiesConfig.getImdbApiUrl(),
                moviePropertiesConfig.getImdbApiKey(),
                formatImdbId(i));
        //tratar erro caso não encontre o filme
        //tem situações em que o número de votos é vazio ou contém . e ,
        MovieImdbDto dto = null;
        try {
            dto = restTemplate.getForObject(url, MovieImdbDto.class);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return dto;
    }
}
