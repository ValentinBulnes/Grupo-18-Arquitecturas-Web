package org.example.mercadopagoms.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PagoRequest {
    private Long cuentaId;
    private Double monto;
}
