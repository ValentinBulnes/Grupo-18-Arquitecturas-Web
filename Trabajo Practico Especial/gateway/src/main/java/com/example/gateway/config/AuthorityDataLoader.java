package com.example.gateway.config;

import com.example.gateway.entity.Authority;
import com.example.gateway.repository.AuthorityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthorityDataLoader implements CommandLineRunner {

    private final AuthorityRepository authorityRepository;

    @Override
    public void run(String... args) {

        // ROLE: ADMIN
        if (!authorityRepository.existsById("ADMIN")) {
            authorityRepository.save(new Authority("ADMIN"));
            System.out.println(">>> Rol ADMIN creado");
        }

        // ROLE: USER
        if (!authorityRepository.existsById("USER")) {
            authorityRepository.save(new Authority("USER"));
            System.out.println(">>> Rol USER creado");
        }

        System.out.println(">>> Roles cargados correctamente.");
    }
}
