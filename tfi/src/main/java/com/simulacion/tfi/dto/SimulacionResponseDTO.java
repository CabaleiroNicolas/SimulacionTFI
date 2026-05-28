package com.simulacion.tfi.dto;

import lombok.Data;

@Data
public class SimulacionResponseDTO {

    private int cantFinalCRT = 0;
    private int cantFinalLCD = 0;
    private int cantFinalLED = 0;
    private double tiempoTotal = 0;

    private double gananicaMaterialesTotal;
    private double costoOperativoTotal;

    private int cantJornadasCuello;
    private int cantJornadasAjustadas;
    private int cantJornadasHolgadas;
    private int cantMonitoresSinProcesar;

    private DatosMaterialesCRT materialesCRT = new DatosMaterialesCRT();
    private DatosMaterialesLCD materialesLCD = new DatosMaterialesLCD();
    private DatosMaterialesLED materialesLED = new DatosMaterialesLED();

    private GananciasMateriales gananciasMateriales = new GananciasMateriales();

}

