package com.simulacion.tfi.service;

import com.simulacion.tfi.dto.HistorialPreciosDTO;
import com.simulacion.tfi.dto.PreciosRequestDTO;
import com.simulacion.tfi.dto.PreciosDTO;
import com.simulacion.tfi.almacenamiento.Memoria;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PreciosService {

    private final Memoria store;

    public PreciosDTO getPrecios() {
        log.info("Consultando precios vigentes");
        return store.getPrecios();
    }

    public PreciosDTO actualizarPrecios(PreciosRequestDTO request) {
        log.info("Actualizando precios — operador: {}", request.actualizadoPor());
        PreciosDTO anterior = store.getPrecios();

        PreciosDTO nuevo = new PreciosDTO(
                LocalDate.now().toString(),
                request.actualizadoPor(),
                new HashMap<>(request.precios())
        );

        store.getHistorialPrecios().add(new HistorialPreciosDTO(
                nuevo.getFechaActualizacion(),
                request.actualizadoPor(),
                new HashMap<>(anterior.getPrecios()),
                new HashMap<>(request.precios())
        ));

        store.setPrecios(nuevo);
        return nuevo;
    }

    public List<HistorialPreciosDTO> getHistorial() {
        return store.getHistorialPrecios();
    }
}
