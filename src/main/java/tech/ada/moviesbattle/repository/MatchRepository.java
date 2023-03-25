package tech.ada.moviesbattle.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import tech.ada.moviesbattle.dto.RankingDto;
import tech.ada.moviesbattle.dto.RankingProjection;
import tech.ada.moviesbattle.entity.Match;
import tech.ada.moviesbattle.entity.User;

import java.util.List;
import java.util.Optional;

public interface MatchRepository extends JpaRepository<Match, Long> {

    Optional<Match> findMatchByUserAndActiveTrue(User user);

    @Query(value = "select u.username as username, round(sum(m.right_answers / ( (m.wrong_answers + m.right_answers) / 100.0))) as points\n" +
            "from users u join matches m on (m.user_id = u.id) \n" +
            "where m.active = false \n" +
            "and (m.wrong_answers != 0 and m.right_answers != 0) \n" +
            "group by u.username \n" +
            "order by points desc \n" +
            "limit 5", nativeQuery = true)
    List<RankingProjection> ranking();
}
