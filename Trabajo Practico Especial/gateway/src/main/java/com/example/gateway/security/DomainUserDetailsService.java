package com.example.gateway.security;

import com.example.gateway.entity.Authority;
import com.example.gateway.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Component("userDetailsService")
@RequiredArgsConstructor
public class DomainUserDetailsService implements ReactiveUserDetailsService {

    private final UserRepository userRepository;

    @Override
    public Mono<UserDetails> findByUsername(String username) {

        return Mono.fromCallable(() ->
                        userRepository.findOneWithAuthoritiesByUsernameIgnoreCase(username.toLowerCase())
                                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username))
                )
                .map(user -> {
                    List<GrantedAuthority> authorities = user.getAuthorities()
                            .stream()
                            .map(auth -> new SimpleGrantedAuthority(auth.getName()))
                            .collect(Collectors.toList());

                    return new org.springframework.security.core.userdetails.User(
                            user.getUsername(),
                            user.getPassword(),
                            authorities
                    );

                });
    }
}
