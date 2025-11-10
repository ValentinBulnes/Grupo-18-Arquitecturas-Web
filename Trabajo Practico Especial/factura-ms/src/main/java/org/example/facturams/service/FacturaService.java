package org.example.facturams.service;

import org.example.facturams.DTOs.CuentaDTO;
import org.example.facturams.DTOs.TarifaDTO;
import org.example.facturams.DTOs.UsuarioDTO;
import org.example.facturams.DTOs.ViajeDTO;
import org.example.facturams.entity.Factura;
import org.example.facturams.feignClients.CuentaFeignClient;
import org.example.facturams.feignClients.TarifaFeignClient;
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
  private final TarifaFeignClient tarifaFeignClient;



    public FacturaService(FacturaRepository facturaRepository, ViajeFeignClient viajeFeignClient, UsuarioFeignClient usuarioFeignClient, CuentaFeignClient cuentaFeignClient, TarifaFeignClient tarifaFeignClient) {
        this.facturaRepository = facturaRepository;
        this.viajeFeignClient = viajeFeignClient;
        this.usuarioFeignClient = usuarioFeignClient;
        this.tarifaFeignClient = tarifaFeignClient;
    }

    //Crear una factura
    public Factura guardar(Factura factura){
        ViajeDTO viaje;
        UsuarioDTO usuario;
        TarifaDTO tarifa = tarifaFeignClient.obtenerTarifaActual();
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

        double monto = viaje.getKilometrosRecorridos() * tarifa.getPrecioPorKm();

        //Agrega tarifa si tuvo una pausa de 15 minutos
        if (viaje.getTotalSegundosPausa() > 900) {
            monto += tarifa.getTarifaExtraPorPausa();
        }

        factura.setMontoTotal(monto);
        factura.setFechaEmision(LocalDate.now());
        factura.setDescripcion("Factura generada por viaje de " + viaje.getKilometrosRecorridos() + " km");
        return facturaRepository.save(factura);
    }

    //Obtener factura por id
    public Factura buscarPorId(Long id) {
        return facturaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Factura no encontrada con ID: " + id));
    }
    // Obtiene todas las facturas
    public List<Factura> listarTodas() {

        return facturaRepository.findAll();
    }

    // Modificar factura
    public Factura actualizarFactura(Long id, Factura nuevaFactura) {
        Factura facturaExistente = facturaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Factura no encontrada con ID: " + id));

        facturaExistente.setFechaEmision(nuevaFactura.getFechaEmision());
        facturaExistente.setMontoTotal(nuevaFactura.getMontoTotal());
        facturaExistente.setDescripcion(nuevaFactura.getDescripcion());
        facturaExistente.setViajeId(nuevaFactura.getViajeId());
        facturaExistente.setCuentaId(nuevaFactura.getCuentaId());
        facturaExistente.setUsuarioId(nuevaFactura.getUsuarioId());

        return facturaRepository.save(facturaExistente);
    }

     // Eliminar factura
    public void eliminarFactura(Long id) {
        if (!facturaRepository.existsById(id)) {
            throw new RuntimeException("Factura no encontrada con ID: " + id);
        }
        facturaRepository.deleteById(id);
    }

    public Double calcularTotalFacturado(int anio, int mesInicio, int mesFin) {
        return facturaRepository.totalFacturadoEnRango(anio, mesInicio, mesFin);
    }
}
