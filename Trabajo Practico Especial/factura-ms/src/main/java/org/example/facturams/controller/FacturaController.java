package org.example.facturams.controller;

import org.example.facturams.entity.Factura;
import org.example.facturams.repository.FacturaRepository;
import org.example.facturams.service.FacturaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/facturas")
public class FacturaController {

    private final FacturaService facturaService;


    public FacturaController(FacturaService facturaService) {
        this.facturaService = facturaService;

    }
    // Agregar factura
    @PostMapping
    public ResponseEntity<Factura> crearFactura(@RequestBody Factura factura) {
        return ResponseEntity.ok(facturaService.guardar(factura));
    }

    // Obtener todas las facturas
    @GetMapping
    public ResponseEntity<List<Factura>> obtenerFacturas() {
        return ResponseEntity.ok(facturaService.listarTodas());
    }

    //Obtener factura por id
    @GetMapping("/{id}")
    public ResponseEntity<Factura> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(facturaService.buscarPorId(id));
    }

    //Modificar Factura
    @PutMapping("/{id}")
    public ResponseEntity<Factura> actualizarFactura(@PathVariable Long id, @RequestBody Factura factura) {
        return ResponseEntity.ok(facturaService.actualizarFactura(id, factura));
    }

    //Eliminar factura
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarFactura(@PathVariable Long id) {
        facturaService.eliminarFactura(id);
        return ResponseEntity.noContent().build();
    }
}
