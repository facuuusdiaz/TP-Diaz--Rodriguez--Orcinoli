package juego;

import java.awt.*;
import entorno.Entorno;
public class Tablero {

	private Entorno entorno;
	private double ancho_pantalla = 800.0;
	private double largo_pantalla = 600.0;
	private double menu = 125.0;
	private double largoYMenu = largo_pantalla - menu;
	private int filas = 5;
	private int columnas = 10;
	
	public Tablero (Entorno entorno) {
		this.entorno = entorno;
	}
	
	public void dibujarTablero() {
		this.entorno = entorno;
		double ancho_casilla = this.ancho_pantalla / this.columnas;
		double largo_casilla = this.largoYMenu / this.filas;
		
		for (int i = 0;i < this.filas; i++) {
			for (int j = 0; j < this.columnas; j++) {
				double medioX = (j * ancho_casilla) + (ancho_casilla /2.0);
				double medioY = this.menu + (i * largo_casilla) + (largo_casilla / 2.0);
				
				
				Color colorCasilla;
				if ((i + j) % 2 == 0) {
					colorCasilla = Color.WHITE;
				} else {
					colorCasilla = Color.RED;
				}
				
				this.entorno.dibujarRectangulo(medioX, medioY, ancho_casilla, largo_casilla, 0.0, colorCasilla);
			}
		}
	}
	
	public void dibujarRegalos() {
		this.entorno = entorno;
		double ancho_casilla = this.ancho_pantalla / this.columnas;
		double largo_casilla = this.largoYMenu / this.filas;
		double cuadrad = 60.0;
		
		for (int i = 0; i < this.filas; i++) {
			double medioX = ancho_casilla / 2;
			double medioY = this.menu + (i * largo_casilla) + (largo_casilla / 2);
			
			this.entorno.dibujarRectangulo(medioX, medioY, cuadrad, cuadrad, 0.0, Color.BLACK);
		}
	}
}
