package org.example.usuarioms.usuario.feignClient;

import org.example.usuarioms.usuario.DTO.ViajeDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "viaje-ms", url = "http://localhost:8007")
public interface ViajeFeignClient {

    @GetMapping("/viajes/por-periodo")
    List<ViajeDTO> getViajesPorPeriodo(@RequestParam String fechaDesde, @RequestParam String fechaHasta);
}