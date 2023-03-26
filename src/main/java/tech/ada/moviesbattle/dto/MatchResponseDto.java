package tech.ada.moviesbattle.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MatchResponseDto {
    private Long id;

    private String user;

    private MovieDto movieOne;

    private MovieDto movieTwo;

    private int rightAnswers;

    private int wrongAnswers;

    private boolean active;
}
