package juego;

import java.awt.Color;
import entorno.Entorno;
import entorno.Herramientas;
import java.awt.Image;

public class ZombieGrinch {

	private double x;
	private double y;
	private double velocidad;
	private double ancho;
	private double alto;
	private double escala;
	
	private int vida;
	private boolean estaAtacando;
	
	private Image img;

	private int retrasoDisparoEnTicks;
	private int contadorTicks;

	
	/**
	 * Constructor para crear un Zombie.
	 */
	public ZombieGrinch(double x, double y, double velocidad, int vida, String nombreImagen, double ancho, double alto, double escala, int retrasoDisparoEnTicks) {
		this.x = x;	
		this.y = y;
		this.velocidad = velocidad;
		this.ancho = ancho; 
		this.alto = alto;   
		this.escala = escala; 
		
		this.vida = vida;
		this.estaAtacando = false;
		
		this.img = Herramientas.cargarImagen(nombreImagen);
		
		this.retrasoDisparoEnTicks = retrasoDisparoEnTicks;
		
		// Inicia el contador en un número negativo aleatorio (para desfasar disparos)
		this.contadorTicks = -(int)(Math.random() * this.retrasoDisparoEnTicks);
	}
	
	/**
	 * Lógica principal del zombie, se ejecuta en cada tick.
	 */
	public ProyectilZombie tick(Entorno e, ManejoPlantas movPlantas) {
	
		if (!this.estaVivo()) {
			return null; // Si estoy muerto, no hago nada
		}
		
		// Mover al zombie
		if (this.estaAtacando != true) {
			this.x = this.x - this.velocidad;
		}

		// Incrementar el contador en cada tick
		this.contadorTicks++;
		
		// Lógica de disparo
		
		// 1. Revisa si hay una planta en la fila
		boolean hayPlanta = movPlantas.hayPlantaEnFila(this.y);
		
		// 2. Decide si disparar
		// NO dispara si:
		// - Está atacando (comiendo)
		// - O el contador no está listo
		// - O NO hay una planta en la fila
		if (this.estaAtacando || this.contadorTicks < this.retrasoDisparoEnTicks || !hayPlanta) {
			return null; // No dispara
		}
		
		// ¡Es hora de disparar!
		this.contadorTicks = 0; // Reinicia el contador de ticks
		return new ProyectilZombie(this.x, this.y);
	}
	
	public void mover() {
		
		if (this.estaAtacando != true) {
			this.x = this.x - this.velocidad;
		}
	}
	
	public void dibujar(Entorno e) {
		
		e.dibujarImagen(this.img, this.x, this.y, 0, this.escala);
	}
	
	public boolean estaTocando(double otroX, double otroY, double otroAncho, double otroAlto) {
		// Calcula los bordes de este zombie
		double miIzquierda = this.x - this.ancho / 2;
		double miDerecha = this.x + this.ancho / 2;
		double miArriba = this.y - this.alto / 2;
		double miAbajo = this.y + this.alto / 2;
		
		// Calcula los bordes del OTRO objeto
		double otroIzquierda = otroX - otroAncho / 2;
		double otroDerecha = otroX + otroAncho / 2;
		double otroArriba = otroY - otroAncho / 2;
		double otroAbajo = otroY + otroAlto / 2;
		
		// Comprueba si hay solapamiento
		return miIzquierda < otroDerecha &&
			   miDerecha > otroIzquierda &&
			   miArriba < otroAbajo &&
			   miAbajo > otroArriba;
	}
	
	// Getters y Setters
	public double getX() { return this.x; }
	public double getY() { return this.y; }
	public double getAncho() { return this.ancho; }
	public double getAlto() { return this.alto; }
	
	public boolean estaVivo() {
		return this.vida > 0;
	}
	
	public void recibirDisparo() {
		this.vida--;
	}
	
	public void setAtacando(boolean atacando) {
		this.estaAtacando = atacando;
	}
	
	/**
	 * Recibe una cantidad específica de daño (para explosiones).
	 */
	public void recibirDisparo(int daño) {
		this.vida -= daño;
	}
	
}