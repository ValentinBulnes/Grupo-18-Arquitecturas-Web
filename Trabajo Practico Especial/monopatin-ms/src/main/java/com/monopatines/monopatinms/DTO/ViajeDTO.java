package com.monopatines.monopatinms.DTO;

import lombok.Data;

@Data
public class ViajeDTO {
    private String id;
    private Double kilometrosRecorridos;
    private Long totalSegundosPausa = 0L;
}
