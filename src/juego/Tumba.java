package juego;

import java.awt.Color;
import java.awt.Image;
import entorno.Entorno;
import entorno.Herramientas;

public class Tumba {
    private double x, y;
    private int fila, col;
    private int vida;
    private Image img;
    private double escala = 0.2; // Ajusta la escala de la imagen
    private double ancho = 40;   // Ancho para colisiones
    private double alto = 60;    // Alto para colisiones

    public Tumba(double x, double y, int fila, int col) {
        this.x = x;
        this.y = y;
        this.fila = fila;
        this.col = col;
        this.vida = 15; // Resistencia: necesita 15 disparos para romperse (ajústalo)
        
        // ¡Necesitas una imagen "tumba.png" en tu proyecto!
        this.img = Herramientas.cargarImagen("Tumbaa.png"); 
    }

    public void dibujar(Entorno e) {
        if (this.estaViva()) {
            e.dibujarImagen(img, x, y, 0, escala);
            
            /* // (Opcional: Descomenta para ver la hitbox)
            Color hitboxColor = new Color(0, 0, 255, 100); // Azul transparente
            e.dibujarRectangulo(this.x, this.y, this.ancho, this.alto, 0, hitboxColor);
            */
        }
    }

    public void recibirDisparo() {
        this.vida--;
    }
    
    public void recibirDisparo(int daño) {
		this.vida -= daño;
	}

    public boolean estaViva() {
        return this.vida > 0;
    }
    
    // Getters para colisiones y posición
    public double getX() { return x; }
    public double getY() { return y; }
    public double getAncho() { return ancho; }
    public double getAlto() { return alto; }
    public int getFila() { return fila; }
    public int getCol() { return col; }

    /**
     * Revisa si esta tumba choca con otro objeto rectangular.
     */
    public boolean estaTocando(double otroX, double otroY, double otroAncho, double otroAlto) {
        double miIzquierda = this.x - this.ancho / 2;
        double miDerecha = this.x + this.ancho / 2;
        double miArriba = this.y - this.alto / 2;
        double miAbajo = this.y + this.alto / 2;

        double otroIzquierda = otroX - otroAncho / 2;
        double otroDerecha = otroX + otroAncho / 2;
        double otroArriba = otroY - otroAncho / 2;
        double otroAbajo = otroY + otroAlto / 2;

        return miIzquierda < otroDerecha &&
               miDerecha > otroIzquierda &&
               miArriba < otroAbajo &&
               miAbajo > otroArriba;
    }
}