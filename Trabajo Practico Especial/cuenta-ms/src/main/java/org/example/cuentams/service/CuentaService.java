package org.example.cuentams.service;

import org.example.cuentams.dto.UsuarioDTO;
import org.example.cuentams.entity.Cuenta;
import org.example.cuentams.feignClient.UsuarioFeignClient;
import org.example.cuentams.repository.CuentaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CuentaService {

    private final CuentaRepository cuentaRepository;
    private final UsuarioFeignClient usuarioFeignClient;

    public CuentaService(CuentaRepository cuentaRepository,  UsuarioFeignClient usuarioFeignClient) {
        this.cuentaRepository = cuentaRepository;
        this.usuarioFeignClient = usuarioFeignClient;
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

    public UsuarioDTO obtenerUsuario(Long id) {
        return usuarioFeignClient.obtenerUsuario(id);
    }

    public Cuenta descontarSaldo(Long id, Double monto) {
        Cuenta cuenta = cuentaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cuenta no encontrada con ID: " + id));

        if (cuenta.getSaldo() < monto) {
            throw new RuntimeException("Saldo insuficiente en la cuenta ID: " + id);
        }

        cuenta.setSaldo(cuenta.getSaldo() - monto);
        return cuentaRepository.save(cuenta);
    }

    //habilitar o deshabilitar cuenta
    public Cuenta actualizarEstadoCuenta(Long id, boolean estado) {
        Cuenta cuenta = cuentaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cuenta no encontrada con ID: " + id));

        cuenta.setActiva(estado);
        return cuentaRepository.save(cuenta);
    }
}
