package tech.ada.moviesbattle.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tech.ada.moviesbattle.dto.AnswerDto;
import tech.ada.moviesbattle.dto.MatchResponseDto;
import tech.ada.moviesbattle.dto.MovieDto;
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

    private final UserRepository userRepository;

    private final MovieRepository movieRepository;

    private final MatchRepository matchRepository;

    @Value("${maxTries}")
    private int maxTries;

    public MatchResponseDto startMatch(User user) {
        User userDB = this.userRepository.findByUsername(user.getUsername()).orElseThrow(UserNotFoundException::new);

        //verifica se ja existe um game ativo
        this.matchRepository.findMatchByUserAndActiveTrue(userDB).ifPresent(m -> {throw new GameActiveException();});

        //cria a partida
        Match newMatch = new Match();
        newMatch.setUser(userDB);
        newMatch.setActive(true);
        newMatch.setRightAnswers(0);
        newMatch.setWrongAnswers(0);

        //seleciona filmes aleatórios
        Round round = generateRandomRound();
        round.setMatch(newMatch);

        newMatch.setLastRound(round);
        final Set<Round> rounds = new HashSet<>();
        rounds.add(round);
        newMatch.setRounds(rounds);

        //será que tá salvando o round?
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
        Match match = this.matchRepository.findMatchByUserAndActiveTrue(userDB).orElseThrow(GameNotFoundException::new);
        match.setActive(false);
        matchRepository.save(match);

        return MatchResponseDto.builder()
                .id(match.getId())
                .rightAnswers(match.getRightAnswers())
                .wrongAnswers(match.getWrongAnswers())
                .movieOne(new MovieDto(match.getLastRound().getMovieOne()))
                .movieTwo(new MovieDto(match.getLastRound().getMovieTwo()))
                .user(userDB.getUsername())
                .build();
    }
    public MatchResponseDto answerRound(AnswerDto answer, User user) {
        User userDB = this.userRepository.findByUsername(user.getUsername()).orElseThrow(UserNotFoundException::new);
        Match match = this.matchRepository.findMatchByUserAndActiveTrue(userDB).orElseThrow(GameNotFoundException::new);

        //verifica se excedeu o número de tentativas
        if (match.getWrongAnswers() >= maxTries) {
            throw new MaximumTriesReachedException();
        }

        //verifica o vencedor e atualizar o placar
        Round lastRound = match.getLastRound();

        Movie winner = checkWinnerRoundMovie(lastRound);
        Movie movieAnswer = movieRepository.findById(answer.getMovieId()).orElseThrow(MovieNotFoundException::new);

        //seta a resposta do usuário
        lastRound.setMovieAnswer(movieAnswer);

        if (winner.equals(movieAnswer)) {
            match.setRightAnswers(match.getRightAnswers() + 1);
            lastRound.setCorrect(true);
        } else {
            match.setWrongAnswers(match.getWrongAnswers() + 1);
        }

        //verifica se excedeu o número de tentativas
        if (match.getWrongAnswers() < maxTries) {
            Round round = generateRandomRound();
            //verificar se o round já foi anteriormente usado
            //pode causar um loop infinito caso não encontre uma combinação
            while (isRoundRepeated(match, round)) {
                round = generateRandomRound();
            }
            round.setMatch(match);
            match.getRounds().add(round);
            match.setLastRound(round);
        } else {
            throw new MaximumTriesReachedException();
        }

        match = this.matchRepository.save(match);

        //SETAR OS CAMPOS
        return MatchResponseDto.builder()
                .id(match.getId())
                .rightAnswers(match.getRightAnswers())
                .wrongAnswers(match.getWrongAnswers())
                .movieOne(new MovieDto(match.getLastRound().getMovieOne()))
                .movieTwo(new MovieDto(match.getLastRound().getMovieTwo()))
                .user(userDB.getUsername())
                .build();
    }

    private static boolean isRoundRepeated(Match match, Round round) {
        return match.getRounds().stream().anyMatch(r -> r.equals(round));
    }

    private Round generateRandomRound() {
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

    public Movie checkWinnerRoundMovie(final Round round) {
        final double scoreOne = round.getMovieOne().getRating() * round.getMovieOne().getVotes();
        final double scoreTwo = round.getMovieTwo().getRating() * round.getMovieTwo().getVotes();
        return scoreOne > scoreTwo ? round.getMovieOne() : round.getMovieTwo();
    }
}
