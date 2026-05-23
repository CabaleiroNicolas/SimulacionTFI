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
     * Normal(media, desviacion) via transformada de Box-Muller.
     * Z = sqrt(-2 * ln(u1)) * cos(2π * u2)
     * X = media + desviacion * Z
     */
    public double Normal(double media, double desviacion) {
        double u1, u2;
        do { u1 = rng.generarU(); } while (u1 == 0.0); // evita ln(0)
        u2 = rng.generarU();
        double z = Math.sqrt(-2.0 * Math.log(u1)) * Math.cos(2.0 * Math.PI * u2);
        return media + desviacion * z;
    }

    /**
     * Poisson(lambda) via método de la multiplicación de uniformes.
     * Cuenta cuántos u's se necesitan hasta que su producto caiga por debajo de e^(-lambda).
     */
    public double Poisson(double lambda) {
        double limite = Math.exp(-lambda);
        int k = 0;
        double p = 1.0;
        do {
            k++;
            p *= rng.generarU();
        } while (p > limite);
        return k - 1;
    }

    /**
     * Uniforme(a, b) = a + (b - a) * u
     */
    public double Uniforme(double a, double b) {
        double u = rng.generarU();
        return a + (b - a) * u;
    }
}
