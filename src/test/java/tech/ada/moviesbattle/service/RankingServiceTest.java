package tech.ada.moviesbattle.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import tech.ada.moviesbattle.dto.RankingDto;
import tech.ada.moviesbattle.dto.RankingProjection;
import tech.ada.moviesbattle.repository.MatchRepository;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class RankingServiceTest {

    @InjectMocks
    RankingService service;

    @Mock
    MatchRepository matchRepository;

    @Test
    void getRanking() {
        List<RankingProjection> projections = new ArrayList<>();
        projections.add(new RankingProjection() {
            @Override
            public String getUsername() {
                return "player1";
            }

            @Override
            public int getPoints() {
                return 100;
            }
        });
        projections.add(new RankingProjection() {
            @Override
            public String getUsername() {
                return "player2";
            }

            @Override
            public int getPoints() {
                return 70;
            }
        });
        Mockito.when(matchRepository.ranking()).thenReturn(projections);
        List<RankingDto> ranking = service.getRanking();
        assertEquals(2, ranking.size());
        assertEquals(ranking.get(0).getUsername(), "player1");
        assertEquals(ranking.get(0).getPoints(), 100);
        assertEquals(ranking.get(1).getUsername(), "player2");
        assertEquals(ranking.get(1).getPoints(), 70);
    }
}