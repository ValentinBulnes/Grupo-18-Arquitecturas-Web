package org.example.mercadopagoms.controller;
import org.example.mercadopagoms.DTO.PagoRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/mercadopago")
public class MockMercadoPagoController {

    @GetMapping("/saldo/{cuentaId}")
    public Double obtenerSaldo(@PathVariable Long cuentaId) {
        return 1000.0; // Simulación
    }

    @PostMapping("/pagar")
    public Boolean realizarPago(@RequestBody PagoRequest request) {
        return true; // Simulación
    }
}
