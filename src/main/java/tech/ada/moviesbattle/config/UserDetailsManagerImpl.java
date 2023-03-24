package tech.ada.moviesbattle.config;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import tech.ada.moviesbattle.repository.UserRepository;

@Service
public class UserDetailsManagerImpl implements UserDetailsService {

    final UserRepository repository;

    public UserDetailsManagerImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.repository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("No user found for username " + username));
    }
}
