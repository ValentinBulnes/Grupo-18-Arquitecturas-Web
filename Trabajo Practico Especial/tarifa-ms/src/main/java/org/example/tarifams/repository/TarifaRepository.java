package org.example.tarifams.repository;

import org.example.tarifams.entity.Tarifa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TarifaRepository extends JpaRepository<Tarifa, Long> {
    Tarifa findTopByOrderByFechaInicioVigenciaDesc();
}
