package org.example.usuarioms.usuario.service;

import org.example.usuarioms.usuario.entity.Usuario;
import org.example.usuarioms.usuario.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
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

}
