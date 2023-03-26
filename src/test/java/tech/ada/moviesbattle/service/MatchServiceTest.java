package tech.ada.moviesbattle.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.event.annotation.BeforeTestMethod;
import org.springframework.test.util.ReflectionTestUtils;
import tech.ada.moviesbattle.config.MoviePropertiesConfig;
import tech.ada.moviesbattle.dto.AnswerDto;
import tech.ada.moviesbattle.dto.MatchResponseDto;
import tech.ada.moviesbattle.entity.Match;
import tech.ada.moviesbattle.entity.Movie;
import tech.ada.moviesbattle.entity.Round;
import tech.ada.moviesbattle.entity.User;
import tech.ada.moviesbattle.exception.*;
import tech.ada.moviesbattle.repository.MatchRepository;
import tech.ada.moviesbattle.repository.MovieRepository;
import tech.ada.moviesbattle.repository.UserRepository;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@ExtendWith(MockitoExtension.class)
class MatchServiceTest {

    @InjectMocks
    private MatchService service;

    @Mock
    private RoundService roundService;

    @Mock
    private MatchRepository matchRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private MovieRepository movieRepository;

    @Mock
    private static MoviePropertiesConfig moviePropertiesConfig;

    private User user;

    @BeforeEach
    public void setup() {
        user = new User(1, "player1", "123456");
    }

    @Test
    void startMatchWithActiveMatch() {
        Mockito.when(userRepository.findByUsername("player1")).thenReturn(Optional.ofNullable(user));
        Optional<Match> match = Optional.of(new Match());
        Mockito.when(matchRepository.findMatchByUserAndActiveTrue(user)).thenReturn(match);
        Assertions.assertThrows(GameActiveException.class, () -> { service.startMatch(user); });
    }

    @Test
    void startMatchWithUserInvalid() {
        Mockito.when(userRepository.findByUsername("player1")).thenReturn(Optional.empty());
        Assertions.assertThrows(UserNotFoundException.class, () -> { service.startMatch(user); });
    }

    @Test
    void startNewMatch() {
        Round round = Round.builder()
                .movieOne(Movie.builder().build())
                .movieTwo(Movie.builder().build())
                .build();
        Mockito.when(userRepository.findByUsername("player1")).thenReturn(Optional.ofNullable(user));
        Mockito.when(matchRepository.findMatchByUserAndActiveTrue(user)).thenReturn(Optional.empty());
        Mockito.when(matchRepository.save(Mockito.any(Match.class))).thenReturn(Match.builder().id(1).build());
        Mockito.when(roundService.generateRandomRound()).thenReturn(round);

        MatchResponseDto response = service.startMatch(user);

        Mockito.verify(matchRepository, Mockito.times(1)).save(Mockito.any(Match.class));

        Assertions.assertEquals(response.getId(), 1);
        Assertions.assertEquals(response.getUser(), user.getUsername());
        Assertions.assertNotNull(response.getMovieOne());
        Assertions.assertNotNull(response.getMovieTwo());

    }

    @Test
    void stopMatch() {
        Match activeMatch = Match.builder()
                .id(1)
                .active(true)
                .lastRound(Round.builder()
                        .movieOne(new Movie())
                        .movieTwo(new Movie())
                        .build())
                .build();
        Mockito.when(userRepository.findByUsername(Mockito.anyString())).thenReturn(Optional.ofNullable(user));
        Mockito.when(matchRepository.findMatchByUserAndActiveTrue(user)).thenReturn(Optional.ofNullable(activeMatch));

        MatchResponseDto response = service.stopMatch(user);

        Mockito.verify(matchRepository).save(Mockito.any());
        Assertions.assertNotNull(response);
        Assertions.assertNotNull(activeMatch);
        Assertions.assertFalse(activeMatch.isActive());
        Assertions.assertEquals(response.getId(), activeMatch.getId());
        Assertions.assertEquals(response.getUser(), user.getUsername());
    }

    @Test
    void answerRoundWithUserInvalid() {
        Mockito.when(userRepository.findByUsername("player1")).thenReturn(Optional.empty());
        Assertions.assertThrows(UserNotFoundException.class, () -> { service.answerRound(Mockito.any(), user); });
    }

    @Test
    void answerRoundWithNoActiveMatch() {
        Mockito.when(userRepository.findByUsername("player1")).thenReturn(Optional.ofNullable(user));
        Mockito.when(matchRepository.findMatchByUserAndActiveTrue(user)).thenReturn(Optional.empty());
        Assertions.assertThrows(MatchNotFoundException.class, () -> { service.answerRound(Mockito.any(), user); });
    }

