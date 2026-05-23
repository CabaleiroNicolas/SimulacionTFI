package com.simulacion.tfi.dto;

public record CentroDTO(
        String nombre,
        String cuit,
        String direccion,
        String responsable,
        TiemposProcesamientoDTO tiemposProcesamiento
) {}
