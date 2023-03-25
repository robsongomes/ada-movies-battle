package tech.ada.moviesbattle.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tech.ada.moviesbattle.entity.Movie;

@Repository
public interface MovieRepository extends JpaRepository<Movie, String> {

    @Query(value = "SELECT * FROM movies m ORDER BY RAND() LIMIT 1", nativeQuery = true)
    Movie findFirstByMovie();
}
