package tech.ada.moviesbattle.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tech.ada.moviesbattle.config.MoviePropertiesConfig;
import tech.ada.moviesbattle.dto.*;
import tech.ada.moviesbattle.entity.Match;
import tech.ada.moviesbattle.entity.Movie;
import tech.ada.moviesbattle.entity.Round;
import tech.ada.moviesbattle.entity.User;
import tech.ada.moviesbattle.exception.*;
import tech.ada.moviesbattle.repository.MatchRepository;
import tech.ada.moviesbattle.repository.MovieRepository;
import tech.ada.moviesbattle.repository.UserRepository;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class MatchService {

    private final RoundService roundService;

    private final UserRepository userRepository;

    private final MovieRepository movieRepository;

    private final MatchRepository matchRepository;

    private final MoviePropertiesConfig moviePropertiesConfig;

    public MatchResponseDto startMatch(User user) {
        User userDB = this.userRepository.findByUsername(user.getUsername()).orElseThrow(UserNotFoundException::new);

        //verifica se ja existe uma partida ativa
        this.matchRepository.findMatchByUserAndActiveTrue(user).ifPresent(m -> { throw new GameActiveException(); });

        //cria a partida
        Match newMatch = Match.builder()
                .user(userDB)
                .active(true)
                .build();

        //seleciona filmes aleatórios
        Round round = roundService.generateRandomRound();
        round.setMatch(newMatch);

        newMatch.setLastRound(round);

        final Set<Round> rounds = new HashSet<>();
        rounds.add(round);
        newMatch.setRounds(rounds);

        Match matchDB = this.matchRepository.save(newMatch);

        MatchResponseDto response = new MatchResponseDto();
        response.setId(matchDB.getId());
        response.setUser(user.getUsername());
        response.setMovieOne(new MovieDto(round.getMovieOne()));
        response.setMovieTwo(new MovieDto(round.getMovieTwo()));

        return response;
    }

    public MatchResponseDto stopMatch(User user) {
        User userDB = this.userRepository.findByUsername(user.getUsername()).orElseThrow(UserNotFoundException::new);
        Match match = this.matchRepository.findMatchByUserAndActiveTrue(userDB).orElseThrow(MatchNotFoundException::new);
        match.setActive(false);
        matchRepository.save(match);

        return buildMatchResponseDto(match);
    }

    public MatchResponseDto answerRound(AnswerDto answer, User user) {
        User userDB = userRepository.findByUsername(user.getUsername()).orElseThrow(UserNotFoundException::new);
        Match match = matchRepository.findMatchByUserAndActiveTrue(user).orElseThrow(MatchNotFoundException::new);

        if (match.getWrongAnswers() >= moviePropertiesConfig.getMaxTries()) throw new MaximumTriesReachedException();

        Round lastRound = match.getLastRound();
        Movie winner = roundService.checkWinnerRoundMovie(lastRound);
        Movie playerAnswer = movieRepository.findById(answer.getMovieId()).orElseThrow(MovieNotFoundException::new);

        updateMatchScore(match, lastRound, winner, playerAnswer);

        if (match.getWrongAnswers() >= moviePropertiesConfig.getMaxTries()) throw new MaximumTriesReachedException();

        generateNextRound(match);

        matchRepository.save(match);

        return buildMatchResponseDto(match);
    }

    private Match getMatchByUser(User user) {
        return this.matchRepository.findMatchByUserAndActiveTrue(user)
                .orElseThrow(MatchNotFoundException::new);
    }

    private void updateMatchScore(Match match, Round lastRound, Movie winner, Movie movieAnswer) {
        lastRound.setMovieAnswer(movieAnswer);
        if (winner.equals(movieAnswer)) {
            match.setRightAnswers(match.getRightAnswers() + 1);
            lastRound.setCorrect(true);
        } else {
            match.setWrongAnswers(match.getWrongAnswers() + 1);
        }
    }

    private void generateNextRound(Match match) {
        Round round = roundService.generateRandomRound();
        while (roundService.isRoundRepeated(match.getRounds(), round)) {
            round = roundService.generateRandomRound();
        }
        round.setMatch(match);
        match.getRounds().add(round);
        match.setLastRound(round);
    }

    private MatchResponseDto buildMatchResponseDto(Match match) {
        return MatchResponseDto.builder()
                .id(match.getId())
                .rightAnswers(match.getRightAnswers())
                .wrongAnswers(match.getWrongAnswers())
                .movieOne(new MovieDto(match.getLastRound().getMovieOne()))
                .movieTwo(new MovieDto(match.getLastRound().getMovieTwo()))
                .user(match.getUser().getUsername())
                .active(match.isActive())
                .build();
    }
}
