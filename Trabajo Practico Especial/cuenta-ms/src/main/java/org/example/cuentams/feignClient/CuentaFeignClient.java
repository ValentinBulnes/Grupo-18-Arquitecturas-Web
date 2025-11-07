package org.example.cuentams.feignClient;

import org.example.cuentams.entity.Cuenta;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "microservicio-cuenta", url = "http://localhost:8002")
@RequestMapping("/cuentas")
    public interface CuentaFeignClient {

    @PostMapping
    Cuenta save(@RequestBody Cuenta cuenta);

    @GetMapping("/{id}")
    Cuenta obtenerCuenta(@PathVariable Long id);


}
