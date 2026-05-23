package com.simulacion.tfi.service;

import com.simulacion.tfi.dto.CentroDTO;
import com.simulacion.tfi.almacenamiento.Memoria;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CentroService {

    private final Memoria store;

    public CentroDTO getCentro() {
        return store.getCentro();
    }

    public CentroDTO actualizarCentro(CentroDTO request) {
        store.setCentro(request);
        return store.getCentro();
    }
}
