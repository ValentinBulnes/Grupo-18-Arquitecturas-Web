package org.example.usuarioms.repository;

import org.example.usuarioms.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // CRUD (ver si va)
    // Insertar
    Usuario save(Usuario usuario);

    // Obtener por id
    Optional<Usuario> findById(Long id);

    // Obtener todos
    List<Usuario> findAll();

    // Eliminar usuario por id
    void deleteById(Long id);
}
