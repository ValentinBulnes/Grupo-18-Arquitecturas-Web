package com.monopatines.monopatinms.controller;

import com.monopatines.monopatinms.DTO.MonopatinConDistanciaDTO;
import com.monopatines.monopatinms.DTO.MonopatinDTO;
import com.monopatines.monopatinms.DTO.ReporteMonopatinDTO;
import com.monopatines.monopatinms.entity.EstadoMonopatin;
import com.monopatines.monopatinms.entity.Monopatin;
import com.monopatines.monopatinms.service.MonopatinService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/monopatines")
@RequiredArgsConstructor
public class MonopatinController {
    private final MonopatinService monopatinService;



    // endpoint para recuperar todos los monopatines
    @GetMapping("")
   public ResponseEntity<List<MonopatinDTO>> getAll(){
        List<MonopatinDTO> list = monopatinService.findAll();
        if(list.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(list);
    }

    // endpoint para recuperar n monopatin por id
    @GetMapping("/{id}")
    public ResponseEntity<MonopatinDTO> getMonopatinById(@PathVariable("id") String id){
        MonopatinDTO m = monopatinService.obtenerMonopatin(id);
        if (m == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(m);
    }
    // endpoint para dar alta a un monopatin
    @PostMapping("")
    public ResponseEntity<Monopatin> save(@RequestBody Monopatin monopatin){
        Monopatin m= monopatinService.saveMonopatin(monopatin);
        return ResponseEntity.ok(m);
    }

    // Endpoint para buscar monopatines en una parada
    @GetMapping("/parada/{paradaId}")
    public ResponseEntity<List<MonopatinDTO>> getMonopatinesEnParada(@PathVariable("paradaId") long paradaId) {
        List<MonopatinDTO> monopatines = monopatinService.findByParada(paradaId);
        if (monopatines.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(monopatines);
    }

    //  Endpoint para buscar por estado
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<MonopatinDTO>> getMonopatinesByEstado(@PathVariable("estado") EstadoMonopatin estado) {
        List<MonopatinDTO> monopatines = monopatinService.findByEstado(estado);
        if (monopatines.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(monopatines);
    }

    // Endpoint para generar un reporte de uso de monopatines por kilómetros
    @GetMapping("/uso")
    public ResponseEntity<List<ReporteMonopatinDTO>> getReporteUso(
            @RequestParam(defaultValue = "false") boolean incluirPausas) {

        List<ReporteMonopatinDTO> reporte = monopatinService.generarReporteUso(incluirPausas);

        if (reporte.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(reporte);
    }

    // Encontrar monopatines cercanos usando query params (alternativa más simple)
    @GetMapping("/cercanos")
    public ResponseEntity<List<MonopatinConDistanciaDTO>> buscarCercanosQuery(
            @RequestParam Double latitud,
            @RequestParam Double longitud,
            @RequestParam(defaultValue = "5.0") Double radioKm) {
        List<MonopatinConDistanciaDTO> resultado = monopatinService.encontrarMonopatinesCercanos(
                latitud, longitud, radioKm);
        return ResponseEntity.ok(resultado);
    }




}
