package org.example.facturams.service;

import org.example.facturams.DTOs.CuentaDTO;
import org.example.facturams.DTOs.UsuarioDTO;
import org.example.facturams.DTOs.ViajeDTO;
import org.example.facturams.entity.Factura;
import org.example.facturams.feignClients.CuentaFeignClient;
import org.example.facturams.feignClients.UsuarioFeignClient;
import org.example.facturams.feignClients.ViajeFeignClient;
import org.example.facturams.repository.FacturaRepository;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.util.List;

@Service
public class FacturaService {

  private final FacturaRepository facturaRepository;
  private final ViajeFeignClient viajeFeignClient;
  private final UsuarioFeignClient usuarioFeignClient;
  private final CuentaFeignClient cuentaFeignClient;


    public FacturaService(FacturaRepository facturaRepository, ViajeFeignClient viajeFeignClient, UsuarioFeignClient usuarioFeignClient, CuentaFeignClient cuentaFeignClient) {
        this.facturaRepository = facturaRepository;
        this.viajeFeignClient = viajeFeignClient;
        this.usuarioFeignClient = usuarioFeignClient;
        this.cuentaFeignClient = cuentaFeignClient;
    }
    public Factura guardar(Factura factura){
        ViajeDTO viaje;
        UsuarioDTO usuario;
        CuentaDTO cuenta;
        try {
            viaje = viajeFeignClient.findById(factura.getViajeId());
        } catch (Exception e) {
            throw new RuntimeException("No se pudo obtener el viaje: " + e.getMessage());
        }
        try {
           usuario = usuarioFeignClient.findById( factura.getUsuarioId());
        } catch (Exception e) {
            throw new RuntimeException("No se pudo obtener el usuario: " + e.getMessage());
        }
        try {
            cuenta = cuentaFeignClient.findById(factura.getCuentaId());
        } catch (Exception e) {
            throw new RuntimeException("No se pudo obtener la cuenta: " + e.getMessage());
        }


        double tarifaPorKm = 100.0;
        double tarifaExtraPorPausa = 200.0;
        double monto = viaje.getDistancia() * tarifaPorKm;
        if (viaje.getTotalSegundosPausa() > 900) {
            monto += tarifaExtraPorPausa;
        }

        if ("premium".equalsIgnoreCase(cuenta.getTipoCuenta())) {
            if (viaje.getDistancia() <= 100) {
                monto = 0.0;
            } else {
                monto = (viaje.getDistancia() - 100) * tarifaPorKm * 0.5;
            }
        }
        if ("prepaga".equalsIgnoreCase(cuenta.getTipoCuenta())) {

            if (cuenta.getSaldo() < monto) {
                throw new RuntimeException("Saldo insuficiente en la cuenta.");
            }else {
                cuentaFeignClient.descontarSaldo(cuenta.getId(), monto);
            }

        }
        factura.setMontoTotal(monto);
        factura.setFechaEmision(LocalDate.now());
        factura.setDescripcion("Factura generada por viaje de " + viaje.getDistancia() + " km");
        return facturaRepository.save(factura);
    }
    public Factura buscarPorId(Long id) {
        return facturaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Factura no encontrada con ID: " + id));
    }

    public List<Factura> listarTodas() {

        return facturaRepository.findAll();
    }
}
