package com.example.paradams.service;

import com.example.paradams.entity.Parada;
import com.example.paradams.repository.ParadaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ParadaService {
    @Autowired
    ParadaRepository paradaRepository;

    public List<Parada> getAll() {
        return paradaRepository.findAll();
    }

    public Parada findById(Long id) {
        return paradaRepository.findById(id).orElse(null);
    }

    public Parada save(Parada parada) {
        Parada paradaNew;
        paradaNew = paradaRepository.save(parada);
        return paradaNew;
    }

    public Parada update(Parada parada) {
        return paradaRepository.save(parada);
    }

    public void delete(Parada parada) {
        paradaRepository.delete(parada);
    }

    public void deleteById(Long id) {
        paradaRepository.deleteById(id);
    }

    public List<Parada> findByNombre(String nombre) {
        return paradaRepository.findByNombre(nombre);
    }

    public List<Parada> findByDireccion(String direccion) {
        return paradaRepository.findByDireccion(direccion);
    }
}
