package org.example.usuarioms.usuario.feignClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "viaje-ms", url = "http://localhost:8005")
public interface ViajeFeignClient {

    @GetMapping("/viajes")
    List<ViajeDTO> getViajesPorPeriodo(@RequestParam String fechaDesde, @RequestParam String fechaHasta);
}