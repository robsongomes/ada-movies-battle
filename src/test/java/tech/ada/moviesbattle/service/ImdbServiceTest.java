package tech.ada.moviesbattle.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;
import tech.ada.moviesbattle.config.MoviePropertiesConfig;
import tech.ada.moviesbattle.dto.MovieImdbDto;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ImdbServiceTest {

    @InjectMocks
    ImdbService service;

    @Mock
    RestTemplate restTemplate;

    @Mock
    MoviePropertiesConfig config;

    @Test
    void loadMovies() {
        Mockito.when(config.getNumOfMoviesLoaded()).thenReturn(5);
        service.loadMovies();
        Mockito.verify(restTemplate, Mockito.times(5)).getForObject(Mockito.anyString(), Mockito.any());
        Mockito.verify(config, Mockito.times(5)).getImdbApiKey();
        Mockito.verify(config, Mockito.times(5)).getImdbApiUrl();
    }

    @Test
    void requestMovie() {
        service.requestMovie(1);
        Mockito.verify(restTemplate, Mockito.times(1)).getForObject(Mockito.anyString(), Mockito.any());
        Mockito.verify(config, Mockito.times(1)).getImdbApiKey();
        Mockito.verify(config, Mockito.times(1)).getImdbApiUrl();
    }

    @Test
    void testFormatImdbId() {
        Assertions.assertEquals("tt0000001", service.formatImdbId(1));
        Assertions.assertEquals("tt0000111", service.formatImdbId(111));
        Assertions.assertEquals("tt1111111", service.formatImdbId(1111111));
    }
}