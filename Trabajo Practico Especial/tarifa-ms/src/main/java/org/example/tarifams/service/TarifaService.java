package org.example.tarifams.service;

import org.example.tarifams.entity.Tarifa;
import org.example.tarifams.repository.TarifaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TarifaService {

    private final TarifaRepository tarifaRepository;

    public TarifaService(TarifaRepository tarifaRepository) {
        this.tarifaRepository = tarifaRepository;
    }
    // Crear tarifa
    public Tarifa crearTarifa(Tarifa tarifa) {
        return tarifaRepository.save(tarifa);
    }
    // Obtiene la tarifa actual (la mas reciente)
    public Tarifa obtenerTarifaActual() {
        return tarifaRepository.findTopByOrderByFechaInicioVigenciaDesc();
    }

    // Modificar tarifa
    public Tarifa actualizarTarifa(Long id, Tarifa nuevaTarifa) {
        Tarifa tarifaExistente = tarifaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tarifa no encontrada con ID: " + id));

        tarifaExistente.setPrecioPorKm(nuevaTarifa.getPrecioPorKm());
        tarifaExistente.setPrecioPorMinuto(nuevaTarifa.getPrecioPorMinuto());
        tarifaExistente.setTarifaExtraPorPausa(nuevaTarifa.getTarifaExtraPorPausa());
        tarifaExistente.setFechaInicioVigencia(nuevaTarifa.getFechaInicioVigencia());

        return tarifaRepository.save(tarifaExistente);
    }
    // Eliminar tarifa
    public void eliminarTarifa(Long id) {
        if (!tarifaRepository.existsById(id)) {
            throw new RuntimeException("Tarifa no encontrada con ID: " + id);
        }
        tarifaRepository.deleteById(id);
    }
    //  Listar todas las tarifas
    public List<Tarifa> listarTodas() {
        return tarifaRepository.findAll();
    }
    // Obtener tarifa por ID
    public Tarifa buscarPorId(Long id) {
        return tarifaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tarifa no encontrada con ID: " + id));
    }
}
