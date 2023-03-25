package tech.ada.moviesbattle;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@ConfigurationPropertiesScan
public class MoviesBattleApplication {

    public static void main(String[] args) {
        SpringApplication.run(MoviesBattleApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
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



}
