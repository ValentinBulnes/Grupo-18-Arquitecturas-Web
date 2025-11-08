package org.example.facturams.feignClients;


import org.example.facturams.DTOs.UsuarioDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "usuario-ms", url = "http://localhost:8001")
public interface UsuarioFeignClient {
    @GetMapping("/usuarios/{id}")
    UsuarioDTO findById(@PathVariable Long id);

}