package com.example.viajems.service;

import java.time.LocalDateTime;
import java.util.List;

import com.example.viajems.DTO.ViajesPorMonopatinDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.viajems.entity.Viaje;
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
}
