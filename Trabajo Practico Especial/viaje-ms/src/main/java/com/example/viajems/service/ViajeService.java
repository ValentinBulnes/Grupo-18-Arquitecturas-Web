package com.example.viajems.service;

import java.time.LocalDateTime;
import java.util.List;

import com.example.viajems.DTO.ViajesPorMonopatinDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.viajems.entity.Viaje;
import com.example.viajems.repository.ViajeRepository;
@Service
public class ViajeService {
    @Autowired
    private ViajeRepository viajeRepository;

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
        if (viaje == null) {
            throw new IllegalArgumentException("Viaje no puede ser nulo");
        }
        Long usuarioId = viaje.getUsuarioId();
        String monopatinId = viaje.getMonopatinId();
        if (usuarioId == null) {
            throw new IllegalArgumentException("usuarioId es requerido");
        }
        if (monopatinId == null || monopatinId.isEmpty()) {
            throw new IllegalArgumentException("monopatinId es requerido");
        }

        org.springframework.web.client.RestTemplate rest = new org.springframework.web.client.RestTemplate();
        String usuarioUrl = "http://localhost:3310/usuarios/" + usuarioId;
        String monopatinUrl = "http://localhost:27018/monopatines/" + monopatinId;

        try {
            org.springframework.http.ResponseEntity<String> userResp =
                rest.getForEntity(usuarioUrl, String.class);
            if (!userResp.getStatusCode().is2xxSuccessful()) {
                throw new IllegalArgumentException("Usuario no encontrado: " + usuarioId);
            }

            org.springframework.http.ResponseEntity<String> monoResp =
                rest.getForEntity(monopatinUrl, String.class);
            if (!monoResp.getStatusCode().is2xxSuccessful()) {
                throw new IllegalArgumentException("Monopatin no encontrado: " + monopatinId);
            }
        } catch (org.springframework.web.client.RestClientException e) {
            throw new IllegalArgumentException("Error al verificar usuario/monopatin: " + e.getMessage(), e);
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


}
