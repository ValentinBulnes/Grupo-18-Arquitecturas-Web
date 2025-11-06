package com.example.viajems.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.viajems.entity.Viaje;

@Repository
public interface ViajeRepository extends JpaRepository<Viaje, Long> {
    List<Viaje> findAllByUsuarioId(Long usuarioId);
    List<Viaje> findAllByUsuarioIdAndMonopatinId(Long usuarioId, Long monopatinId);
}
