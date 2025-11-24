package com.example.gateway.config;

import com.example.gateway.security.AuthorityConstant;
import com.example.gateway.security.jwt.JwtFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public ReactiveAuthenticationManager authenticationManager(
            ReactiveUserDetailsService userDetailsService,
            PasswordEncoder encoder
    ) {
        UserDetailsRepositoryReactiveAuthenticationManager authManager =
                new UserDetailsRepositoryReactiveAuthenticationManager(userDetailsService);
        authManager.setPasswordEncoder(encoder);
        return authManager;
    }


    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http, JwtFilter jwtFilter) {

        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchange -> exchange

                        // PUBLIC
                        .pathMatchers(HttpMethod.POST, "/auth/login").permitAll()
                        .pathMatchers(HttpMethod.POST, "/auth/register").permitAll()

                        // ADMIN
                        .pathMatchers(HttpMethod.GET, "/monopatines/uso").hasAuthority(AuthorityConstant._ADMIN)
                        .pathMatchers(HttpMethod.GET, "/viajes/monopatines/mas-viajes").hasAuthority(AuthorityConstant._ADMIN)
                        .pathMatchers(HttpMethod.GET, "/facturas/total").hasAuthority(AuthorityConstant._ADMIN)
                        .pathMatchers(HttpMethod.GET, "/usuarios/uso-frecuente").hasAuthority(AuthorityConstant._ADMIN)
                        .pathMatchers(HttpMethod.POST, "/tarifas/vigente").hasAuthority(AuthorityConstant._ADMIN)

                        // USER
                        .pathMatchers(HttpMethod.GET, "/monopatines/cercanos").hasAuthority(AuthorityConstant._USER)
                        .pathMatchers(HttpMethod.GET, "/viajes/uso-por-cuenta").hasAuthority(AuthorityConstant._USER)
                        .pathMatchers(HttpMethod.GET, "/viajes/mis-viajes").hasAuthority(AuthorityConstant._USER)

                        // ANY OTHER REQUEST → AUTH REQUIRED
                        .anyExchange().authenticated()
                )
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)

                .addFilterAt(jwtFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .build();
    }
}
