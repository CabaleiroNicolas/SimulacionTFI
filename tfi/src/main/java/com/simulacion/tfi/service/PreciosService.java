package com.simulacion.tfi.service;

import com.simulacion.tfi.dto.PreciosDTO;
import com.simulacion.tfi.almacenamiento.Memoria;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class PreciosService {

    private final Memoria store;

    public PreciosDTO getPrecios() {
        log.info("Consultando precios vigentes");
        return store.getPrecios();
    }

    public PreciosDTO actualizarPrecios(PreciosDTO request) {
        log.info("Actualizando precios");
        store.getPrecios().setPrecios(new HashMap<>(request.getPrecios()));
        return store.getPrecios();
    }

}
