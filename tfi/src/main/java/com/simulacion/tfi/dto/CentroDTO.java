package com.simulacion.tfi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CentroDTO {
    private String nombre;
    private String cuit;
    private String direccion;
    private String responsable;
    private int cantMonitoresPromedio;
}
