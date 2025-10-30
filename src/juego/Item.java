package juego;

import java.awt.Color;
import entorno.Entorno;

public class Item {
    private double x, y;
    private String tipo; // "bueno" o "malo"
    private double radio = 15; // El tamaño del item para dibujarlo y clickearlo

    public Item(double x, double y, String tipo) {
        this.x = x;
        this.y = y;
        this.tipo = tipo;
    }

    public void dibujar(Entorno e) {
        Color colorBase;
        Color colorBorde;

        if (this.tipo.equals("bueno")) {
            colorBase = new Color(0, 255, 0, 150); // Verde transparente
            colorBorde = Color.GREEN;
        } else {
            colorBase = new Color(255, 0, 0, 150); // Rojo transparente
            colorBorde = Color.RED;
        }

        // Dibuja el item como un círculo brillante
        e.dibujarCirculo(this.x, this.y, this.radio, colorBase);
        e.dibujarCirculo(this.x, this.y, this.radio, colorBorde);
    }

    /**
     * Revisa si las coordenadas (mx, my) están dentro del item.
     */
    public boolean estaClickeado(int mx, int my) {
        // Fórmula de distancia entre dos puntos
        double distancia = Math.sqrt(Math.pow(mx - this.x, 2) + Math.pow(my - this.y, 2));
        return distancia < this.radio;
    }

    public String getTipo() {
        return this.tipo;
    }
}
