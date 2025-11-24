package com.monopatines.monopatinms.DTO;

import com.monopatines.monopatinms.entity.EstadoMonopatin;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MonopatinDTO {
    private String id;
    private EstadoMonopatin estado;
    private Double kmTotales;
    private Long tiempoUsoTotal;
    private Long paradaActualId;
    private LocalDateTime fechaAlta;
    private LocalDateTime ultimoMantenimiento;
    private Boolean requiereMantenimiento;
    private Double latitud;
    private Double longitud;
}
