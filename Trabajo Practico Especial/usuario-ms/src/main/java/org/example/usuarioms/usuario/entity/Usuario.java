package org.example.usuarioms.usuario.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.usuarioms.cuenta.entity.Cuenta;

import java.util.List;

@Data // Genera getters y setters, toString, equals, hashcode
@NoArgsConstructor // Genera un constructor vacio
@AllArgsConstructor // Genera un constructor con todos los atributos
@Entity
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String nombre;

    @Column
    private String apellido;

    @Column
    private String email;

    @Column
    private String telefono;

    @Column
    private Double latitud;

    @Column
    private Double longitud;

    @ManyToMany(mappedBy = "usuarios")
    private List<Cuenta> cuentas;

}
