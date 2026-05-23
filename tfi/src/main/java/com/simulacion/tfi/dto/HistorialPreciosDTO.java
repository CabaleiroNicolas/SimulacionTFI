package com.simulacion.tfi.dto;

import java.util.Map;

public record HistorialPreciosDTO(
        String fecha,
        String actualizadoPor,
        Map<String, Double> preciosAnteriores,
        Map<String, Double> preciosNuevos
) {}
