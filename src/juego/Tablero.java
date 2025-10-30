package juego;

import java.awt.*;
import entorno.Entorno;
import entorno.Herramientas;
import java.awt.Image;

public class Tablero {

	private Entorno entorno;
	Image img;
	private double ancho_pantalla = 800.0;
	private double largo_pantalla = 600.0;
	private double menu = 125.0;
	private double largoYMenu = largo_pantalla - menu;
	private int filas = 5;
	private int columnas = 10;
	
	public Tablero (Entorno entorno) {
		this.entorno = entorno;
		this.img = Herramientas.cargarImagen("Regalooo.png");
	}
	
	public void dibujarTablero() {
		double ancho_casilla = this.ancho_pantalla / this.columnas;
		double largo_casilla = this.largoYMenu / this.filas;
		
		for (int i = 0;i < this.filas; i++) {
			for (int j = 0; j < this.columnas; j++) {
				double medioX = (j * ancho_casilla) + (ancho_casilla /2.0);
				double medioY = this.menu + (i * largo_casilla) + (largo_casilla / 2.0);
				
				
				Color colorCasilla;
				if ((i + j) % 2 == 0) {
					colorCasilla = new Color (144, 238, 144);
				} else {
					colorCasilla = new Color (0, 100, 0);
				}
				
				this.entorno.dibujarRectangulo(medioX, medioY, ancho_casilla, largo_casilla, 0.0, colorCasilla);
			}
		}
	}
	
	public void dibujarRegalos() {
		double ancho_casilla = this.ancho_pantalla / this.columnas;
		double largo_casilla = this.largoYMenu / this.filas;
		double cuadrad = 60.0;
		
		
		for (int i = 0; i < this.filas; i++) {
			double medioX = ancho_casilla / 2;
			double medioY = this.menu + (i * largo_casilla) + (largo_casilla / 2);
			
			this.entorno.dibujarImagen(this.img, medioX, medioY, 0, 0.1);
		}
	}
	
	public double getMenuHeight() { return this.menu; }
	public double getAnchoCasilla() { return this.ancho_pantalla / this.columnas; }
	public double getLargoCasilla() { return this.largoYMenu / this.filas; }
	
	/**
	 * Devuelve true si las coordenadas del mouse están dentro 
	 * de los límites del tablero (no en el menú).
	 */
	public boolean estaEnTablero(int mx, int my) {
		return my >= this.menu && my <= this.largo_pantalla &&
			   mx >= 0 && mx <= this.ancho_pantalla;
	}
	
	/**
	 * Convierte coordenadas de píxeles (mx, my) a coordenadas 
	 * de la grilla [fila, columna]. Devuelve null si está fuera.
	 */
	public int[] getCasilla(int mx, int my) {
		if (!estaEnTablero(mx, my)) {
			return null;
		}
		int col = (int) (mx / getAnchoCasilla());
		int fila = (int) ((my - this.menu) / getLargoCasilla());
		
		return new int[]{fila, col};
	}
	
	/**
	 * Devuelve las coordenadas [x, y] exactas del CENTRO de una 
	 * casilla dada por [fila, col].
	 */
	public double[] getCentroCasilla(int fila, int col) {
		if (fila < 0 || fila >= this.filas || col < 0 || col >= this.columnas) {
			return null;
		}
		double x = (col * getAnchoCasilla()) + (getAnchoCasilla() / 2.0);
		double y = this.menu + (fila * getLargoCasilla()) + (getLargoCasilla() / 2.0);
		return new double[]{x, y};
	}

}
