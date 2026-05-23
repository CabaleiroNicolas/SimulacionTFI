package com.simulacion.tfi.service;

import com.simulacion.tfi.dto.CentroDTO;
import com.simulacion.tfi.almacenamiento.Memoria;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CentroService {

    private final Memoria store;

    public CentroDTO getCentro() {
        log.info("Consultando datos del centro");
        return store.getCentro();
    }

    public CentroDTO actualizarCentro(CentroDTO request) {
        log.info("Actualizando datos del centro — responsable: {}", request.responsable());
        store.setCentro(request);
        return store.getCentro();
    }
}
