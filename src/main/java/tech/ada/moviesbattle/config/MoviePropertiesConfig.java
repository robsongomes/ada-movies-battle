package tech.ada.moviesbattle.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "movies")
@Data
public class MoviePropertiesConfig {

    private String imdbApiKey;

    private int numOfMoviesLoaded;
}
