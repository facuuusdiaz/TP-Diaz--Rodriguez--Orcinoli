package juego;

import java.awt.Color;
import entorno.Entorno;
import java.awt.Image;
import entorno.Herramientas;;

public class CartaPlanta {

	private double x,y;
	private double ancho, largo;
	private boolean seleccionada; // Ahora se inicializa en false
	private String nombreCarta;
	private int enfriamiento;
	private int tiempoProxDisponible;
	boolean estaLista;
	private Image imgCarta;
	
	public CartaPlanta (double x, double y, String nombreCarta) {
		this.x = x;
		this.y = y;
		this.ancho = 80.0;
		this.largo = 100.0;
		this.nombreCarta = nombreCarta;
		this.seleccionada = false; // Importante: empieza sin seleccionar
	
		this.estaLista = true;
		this.tiempoProxDisponible = 0;
	
		// --- NUEVO: Cargar la imagen de la carta ---
				if (this.nombreCarta.equals("Rose Blade")) {
					this.imgCarta = Herramientas.cargarImagen("RoseBlade.png");
					this.enfriamiento = 4000;
				}
				else if (this.nombreCarta.equals("Wall-nut")) {
					// ¡Cuidado! Asegúrate de que el nombre sea "wallnut.png" (1 't')
					// Tu código anterior tenía "Wall-nutt.png" (2 't'), lo cual da error.
					this.imgCarta = Herramientas.cargarImagen("Wall-nutt.png"); 
					this.enfriamiento = 10000;
				}	else if (this.nombreCarta.equals("Rose-Bomba")) {
					this.imgCarta = Herramientas.cargarImagen("Rose-Bombaa.png");
					this.enfriamiento = 15000;
				}
		
	}
	
	public void tick (Entorno entorno) {
		if (this.estaLista != true) {
			if (entorno.tiempo() >= this.tiempoProxDisponible) {
				this.estaLista = true;
			}
		}
	}
	
	public void enfriarPlanta(Entorno entorno) {
		this.estaLista = false;
		this.tiempoProxDisponible = entorno.tiempo() + enfriamiento;
	}
	
	/**
	 * Dibuja la carta. Ahora muestra un borde VERDE si está seleccionada,
	 * o un borde GRIS si no lo está.
	 */
public void dibujar(Entorno entorno) {
		
		// 1. Dibujar el fondo/borde de la carta
		Color colorBorde;
		if (this.seleccionada) {
			colorBorde = Color.GREEN; // Borde verde si está seleccionada
		} else {
			colorBorde = new Color(80, 80, 80); // Borde gris si no
		}
		// Dibuja un fondo oscuro
		entorno.dibujarRectangulo(this.x, this.y, this.ancho, this.largo, 0, Color.DARK_GRAY);
		// Dibuja el borde
		entorno.dibujarRectangulo(this.x, this.y, this.ancho, this.largo, 0, colorBorde);
		
		
		// 2. Dibujar la imagen de la planta ENCIMA del fondo
		if (this.imgCarta != null) {
			// Ajustamos la escala para que quepa bien en la tarjeta
			double escala = 0; // Escala para Rose Blade
			
			if (this.nombreCarta.equals("Rose Blade")) {
	             escala = 0.2; // Escala para Rose Blade
	        }
			if (this.nombreCarta.equals("Wall-nut")) {
				escala = 0.1; // Escala para Wall-nut
			}
			
			if (this.nombreCarta.equals("Rose-Bomba")) {
				escala = 0.1; // Misma escala que Rose Blade
			}																								
			// La dibujamos un poco más arriba del centro para dejar espacio para el nombre
			entorno.dibujarImagen(this.imgCarta, this.x, this.y - 10, 0, escala);
		} else {
            // Si la imagen falló al cargar, dibuja un cuadrado amarillo (como antes)
            entorno.dibujarRectangulo(this.x, this.y, this.ancho - 5, this.largo - 5, 0, Color.ORANGE);
        }
		
		// 3. Dibujar el nombre de la planta debajo
		entorno.cambiarFont("Arial", 12, Color.WHITE);
		entorno.escribirTexto(this.nombreCarta, this.x - 30, this.y + this.largo/2 - 10);
		
		
		// 4. Dibujar el enfriamiento (cooldown) encima de todo
		if (this.estaLista != true) {
			long tiempoActual = entorno.tiempo();
			long tiempoRestante = this.tiempoProxDisponible - tiempoActual;
			
			if ( tiempoRestante < 0) {
				tiempoRestante = 0;
			}
			
			double progreso = 1.0 * tiempoRestante / enfriamiento ;
			int alfa = (int) (progreso * 75); // Transparencia
			
			Color enf = new Color (89, 75, 50, alfa); // Un color oscuro semitransparente
			entorno.dibujarRectangulo(this.x, this.y, this.ancho, this.largo, 0, enf);
		}
	}
	
	public boolean estaClickeada(int mx, int my) {
		double minX = this.x - this.ancho / 2;
		double maxX = this.x + this.ancho / 2;
		double minY = this.y - this.largo / 2;
		double maxY = this.y + this.largo / 2;
		
		return (mx >= minX && mx <= maxX && my >= minY && my <= maxY);
	}
	
	public void modificarEnfriamiento(String tipo) {
		// El efecto solo aplica si la carta está actualmente en enfriamiento
		if (!this.estaLista) {
			
			// Define cuánto tiempo se suma o resta (en milisegundos)
			int modificador = 3000; // 3 segundos
			
			if (tipo.equals("bueno")) {
				// Acelera (reduce el tiempo de espera)
				this.tiempoProxDisponible -= modificador;
				
			} else if (tipo.equals("malo")) {
				// Desacelera (aumenta el tiempo de espera)
				this.tiempoProxDisponible += modificador;
			}
		}
	}
	
	// --- Getters y Setters ---
	
	public void setSeleccionada(boolean seleccionada) {
		this.seleccionada = seleccionada;
	}
	
	public boolean estaSeleccionada() {
		return this.seleccionada;
	}
	
	public String getNombreCarta() {
		return this.nombreCarta;
	}
}

