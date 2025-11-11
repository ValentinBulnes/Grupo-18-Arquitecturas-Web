package org.example.tarifams.controller;

import org.example.tarifams.dto.TarifaDTO;
import org.example.tarifams.entity.Tarifa;
import org.example.tarifams.service.TarifaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tarifas")
public class TarifaController {

    private final TarifaService tarifaService;

    public TarifaController(TarifaService tarifaService) {
        this.tarifaService = tarifaService;
    }

    //  Crear nueva tarifa
    @PostMapping
    public ResponseEntity<Tarifa> crearTarifa(@RequestBody Tarifa tarifa) {
        return ResponseEntity.ok(tarifaService.crearTarifa(tarifa));
    }

    //  Listar todas las tarifas
    @GetMapping
    public ResponseEntity<List<Tarifa>> listarTarifas() {
        return ResponseEntity.ok(tarifaService.listarTodas());
    }

    //  Obtener tarifa por ID
    @GetMapping("/{id}")
    public ResponseEntity<Tarifa> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(tarifaService.buscarPorId(id));
    }

    //  Obtener tarifa actual (la más reciente)
    @GetMapping("/actual")
    public ResponseEntity<Tarifa> obtenerTarifaActual() {
        return ResponseEntity.ok(tarifaService.obtenerTarifaActual());
    }

    //  Modificar tarifa
    @PutMapping("/{id}")
    public ResponseEntity<Tarifa> actualizarTarifa(@PathVariable Long id, @RequestBody Tarifa nuevaTarifa) {
        return ResponseEntity.ok(tarifaService.actualizarTarifa(id, nuevaTarifa));
    }

    //  Eliminar tarifa
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarTarifa(@PathVariable Long id) {
        tarifaService.eliminarTarifa(id);
        return ResponseEntity.noContent().build();
    }

    // punto F
    @GetMapping("/vigente")
    public ResponseEntity<TarifaDTO> obtenerTarifaVigente() {
        try {
            TarifaDTO dto = tarifaService.obtenerTarifaVigente();
            return ResponseEntity.ok(dto);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
