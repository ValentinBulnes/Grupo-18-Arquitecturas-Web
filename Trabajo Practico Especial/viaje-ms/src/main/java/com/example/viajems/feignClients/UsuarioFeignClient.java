package com.example.viajems.feignClients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.viajems.DTO.UsuarioDTO;

// URI directa (sin Eureka)
@FeignClient(name = "usuarios-ms", url = "http://localhost:8001")
public interface UsuarioFeignClient {

    @GetMapping("/usuarios/{id}")
    UsuarioDTO getUsuario(@PathVariable("id") Long id);
}
