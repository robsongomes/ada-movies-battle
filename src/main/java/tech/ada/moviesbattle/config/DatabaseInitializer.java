package tech.ada.moviesbattle.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import tech.ada.moviesbattle.dto.MovieImdbDto;
import tech.ada.moviesbattle.entity.User;
import tech.ada.moviesbattle.repository.MovieRepository;
import tech.ada.moviesbattle.repository.UserRepository;
import tech.ada.moviesbattle.service.ImdbService;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DatabaseInitializer implements CommandLineRunner {

    private final UserRepository repository;
    private final MovieRepository movieRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ImdbService imdbService;

    @Override
    public void run(String... args) {
        loadUsers();
//        loadMovies();
    }

    private void loadUsers() {
        User player1 = new User();
        player1.setUsername("player1");
        player1.setPassword(bCryptPasswordEncoder.encode("123456"));
        User player2 = new User();
        player2.setUsername("player2");
        player2.setPassword(bCryptPasswordEncoder.encode("123456"));
        this.repository.save(player1);
        this.repository.save(player2);
    }

    private void loadMovies() {
        List<MovieImdbDto> movies = this.imdbService.loadMovies();
        movies.stream().map(MovieImdbDto::toMovie).forEach(this.movieRepository::save);
    }
}
