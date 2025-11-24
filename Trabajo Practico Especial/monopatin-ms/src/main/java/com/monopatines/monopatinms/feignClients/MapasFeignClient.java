package com.monopatines.monopatinms.feignClients;


import com.monopatines.monopatinms.DTO.MonopatinDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "mapasClient", url = "http://mapas-ms:8090")
public interface MapasFeignClient{
    @GetMapping("/mapas/monopatines-cercanos")
    List<MonopatinDTO> buscarCercanos(@RequestParam("lat") double lat, @RequestParam("lon") double lon);
}
