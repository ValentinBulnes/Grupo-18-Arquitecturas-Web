package org.example.tarifams.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


import java.time.LocalDate;

@Getter
@Setter
@Entity
public class Tarifa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    private Double precioPorKm;

    @NotNull
    private Double precioPorMinuto;

    @NotNull
    private Double tarifaExtraPorPausa;

    private LocalDate fechaInicioVigencia;
}
