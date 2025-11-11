package com.example.viajems.feignClients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.viajems.DTO.CuentaDTO;

@FeignClient(name = "cuenta-ms", url = "http://localhost:8001")
public interface CuentaFeignClient {

    @GetMapping("/cuentas/{id}")
    CuentaDTO getCuenta(@PathVariable Long id);
}
