package com.example.paradams.repository;

import com.example.paradams.entity.Parada;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParadaRepository extends JpaRepository<Parada, Long> {
    List<Parada> findByNombre(String nombre);
    List<Parada> findByDireccion(String direccion);
}
