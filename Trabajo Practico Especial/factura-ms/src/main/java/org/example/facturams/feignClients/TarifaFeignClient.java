package org.example.facturams.feignClients;

import org.example.facturams.DTOs.TarifaDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "tarifa-ms", url = "http://localhost:8004")
public interface TarifaFeignClient {
    @GetMapping("/tarifas/actual")
    TarifaDTO obtenerTarifaActual();
}
