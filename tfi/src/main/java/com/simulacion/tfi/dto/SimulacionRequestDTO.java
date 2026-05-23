package com.simulacion.tfi.dto;

public record SimulacionRequestDTO(
        int operarios,
        double horasTurno,
        double costoOperarioHora,
        int jornadasASimular
) {}
