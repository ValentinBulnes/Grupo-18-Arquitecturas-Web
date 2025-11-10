package com.monopatines.monopatinms.feignClients;

import com.monopatines.monopatinms.DTO.ViajeDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "viaje-ms", url = "http://localhost:8007")
public interface ViajeFeignClient {

    @GetMapping("/viajes")
    List<ViajeDTO> getViajesPorMonopatin(@RequestParam("monopatinId") String monopatinId);
}
