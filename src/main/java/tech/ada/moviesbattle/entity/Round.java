package tech.ada.moviesbattle.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "rounds")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Round {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    private Match match;

    @ManyToOne
    private Movie movieOne;

    @ManyToOne
    private Movie movieTwo;

    @ManyToOne
    private Movie movieAnswer;

    private boolean correct;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Round that = (Round) o;
        var result = movieOne.equals(that.movieOne) && movieTwo.equals(that.movieTwo) || movieOne.equals(that.movieTwo) && movieTwo.equals(that.movieOne) ;
        return result;
    }
}
