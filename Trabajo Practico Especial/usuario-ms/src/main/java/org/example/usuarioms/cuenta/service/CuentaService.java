package org.example.usuarioms.cuenta.service;

import jakarta.transaction.Transactional;
import org.example.usuarioms.cuenta.entity.Cuenta;
import org.example.usuarioms.cuenta.repository.CuentaRepository;
import org.example.usuarioms.usuario.entity.Usuario;
import org.example.usuarioms.usuario.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CuentaService {

    private final CuentaRepository cuentaRepository;
    private final UsuarioRepository usuarioRepository;

    public CuentaService(CuentaRepository cuentaRepository, UsuarioRepository usuarioRepository) {
        this.cuentaRepository = cuentaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    // Insertar cuenta
    @Transactional
    public Cuenta insertar(Cuenta cuenta) {
        // 1. Validar si vienen usuarios en el JSON
        if (cuenta.getUsuarios() != null && !cuenta.getUsuarios().isEmpty()) {
            List<Usuario> usuariosReales = new ArrayList<>();

            for (Usuario u : cuenta.getUsuarios()) {
                // 2. Buscamos si el usuario existe realmente en la BD
                if (u.getId() != null) {
                    Usuario usuarioEncontrado = usuarioRepository.findById(u.getId())
                            .orElseThrow(() -> new RuntimeException("Error al crear cuenta: El usuario con ID " + u.getId() + " no existe."));

                    // 3. Agregamos la instancia "real" (gestionada por JPA) a la lista
                    usuariosReales.add(usuarioEncontrado);
                }
            }
            // 4. Reemplazamos la lista "falsa" del JSON por la "real" de la BD
            cuenta.setUsuarios(usuariosReales);
        }
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
