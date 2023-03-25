package tech.ada.moviesbattle.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tech.ada.moviesbattle.dto.RankingDto;
import tech.ada.moviesbattle.dto.RankingProjection;
import tech.ada.moviesbattle.repository.MatchRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RankingService {

    private final MatchRepository matchRepository;

    public List<RankingDto> getRanking() {
        List<RankingProjection> projection = matchRepository.ranking();
        return projection.stream()
                .map(r -> RankingDto.builder()
                        .username(r.getUsername())
                        .points(r.getPoints())
                        .build()
                ).collect(Collectors.toList());
    }
}
