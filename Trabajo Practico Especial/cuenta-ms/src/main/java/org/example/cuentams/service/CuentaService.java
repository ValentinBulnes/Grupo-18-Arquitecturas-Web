package org.example.cuentams.service;

import org.example.cuentams.entity.Cuenta;
import org.example.cuentams.repository.CuentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CuentaService {

    private final CuentaRepository cuentaRepository;

    public CuentaService(CuentaRepository cuentaRepository) {
        this.cuentaRepository = cuentaRepository;
    }

    // Insertar cuenta
    public Cuenta insertar(Cuenta cuenta) {
        return cuentaRepository.save(cuenta);
    }

    // Obtener cuenta por id
    public Optional<Cuenta> buscarPorId(Long id) {
        return cuentaRepository.findById(id);
    }

    // Obtener todas las cuentas
    public List<Cuenta> buscarTodos() {
        return cuentaRepository.findAll();
    }

    // Eliminar cuenta por id
    public void eliminar(Long id) {
        cuentaRepository.deleteById(id);
    }
}
