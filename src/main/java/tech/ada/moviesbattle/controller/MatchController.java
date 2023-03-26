package tech.ada.moviesbattle.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import tech.ada.moviesbattle.dto.AnswerDto;
import tech.ada.moviesbattle.dto.MatchResponseDto;
import tech.ada.moviesbattle.entity.User;
import tech.ada.moviesbattle.exception.*;
import tech.ada.moviesbattle.service.MatchService;

@Tag(name = "Match API", description = "REST API that control all the game operations.")
@SecurityScheme(
        type = SecuritySchemeType.HTTP,
        name = "basicAuth",
        scheme = "basic")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/match/")
public class MatchController {

    private final MatchService service;

    @Operation(summary = "Start a match(game) if it does not exist a previous game active. \nEach game has unique rounds")
    @SecurityRequirement(name = "basicAuth")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Info about the match created",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = MatchResponseDto.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "There is already a match active. You should stop it before start new one",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = MatchNotFoundException.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found or Invalid Credentials",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = UserNotFoundException.class)
                            )
                    }
            ),
        }
    )
    @PostMapping(path = "/new")
    public ResponseEntity<MatchResponseDto> startMatch(Authentication authentication) throws UserNotFoundException, GameActiveException {
        User user = (User) authentication.getPrincipal();
        MatchResponseDto match = this.service.startMatch(user);
        return new ResponseEntity<>(match, HttpStatus.CREATED);
    }

    @Operation(summary = "Stop an active match(game)")
    @SecurityRequirement(name = "basicAuth")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Info about the match stopped",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = MatchResponseDto.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "There is already a match active. You should stop it before start new one",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = MatchNotFoundException.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found or Invalid Credentials",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = UserNotFoundException.class)
                            )
                    }
            ),
    }
    )
    @PostMapping(path = "/stop")
    public ResponseEntity<MatchResponseDto> stopMatch(Authentication authentication) throws MatchNotFoundException, UserNotFoundException {
        User user = (User) authentication.getPrincipal();
        return new ResponseEntity<>(this.service.stopMatch(user), HttpStatus.CREATED);
    }

    @Operation(summary = "Endpoint that implements the Round operation. Each request answer the current active Round " +
            "and generate a new Round while the user does not reach the maximum number of tries")
    @SecurityRequirement(name = "basicAuth")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Info about the current match and the next Round",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = MatchResponseDto.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "There is already a match active. You should stop it before start new one",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = MatchNotFoundException.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found or Invalid Credentials",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = UserNotFoundException.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Movie not found",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = MovieNotFoundException.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "406",
                    description = "Maximum of wrong answers reached",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = MaximumTriesReachedException.class)
                            )
                    }
            ),
    }
    )
    @PostMapping(path = "/round")
    public ResponseEntity<MatchResponseDto> answerRound(@RequestBody final AnswerDto answer, Authentication authentication)
            throws MatchNotFoundException, UserNotFoundException, MovieNotFoundException, MaximumTriesReachedException {
        User user = (User) authentication.getPrincipal();
        return new ResponseEntity<>(this.service.answerRound(answer, user), HttpStatus.CREATED);
    }
}
