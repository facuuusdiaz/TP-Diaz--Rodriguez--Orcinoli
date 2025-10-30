package juego;

import java.awt.Color;
import java.awt.Image;       // ¡Importar!
import entorno.Entorno;
import entorno.Herramientas; // ¡Importar!

public class Item {
    private double x, y;
    private String tipo; // "bueno" o "malo"
    private double radio = 15;
    private Image imagen; // ¡Variable nueva!

    public Item(double x, double y, String tipo) {
        this.x = x;
        this.y = y;
        this.tipo = tipo;

        // --- ¡MODIFICADO! ---
        // Cargamos una única imagen para la poción
        // (Así es "sorpresa" si es buena o mala)
        // ¡Asegurate de que el nombre "pocion.png" sea exacto!
        this.imagen = Herramientas.cargarImagen("pocion.png");
        // --------------------
    }

    public void dibujar(Entorno e) {
        // --- ¡MODIFICADO! ---
        // Dibujamos la imagen si cargó
        if (this.imagen != null) {
            // Ajustá la escala (ej: 0.05) si es necesario
            e.dibujarImagen(this.imagen, this.x, this.y, 0, 0.15);
        } else {
            // Fallback: si no carga, dibuja los círculos de colores como antes
            Color colorBase;
            Color colorBorde;

            if (this.tipo.equals("bueno")) {
                colorBase = new Color(0, 255, 0, 150); // Verde transparente
                colorBorde = Color.GREEN;
            } else {
                colorBase = new Color(255, 0, 0, 150); // Rojo transparente
                colorBorde = Color.RED;
            }
            e.dibujarCirculo(this.x, this.y, this.radio, colorBase);
            e.dibujarCirculo(this.x, this.y, this.radio, colorBorde);
        }
        // --------------------
    }

    /**
     * Revisa si las coordenadas (mx, my) están dentro del item.
     */
    public boolean estaClickeado(int mx, int my) {
        double distancia = Math.sqrt(Math.pow(mx - this.x, 2) + Math.pow(my - this.y, 2));
        // Aumentamos un poco el radio de clic para que sea más fácil
        return distancia < this.radio * 1.5;
    }

    public String getTipo() {
        return this.tipo;
    }
}
