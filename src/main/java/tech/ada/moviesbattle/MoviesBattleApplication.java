package tech.ada.moviesbattle;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.client.RestTemplate;
import tech.ada.moviesbattle.dto.MovieImdbDto;
import tech.ada.moviesbattle.entity.User;
import tech.ada.moviesbattle.repository.MovieRepository;
import tech.ada.moviesbattle.repository.UserRepository;
import tech.ada.moviesbattle.service.ImdbService;

import java.util.List;

@SpringBootApplication
@RequiredArgsConstructor
@ConfigurationPropertiesScan
public class MoviesBattleApplication implements ApplicationRunner {

    private final UserRepository repository;
    private final MovieRepository movieRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ImdbService imdbService;

    public static void main(String[] args) {
        SpringApplication.run(MoviesBattleApplication.class, args);
    }



    @Bean
    public OpenAPI springShopOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Movies Battle")
                        .description("REST API for a card game style application, " +
                                "where two movies will be informed and the player must choose " +
                                "the one with the best rating on IMDB. A match can have one or several rounds, and " +
                                "the user can make up to three mistakes in each match. " +
                                "The user can end the game at any time, but can only start a new game " +
                                "if no game has started.")
                        .version("v0.0.1")
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")));
    }


    @Override
    public void run(ApplicationArguments args) throws Exception {
        loadUsers();
        loadMoviesFromImdbAPI();
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


    private void loadMoviesFromImdbAPI() {
        List<MovieImdbDto> movies = this.imdbService.loadMovies();
        movies.stream().map(MovieImdbDto::toMovie).forEach(this.movieRepository::save);
    }
}
