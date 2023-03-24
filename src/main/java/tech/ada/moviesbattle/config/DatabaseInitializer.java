package tech.ada.moviesbattle.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import tech.ada.moviesbattle.entity.User;
import tech.ada.moviesbattle.repository.UserRepository;

@Component
public class DatabaseInitializer implements CommandLineRunner {

    private final UserRepository repository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public DatabaseInitializer(UserRepository repository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.repository = repository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public void run(String... args) {
        User player1 = new User();
        player1.setUsername("player1");
        player1.setPassword(bCryptPasswordEncoder.encode("123456"));
        User player2 = new User();
        player2.setUsername("player2");
        player2.setPassword(bCryptPasswordEncoder.encode("123456"));
        this.repository.save(player1);
        this.repository.save(player2);
    }
}
