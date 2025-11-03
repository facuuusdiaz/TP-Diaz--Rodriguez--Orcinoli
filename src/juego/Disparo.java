package juego;

import java.awt.Color;
import entorno.Entorno;

public class Disparo {

	private double x;
	private double y;
	private double velocidad;
	private double ancho;
	private double alto;
	
	public Disparo(double x, double y) {
		this.x = x + 20; // Nace un poco adelante de la planta
		this.y = y;
		this.velocidad = 3.0; // Velocidad del disparo
		
		this.ancho = 12.0;
		this.alto = 6.0;
	}
	
	public void mover() {
		// Se mueve hacia la derecha
		this.x = this.x + this.velocidad;
	}
	
	public void dibujar(Entorno e) {
		
		double anchoBala = 12.0;
		double altoBala = 6.0;
		e.dibujarRectangulo(this.x, this.y, this.ancho, this.alto, 0, Color.ORANGE);
	}
	
	// Getters para las colisiones
	public double getX() { return this.x; }
	public double getY() { return this.y; }
	public double getAncho() { return this.ancho; }
	public double getAlto() {return this.alto;}

}