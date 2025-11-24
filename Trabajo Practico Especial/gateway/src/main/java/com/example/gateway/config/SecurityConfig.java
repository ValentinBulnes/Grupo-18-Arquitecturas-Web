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

                        // 1. PÚBLICO (Auth)
                        .pathMatchers(HttpMethod.POST, "/auth/login", "/auth/register").permitAll()

                        // 2. EXCLUSIVO ADMIN (Gestión de Negocio e Infraestructura)
                        // Reportes
                        .pathMatchers("/monopatines/uso",
                                "/viajes/monopatines/mas-viajes",
                                "/facturas/total",
                                "/usuarios/uso-frecuente").hasAuthority(AuthorityConstant._ADMIN)

                        // Gestión de Tarifas (Crear y Modificar)
                        .pathMatchers(HttpMethod.POST, "/tarifas").hasAuthority(AuthorityConstant._ADMIN)
                        .pathMatchers(HttpMethod.PUT, "/tarifas/**").hasAuthority(AuthorityConstant._ADMIN)
                        .pathMatchers(HttpMethod.DELETE, "/tarifas/**").hasAuthority(AuthorityConstant._ADMIN)

                        // Gestión de Paradas (Solo Admin construye paradas)
                        .pathMatchers(HttpMethod.POST, "/paradas").hasAuthority(AuthorityConstant._ADMIN)
                        .pathMatchers(HttpMethod.PUT, "/paradas/**").hasAuthority(AuthorityConstant._ADMIN)
                        .pathMatchers(HttpMethod.DELETE, "/paradas/**").hasAuthority(AuthorityConstant._ADMIN)

                        // Gestión de Monopatines (Alta y Baja de flota)
                        .pathMatchers(HttpMethod.POST, "/monopatines").hasAuthority(AuthorityConstant._ADMIN)
                        .pathMatchers(HttpMethod.PUT, "/monopatines/**").hasAuthority(AuthorityConstant._ADMIN)
                        .pathMatchers(HttpMethod.DELETE, "/monopatines/**").hasAuthority(AuthorityConstant._ADMIN)

                        // Gestión de Cuentas (Admin puede inhabilitar usuarios)
                        .pathMatchers(HttpMethod.PUT, "/cuentas/*/inhabilitar").hasAuthority(AuthorityConstant._ADMIN)
                        .pathMatchers(HttpMethod.PUT, "/cuentas/*/habilitar").hasAuthority(AuthorityConstant._ADMIN)


                        // 3. EXCLUSIVO USER (Uso del Servicio)
                        // Viajar
                        .pathMatchers(HttpMethod.POST, "/viajes").hasAuthority(AuthorityConstant._USER)
                        .pathMatchers(HttpMethod.PUT, "/viajes/**").hasAuthority(AuthorityConstant._USER)
                        // Consultar flota cercana para viajar
                        .pathMatchers(HttpMethod.GET, "/monopatines/cercanos").hasAuthority(AuthorityConstant._USER)
                        // Consultar sus propios consumos
                        .pathMatchers(HttpMethod.GET, "/viajes/uso-por-cuenta").hasAuthority(AuthorityConstant._USER)

                        // 4. EXCLUSIVO PREMIUM (Servicio de IA)
                        .pathMatchers("/chat/**").hasAuthority(AuthorityConstant._PREMIUM)


                        // 5. COMPARTIDOS (Autenticados)
                        // Ver paradas, ver tarifas vigentes, ver monopatines (lectura)
                        .pathMatchers(HttpMethod.GET, "/paradas/**").authenticated()
                        .pathMatchers(HttpMethod.GET, "/tarifas/**").authenticated()
                        .pathMatchers(HttpMethod.GET, "/monopatines/**").authenticated()

                        // Cualquier otra cosa requiere estar logueado (por seguridad)
                        .anyExchange().authenticated()
                )
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .addFilterAt(jwtFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .build();
    }
}
