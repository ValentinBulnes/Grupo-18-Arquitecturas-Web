package org.example.facturams.feignClients;

import org.example.facturams.DTOs.CuentaDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "cuenta-ms", url = "http://localhost:8002")
public interface CuentaFeignClient {

    @GetMapping("/cuentas/{id}")
    CuentaDTO findById(@PathVariable Long id);

    @PutMapping("/cuentas/{id}/descontar")
    CuentaDTO descontarSaldo(@PathVariable Long id, @RequestParam("monto") Double monto);
}