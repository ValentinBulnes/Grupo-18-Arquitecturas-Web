package com.monopatines.monopatinms.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MonopatinConDistanciaDTO {
    private String id;
    private String estado;
    private Double latitud;
    private Double longitud;
    private Double distanciaKm;
    private long paradaActualID;
    private boolean requiereMantenimiento;
}