    @Test
    void answerRoundWithMaximumErrorsReached() {
        Match match = Match.builder().wrongAnswers(3).build();
        Mockito.when(userRepository.findByUsername("player1")).thenReturn(Optional.ofNullable(user));
        Mockito.when(matchRepository.findMatchByUserAndActiveTrue(user)).thenReturn(Optional.ofNullable(match));
        Assertions.assertThrows(MaximumTriesReachedException.class, () -> { service.answerRound(Mockito.any(), user); });
    }

    @Test
    void answerRoundWithWrongAnswer() {
        AnswerDto answer = new AnswerDto("t1");
        Movie movieWinner = Movie.builder().id("t2").build();
        Round round = Round.builder()
                .movieOne(Movie.builder().build())
                .movieTwo(Movie.builder().build())
                .build();
        Round newRound = Round.builder()
                .movieOne(Movie.builder().build())
                .movieTwo(Movie.builder().build())
                .build();

        Match match = Match.builder().user(user).lastRound(round).rounds(new HashSet<>()).build();
        Mockito.when(moviePropertiesConfig.getMaxTries()).thenReturn(3);
        Mockito.when(userRepository.findByUsername("player1")).thenReturn(Optional.ofNullable(user));
        Mockito.when(matchRepository.findMatchByUserAndActiveTrue(user)).thenReturn(Optional.ofNullable(match));
        Mockito.when(roundService.checkWinnerRoundMovie(round)).thenReturn(movieWinner);
        Mockito.when(movieRepository.findById(answer.getMovieId())).thenReturn(Optional.of(Movie.builder().id("t1").build()));
        Mockito.when(roundService.generateRandomRound()).thenReturn(newRound);
        Mockito.when(matchRepository.save(Mockito.any(Match.class))).thenReturn(match);

        MatchResponseDto response = service.answerRound(answer, user);

        Assertions.assertNotNull(match);
        Assertions.assertEquals(match.getWrongAnswers(), 1);
    }

    @Test
    void answerRoundWithRightAnswer() {
        AnswerDto answer = new AnswerDto("t1");
        Movie movieWinner = Movie.builder().id("t1").build();
        Round round = Round.builder()
                .movieOne(Movie.builder().build())
                .movieTwo(Movie.builder().build())
                .build();
        Round newRound = Round.builder()
                .movieOne(Movie.builder().build())
                .movieTwo(Movie.builder().build())
                .build();

        Match match = Match.builder().user(user).lastRound(round).rounds(new HashSet<>()).build();
        Mockito.when(moviePropertiesConfig.getMaxTries()).thenReturn(3);
        Mockito.when(userRepository.findByUsername("player1")).thenReturn(Optional.ofNullable(user));
        Mockito.when(matchRepository.findMatchByUserAndActiveTrue(user)).thenReturn(Optional.ofNullable(match));
        Mockito.when(roundService.checkWinnerRoundMovie(round)).thenReturn(movieWinner);
        Mockito.when(movieRepository.findById(answer.getMovieId())).thenReturn(Optional.ofNullable(movieWinner));
        Mockito.when(roundService.generateRandomRound()).thenReturn(newRound);
        Mockito.when(matchRepository.save(Mockito.any(Match.class))).thenReturn(match);

        MatchResponseDto response = service.answerRound(answer, user);

        Assertions.assertNotNull(match);
        Assertions.assertEquals(match.getWrongAnswers(), 0);
        Assertions.assertEquals(match.getRightAnswers(), 1);
    }

    @Test
    void answerRoundWithMovieNotFound() {
        AnswerDto answer = new AnswerDto("t1");
        Movie movieWinner = Movie.builder().id("t1").build();
        Round round = Round.builder()
                .movieOne(Movie.builder().build())
                .movieTwo(Movie.builder().build())
                .build();
        Round newRound = Round.builder()
                .movieOne(Movie.builder().build())
                .movieTwo(Movie.builder().build())
                .build();

        Match match = Match.builder().user(user).lastRound(round).rounds(new HashSet<>()).build();
        Mockito.when(moviePropertiesConfig.getMaxTries()).thenReturn(3);
        Mockito.when(userRepository.findByUsername("player1")).thenReturn(Optional.ofNullable(user));
        Mockito.when(matchRepository.findMatchByUserAndActiveTrue(user)).thenReturn(Optional.ofNullable(match));
        Mockito.when(roundService.checkWinnerRoundMovie(round)).thenReturn(movieWinner);
        Mockito.when(movieRepository.findById(answer.getMovieId())).thenReturn(Optional.empty());
        Assertions.assertThrows(MovieNotFoundException.class, () -> { service.answerRound(answer, user); });
    }
}