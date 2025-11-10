package com.monopatines.monopatinms.service;

import com.monopatines.monopatinms.DTO.MonopatinDTO;
import com.monopatines.monopatinms.DTO.ParadaDTO;
import com.monopatines.monopatinms.feignClients.ParadaFeignClient;
import com.monopatines.monopatinms.entity.EstadoMonopatin;
import com.monopatines.monopatinms.entity.Monopatin;
import com.monopatines.monopatinms.repository.MonopatinRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MonopatinService {
    private final MonopatinRepository monopatinRepository;
    private final ParadaFeignClient paradaFeignClient;

    // recuperar todos
    public List<MonopatinDTO> findAll() {
        return monopatinRepository.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    // recuperar uno
    public MonopatinDTO obtenerMonopatin(String id) {
        Optional<Monopatin> monopatinOpt = monopatinRepository.findById(id);
        return monopatinOpt.map(this::convertirADTO).orElse(null);
    }

    // eliminar uno
    public void eliminarMonopatin(String id) {
       monopatinRepository.deleteById(id);
    }

    // recuperar por estado
    public List<MonopatinDTO> findByEstado(EstadoMonopatin estado) {
        return monopatinRepository.findByEstado(estado).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    // insertar monopatin
    public Monopatin saveMonopatin(Monopatin monopatin) {
        Monopatin m;
        m=  monopatinRepository.save(monopatin);
        return m;
    }

    // update monopatin
    public Monopatin updateMonopatin(Monopatin monopatin) {
        Monopatin m;
        m= monopatinRepository.save(monopatin);
        return m;
    }
    // encontrar monopatines en determinada parada
    public List<MonopatinDTO> findByParada(long idParada) {
        ParadaDTO parada = paradaFeignClient.findById(idParada);
        if (parada != null) {
            return monopatinRepository.findByParadaActualID(idParada).stream()
                    .map(this::convertirADTO)
                    .collect(Collectors.toList());
        }
        return null;
    }

    //metodo auxiliar para convertir a dto
    private MonopatinDTO convertirADTO(Monopatin monopatin) {
        MonopatinDTO dto = new MonopatinDTO();

        dto.setId(monopatin.getId());
        dto.setEstado(monopatin.getEstado());
        dto.setKmTotales(monopatin.getKmTotales());
        dto.setTiempoUsoTotal(monopatin.getTiempoUsoTotal());
        dto.setLongitud(monopatin.getLongitud());
        dto.setLatitud(monopatin.getLatitud());
        dto.setFechaAlta(monopatin.getFechaAlta());
        dto.setUltimoMantenimiento(monopatin.getUltimoMantenimiento());
        dto.setRequiereMantenimiento(monopatin.isRequiereMantenimiento());
        return dto;
    }

}
