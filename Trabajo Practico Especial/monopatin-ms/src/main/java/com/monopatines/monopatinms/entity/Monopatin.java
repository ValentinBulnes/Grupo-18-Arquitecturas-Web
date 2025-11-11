package com.monopatines.monopatinms.entity;

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
    private double kilometrajeMaximo;

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

    public void actualizarEstadisticas(Double kmRecorridos) {
        this.kmTotales = kmRecorridos;
        // Lógica para determinar si requiere mantenimiento
        // Ejemplo: más de 1000 km
        if (this.kmTotales > 1000 ) {
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

    // Calcular distancia a un punto (en km) usando fórmula de Haversine
    public Double calcularDistanciaA(Double latDestino, Double lonDestino) {
        if (this.latitud == null || this.longitud == null) {
            return null;
        }

        final int RADIO_TIERRA_KM = 6371;

        double latDistancia = Math.toRadians(latDestino - this.latitud);
        double lonDistancia = Math.toRadians(lonDestino - this.longitud);

        double a = Math.sin(latDistancia / 2) * Math.sin(latDistancia / 2)
                + Math.cos(Math.toRadians(this.latitud)) * Math.cos(Math.toRadians(latDestino))
                * Math.sin(lonDistancia / 2) * Math.sin(lonDistancia / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return RADIO_TIERRA_KM * c;
    }

    // Verificar si está cerca de una ubicación (dentro de un radio en km)
    public boolean estaCercaDe(Double latDestino, Double lonDestino, Double radioKm) {
        Double distancia = calcularDistanciaA(latDestino, lonDestino);
        return distancia != null && distancia <= radioKm;
    }


}
