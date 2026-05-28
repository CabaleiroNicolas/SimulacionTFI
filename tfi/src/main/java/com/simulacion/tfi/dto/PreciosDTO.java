package com.simulacion.tfi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PreciosDTO {
    private String fechaActualizacion;
    private String actualizadoPor;
    private Map<String, Double> precios;
}
