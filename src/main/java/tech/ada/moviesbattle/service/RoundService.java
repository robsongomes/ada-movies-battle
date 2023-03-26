package tech.ada.moviesbattle.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tech.ada.moviesbattle.entity.Match;
import tech.ada.moviesbattle.entity.Movie;
import tech.ada.moviesbattle.entity.Round;
import tech.ada.moviesbattle.repository.MovieRepository;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class RoundService {

    private MovieRepository movieRepository;

    public Movie checkWinnerRoundMovie(final Round round) {
        final double scoreOne = round.getMovieOne().getRating() * round.getMovieOne().getVotes();
        final double scoreTwo = round.getMovieTwo().getRating() * round.getMovieTwo().getVotes();
        return scoreOne > scoreTwo ? round.getMovieOne() : round.getMovieTwo();
    }

    public boolean isRoundRepeated(Set<Round> rounds, Round round) {
        return rounds.stream().anyMatch(r -> r.equals(round));
    }

    public Round generateRandomRound() {
        Movie movie1 = this.movieRepository.findFirstByMovie();
        Movie movie2 = this.movieRepository.findFirstByMovie();
        while (movie1.equals(movie2)) {
            movie2 = this.movieRepository.findFirstByMovie();
        }
        Round round = new Round();
        round.setMovieOne(movie1);
        round.setMovieTwo(movie2);
        return round;
    }
}
