package com.simulacion.tfi.service;

import com.simulacion.tfi.dto.HistorialPreciosDTO;
import com.simulacion.tfi.dto.PreciosRequestDTO;
import com.simulacion.tfi.dto.PreciosResponseDTO;
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

    public PreciosResponseDTO getPrecios() {
        log.info("Consultando precios vigentes");
        return store.getPrecios();
    }

    public PreciosResponseDTO actualizarPrecios(PreciosRequestDTO request) {
        log.info("Actualizando precios — operador: {}", request.actualizadoPor());
        PreciosResponseDTO anterior = store.getPrecios();

        PreciosResponseDTO nuevo = new PreciosResponseDTO(
                LocalDate.now().toString(),
                request.actualizadoPor(),
                new HashMap<>(request.precios())
        );

        store.getHistorialPrecios().add(new HistorialPreciosDTO(
                nuevo.fechaActualizacion(),
                request.actualizadoPor(),
                new HashMap<>(anterior.precios()),
                new HashMap<>(request.precios())
        ));

        store.setPrecios(nuevo);
        return nuevo;
    }

    public List<HistorialPreciosDTO> getHistorial() {
        return store.getHistorialPrecios();
    }
}
