package tech.ada.moviesbattle.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import tech.ada.moviesbattle.dto.AnswerDto;
import tech.ada.moviesbattle.dto.MatchResponseDto;
import tech.ada.moviesbattle.dto.RankingDto;
import tech.ada.moviesbattle.entity.User;
import tech.ada.moviesbattle.service.MatchService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/match/")
public class MatchController {

    private final MatchService service;

    @PostMapping(path = "/new")
    public ResponseEntity<MatchResponseDto> startMatch(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        MatchResponseDto match = this.service.startMatch(user);
        return new ResponseEntity<>(match, HttpStatus.CREATED);
    }

    @PostMapping(path = "/stop")
    public ResponseEntity<MatchResponseDto> stopMatch(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return new ResponseEntity<>(this.service.stopMatch(user), HttpStatus.CREATED);
    }

    @PostMapping(path = "/round")
    public ResponseEntity<MatchResponseDto> answerRound(@RequestBody final AnswerDto answer, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return new ResponseEntity<>(this.service.answerRound(answer, user), HttpStatus.CREATED);
    }
}
