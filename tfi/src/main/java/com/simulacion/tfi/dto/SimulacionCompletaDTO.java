package com.simulacion.tfi.dto;

import lombok.Data;

@Data
public class SimulacionCompletaDTO {
    private SimulacionRequestDTO parametros;
    private SimulacionResponseDTO resultado;
}