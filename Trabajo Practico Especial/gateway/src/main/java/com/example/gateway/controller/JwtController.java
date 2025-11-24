package com.example.gateway.controller;

import com.example.gateway.DTO.LoginDTO;
import com.example.gateway.security.jwt.JwtFilter;
import com.example.gateway.security.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class JwtController {

    private final TokenProvider tokenProvider;
    private final ReactiveUserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    private ReactiveAuthenticationManager authenticationManager;


    @PostMapping("/login")
    public Mono<ResponseEntity<?>> login(@RequestBody LoginDTO login) {

        Authentication authToken =
                new UsernamePasswordAuthenticationToken(login.getUsername(), login.getPassword());

        return authenticationManager.authenticate(authToken)
                .map(auth -> {
                    String token = tokenProvider.createToken(auth);
                    return ResponseEntity.ok().body(Map.of("id_token", token));
                });
    }


    record JWTToken(String id_token) {}
}
