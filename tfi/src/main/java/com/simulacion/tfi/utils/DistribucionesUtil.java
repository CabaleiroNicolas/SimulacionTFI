package com.simulacion.tfi.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Transforma números uniformes u ~ U(0,1) en muestras de distribuciones
 * de probabilidad específicas.
 */
@Component
@RequiredArgsConstructor
public class DistribucionesUtil {

    private final GeneradorRandomUtil rng;

    /**
     * Normal(media, desviacion) — algoritmo de la cátedra (Clase 5).
     * Aproximación por Teorema Central del Límite con 12 uniformes:
     *   x = desviacion * (sum(u1..u12) - 6) + media
     */
    public double Normal(double media, double desviacion) {
        double sum = 0;
        for (int i = 1; i <= 12; i++) {
            sum += rng.generarU();
        }
        return desviacion * (sum - 6) + media;
    }

    /**
     * Poisson(a) — algoritmo de la cátedra (Clase 5).
     *   b = e^(-a)
     *   mientras p > b: p = p * u, x = x + 1
     */
    public double Poisson(double a) {
        double b = Math.exp(-a);
        int x = 0;
        double p = 1.0;
        while (p > b) {
            p *= rng.generarU();
            x++;
        }
        return x;
    }

    /**
     * Uniforme(a, b) = a + (b - a) * u
     */
    public double Uniforme(double a, double b) {
        double u = rng.generarU();
        return a + (b - a) * u;
    }
}
