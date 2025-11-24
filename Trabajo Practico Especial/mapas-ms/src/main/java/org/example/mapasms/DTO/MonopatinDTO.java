package org.example.mapasms.DTO;

import org.example.mapasms.entity.EstadoMonopatin;

import java.time.LocalDate;
import java.time.LocalDateTime;

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

    // Constructor vacío
    public MonopatinDTO() {}

    // Constructor con todos los parámetros
    public MonopatinDTO(String id, EstadoMonopatin estado, Double kmTotales, Long tiempoUsoTotal,
                        Long paradaActualId, LocalDateTime fechaAlta, LocalDateTime ultimoMantenimiento,
                        Boolean requiereMantenimiento, Double latitud, Double longitud) {
        this.id = id;
        this.estado = estado;
        this.kmTotales = kmTotales;
        this.tiempoUsoTotal = tiempoUsoTotal;
        this.paradaActualId = paradaActualId;
        this.fechaAlta = fechaAlta;
        this.ultimoMantenimiento = ultimoMantenimiento;
        this.requiereMantenimiento = requiereMantenimiento;
        this.latitud = latitud;
        this.longitud = longitud;
    }
    // Getters y setters para cada campo
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public EstadoMonopatin getEstado() { return estado; }
    public void setEstado(EstadoMonopatin estado) { this.estado = estado; }

    public double getKmTotales() { return kmTotales; }
    public void setKmTotales(double kmTotales) { this.kmTotales = kmTotales; }

    public long getTiempoUsoTotal() { return tiempoUsoTotal; }
    public void setTiempoUsoTotal(long tiempoUsoTotal) { this.tiempoUsoTotal = tiempoUsoTotal; }

    public long getParadaActualId() { return paradaActualId; }
    public void setParadaActualId(long paradaActualId) { this.paradaActualId = paradaActualId; }

    public LocalDateTime getFechaAlta() { return fechaAlta; }
    public void setFechaAlta(LocalDateTime fechaAlta) { this.fechaAlta = fechaAlta; }

    public LocalDateTime getUltimoMantenimiento() { return ultimoMantenimiento; }
    public void setUltimoMantenimiento(LocalDateTime ultimoMantenimiento) { this.ultimoMantenimiento = ultimoMantenimiento; }

    public boolean isRequiereMantenimiento() { return requiereMantenimiento; }
    public void setRequiereMantenimiento(boolean requiereMantenimiento) { this.requiereMantenimiento = requiereMantenimiento; }

    public double getLatitud() { return latitud; }
    public void setLatitud(double latitud) { this.latitud = latitud; }

    public double getLongitud() { return longitud; }
    public void setLongitud(double longitud) { this.longitud = longitud; }
}
