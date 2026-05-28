package com.simulacion.tfi.almacenamiento;

import com.simulacion.tfi.dto.CentroDTO;
import com.simulacion.tfi.dto.PreciosDTO;
import com.simulacion.tfi.dto.SimulacionGuardada;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class Memoria {

    // ── Precios ───────────────────────────────────────────────────────────────

    private PreciosDTO precios = new PreciosDTO(
            new HashMap<>(Map.ofEntries(
                    Map.entry("vidrioPanelCRT",    85.0),
                    Map.entry("vidrioPanelLCDLED", 117.0),
                    Map.entry("plastABS",          160.0),
                    Map.entry("plastPC",           160.0),
                    Map.entry("acero",              95.0),
                    Map.entry("cobre",            6750.0),
                    Map.entry("aluminio",         1500.0),
                    Map.entry("placasPCB",        2250.0),
                    Map.entry("oro",            215000.0),
                    Map.entry("plata",            3600.0),
                    Map.entry("paladio",         72500.0),
                    Map.entry("estanio",             2.3),
                    Map.entry("niquel",              3.0),
                    Map.entry("indio",          290000.0),
                    Map.entry("lc",                  3.5),
                    Map.entry("tirasLed",            0.75)
            ))
    );


    // ── Centro ────────────────────────────────────────────────────────────────

    private CentroDTO centro = new CentroDTO(
            "Centro Norte",
            "30-12345678-9",
            "Av. Siempreviva 742",
            "Juan Pérez",
            50
    );

    // ── Accessors: Precios ────────────────────────────────────────────────────

    public PreciosDTO getPrecios() {
        return precios;
    }

    // ── Historial Simulaciones ────────────────────────────────────────────────

    private final List<SimulacionGuardada> historialSimulaciones = new ArrayList<>();

    public List<SimulacionGuardada> getHistorialSimulaciones() {
        return historialSimulaciones;
    }

    // ── Accessors: Centro ─────────────────────────────────────────────────────

    public CentroDTO getCentro() {
        return centro;
    }

    public void setCentro(CentroDTO centro) {
        this.centro = centro;
    }
}
