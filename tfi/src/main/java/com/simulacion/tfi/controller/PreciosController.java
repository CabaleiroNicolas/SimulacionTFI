package com.simulacion.tfi.controller;

import com.simulacion.tfi.dto.PreciosRequestDTO;
import com.simulacion.tfi.dto.PreciosDTO;
import com.simulacion.tfi.service.PreciosService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/precios")
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
public class PreciosController {

    private final PreciosService preciosService;

    @GetMapping
    public ResponseEntity<PreciosDTO> getPrecios() {
        return ResponseEntity.ok(preciosService.getPrecios());
    }

    @PutMapping
    public ResponseEntity<PreciosDTO> actualizarPrecios(@RequestBody PreciosRequestDTO request) {
        return ResponseEntity.ok(preciosService.actualizarPrecios(request));
    }
}
