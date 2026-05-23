package com.simulacion.tfi.controller;

import com.simulacion.tfi.dto.SimulacionRequestDTO;
import com.simulacion.tfi.dto.SimulacionResponseDTO;
import com.simulacion.tfi.service.SimulacionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/simulacion")
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
public class SimulacionController {

    private final SimulacionService simulacionService;

    @PostMapping("/ejecutar")
    public ResponseEntity<SimulacionResponseDTO> ejecutar(@RequestBody SimulacionRequestDTO datos) {
        return ResponseEntity.ok(simulacionService.simular(datos));
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of("status", "ok", "version", "1.0"));
    }
}
