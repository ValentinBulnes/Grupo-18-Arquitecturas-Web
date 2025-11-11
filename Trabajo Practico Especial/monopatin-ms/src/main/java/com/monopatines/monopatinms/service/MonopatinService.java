package com.monopatines.monopatinms.service;

import com.monopatines.monopatinms.DTO.*;
import com.monopatines.monopatinms.feignClients.ParadaFeignClient;
import com.monopatines.monopatinms.entity.EstadoMonopatin;
import com.monopatines.monopatinms.entity.Monopatin;
import com.monopatines.monopatinms.feignClients.ViajeFeignClient;
import com.monopatines.monopatinms.repository.MonopatinRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MonopatinService {
    private final MonopatinRepository monopatinRepository;
    private final ParadaFeignClient paradaFeignClient;
    private final ViajeFeignClient viajeFeignClient;

    // recuperar todos
    public List<MonopatinDTO> findAll() {
        return monopatinRepository.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    // recuperar uno
    public MonopatinDTO obtenerMonopatin(String id) {
        Optional<Monopatin> monopatinOpt = monopatinRepository.findById(id);
        return monopatinOpt.map(this::convertirADTO).orElse(null);
    }

    // eliminar uno
    public void eliminarMonopatin(String id) {
       monopatinRepository.deleteById(id);
    }

    // recuperar por estado
    public List<MonopatinDTO> findByEstado(EstadoMonopatin estado) {
        return monopatinRepository.findByEstado(estado).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    // insertar monopatin
    public Monopatin saveMonopatin(Monopatin monopatin) {
        Monopatin m;
        validarParada(monopatin.getParadaActualID());
        m=  monopatinRepository.save(monopatin);
        return m;
    }

    // update monopatin
    public Monopatin updateMonopatin(Monopatin monopatin) {
        Monopatin m;
        validarParada(monopatin.getParadaActualID());
        m= monopatinRepository.save(monopatin);
        return m;
    }
    // encontrar monopatines en determinada parada
    public List<MonopatinDTO> findByParada(long idParada) {
        ParadaDTO parada = paradaFeignClient.findById(idParada);
        if (parada != null) {
            return monopatinRepository.findByParadaActualID(idParada).stream()
                    .map(this::convertirADTO)
                    .collect(Collectors.toList());
        }
        return null;
    }

    public List<ReporteMonopatinDTO> generarReporteUso(boolean incluirPausas) {
        List<Monopatin> monopatines = monopatinRepository.findAll();
        List<ReporteMonopatinDTO> reporte = new ArrayList<>();

        for (Monopatin monopatin : monopatines) {
            List<ViajeDTO> viajes = viajeFeignClient.getViajesPorMonopatin(monopatin.getId());

            // Calcular km totales desde los viajes
            double kmTotales = viajes.stream()
                    .mapToDouble(ViajeDTO::getKilometrosRecorridos)
                    .sum();

            // Actualizar el monopatín con los km recorridos
            monopatin.setKmTotales(kmTotales); // Usa set, no suma

            // Verificar si requiere mantenimiento (según tu lógica de negocio)
            monopatin.actualizarEstadisticas(kmTotales);
            monopatinRepository.save(monopatin);

            // Persistir cambios si es necesario
            monopatinRepository.save(monopatin);

            // Crear el DTO de reporte
            ReporteMonopatinDTO reporteDTO = new ReporteMonopatinDTO();
            reporteDTO.setMonopatinId(monopatin.getId());
            reporteDTO.setKilometrosTotales(kmTotales);
            reporteDTO.setRequiereMantenimiento(monopatin.isRequiereMantenimiento());

            // Incluir tiempos de pausa si se solicita
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
    // Encontrar monopatines cercanos a una ubicación
    public List<MonopatinConDistanciaDTO> encontrarMonopatinesCercanos(
            Double latitud, Double longitud, Double radioKm) {

        // Calcular bounding box aproximado basado en el radio
        // 1 grado de latitud ≈ 111 km
        // 1 grado de longitud ≈ 111 km * cos(latitud)
        Double deltaLat = radioKm / 111.0;
        Double deltaLon = radioKm / (111.0 * Math.cos(Math.toRadians(latitud)));

        Double minLat = latitud - deltaLat;
        Double maxLat = latitud + deltaLat;
        Double minLon = longitud - deltaLon;
        Double maxLon = longitud + deltaLon;

        // Obtener monopatines disponibles en el área usando la query
        List<Monopatin> monopatines = monopatinRepository.findMonopatinesDisponiblesEnArea(
                EstadoMonopatin.DISPONIBLE, minLat, maxLat, minLon, maxLon);

        // Calcular distancia exacta y filtrar por radio (el bounding box es aproximado)
        List<MonopatinConDistanciaDTO> resultado = new ArrayList<>();

        for (Monopatin monopatin : monopatines) {
            if (monopatin.getLatitud() != null && monopatin.getLongitud() != null
                    && !monopatin.isRequiereMantenimiento()) {
                Double distancia = monopatin.calcularDistanciaA(latitud, longitud);

                if (distancia != null && distancia <= radioKm) {
                    MonopatinConDistanciaDTO dto = new MonopatinConDistanciaDTO(
                            monopatin.getId(),
                            monopatin.getEstado().toString(),
                            monopatin.getLatitud(),
                            monopatin.getLongitud(),
                            distancia,
                            monopatin.getParadaActualID(),
                            monopatin.isRequiereMantenimiento()
                    );
                    resultado.add(dto);
                }
            }
        }

        // Ordenar por distancia (más cercano primero)
        resultado.sort(Comparator.comparing(MonopatinConDistanciaDTO::getDistanciaKm));

        return resultado;
    }

    //metodo auxiliar para convertir a dto
    private MonopatinDTO convertirADTO(Monopatin monopatin) {
        MonopatinDTO dto = new MonopatinDTO();

        dto.setId(monopatin.getId());
        dto.setEstado(monopatin.getEstado());
        dto.setKmTotales(monopatin.getKmTotales());
        dto.setTiempoUsoTotal(monopatin.getTiempoUsoTotal());
        dto.setLongitud(monopatin.getLongitud());
        dto.setLatitud(monopatin.getLatitud());
        dto.setFechaAlta(monopatin.getFechaAlta());
        dto.setUltimoMantenimiento(monopatin.getUltimoMantenimiento());
        dto.setRequiereMantenimiento(monopatin.isRequiereMantenimiento());
        return dto;
    }

    private void validarParada(Long paradaId) {
        if (paradaId == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El ID de la parada no puede ser nulo."
            );
        }

        try {
            ParadaDTO parada = paradaFeignClient.findById(paradaId);
            if (parada == null) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "La parada con ID " + paradaId + " no existe."
                );
            }
        } catch (feign.FeignException.NotFound e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "La parada con ID " + paradaId + " no existe."
            );
        } catch (feign.FeignException e) {
            throw new ResponseStatusException(
                    HttpStatus.SERVICE_UNAVAILABLE,
                    "Error al comunicarse con el servicio de paradas."
            );
        }
    }
}
