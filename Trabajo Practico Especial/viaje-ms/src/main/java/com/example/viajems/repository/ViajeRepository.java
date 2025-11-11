package com.example.viajems.repository;

import java.time.LocalDateTime;
import java.util.List;

import com.example.viajems.DTO.ViajesPorMonopatinDTO;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.viajems.entity.Viaje;

@Repository
public interface ViajeRepository extends MongoRepository<Viaje, String> {
    List<Viaje> findAllByUsuarioId(Long usuarioId);
    List<Viaje> findAllByUsuarioIdAndMonopatinId(Long usuarioId, String monopatinId);

    @Aggregation(pipeline = {
            // 1. Filtrar por año
            "{ $match: { 'fechaInicio': { $gte: ?0, $lt: ?1 } } }",

            // 2. Agrupar por idMonopatin y contar
            "{ $group: { _id: '$monopatinId', totalViajes: { $sum: 1 } } }",

            // 3. Filtrar los que tienen más de X viajes
            "{ $match: { totalViajes: { $gte: ?2 } } }",

            // 4. Ordenar por cantidad de viajes (descendente)
            "{ $sort: { totalViajes: -1 } }",

            // 5. Proyectar el resultado
            "{ $project: { _id: 0, idMonopatin: '$_id', totalViajes: 1 } }"
    })
    List<ViajesPorMonopatinDTO> findMonopatinesConMasDeXViajesEnAnio(
            LocalDateTime startOfYear,
            LocalDateTime endOfYear,
            int cantidadMinima
    );
}
