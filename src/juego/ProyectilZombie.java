package juego;

import java.awt.Color;
import entorno.Entorno;

public class ProyectilZombie {

	private double x;
	private double y;
	private double velocidad;
	private double diametro;
	
	public ProyectilZombie(double x, double y) {
		this.x = x - 20; // Nace un poco adelante (izquierda) del zombie
		this.y = y;
		this.velocidad = -2.0; // Velocidad negativa para ir a la izquierda
		this.diametro = 12;
	}
	
	public void mover() {
		// Se mueve hacia la izquierda
		this.x = this.x + this.velocidad;
	}
	
	public void dibujar(Entorno e) {
		e.dibujarCirculo(this.x, this.y, this.diametro, Color.WHITE); // Bola de nieve blanca
	}
	
	// Getters para las colisiones
	public double getX() { return this.x; }
	public double getY() { return this.y; }
	public double getDiametro() { return this.diametro; }
}