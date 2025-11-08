package com.example.viajems.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.viajems.entity.Viaje;

@Repository
public interface ViajeRepository extends MongoRepository<Viaje, String> {
    List<Viaje> findAllByUsuarioId(String usuarioId);
    List<Viaje> findAllByUsuarioIdAndMonopatinId(String usuarioId, String monopatinId);
}
