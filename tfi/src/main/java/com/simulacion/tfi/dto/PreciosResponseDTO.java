package com.simulacion.tfi.dto;

import java.util.Map;

public record PreciosResponseDTO(
        String fechaActualizacion,
        String actualizadoPor,
        Map<String, Double> precios
) {}
