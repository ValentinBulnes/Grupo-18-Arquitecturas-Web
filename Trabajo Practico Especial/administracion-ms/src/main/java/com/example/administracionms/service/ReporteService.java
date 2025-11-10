package com.example.administracionms.service;

import com.example.administracionms.dto.MonopatinDTO;
import com.example.administracionms.dto.ReporteMonopatinDTO;
import com.example.administracionms.dto.ViajeDTO;
import com.example.administracionms.feign.MonopatinFeignClient;
import com.example.administracionms.feign.ViajeFeignClient;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReporteService {
    private final MonopatinFeignClient monopatinFeignClient;
    private final ViajeFeignClient viajeFeignClient;

    // Constante para el umbral de mantenimiento
    private static final double KILOMETROS_PARA_MANTENIMIENTO = 1000.0;

    public ReporteService(MonopatinFeignClient monopatinFeignClient, ViajeFeignClient viajeFeignClient) {
        this.monopatinFeignClient = monopatinFeignClient;
        this.viajeFeignClient = viajeFeignClient;
    }

    public List<ReporteMonopatinDTO> generarReporteUso(boolean incluirPausas) {
        // 1. Obtener todos los monopatines
        List<MonopatinDTO> monopatines = monopatinFeignClient.getAll();
        List<ReporteMonopatinDTO> reporte = new ArrayList<>();

        // 2. Por cada monopatín, buscar sus viajes
        for (MonopatinDTO monopatin : monopatines) {
            List<ViajeDTO> viajes = viajeFeignClient.getViajesPorMonopatin(monopatin.getId());

            // 3. Sumar los kilómetros
            // Usamos un stream para sumar todos los km de la lista de viajes
            double kmTotales = viajes.stream()
                    .mapToDouble(ViajeDTO::getKilometrosRecorridos)
                    .sum();

            // 4. Crear el DTO de reporte básico
            ReporteMonopatinDTO reporteDTO = new ReporteMonopatinDTO();
            reporteDTO.setMonopatinId(monopatin.getId());
            reporteDTO.setKilometrosTotales(kmTotales);

            // 5. Verificar si requiere mantenimiento
            // Usamos la lógica de negocio de 1000km
            if (kmTotales > KILOMETROS_PARA_MANTENIMIENTO) {
                reporteDTO.setRequiereMantenimiento(true);
            } else {
                reporteDTO.setRequiereMantenimiento(monopatin.isRequiereMantenimiento()); // Mantenemos el flag actual
            }

            // 6. (Configurable) Sumar tiempos de pausa si se solicitó
            if (incluirPausas) {
                long tiempoPausaTotal = viajes.stream()
                        .mapToLong(ViajeDTO::getTotalSegundosPausa)
                        .sum();
                reporteDTO.setTiempoTotalPausasSeg(tiempoPausaTotal);
            }

            reporte.add(reporteDTO);
        }
        return reporte;
    }
}
