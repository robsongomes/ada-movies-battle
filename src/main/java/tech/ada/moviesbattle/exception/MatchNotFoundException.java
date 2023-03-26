package tech.ada.moviesbattle.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "You don't have a match started")
public class MatchNotFoundException extends RuntimeException {
}
