package com.example.viajems.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.example.viajems.DTO.CuentaDTO;
import com.example.viajems.DTO.UsuarioDTO;
import com.example.viajems.DTO.ViajesPorMonopatinDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.viajems.entity.Viaje;
import com.example.viajems.feignClients.CuentaFeignClient;
import com.example.viajems.feignClients.MonopatinFeignClient;
import com.example.viajems.feignClients.UsuarioFeignClient;
import com.example.viajems.repository.ViajeRepository;

@Service
public class ViajeService {

    @Autowired
    private ViajeRepository viajeRepository;

    @Autowired
    private UsuarioFeignClient usuarioClient;
    @Autowired
    private MonopatinFeignClient monopatinClient;
    @Autowired
    private CuentaFeignClient cuentaClient;

    public Viaje findById(String id) {
        return viajeRepository.findById(id).orElse(null);
    }

    public List<Viaje> getAll() {
        return viajeRepository.findAll();
    }

    public List<Viaje> getAllbyUserId(Long userId) {
        return viajeRepository.findAllByUsuarioId(userId);
    }

    public List<Viaje> getAllByUserIdAndMonopatinId(Long userId, String monopatinId) {
        return viajeRepository.findAllByUsuarioIdAndMonopatinId(userId, monopatinId);
    }

    public Viaje save(Viaje viaje) {

        if (viaje == null)
            throw new IllegalArgumentException("Viaje no puede ser nulo");

        if (viaje.getUsuarioId() == null)
            throw new IllegalArgumentException("usuarioId es requerido");

        if (viaje.getMonopatinId() == null || viaje.getMonopatinId().isBlank())
            throw new IllegalArgumentException("monopatinId es requerido");

        try {
            usuarioClient.getUsuario(viaje.getUsuarioId());
        } catch (Exception e) {
            throw new IllegalArgumentException("Usuario no existe: " + viaje.getUsuarioId());
        }

        try {
            monopatinClient.getMonopatin(viaje.getMonopatinId());
        } catch (Exception e) {
            throw new IllegalArgumentException("Monopatín no existe: " + viaje.getMonopatinId());
        }

        return viajeRepository.save(viaje);
    }

    public Viaje update(Viaje viaje) {
        return viajeRepository.save(viaje);
    }

    public List<ViajesPorMonopatinDTO> getMonopatinesConMasDeXViajesEnAnio(int anio, int cantidadMinima) {
        LocalDateTime startOfYear = LocalDateTime.of(anio, 1, 1, 0, 0);
        LocalDateTime endOfYear = LocalDateTime.of(anio + 1, 1, 1, 0, 0);

        return viajeRepository.findMonopatinesConMasDeXViajesEnAnio(startOfYear, endOfYear, cantidadMinima);
    }

    public Map<String, Object> getUsoPorCuenta(Long cuentaId, Long usuarioId, LocalDate desde, LocalDate hasta,
            boolean incluirUsuariosRelacionados) {
        CuentaDTO cuenta = cuentaClient.getCuenta(cuentaId);
        if (cuenta == null)
            return null;

        List<Long> usuariosIds = cuenta.getUsuarios()
                .stream()
                .map(UsuarioDTO::getId)
                .toList();

        if (!incluirUsuariosRelacionados) {
            usuariosIds = List.of(usuarioId); 
        }

        LocalDateTime inicio = desde.atStartOfDay();
        LocalDateTime fin = hasta.atTime(23, 59, 59);

        List<Viaje> viajes = viajeRepository
                .findAllByUsuarioIdInAndFechaInicioBetween(usuariosIds, inicio, fin);

        // Calcular totales
        double km = viajes.stream()
                .mapToDouble(v -> v.getKilometrosRecorridos() != null ? v.getKilometrosRecorridos() : 0.0)
                .sum();

        long minutos = viajes.stream()
                .mapToLong(v -> {
                    if (v.getFechaInicio() == null || v.getFechaFin() == null)
                        return 0;

                    long totalSegundos = java.time.Duration.between(
                            v.getFechaInicio(), v.getFechaFin()).getSeconds();

                    long segundosReales = totalSegundos
                            - (v.getTotalSegundosPausa() != null ? v.getTotalSegundosPausa() : 0);

                    return segundosReales / 60;
                })
                .sum();

        Map<String, Object> resp = new HashMap<>();
        resp.put("cuentaId", cuentaId);
        resp.put("usuariosIds", usuariosIds);
        resp.put("totalKm", km);
        resp.put("totalMinutos", minutos);
        resp.put("cantidadViajes", viajes.size());

        return resp;
    }

}
