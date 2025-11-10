package com.example.administracionms.controller;

import com.example.administracionms.dto.ReporteMonopatinDTO;
import com.example.administracionms.service.ReporteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/reportes")
public class ReporteController {
    private final ReporteService reporteService;

    public ReporteController(ReporteService reporteService) {
        this.reporteService = reporteService;
    }

    @GetMapping("/monopatines/uso")
    public ResponseEntity<List<ReporteMonopatinDTO>> getReporteUso(
            @RequestParam(defaultValue = "false") boolean incluirPausas) {

        List<ReporteMonopatinDTO> reporte = reporteService.generarReporteUso(incluirPausas);

        if (reporte.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(reporte);
    }
}
