package com.monopatines.monopatinms.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReporteMonopatinDTO {
    private String monopatinId;
    private Double kilometrosTotales;
    private Long tiempoTotalPausasSeg;
    private boolean requiereMantenimiento;

    public ReporteMonopatinDTO(String monopatinId, Double kilometrosTotales, boolean requiereMantenimiento) {
        this.monopatinId = monopatinId;
        this.kilometrosTotales = kilometrosTotales;
        this.requiereMantenimiento = requiereMantenimiento;
    }
}
