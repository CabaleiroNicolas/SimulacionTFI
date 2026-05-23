package com.simulacion.tfi.controller;

import com.simulacion.tfi.dto.SimulacionRequestDTO;
import com.simulacion.tfi.dto.SimulacionResponseDTO;
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

    @PostMapping("/ejecutar")
    public ResponseEntity<SimulacionResponseDTO> ejecutar(@RequestBody SimulacionRequestDTO request) {
        return ResponseEntity.ok(new SimulacionResponseDTO());
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of("status", "ok", "version", "1.0"));
    }
}
