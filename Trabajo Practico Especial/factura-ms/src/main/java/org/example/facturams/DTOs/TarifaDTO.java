package org.example.facturams.DTOs;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TarifaDTO {
    private Double precioPorKm;
    private Double precioPorMinuto;
    private Double tarifaExtraPorPausa;
}
