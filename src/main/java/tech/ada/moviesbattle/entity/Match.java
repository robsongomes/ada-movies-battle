package tech.ada.moviesbattle.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "matches")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Match {
    @Id
    @GeneratedValue
    private long id;

    @ManyToOne
    private User user;

    private boolean active;

    @ManyToOne(optional = true)
    private Round lastRound;

    private int wrongAnswers;

    private int rightAnswers;

    @OneToMany(mappedBy = "match", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Round> rounds;

}
