package tech.ada.moviesbattle.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(name = "Ranking API", description = "Display the top 5 best players")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/ranking")
public class RankingController {

    private final RankingService rankingService;

    @Operation(summary = "Show the ranking of the top 5 best players")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "The list of top 5 players",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = RankingDto.class))
                    }
            )
    })
    @GetMapping
    public ResponseEntity<List<RankingDto>> answerRound() {
        List<RankingDto> ranking = new ArrayList<>();
        ranking.add(RankingDto.builder().username("player1").points(150).build());
        ranking.add(RankingDto.builder().username("player2").points(120).build());
        return new ResponseEntity<>(this.rankingService.getRanking(), HttpStatus.CREATED);
    }
}
