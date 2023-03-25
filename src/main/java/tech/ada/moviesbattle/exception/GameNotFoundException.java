package tech.ada.moviesbattle.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "You don't have a game started")
public class GameNotFoundException extends RuntimeException {
}
