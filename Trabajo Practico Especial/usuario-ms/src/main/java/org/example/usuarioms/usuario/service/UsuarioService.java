package org.example.usuarioms.usuario.service;

import jakarta.persistence.Access;
import org.example.usuarioms.cuenta.entity.Cuenta;
import org.example.usuarioms.usuario.DTO.PagoRequest;
import org.example.usuarioms.usuario.DTO.UsuarioUsoDTO;
import org.example.usuarioms.usuario.DTO.ViajeDTO;
import org.example.usuarioms.usuario.entity.Usuario;
import org.example.usuarioms.usuario.feignClient.MercadoPagoClient;
import org.example.usuarioms.usuario.feignClient.ViajeFeignClient;
import org.example.usuarioms.usuario.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    private  ViajeFeignClient  viajeFeignClient;
    private final MercadoPagoClient mercadoPagoClient;

    public UsuarioService(UsuarioRepository usuarioRepository, ViajeFeignClient viajeFeignClient, MercadoPagoClient mercadoPagoClient) {
        this.usuarioRepository = usuarioRepository;
        this.viajeFeignClient = viajeFeignClient;
        this.mercadoPagoClient = mercadoPagoClient;
    }

    // Insertar usuario
    public Usuario insertar(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    // Buscar usuario por id
    public Optional<Usuario> buscarPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    // Buscar todos los usuarios
    public List<Usuario> buscarTodos() {
        return usuarioRepository.findAll();
    }

    // Eliminar usuario por id
    public void eliminar(Long id) {
        usuarioRepository.deleteById(id);
    }

    // Actualizar (Modificar) usuario
    public Usuario actualizar(Long id, Usuario usuarioDetalles) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));

        usuario.setNombre(usuarioDetalles.getNombre());
        usuario.setApellido(usuarioDetalles.getApellido());
        usuario.setEmail(usuarioDetalles.getEmail());
        usuario.setTelefono(usuarioDetalles.getTelefono());
        usuario.setLatitud(usuarioDetalles.getLatitud());
        usuario.setLongitud(usuarioDetalles.getLongitud());

        return usuarioRepository.save(usuario);
    }
    //Punto e
    public List<UsuarioUsoDTO> obtenerUsuariosFrecuentes(LocalDate desde, LocalDate hasta, String tipoUsuario) {

        List<Usuario> todos = usuarioRepository.findAll();

        //  Filtrar por tipo de cuenta (premium o basico)
        List<Usuario> filtrados = new ArrayList<>();
        for (Usuario u : todos) {
            if (u.getCuentas() != null) {
                for (Cuenta c : u.getCuentas()) {
                    if (c.getTipo().equalsIgnoreCase(tipoUsuario)) {
                        filtrados.add(u);
                        break;
                    }
                }
            }
        }

        List<ViajeDTO> viajes = new ArrayList<>();
        try {
            viajes = viajeFeignClient.getViajesPorPeriodo(desde.toString(), hasta.toString());
        } catch (Exception e) {
            System.out.println("Error al conectar con viaje-ms: " + e.getMessage());
        }



        List<UsuarioUsoDTO> resultado = new ArrayList<>();
        for (Usuario usuario : filtrados) {
            int cantidad = 0;
            for (ViajeDTO viaje : viajes) {
                if (viaje.getUsuarioId().equals(usuario.getId())) {
                    cantidad++;
                }
            }

            UsuarioUsoDTO dto = new UsuarioUsoDTO();
            dto.setUsuarioId(usuario.getId());
            dto.setNombre(usuario.getNombre());
            dto.setTipoUsuario(tipoUsuario);
            dto.setCantidadViajes(cantidad);

            resultado.add(dto);
        }


        resultado.sort((a, b) -> Integer.compare(b.getCantidadViajes(), a.getCantidadViajes()));

        return resultado;
    }
    public boolean realizarPago(Long cuentaId, Double monto) {
        PagoRequest request = new PagoRequest();
        request.setCuentaId(cuentaId);
        request.setMonto(monto);

        return mercadoPagoClient.realizarPago(request);
    }

}
