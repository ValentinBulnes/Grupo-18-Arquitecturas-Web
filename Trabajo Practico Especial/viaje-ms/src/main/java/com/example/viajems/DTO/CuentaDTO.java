package com.example.viajems.DTO;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CuentaDTO {
    private Long id;
    private String tipo;
    private Double saldo;
    private boolean activa;
    private List<UsuarioDTO> usuarios;
}
