package com.monopatines.monopatinms.entity;



import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "monopatines")
public class Monopatin {

    @Id
    private String id;

    private EstadoMonopatin estado;
    private Double kmTotales;
    private long tiempoUsoTotal;
    private long paradaActualID;
    private LocalDateTime fechaAlta;
    private LocalDateTime ultimoMantenimiento;
    private boolean requiereMantenimiento;
    private Double latitud;
    private Double longitud;

    public void entrarEnMantenimiento(){
        this.estado = EstadoMonopatin.MANTENIMIENTO;
    }
    public boolean estaDisponible(){
        return this.estado == EstadoMonopatin.DISPONIBLE;
    }
    public void marcarDisponible(){
        this.estado = EstadoMonopatin.DISPONIBLE;
    }
    public void marcarEnUso(){
        this.estado= EstadoMonopatin.EN_USO;
    }

    public void actualizarEstadisticas(Double kmRecorridos, Long tiempoUso) {
        this.kmTotales += kmRecorridos;
        this.tiempoUsoTotal += tiempoUso;

        // Lógica para determinar si requiere mantenimiento
        // Ejemplo: más de 1000 km o más de 500 horas de uso
        if (this.kmTotales > 1000 || this.tiempoUsoTotal > 30000) {
            this.requiereMantenimiento = true;
        }
    }

    // Actualizar ubicación del monopatín
    public void actualizarUbicacion(Double nuevaLatitud, Double nuevaLongitud) {
        this.latitud = nuevaLatitud;
        this.longitud = nuevaLongitud;
    }

    public boolean mismaUbicacion(Double latitud, Double longitud) {
        return this.latitud.equals(latitud) && this.longitud.equals(longitud);
    }


}
