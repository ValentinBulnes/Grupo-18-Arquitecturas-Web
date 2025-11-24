package org.example.usuarioms.usuario.feignClient;

import org.example.usuarioms.usuario.DTO.PagoRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "mercadoPagoClient", url = "http://mercadopago-ms:8089")
public interface MercadoPagoClient {
    @GetMapping("/mercadopago/saldo/{cuentaId}")
    Double obtenerSaldo(@PathVariable("cuentaId") Long cuentaId);
    @PostMapping("/mercadopago/pagar")
    Boolean realizarPago(@RequestBody PagoRequest request);
}