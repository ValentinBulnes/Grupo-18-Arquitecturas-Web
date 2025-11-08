package org.example.usuarioms.feignClient;

import org.example.usuarioms.entity.Usuario;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "microservicio-usuario", url = "http://localhost:8001/usuarios")
public interface UsuarioFeignClient {

    @PostMapping
    Usuario save(@RequestBody Usuario usuario);

    @GetMapping("/usuarios/{id}")
    Usuario obtenerUsuario(@PathVariable Long id);
}
