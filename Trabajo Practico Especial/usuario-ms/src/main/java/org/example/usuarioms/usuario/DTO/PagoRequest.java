package org.example.usuarioms.usuario.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PagoRequest {

        private Long cuentaId;
        private Double monto;

}
