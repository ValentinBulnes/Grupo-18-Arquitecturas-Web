package org.example.facturams.DTOs;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ViajeDTO {

    private String id;
    private LocalDate fecha;
    private long usuarioId;
    private Double kilometrosRecorridos;
    private Long totalSegundosPausa;
}
