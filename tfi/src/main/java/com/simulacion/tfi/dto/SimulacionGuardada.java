package com.simulacion.tfi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SimulacionGuardada {
    private String fecha;
    private SimulacionCompletaDTO simulacion;
}
