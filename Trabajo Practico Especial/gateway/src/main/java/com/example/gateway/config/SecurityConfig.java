package com.example.gateway.config;

import com.example.gateway.security.AuthorityConstant;
import com.example.gateway.security.jwt.JwtFilter;
import com.example.gateway.security.jwt.TokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    private final TokenProvider tokenProvider;

    public SecurityConfig(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(final HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);
        http.sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .securityMatcher("/api/**")
         .authorizeHttpRequests(authz -> authz
                // Endpoints públicos
                .requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/auth/register").permitAll()

                // Endpoints de ADMINISTRADOR
                .requestMatchers(HttpMethod.GET, "/api/monopatines/uso").hasAuthority(AuthorityConstant._ADMIN)
               // .requestMatchers(HttpMethod.PUT, "/api/usuarios/*/anular").hasAuthority(AuthorityConstant._ADMIN)
                //.requestMatchers(HttpMethod.PATCH, "/api/usuarios/*/anular").hasAuthority(AuthorityConstant._ADMIN)
                .requestMatchers(HttpMethod.GET, "/api/viajes/monopatines/mas-viajes").hasAuthority(AuthorityConstant._ADMIN)
                .requestMatchers(HttpMethod.GET, "/api/facturas/total").hasAuthority(AuthorityConstant._ADMIN)
                .requestMatchers(HttpMethod.GET, "/api/usuarios/uso-frecuente").hasAuthority(AuthorityConstant._ADMIN)
                .requestMatchers(HttpMethod.POST, "/api/tarifas/vigente").hasAuthority(AuthorityConstant._ADMIN)


                // Endpoints de USUARIO
                .requestMatchers(HttpMethod.GET, "/api/monopatines/cercanos").hasAuthority(AuthorityConstant._USER)
                .requestMatchers(HttpMethod.GET, "/api/viajes/uso-por-cuenta").hasAuthority(AuthorityConstant._USER)
                .requestMatchers(HttpMethod.GET, "/api/viajes/mis-viajes").hasAuthority(AuthorityConstant._USER)


                // Cualquier otra request requiere autenticación
                .anyRequest().authenticated()
        )
                .httpBasic(Customizer.withDefaults())
                .addFilterBefore(new JwtFilter(this.tokenProvider), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
