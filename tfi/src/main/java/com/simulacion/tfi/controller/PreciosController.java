package com.simulacion.tfi.controller;

import com.simulacion.tfi.dto.PreciosDTO;
import com.simulacion.tfi.service.PreciosService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/precios")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class PreciosController {

    private final PreciosService preciosService;

    @GetMapping
    public ResponseEntity<PreciosDTO> getPrecios() {
        return ResponseEntity.ok(preciosService.getPrecios());
    }

    @PutMapping
    public ResponseEntity<PreciosDTO> actualizarPrecios(@RequestBody PreciosDTO request) {
        return ResponseEntity.ok(preciosService.actualizarPrecios(request));
    }
}
