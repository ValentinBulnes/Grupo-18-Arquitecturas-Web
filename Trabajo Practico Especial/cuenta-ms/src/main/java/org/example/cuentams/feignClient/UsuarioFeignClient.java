package org.example.cuentams.feignClient;

import org.example.cuentams.dto.UsuarioDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "usuario-ms", url = "http://localhost:8001")
// @RequestMapping("/usuarios")
    public interface UsuarioFeignClient {

//    @PostMapping
//    Cuenta save(@RequestBody Cuenta cuenta);

    @GetMapping("/usuarios/{id}")
    UsuarioDTO obtenerUsuario(@PathVariable Long id);


}
