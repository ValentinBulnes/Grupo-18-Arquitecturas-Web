package org.example.tarifams.repository;

import org.example.tarifams.entity.Tarifa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface TarifaRepository extends JpaRepository<Tarifa, Long> {
    Tarifa findTopByOrderByFechaInicioVigenciaDesc();

    // punto F
    Optional<Tarifa> findFirstByFechaInicioVigenciaLessThanEqualOrderByFechaInicioVigenciaDesc(LocalDate fecha);
}
