package org.example.usuarioms.usuario.DTO;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ViajeDTO {
    private Long usuarioId;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
}
