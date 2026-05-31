package com.simulacion.tfi.service;

import com.simulacion.tfi.dto.GananciasMateriales;
import com.simulacion.tfi.dto.SimulacionResponseDTO;
import com.simulacion.tfi.almacenamiento.Memoria;
import com.simulacion.tfi.dto.SimulacionRequestDTO;
import com.simulacion.tfi.utils.DistribucionesUtil;
import com.simulacion.tfi.utils.GeneradorRandomUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class SimulacionService {

    private final DistribucionesUtil distribuciones;
    private final GeneradorRandomUtil generadorU;
    private final Memoria memoria;

    public SimulacionResponseDTO simular(SimulacionRequestDTO datosSimulacion) {

        final double probabilidadAcumuladaCRT = 0.60;
        final double probabilidadAcumuladaLCD = 0.90;

        log.info("Ejecutando simulacion...");

        SimulacionResponseDTO resultados = new SimulacionResponseDTO();

        int promedioMonitores = memoria.getCentro().getCantMonitoresPromedio();
        double horasTurno = datosSimulacion.horasTurno();
        double capacidadJornada = horasTurno * datosSimulacion.operarios();
        double costoOperativo = horasTurno*datosSimulacion.jornadasASimular()*datosSimulacion.costoOperarioHora()*datosSimulacion.operarios();
        int cantJornadas = datosSimulacion.jornadasASimular();
        int jornadaActual = 1;

        log.info("Comenzando simulacion de {} jornadas", cantJornadas);
        // Recorre jornadas
        while(jornadaActual <= cantJornadas) {

            int cantMonitores = (int) distribuciones.Poisson(promedioMonitores);
            int monitorActual = 1;
            double tiempoJornada = 0;

            log.info("Llegaron {} monitores en la jornada {}",cantMonitores,jornadaActual);
            // Recorre monitores de una jornada
            while((monitorActual <= cantMonitores) && (tiempoJornada < capacidadJornada)) {
                log.info("Procesando monitor numero {} de la jornada {}", monitorActual, jornadaActual);


                double tiempoMonitor = 0;
                double u = generadorU.generarU();

                // Binomiales
                if(u <= probabilidadAcumuladaCRT){ // es CRT
                    log.info("Monitor numero {}/{} de la jornada {} es CRT", monitorActual, cantMonitores, jornadaActual);
                    resultados.setCantFinalCRT(resultados.getCantFinalCRT() + 1);

                    tiempoMonitor = procesarCRT(resultados);
                }
                else if(u <= probabilidadAcumuladaLCD){ // es LCD
                    log.info("Monitor numero {}/{} de la jornada {} es LCD", monitorActual, cantMonitores, jornadaActual);
                    resultados.setCantFinalLCD(resultados.getCantFinalLCD() + 1);

                    tiempoMonitor = procesarLCD(resultados);
                }
                else { // es LED
                    log.info("Monitor numero {}/{} de la jornada {} es LED", monitorActual, cantMonitores, jornadaActual);
                    resultados.setCantFinalLED(resultados.getCantFinalLED() + 1);

                    tiempoMonitor = procesarLED(resultados);
                }

                double tiempoMonitorHoras = tiempoMonitor / 60.0;
                resultados.setTiempoTotal(resultados.getTiempoTotal() + tiempoMonitorHoras);
                tiempoJornada = tiempoJornada + tiempoMonitorHoras;
                monitorActual++;
            }

            resultados.setCantMonitoresSinProcesar(resultados.getCantMonitoresSinProcesar() + (cantMonitores-monitorActual+1));
            calcularEstadoJornada(resultados, tiempoJornada, capacidadJornada);
            jornadaActual++;
        }

        calcularGanancias(resultados, costoOperativo);
        calcularImpactoAmbiental(resultados);
        return resultados;
    }


    private void calcularEstadoJornada(SimulacionResponseDTO datos, double horasTotalJornada, double horasTurno) {

        if(horasTurno <= horasTotalJornada) {
            datos.setCantJornadasCuello(datos.getCantJornadasCuello() + 1);
        }
        else if((horasTurno-horasTotalJornada) > 1) {
            datos.setCantJornadasHolgadas(datos.getCantJornadasHolgadas() + 1);
        }
        else {
            datos.setCantJornadasAjustadas(datos.getCantJornadasAjustadas() + 1);
        }
    }

    private double procesarLED(SimulacionResponseDTO datos) {

        double tiempo = distribuciones.Uniforme(8, 15);

        // Peligrosos
        datos.getMaterialesLED().setGrsPlomoTotalLED(datos.getMaterialesLED().getGrsPlomoTotalLED() + distribuciones.Uniforme(0,18));
        datos.getMaterialesLED().setGrsBFRTotalLED(datos.getMaterialesLED().getGrsBFRTotalLED() + distribuciones.Uniforme(5,35));

        //Normales
        datos.getMaterialesLED().setKgVidrioPanelTotalLED(datos.getMaterialesLED().getKgVidrioPanelTotalLED() + distribuciones.Normal(0.37,0.065));
        datos.getMaterialesLED().setKgPlastABSTotalLED(datos.getMaterialesLED().getKgPlastABSTotalLED() + distribuciones.Normal(0.62,0.105));
        datos.getMaterialesLED().setKgPlastPCTotalLED(datos.getMaterialesLED().getKgPlastPCTotalLED() + distribuciones.Uniforme(0.06,0.24));
        datos.getMaterialesLED().setKgAceroTotalLED(datos.getMaterialesLED().getKgAceroTotalLED() + distribuciones.Normal(0.98,0.175));
        datos.getMaterialesLED().setKgCobreTotalLED(datos.getMaterialesLED().getKgCobreTotalLED() + distribuciones.Normal(0.115,0.035));
        datos.getMaterialesLED().setKgAluminioTotalLED(datos.getMaterialesLED().getKgAluminioTotalLED() + distribuciones.Normal(0.49,0.1));
        datos.getMaterialesLED().setKgPlacasPCBTotalLED(datos.getMaterialesLED().getKgPlacasPCBTotalLED() + distribuciones.Normal(0.31,0.062));
        datos.getMaterialesLED().setGrLCTotalLED(datos.getMaterialesLED().getGrLCTotalLED() + distribuciones.Uniforme(8,25));
        datos.getMaterialesLED().setGrIndioTotalLED(datos.getMaterialesLED().getGrIndioTotalLED() + distribuciones.Uniforme(0.08,0.4));
        datos.getMaterialesLED().setGrOroTotalLED(datos.getMaterialesLED().getGrOroTotalLED() + distribuciones.Uniforme(0.12,0.45));
        datos.getMaterialesLED().setGrPlataTotalLED(datos.getMaterialesLED().getGrPlataTotalLED() + distribuciones.Uniforme(0.6,2));
        datos.getMaterialesLED().setGrPaladioTotalLED(datos.getMaterialesLED().getGrPaladioTotalLED() + distribuciones.Uniforme(0.03,0.21));
        datos.getMaterialesLED().setGrEstanioTotalLED(datos.getMaterialesLED().getGrEstanioTotalLED() + distribuciones.Normal(28,7));
        datos.getMaterialesLED().setGrTirasLedTotalLED(datos.getMaterialesLED().getGrTirasLedTotalLED() + distribuciones.Uniforme(12,38));

        return tiempo;
    }

    private double procesarLCD(SimulacionResponseDTO datos) {

        double tiempo = distribuciones.Normal(25, 5);
        double u = generadorU.generarU();

        if(u <= 0.2) {
            double tiempoAdicional = distribuciones.Uniforme(15, 25);
            tiempo = tiempo + tiempoAdicional;
        }

        // Peligrosos
        datos.getMaterialesLCD().setGrsPlomoTotalLCD(datos.getMaterialesLCD().getGrsPlomoTotalLCD() + distribuciones.Uniforme(0,55));
        datos.getMaterialesLCD().setMgsMercurioTotalLCD(datos.getMaterialesLCD().getMgsMercurioTotalLCD() + distribuciones.Normal(15,6));
        datos.getMaterialesLCD().setGrsCadmioTotalLCD(datos.getMaterialesLCD().getGrsCadmioTotalLCD() + distribuciones.Uniforme(0,0.15));
        datos.getMaterialesLCD().setGrsBFRTotalLCD(datos.getMaterialesLCD().getGrsBFRTotalLCD() + distribuciones.Uniforme(10,45));

        //Normales
        datos.getMaterialesLCD().setKgVidrioPanelTotalLCD(datos.getMaterialesLCD().getKgVidrioPanelTotalLCD() + distribuciones.Normal(0.39,0.068));
        datos.getMaterialesLCD().setKgPlastABSTotalLCD(datos.getMaterialesLCD().getKgPlastABSTotalLCD() + distribuciones.Normal(0.78,0.125));
        datos.getMaterialesLCD().setKgPlastPCTotalLCD(datos.getMaterialesLCD().getKgPlastPCTotalLCD() + distribuciones.Uniforme(0.008,0.3));
        datos.getMaterialesLCD().setKgAceroTotalLCD(datos.getMaterialesLCD().getKgAceroTotalLCD() + distribuciones.Normal(1.35,0.23));
        datos.getMaterialesLCD().setKgCobreTotalLCD(datos.getMaterialesLCD().getKgCobreTotalLCD() + distribuciones.Normal(0.225,0.048));
        datos.getMaterialesLCD().setKgAluminioTotalLCD(datos.getMaterialesLCD().getKgAluminioTotalLCD() + distribuciones.Normal(0.54,0.12));
        datos.getMaterialesLCD().setKgPlacasPCBTotalLCD(datos.getMaterialesLCD().getKgPlacasPCBTotalLCD() + distribuciones.Normal(0.36,0.072));
        datos.getMaterialesLCD().setGrLCTotalLCD(datos.getMaterialesLCD().getGrLCTotalLCD() + distribuciones.Uniforme(10,28));
        datos.getMaterialesLCD().setGrIndioTotalLCD(datos.getMaterialesLCD().getGrIndioTotalLCD() + distribuciones.Uniforme(0.1,0.45));
        datos.getMaterialesLCD().setGrOroTotalLCD(datos.getMaterialesLCD().getGrOroTotalLCD() + distribuciones.Uniforme(0.15,0.50));
        datos.getMaterialesLCD().setGrPlataTotalLCD(datos.getMaterialesLCD().getGrPlataTotalLCD() + distribuciones.Uniforme(0.8,2.5));
        datos.getMaterialesLCD().setGrPaladioTotalLCD(datos.getMaterialesLCD().getGrPaladioTotalLCD() + distribuciones.Uniforme(0.04,0.20));
        datos.getMaterialesLCD().setGrEstanioTotalLCD(datos.getMaterialesLCD().getGrEstanioTotalLCD() + distribuciones.Normal(35,9));

        return tiempo;
    }


    private double procesarCRT(SimulacionResponseDTO datos) {

        double tiempo = distribuciones.Normal(45, 8);
        double u = generadorU.generarU();

        if(u <= 0.3) {
            double tiempoAdicional = distribuciones.Uniforme(10, 20);
            tiempo = tiempo + tiempoAdicional;

        }

        // Peligrosos
        datos.getMaterialesCRT().setGrsPlomoTotalCRT(datos.getMaterialesCRT().getGrsPlomoTotalCRT() + distribuciones.Normal(1600,290));
        datos.getMaterialesCRT().setMgsMercurioTotalCRT(datos.getMaterialesCRT().getMgsMercurioTotalCRT() + distribuciones.Uniforme(1,8));
        datos.getMaterialesCRT().setGrsCadmioTotalCRT(datos.getMaterialesCRT().getGrsCadmioTotalCRT() + distribuciones.Uniforme(0.5,3));
        datos.getMaterialesCRT().setGrsBFRTotalCRT(datos.getMaterialesCRT().getGrsBFRTotalCRT() + distribuciones.Uniforme(15,60));

        //Normales
        datos.getMaterialesCRT().setKgVidrioPanelTotalCRT(datos.getMaterialesCRT().getKgVidrioPanelTotalCRT() + distribuciones.Normal(5.8,0.7));
        datos.getMaterialesCRT().setKgPlastABSTotalCRT(datos.getMaterialesCRT().getKgPlastABSTotalCRT() + distribuciones.Normal(3.2,0.4));
        datos.getMaterialesCRT().setKgPlastPCTotalCRT(datos.getMaterialesCRT().getKgPlastPCTotalCRT() + distribuciones.Uniforme(0.2,0.7));
        datos.getMaterialesCRT().setKgAceroTotalCRT(datos.getMaterialesCRT().getKgAceroTotalCRT() + distribuciones.Normal(1.2,0.23));
        datos.getMaterialesCRT().setKgCobreTotalCRT(datos.getMaterialesCRT().getKgCobreTotalCRT() + distribuciones.Normal(0.82,0.125));
        datos.getMaterialesCRT().setKgAluminioTotalCRT(datos.getMaterialesCRT().getKgAluminioTotalCRT() + distribuciones.Normal(0.28,0.065));
        datos.getMaterialesCRT().setKgPlacasPCBTotalCRT(datos.getMaterialesCRT().getKgPlacasPCBTotalCRT() + distribuciones.Normal(0.48,0.09));
        datos.getMaterialesCRT().setGrOroTotalCRT(datos.getMaterialesCRT().getGrOroTotalCRT() + distribuciones.Uniforme(0.2,0.55));
        datos.getMaterialesCRT().setGrPlataTotalCRT(datos.getMaterialesCRT().getGrPlataTotalCRT() + distribuciones.Uniforme(1,3));
        datos.getMaterialesCRT().setGrPaladioTotalCRT(datos.getMaterialesCRT().getGrPaladioTotalCRT() + distribuciones.Uniforme(0.05,0.25));
        datos.getMaterialesCRT().setGrNiquelTotalCRT(datos.getMaterialesCRT().getGrNiquelTotalCRT() + distribuciones.Uniforme(25,75));
        datos.getMaterialesCRT().setGrEstanioTotalCRT(datos.getMaterialesCRT().getGrEstanioTotalCRT() + distribuciones.Normal(60,14));

        return tiempo;
    }

    private void calcularGanancias(SimulacionResponseDTO datos, double costoOperativo) {

        datos.setCostoOperativoTotal(costoOperativo);

        // Precios desde memoria
        Map<String, Double> precios = memoria.getPrecios().getPrecios();
        double precioVidrioPanelCRT    = precios.get("vidrioPanelCRT");
        double precioVidrioPanelLCDLED = precios.get("vidrioPanelLCDLED");
        double precioPlastABS          = precios.get("plastABS");
        double precioPlastPC           = precios.get("plastPC");
        double precioAcero             = precios.get("acero");
        double precioCobre             = precios.get("cobre");
        double precioAluminio          = precios.get("aluminio");
        double precioPlacasPCB         = precios.get("placasPCB");
        double precioOro               = precios.get("oro");
        double precioPlata             = precios.get("plata");
        double precioPaladio           = precios.get("paladio");
        double precioEstanio           = precios.get("estanio");
        double precioNiquel            = precios.get("niquel");
        double precioIndio             = precios.get("indio");
        double precioLC                = precios.get("lc");
        double precioTirasLed          = precios.get("tirasLed");

        // Totales por material sumando todos los tipos de monitor
        double totalVidrioPanelCRT = datos.getMaterialesCRT().getKgVidrioPanelTotalCRT();

        double totalVidrioPanelLCDLED =  datos.getMaterialesLCD().getKgVidrioPanelTotalLCD()
                                        + datos.getMaterialesLED().getKgVidrioPanelTotalLED();

        double totalPlastABS   = datos.getMaterialesCRT().getKgPlastABSTotalCRT()
                               + datos.getMaterialesLCD().getKgPlastABSTotalLCD()
                               + datos.getMaterialesLED().getKgPlastABSTotalLED();

        double totalPlastPC    = datos.getMaterialesCRT().getKgPlastPCTotalCRT()
                               + datos.getMaterialesLCD().getKgPlastPCTotalLCD()
                               + datos.getMaterialesLED().getKgPlastPCTotalLED();

        double totalAcero      = datos.getMaterialesCRT().getKgAceroTotalCRT()
                               + datos.getMaterialesLCD().getKgAceroTotalLCD()
                               + datos.getMaterialesLED().getKgAceroTotalLED();

        double totalCobre      = datos.getMaterialesCRT().getKgCobreTotalCRT()
                               + datos.getMaterialesLCD().getKgCobreTotalLCD()
                               + datos.getMaterialesLED().getKgCobreTotalLED();

        double totalAluminio   = datos.getMaterialesCRT().getKgAluminioTotalCRT()
                               + datos.getMaterialesLCD().getKgAluminioTotalLCD()
                               + datos.getMaterialesLED().getKgAluminioTotalLED();

        double totalPlacasPCB  = datos.getMaterialesCRT().getKgPlacasPCBTotalCRT()
                               + datos.getMaterialesLCD().getKgPlacasPCBTotalLCD()
                               + datos.getMaterialesLED().getKgPlacasPCBTotalLED();

        double totalOro        = datos.getMaterialesCRT().getGrOroTotalCRT()
                               + datos.getMaterialesLCD().getGrOroTotalLCD()
                               + datos.getMaterialesLED().getGrOroTotalLED();

        double totalPlata      = datos.getMaterialesCRT().getGrPlataTotalCRT()
                               + datos.getMaterialesLCD().getGrPlataTotalLCD()
                               + datos.getMaterialesLED().getGrPlataTotalLED();

        double totalPaladio    = datos.getMaterialesCRT().getGrPaladioTotalCRT()
                               + datos.getMaterialesLCD().getGrPaladioTotalLCD()
                               + datos.getMaterialesLED().getGrPaladioTotalLED();

        double totalEstanio    = datos.getMaterialesCRT().getGrEstanioTotalCRT()
                               + datos.getMaterialesLCD().getGrEstanioTotalLCD()
                               + datos.getMaterialesLED().getGrEstanioTotalLED();

        double totalNiquel     = datos.getMaterialesCRT().getGrNiquelTotalCRT();

        double totalIndio      = datos.getMaterialesLCD().getGrIndioTotalLCD()
                               + datos.getMaterialesLED().getGrIndioTotalLED();

        double totalLC         = datos.getMaterialesLCD().getGrLCTotalLCD()
                               + datos.getMaterialesLED().getGrLCTotalLED();

        double totalTirasLed   = datos.getMaterialesLED().getGrTirasLedTotalLED();

        // Ganancias por material
        datos.getGananciasMateriales().setGananciaKgVidrioPanelCRT(totalVidrioPanelCRT       * precioVidrioPanelCRT);
        datos.getGananciasMateriales().setGananciaKgVidrioPanelLCDLED(totalVidrioPanelLCDLED * precioVidrioPanelLCDLED);
        datos.getGananciasMateriales().setGananciaKgPlastABS(totalPlastABS                   * precioPlastABS);
        datos.getGananciasMateriales().setGananciaKgPlastPC(totalPlastPC                     * precioPlastPC);
        datos.getGananciasMateriales().setGananciaKgAcero(totalAcero                         * precioAcero);
        datos.getGananciasMateriales().setGananciaKgCobre(totalCobre                         * precioCobre);
        datos.getGananciasMateriales().setGananciaKgAluminio(totalAluminio                   * precioAluminio);
        datos.getGananciasMateriales().setGananciaKgPlacasPCB(totalPlacasPCB                 * precioPlacasPCB);
        datos.getGananciasMateriales().setGananciaGrOro(totalOro                             * precioOro);
        datos.getGananciasMateriales().setGananciaGrPlata(totalPlata                         * precioPlata);
        datos.getGananciasMateriales().setGananciaGrPaladio(totalPaladio                     * precioPaladio);
        datos.getGananciasMateriales().setGananciaGrEstanio(totalEstanio                     * precioEstanio);
        datos.getGananciasMateriales().setGananciaGrNiquel(totalNiquel                       * precioNiquel);
        datos.getGananciasMateriales().setGananciaGrIndio(totalIndio                         * precioIndio);
        datos.getGananciasMateriales().setGananciaGrLC(totalLC                               * precioLC);
        datos.getGananciasMateriales().setGananciaGrTirasLed(totalTirasLed                   * precioTirasLed);

        // Ganancia total
        GananciasMateriales g = datos.getGananciasMateriales();
        datos.setGananicaMaterialesTotal(
                g.getGananciaKgVidrioPanelCRT()    +
                g.getGananciaKgVidrioPanelLCDLED() +
                g.getGananciaKgPlastABS()           +
                g.getGananciaKgPlastPC()            +
                g.getGananciaKgAcero()              +
                g.getGananciaKgCobre()              +
                g.getGananciaKgAluminio()           +
                g.getGananciaKgPlacasPCB()          +
                g.getGananciaGrOro()                +
                g.getGananciaGrPlata()              +
                g.getGananciaGrPaladio()            +
                g.getGananciaGrEstanio()            +
                g.getGananciaGrNiquel()             +
                g.getGananciaGrIndio()              +
                g.getGananciaGrLC()                 +
                g.getGananciaGrTirasLed()
        );
    }

    private void calcularImpactoAmbiental(SimulacionResponseDTO datos) {

        double limiteSueloPlomo = 100.0;
        double limiteAguaPlomo = 0.01;
        double densidadSueloPlomo = 1500.0;

        double limiteSueloMercurio = 0.5;
        double limiteAguaMercurio= 0.001;
        double densidadSueloMercurio = 1000.0;

        double limiteSueloCadmio = 3.0;
        double limiteAguaCadmio = 0.003;
        double densidadSueloCadmio = 1300.0;

        double limiteSueloBFR = 0.05;
        double limiteAguaBFR = 0.0001;
        double densidadSueloBFR = 900.0;


        double cEfPlomo = distribuciones.Normal(0.016, 0.004);
        double cEfMecurio = distribuciones.Normal(0.165, 0.045);
        double cEfCadmio = distribuciones.Normal(0.338, 0.075);
        double cEfBFR = distribuciones.Normal(0.0044, 0.0015);

        double plomoTotal   = datos.getMaterialesCRT().getGrsPlomoTotalCRT()
                + datos.getMaterialesLCD().getGrsPlomoTotalLCD()
                + datos.getMaterialesLED().getGrsPlomoTotalLED();

        double mercurioTotal = datos.getMaterialesCRT().getMgsMercurioTotalCRT()
                + datos.getMaterialesLCD().getMgsMercurioTotalLCD();

        double cadmioTotal  = datos.getMaterialesCRT().getGrsCadmioTotalCRT()
                + datos.getMaterialesLCD().getGrsCadmioTotalLCD();

        double bfrTotal     = datos.getMaterialesCRT().getGrsBFRTotalCRT()
                + datos.getMaterialesLCD().getGrsBFRTotalLCD()
                + datos.getMaterialesLED().getGrsBFRTotalLED();

        double volSueloPlomo = (plomoTotal*1000*cEfPlomo)/(limiteSueloPlomo*densidadSueloPlomo);
        double volAguaPlomo = (plomoTotal*1000*cEfPlomo)/limiteAguaPlomo;

        double volSueloMercurio = (mercurioTotal*cEfMecurio)/(limiteSueloMercurio*densidadSueloMercurio);
        double volAguaMercurio = (mercurioTotal*cEfMecurio)/limiteAguaMercurio;

        double volSueloCadmio = (cadmioTotal*1000*cEfCadmio)/(limiteSueloCadmio*densidadSueloCadmio);
        double volAguaCadmio = (cadmioTotal*1000*cEfCadmio)/limiteAguaCadmio;

        double volSueloBFR = (bfrTotal*1000*cEfBFR)/(limiteSueloBFR*densidadSueloBFR);
        double volAguaBFR = (bfrTotal*1000*cEfBFR)/limiteAguaBFR;

        datos.setSueloProt(volSueloPlomo + volSueloMercurio + volSueloCadmio + volSueloBFR);
        datos.setAguaProt(volAguaPlomo + volAguaMercurio + volAguaCadmio + volAguaBFR);
    }
}
