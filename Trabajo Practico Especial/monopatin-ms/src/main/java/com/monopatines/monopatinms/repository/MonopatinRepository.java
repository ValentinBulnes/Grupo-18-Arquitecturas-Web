package com.monopatines.monopatinms.repository;

import com.monopatines.monopatinms.entity.EstadoMonopatin;
import com.monopatines.monopatinms.entity.Monopatin;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MonopatinRepository extends MongoRepository<Monopatin, String> {

    // Buscar por estado
    List<Monopatin> findByEstado(EstadoMonopatin estado);

    // Buscar por parada actual
    List<Monopatin> findByParadaActualID(long paradaActualID);

    // Buscar por mantenimiento requerido
    List<Monopatin> findByRequiereMantenimiento(boolean requiere);
}
