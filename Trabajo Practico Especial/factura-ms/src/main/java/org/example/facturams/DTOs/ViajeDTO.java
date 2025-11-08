package org.example.facturams.DTOs;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ViajeDTO {

    private long id;
    private LocalDate fecha;
    private long usuarioId;
    private Double distancia;
    private Long totalSegundosPausa;
}
