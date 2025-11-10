package org.example.facturams.feignClients;


import org.example.facturams.DTOs.ViajeDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "viaje-ms", url = "http://localhost:8007")
public interface ViajeFeignClient {

    @GetMapping("/viajes/{id}")
    ViajeDTO findById(@PathVariable String id);
}
