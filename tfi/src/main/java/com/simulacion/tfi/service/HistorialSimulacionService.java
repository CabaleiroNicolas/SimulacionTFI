package com.simulacion.tfi.service;

import com.simulacion.tfi.almacenamiento.Memoria;
import com.simulacion.tfi.dto.SimulacionCompletaDTO;
import com.simulacion.tfi.dto.SimulacionGuardada;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class HistorialSimulacionService {

    private final Memoria memoria;

    public SimulacionGuardada guardar(SimulacionCompletaDTO simulacion) {
        SimulacionGuardada guardada = new SimulacionGuardada(LocalDate.now().toString(), simulacion);
        memoria.getHistorialSimulaciones().add(guardada);
        log.info("Simulacion guardada — fecha: {}", guardada.getFecha());
        return guardada;
    }

    public List<SimulacionGuardada> getHistorial() {
        log.info("Consultando historial de simulaciones — {} registros", memoria.getHistorialSimulaciones().size());
        return memoria.getHistorialSimulaciones();
    }
}