package com.simulacion.tfi.almacenamiento;

import com.simulacion.tfi.dto.CentroDTO;
import com.simulacion.tfi.dto.HistorialPreciosDTO;
import com.simulacion.tfi.dto.PreciosResponseDTO;
import com.simulacion.tfi.dto.TiemposProcesamientoDTO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class Memoria {

    // ── Precios ───────────────────────────────────────────────────────────────

    private PreciosResponseDTO precios = new PreciosResponseDTO(
            "2026-05-18",
            "Juan Pérez",
            new HashMap<>(Map.of(
                    "cobre",    8500.0,
                    "aluminio", 2300.0,
                    "plastico",  400.0,
                    "vidrio",    150.0
            ))
    );

    private final List<HistorialPreciosDTO> historialPrecios = new ArrayList<>();

    // ── Centro ────────────────────────────────────────────────────────────────

    private CentroDTO centro = new CentroDTO(
            "Centro Norte",
            "30-12345678-9",
            "Av. Siempreviva 742",
            "Juan Pérez",
            new TiemposProcesamientoDTO(25, 15, 10)
    );

    // ── Accessors: Precios ────────────────────────────────────────────────────

    public PreciosResponseDTO getPrecios() {
        return precios;
    }

    public void setPrecios(PreciosResponseDTO precios) {
        this.precios = precios;
    }

    public List<HistorialPreciosDTO> getHistorialPrecios() {
        return historialPrecios;
    }

    // ── Accessors: Centro ─────────────────────────────────────────────────────

    public CentroDTO getCentro() {
        return centro;
    }

    public void setCentro(CentroDTO centro) {
        this.centro = centro;
    }
}
