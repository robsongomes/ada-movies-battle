package tech.ada.moviesbattle.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.verification.VerificationMode;
import tech.ada.moviesbattle.entity.Movie;
import tech.ada.moviesbattle.entity.Round;
import tech.ada.moviesbattle.repository.MovieRepository;

import java.util.Collections;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class RoundServiceTest {

    @InjectMocks
    RoundService roundService;

    @Mock
    MovieRepository movieRepository;

    @Test
    void checkWinnerRoundMovie() {
        Movie movie1 = Movie.builder().id("m1").rating(2.0).votes(50).build();
        Movie movie2 = Movie.builder().id("m2").rating(1.5).votes(40).build();
        Round round = Round.builder()
                .movieOne(movie1)
                .movieTwo(movie2)
                .build();
        Movie winner = roundService.checkWinnerRoundMovie(round);
        assertEquals(winner.getId(), movie1.getId());
    }

    @Test
    void isRoundRepeated() {
        Movie movie1 = Movie.builder().id("m1").build();
        Movie movie2 = Movie.builder().id("m2").build();
        Movie movie3 = Movie.builder().id("m3").build();
        Movie movie4 = Movie.builder().id("m4").build();
        Round round = Round.builder()
                .movieOne(movie1)
                .movieTwo(movie2)
                .build();
        Round round1 = Round.builder()
                .movieOne(movie1)
                .movieTwo(movie2)
                .build();
        Round round2 = Round.builder()
                .movieOne(movie3)
                .movieTwo(movie4)
                .build();
        Round round3 = Round.builder()
                .movieOne(movie1)
                .movieTwo(movie3)
                .build();

        boolean isRepeated = roundService.isRoundRepeated(Set.of(round1, round2), round);
        assertTrue(isRepeated);

        isRepeated = roundService.isRoundRepeated(Set.of(round2, round3), round);
        assertFalse(isRepeated);
    }

    @Test
    void generateRandomRound() {
        Movie movie1 = Movie.builder().id("m1").rating(2.0).votes(50).build();
        Movie movie2 = Movie.builder().id("m2").rating(1.5).votes(40).build();
        Mockito.when(movieRepository.findFirstByMovie()).thenReturn(movie1, movie2);

        Round round = roundService.generateRandomRound();
        Mockito.verify(movieRepository, Mockito.times(2)).findFirstByMovie();
        assertEquals(round.getMovieOne().getId(), movie1.getId());
        assertEquals(round.getMovieTwo().getId(), movie2.getId());

        Mockito.reset(movieRepository);

        Mockito.when(movieRepository.findFirstByMovie()).thenReturn(movie1, movie1, movie2);
        round = roundService.generateRandomRound();

        Mockito.verify(movieRepository, Mockito.times(3)).findFirstByMovie();

        assertEquals(round.getMovieOne().getId(), movie1.getId());
        assertEquals(round.getMovieTwo().getId(), movie2.getId());

    }
}