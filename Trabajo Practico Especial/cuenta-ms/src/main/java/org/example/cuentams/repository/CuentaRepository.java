package org.example.cuentams.repository;

import org.example.cuentams.entity.Cuenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CuentaRepository extends JpaRepository<Cuenta,Long> {

    // CRUD (ver si van)
    // Insertar cuenta
    Cuenta save(Cuenta cuenta);

    // Obtener cuenta por id
    Optional<Cuenta> findById(Long id);

    // Obtener todas las cuentas
    List<Cuenta> findAll();

    // Eliminar cuenta por id
    void deleteById(Long id);
}
