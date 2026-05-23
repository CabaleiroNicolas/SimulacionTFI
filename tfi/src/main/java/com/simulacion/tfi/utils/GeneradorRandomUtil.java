package com.simulacion.tfi.utils;

import org.springframework.stereotype.Component;

/**
 * Genera números pseudoaleatorios uniformes en [0, 1) usando el método
 * Congruencial Mixto (Linear Congruential Generator).
 *
 * Fórmula: n_{i+1} = (A * n_i + C) % M
 * Semilla:  últimos dígitos del timestamp actual.
 */
@Component
public class GeneradorRandomUtil {

    private static final long A = 1_664_525L;
    private static final long C = 1_013_904_223L;
    private static final long M = 4_294_967_296L; // 2^32

    private long estado = System.currentTimeMillis() % M;

    /**
     * Genera un u ~ Uniforme(0, 1).
     * Cada llamada avanza el estado interno, por lo que llamadas consecutivas
     * en el mismo milisegundo producen valores distintos.
     */
    public double generarU() {
        estado = (A * estado + C) % M;
        return (double) estado / M;
    }
}
