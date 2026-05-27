package com.simulacion.tfi;

import lombok.Data;

@Data
public class DatosProcesamientoMonitores {

    private int cantFinalCRT = 0;
    private int cantFinalLCD = 0;
    private int cantFinalLED = 0;
    private double tiempoTotal = 0;

    int cantJornadasCuello;
    int cantJornadasAjustadas;
    int cantJornadasHolgadas;

    private DatosMaterialesCRT materialesCRT = new DatosMaterialesCRT();
    private DatosMaterialesLCD materialesLCD = new DatosMaterialesLCD();
    private DatosMaterialesLED materialesLED = new DatosMaterialesLED();

}

