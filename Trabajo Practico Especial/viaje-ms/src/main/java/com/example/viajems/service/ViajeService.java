package com.example.viajems.service;

import java.util.List;

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
        Viaje viajeNew;
        viajeNew = viajeRepository.save(viaje);
        return viajeNew;
    }

    public Viaje update(Viaje viaje) {
        return viajeRepository.save(viaje);
    }
}
