package com.simulacion.tfi.controller;

import com.simulacion.tfi.dto.CentroDTO;
import com.simulacion.tfi.service.CentroService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/centro")
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
public class CentroController {

    private final CentroService centroService;

    @GetMapping
    public ResponseEntity<CentroDTO> getCentro() {
        return ResponseEntity.ok(centroService.getCentro());
    }

    @PutMapping
    public ResponseEntity<CentroDTO> actualizarCentro(@RequestBody CentroDTO request) {
        return ResponseEntity.ok(centroService.actualizarCentro(request));
    }
}
