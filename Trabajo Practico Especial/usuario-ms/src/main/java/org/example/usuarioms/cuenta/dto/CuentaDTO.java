package org.example.usuarioms.cuenta.dto;

import java.time.LocalDate;

public class CuentaDTO {
    private String tipo;
    private Double saldo;
    private LocalDate fechaAlta;
    private boolean activa;

    public CuentaDTO() {}

    public CuentaDTO(String tipo, Double saldo, LocalDate fechaAlta,  boolean activa) {
        this.tipo = tipo;
        this.saldo = saldo;
        this.fechaAlta = fechaAlta;
        this.activa = activa;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Double getSaldo() {
        return saldo;
    }

    public void setSaldo(Double saldo) {
        this.saldo = saldo;
    }

    public LocalDate getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(LocalDate fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    public boolean isActiva() {
        return activa;
    }

    public void setActiva(boolean activa) {
        this.activa = activa;
    }

    @Override
    public String toString() {
        return "CuentaDTO{" +
                "tipo='" + tipo + '\'' +
                ", saldo=" + saldo +
                ", fechaAlta=" + fechaAlta +
                ", activa=" + activa +
                '}';
    }
}
