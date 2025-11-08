package org.example.facturams.controller;

import org.example.facturams.entity.Factura;
import org.example.facturams.service.FacturaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Facturas")
public class FacturaController {

    private final FacturaService facturaService;

    public FacturaController(FacturaService facturaService) {
        this.facturaService = facturaService;
    }
    @PostMapping
    public ResponseEntity<Factura> crearFactura(@RequestBody Factura factura) {
        return ResponseEntity.ok(facturaService.guardar(factura));
    }

    @GetMapping
    public ResponseEntity<List<Factura>> obtenerFacturas() {
        return ResponseEntity.ok(facturaService.listarTodas());
    }
    @GetMapping("/{id}")
    public ResponseEntity<Factura> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(facturaService.buscarPorId(id));
    }
}
