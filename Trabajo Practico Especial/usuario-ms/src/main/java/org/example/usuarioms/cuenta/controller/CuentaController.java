package org.example.usuarioms.cuenta.controller;

import org.example.usuarioms.cuenta.entity.Cuenta;
import org.example.usuarioms.cuenta.service.CuentaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cuentas")
public class CuentaController {

    private final CuentaService cuentaService;

    public CuentaController(CuentaService cuentaService) {
        this.cuentaService = cuentaService;
    }

    // Crear cuenta
    @PostMapping
    public ResponseEntity<Cuenta> crearCuenta(@RequestBody Cuenta cuenta) {
        Cuenta creada = cuentaService.insertar(cuenta);
        return ResponseEntity.status(HttpStatus.CREATED).body(creada);
    }

    // Obtener cuenta por id
    @GetMapping("/{id}")
    public ResponseEntity<Cuenta> obtenerCuenta(@PathVariable Long id) {
        // Si la cuenta existe devuelve 200 OK, si no existe devuelve 404 NOT FOUND
        return cuentaService.buscarPorId(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    // Obtener todas las cuentas
    @GetMapping
    public ResponseEntity<List<Cuenta>> obtenerCuentas() {
        List<Cuenta> cuentas = cuentaService.buscarTodos();
        return ResponseEntity.ok(cuentas);
    }

    // Eliminar cuenta por id
    @DeleteMapping("/{id}")
    public ResponseEntity<Cuenta> eliminarCuenta(@PathVariable Long id) {
        cuentaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/descontar")
    public ResponseEntity<?> descontarSaldo(@PathVariable Long id, @RequestParam("monto") Double monto) {
        try {
            Cuenta cuentaActualizada = cuentaService.descontarSaldo(id, monto);
            return ResponseEntity.ok(cuentaActualizada);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //inhabilitar una cuenta por id
    @PutMapping("/{id}/inhabilitar")
    public ResponseEntity<?> anularCuenta(@PathVariable Long id) {
        try {
            Cuenta cuentaActualizada = cuentaService.actualizarEstadoCuenta(id, false);
            return ResponseEntity.ok(cuentaActualizada);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    //habilitar una cuenta por id
    @PutMapping("/{id}/habilitar")
    public ResponseEntity<?> habilitarCuenta(@PathVariable Long id) {
        try {
            Cuenta cuentaActualizada = cuentaService.actualizarEstadoCuenta(id, true);
            return ResponseEntity.ok(cuentaActualizada);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

}
