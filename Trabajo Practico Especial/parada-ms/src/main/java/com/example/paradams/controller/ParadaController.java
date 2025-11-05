package com.example.paradams.controller;

import com.example.paradams.entity.Parada;
import com.example.paradams.service.ParadaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/paradas")
public class ParadaController {
    @Autowired
    ParadaService paradaService;

    @GetMapping
    public ResponseEntity<List<Parada>> getAllParadas() {
        List<Parada> paradas = paradaService.getAll();
        if (paradas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(paradas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Parada> getParadaById(@PathVariable("id") Long id) {
        Parada parada = paradaService.findById(id);
        if (parada == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(parada);
    }

    @PostMapping("")
    public ResponseEntity<Parada> save(@RequestBody Parada parada) {
        Parada paradaNew = paradaService.save(parada);
        return ResponseEntity.ok(paradaNew);
    }

    @PutMapping("")
    public ResponseEntity<Parada> update(@RequestBody Parada parada) {
        Parada paradaUpdated = paradaService.update(parada);
        return ResponseEntity.ok(paradaUpdated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable("id") Long id) {
        Parada parada = paradaService.findById(id);
        if (parada == null) {
            return ResponseEntity.notFound().build();
        }
        paradaService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/buscar/nombre/{nombre}")
    public ResponseEntity<List<Parada>> getParadasByNombre(@PathVariable("nombre") String nombre) {
        List<Parada> paradas = paradaService.findByNombre(nombre);
        if (paradas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(paradas);
    }

    @GetMapping("/buscar/direccion/{direccion}")
    public ResponseEntity<List<Parada>> getParadasByDireccion(@PathVariable("direccion") String direccion) {
        List<Parada> paradas = paradaService.findByDireccion(direccion);
        if (paradas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(paradas);
    }

}
