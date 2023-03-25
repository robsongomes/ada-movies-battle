package tech.ada.moviesbattle.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.ada.moviesbattle.dto.RankingDto;
import tech.ada.moviesbattle.service.RankingService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/ranking")
public class RankingController {

    private final RankingService rankingService;

    @GetMapping
    public ResponseEntity<List<RankingDto>> answerRound() {
        List<RankingDto> ranking = new ArrayList<>();
        ranking.add(RankingDto.builder().username("player1").points(150).build());
        ranking.add(RankingDto.builder().username("player2").points(120).build());
        return new ResponseEntity<>(this.rankingService.getRanking(), HttpStatus.CREATED);
    }
}
