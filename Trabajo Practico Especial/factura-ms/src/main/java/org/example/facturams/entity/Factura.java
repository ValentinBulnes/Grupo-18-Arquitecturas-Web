package org.example.facturams.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Positive;
import lombok.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

@Setter
@Getter
@Entity
public class Factura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    private LocalDate fechaEmision;

    @Positive
    @NotNull
    private double montoTotal;

    private String descripcion;

    private String  viajeId;

    private long  cuentaId;

    private long  usuarioId;

    public Factura() {}

    public Factura(LocalDate fechaEmision, Double montoTotal, String descripcion,
                   String  viajeId, long  cuentaId, long  usuarioId) {
        this.fechaEmision = fechaEmision;
        this.montoTotal = montoTotal;
        this.descripcion = descripcion;
        this.viajeId = viajeId;
        this.cuentaId = cuentaId;
        this.usuarioId = usuarioId;
    }

    public String  getViajeId() {
        return viajeId;
    }
    public long  getUsuarioId() {
        return usuarioId;
    }
    public long  getCuentaId() {
        return cuentaId;
    }
}
