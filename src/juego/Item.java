package juego;

import java.awt.Color;
import java.awt.Image;
import entorno.Entorno;
import entorno.Herramientas;

public class Item {
    private double x, y;
    private String tipo; // "bueno" o "malo"
    private double radio = 15;
    private Image imagen; 

    public Item(double x, double y, String tipo) {
        this.x = x;
        this.y = y;
        this.tipo = tipo;

        // Cargamos una única imagen para la poción
        // (Así es "sorpresa" si es buena o mala)
        this.imagen = Herramientas.cargarImagen("pocion.png");
    }

    public void dibujar(Entorno e) {
        // Dibujamos la imagen si cargó
        if (this.imagen != null) {
            // Ajustar la escala si es necesario
            e.dibujarImagen(this.imagen, this.x, this.y, 0, 0.15);
        } else {
            // Fallback: si no carga, dibuja los círculos de colores
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
