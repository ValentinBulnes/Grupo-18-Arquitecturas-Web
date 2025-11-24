package org.example.mapasms.controller;

import org.example.mapasms.DTO.MonopatinDTO;
import org.example.mapasms.entity.EstadoMonopatin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@RestController
@RequestMapping("/mapas")
public class MockMapasController {
    @GetMapping("/monopatines-cercanos")
    public List<MonopatinDTO> buscarCercanos(@RequestParam double lat, @RequestParam double lon) {
        return List.of(
                new MonopatinDTO("1", EstadoMonopatin.DISPONIBLE, 0.0, 0L, 1L, null, null, false, lat + 0.001, lon + 0.001),
                new MonopatinDTO("2", EstadoMonopatin.EN_USO, 0.0, 0L, 2L, null, null, false, lat + 0.002, lon + 0.002)
        );
    }

}
