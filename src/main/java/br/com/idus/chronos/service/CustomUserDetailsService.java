package br.com.idus.chronos.service;

import br.com.idus.chronos.domain.User;
import br.com.idus.chronos.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com o email: " + username));

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                getAuthority(user)
        );
    }

    private Collection<? extends GrantedAuthority> getAuthority(User user) {
        Set<GrantedAuthority> authorities = new HashSet<>();

        if (user.getRole() == null) {
            return authorities;
        }

        switch (user.getRole()) {
            case ROLE_MANAGER:
                authorities.add(new SimpleGrantedAuthority("ROLE_MANAGER"));
                authorities.add(new SimpleGrantedAuthority("ROLE_EMPLOYEE"));
                break;
            case ROLE_EMPLOYEE:
                authorities.add(new SimpleGrantedAuthority("ROLE_EMPLOYEE"));
                break;
        }
        return authorities;
    }
}
