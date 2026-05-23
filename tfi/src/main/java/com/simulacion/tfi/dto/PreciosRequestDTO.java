package com.simulacion.tfi.dto;

import java.util.Map;

public record PreciosRequestDTO(
        String actualizadoPor,
        Map<String, Double> precios
) {}
