package com.example.administracionms.feign;

import com.example.administracionms.dto.MonopatinDTO;
import com.example.administracionms.dto.ViajeDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "viaje-ms", url = "http://localhost:8007")
public interface ViajeFeignClient {

    @GetMapping("/viajes")
    List<ViajeDTO> getViajesPorMonopatin(@RequestParam("monopatinId") String monopatinId);
}
