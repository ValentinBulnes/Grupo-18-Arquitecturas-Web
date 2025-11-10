package org.example.usuarioms.cuenta.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.usuarioms.usuario.entity.Usuario;

import java.time.LocalDate;
import java.util.List;

@Data // Genera getters y setters, toString, equals, hashcode
@NoArgsConstructor // Genera un constructor vacio
@AllArgsConstructor // Genera un constructor con todos los atributos
@Entity
public class Cuenta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "numero_identificatorio")
    private Long id;

    @Column
    private String tipo; // BÁSICA o PREMIUM

    @Column
    private Double saldo;

    @Column
    private String mercadoPagoId;

    @Column
    private LocalDate fechaAlta;

    @Column
    private boolean activa = true;

   @ManyToMany
   @JoinTable(name = "cuenta_usuario",
           joinColumns = @JoinColumn(name = "cuenta_id"),
           inverseJoinColumns = @JoinColumn(name = "usuario_id"))
   private List<Usuario> usuarios;
}
