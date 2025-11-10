package com.example.viajems.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Document(collection = "viajes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Viaje {
    @Id
    private String id;

    private Long usuarioId;
    private String monopatinId;
    private Long paradaOrigenId;
    private Long paradaDestinoId;

    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;

    private Double kilometrosRecorridos;

    private LocalDateTime pausaInicio;
    private Long totalSegundosPausa = 0L;
}
