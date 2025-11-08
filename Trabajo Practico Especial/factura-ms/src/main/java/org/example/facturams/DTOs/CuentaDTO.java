package org.example.facturams.DTOs;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CuentaDTO {
    private Long id;
    private String tipoCuenta;
    private Double saldo;
}
