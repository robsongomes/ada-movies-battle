package tech.ada.moviesbattle.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE,
        reason = "You have reached the maximum number of attempts for this match. Finish this match.")
public class MaximumTriesReachedException extends RuntimeException {
}
