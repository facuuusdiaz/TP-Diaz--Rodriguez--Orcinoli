package juego;

import java.awt.Color;
import java.awt.Image;
import entorno.Entorno;
import entorno.Herramientas;

/**
 * Esta clase representa la "carta" visual para un tipo de zombie 
 * en la barra de la derecha. Es solo para mostrar información.
 */
public class CartaZombie {
	private double x, y;
	private double ancho = 80.0;
	private double largo = 100.0;
	private String nombreZombie;
	private Image imgCarta;
	private double escala; // Escala para la imagen

	public CartaZombie(double x, double y, String nombre, String archivoImagen, double escala) {
		this.x = x;
		this.y = y;
		this.nombreZombie = nombre;
		this.escala = escala;
		
		// Carga la imagen del zombie
		this.imgCarta = Herramientas.cargarImagen(archivoImagen);
	}

	public void dibujar(Entorno entorno) {
		// 1. Dibujar el fondo/borde de la carta
		entorno.dibujarRectangulo(this.x, this.y, this.ancho, this.largo, 0, Color.DARK_GRAY);
		// Un borde rojo oscuro para diferenciar
		entorno.dibujarRectangulo(this.x, this.y, this.ancho, this.largo, 0, Color.BLUE); 

		// 2. Dibujar la imagen del zombie
		if (this.imgCarta != null) {
			entorno.dibujarImagen(this.imgCarta, this.x, this.y - 10, 0, this.escala);
		} else {
			// Si falla, dibuja un cuadrado rojo
			entorno.dibujarRectangulo(this.x, this.y, this.ancho - 5, this.largo - 5, 0, Color.RED);
		}

		// 3. Dibujar el nombre
		entorno.cambiarFont("Arial", 12, Color.WHITE);
		entorno.escribirTexto(this.nombreZombie, this.x - 30, this.y + this.largo/2 - 10);
	}
}
