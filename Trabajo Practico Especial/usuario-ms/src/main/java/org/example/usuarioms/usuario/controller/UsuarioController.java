package org.example.usuarioms.usuario.controller;

import org.example.usuarioms.usuario.DTO.UsuarioUsoDTO;
import org.example.usuarioms.usuario.entity.Usuario;
import org.example.usuarioms.usuario.service.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    // Crear usuario
    @PostMapping
    public ResponseEntity<Usuario> crearUsuario(@RequestBody Usuario usuario) {
        Usuario creado = usuarioService.insertar(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    // Obtener usuario por id
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> obtenerUsuario(@PathVariable Long id) {
        // Si el usuario existe devuelve 200 OK, si no existe devuelve 404 NOT FOUND
        return usuarioService.buscarPorId(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    // Obtener todos los usuarios
    @GetMapping
    public ResponseEntity<List<Usuario>> obtenerUsuarios() {
        List<Usuario> usuarios = usuarioService.buscarTodos();
        return ResponseEntity.ok(usuarios);
    }

    // Eliminar usuario por id
    @DeleteMapping("/{id}")
    public ResponseEntity<Usuario> eliminarUsuario(@PathVariable Long id) {
        usuarioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    // Modificar usuario
    @PutMapping("/{id}")
    public ResponseEntity<Usuario> actualizarUsuario(@PathVariable Long id, @RequestBody Usuario usuarioDetalles) {
        try {
            Usuario actualizado = usuarioService.actualizar(id, usuarioDetalles);
            return ResponseEntity.ok(actualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/uso-frecuente")
    public ResponseEntity<List<UsuarioUsoDTO>> getUsuariosFrecuentes(
            @RequestParam String desde,
            @RequestParam String hasta,
            @RequestParam String tipo) {

        LocalDate inicio = LocalDate.parse(desde);
        LocalDate fin = LocalDate.parse(hasta);

        List<UsuarioUsoDTO> resultado = usuarioService.obtenerUsuariosFrecuentes(inicio, fin, tipo);
        return ResponseEntity.ok(resultado);

    }
}
