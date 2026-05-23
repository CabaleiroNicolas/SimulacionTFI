package com.simulacion.tfi.service;

import com.simulacion.tfi.dto.SimulacionRequestDTO;
import com.simulacion.tfi.dto.SimulacionResponseDTO;
import com.simulacion.tfi.utils.DistribucionesUtil;
import com.simulacion.tfi.utils.GeneradorRandomUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SimulacionService {

    private final DistribucionesUtil distribuciones;
    private final GeneradorRandomUtil generadorU;

    private final double promedioMonitores = 25.0;
    private final double probabilidadCRT = 0.60;
    private final double probabilidadLCD = 0.30;

    public SimulacionResponseDTO simular(SimulacionRequestDTO datosSimulacion) {
        log.info("Ejecutando simulacion...");
        int cantFinalCRT = 0;
        int cantFinalLCD = 0;
        int cantFinalLED = 0;

        double tiempoTotal = 0;

        SimulacionResponseDTO resultado = new SimulacionResponseDTO();

        int cantJornadas = datosSimulacion.jornadasASimular();
        int jornadaActual = 1;

        // Recorre jornadas
        while(jornadaActual <= cantJornadas) {
            log.info("Comenzando simulacion de {} jornadas", cantJornadas);

            int cantMonitores = (int) distribuciones.Poisson(promedioMonitores);
            int monitorActual = 1;

            log.info("Llegaron {} monitores en la jornada {}",cantMonitores,jornadaActual);
            // Recorre monitores de una jornada
            while(monitorActual <= cantMonitores) {
                log.info("Procesando monitor numero {} de la jornada {}", monitorActual, jornadaActual);

                double u = generadorU.generarU();
                // Binomiales

                if(u <= probabilidadCRT){ // es CRT
                    log.info("Monitor numero {}/{} de la jornada {} es CRT", monitorActual, cantMonitores, jornadaActual);
                    cantFinalCRT++;

                    tiempoTotal = tiempoTotal + distribuciones.Normal(45, 8);
                }
                if(u <= probabilidadLCD){ // es LCD
                    log.info("Monitor numero {}/{} de la jornada {} es LCD", monitorActual, cantMonitores, jornadaActual);
                    cantFinalLCD++;

                    tiempoTotal = tiempoTotal + distribuciones.Normal(25, 5);
                }
                else { // es LED
                    log.info("Monitor numero {}/{} de la jornada {} es LED", monitorActual, cantMonitores, jornadaActual);
                    cantFinalLED++;

                    tiempoTotal = tiempoTotal + distribuciones.Normal(15, 3);
                }

                monitorActual++;
            }

            jornadaActual++;
        }



        return resultado;
    }
}
