package com.example.viajems.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.viajems.entity.Viaje;
import com.example.viajems.service.ViajeService;

@RestController
@RequestMapping("/viajes")
public class ViajeController {

    @Autowired
    private ViajeService viajeService;

    @GetMapping("/{id}")
    public ResponseEntity<Viaje> getById(@PathVariable String id) {
        Viaje viaje = viajeService.findById(id);
        if (viaje == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(viaje);
    }

    @GetMapping
    public ResponseEntity<List<Viaje>> getViajes(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) String monopatinId) {

        List<Viaje> viajes;

        if (userId != null && monopatinId != null) {
            viajes = viajeService.getAllByUserIdAndMonopatinId(userId, monopatinId);
        } else if (userId != null) {
            viajes = viajeService.getAllbyUserId(userId);
        } else {
            viajes = viajeService.getAll();
        }

        return ResponseEntity.ok(viajes);
    }

    @PostMapping
    public ResponseEntity<Viaje> create(@RequestBody Viaje viaje) {
        Viaje created = viajeService.save(viaje);
        return ResponseEntity.status(201).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Viaje> update(@PathVariable String id, @RequestBody Viaje viaje) {
        Viaje existing = viajeService.findById(id);
        if (existing == null) {
            return ResponseEntity.notFound().build();
        }
        viaje.setId(id);
        Viaje updated = viajeService.update(viaje);
        return ResponseEntity.ok(updated);
    }
}
