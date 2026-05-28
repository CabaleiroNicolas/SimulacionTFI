package com.simulacion.tfi.controller;

import com.simulacion.tfi.dto.SimulacionCompletaDTO;
import com.simulacion.tfi.dto.SimulacionGuardada;
import com.simulacion.tfi.dto.SimulacionResponseDTO;
import com.simulacion.tfi.dto.SimulacionRequestDTO;
import com.simulacion.tfi.service.HistorialSimulacionService;
import com.simulacion.tfi.service.SimulacionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/simulacion")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class SimulacionController {

    private final SimulacionService simulacionService;
    private final HistorialSimulacionService historialSimulacionService;

    @PostMapping("/ejecutar")
    public ResponseEntity<SimulacionResponseDTO> ejecutar(@RequestBody SimulacionRequestDTO datos) {
        return ResponseEntity.ok(simulacionService.simular(datos));
    }

    @PostMapping("/guardar")
    public ResponseEntity<SimulacionGuardada> guardar(@RequestBody SimulacionCompletaDTO simulacion) {
        return ResponseEntity.ok(historialSimulacionService.guardar(simulacion));
    }

    @GetMapping("/historial")
    public ResponseEntity<List<SimulacionGuardada>> getHistorial() {
        return ResponseEntity.ok(historialSimulacionService.getHistorial());
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of("status", "ok", "version", "1.0"));
    }
}
