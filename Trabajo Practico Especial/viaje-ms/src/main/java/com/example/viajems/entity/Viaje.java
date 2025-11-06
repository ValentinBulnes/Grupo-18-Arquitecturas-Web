package com.example.viajems.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Viaje {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long usuarioId;
    
    private Long monopatinId;

    private Long paradaOrigenId;
    private Long paradaDestinoId;

    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;

    private Double kilometrosRecorridos;

    private LocalDateTime pausaInicio;
    private Long totalSegundosPausa = 0L;
}
