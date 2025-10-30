package juego;

import entorno.Entorno;

public class BarraCartas {

	private CartaPlanta [] mazo;
	
	public BarraCartas () {
		this.mazo = new CartaPlanta[3];
		this.mazo [0] = new CartaPlanta (60.0, 60.0, "Rose Blade");
		this.mazo [1] = new CartaPlanta (160.0, 60.0, "Wall-nut");
		this.mazo [2] = new CartaPlanta (260.0, 60.0, "Rose-Bomba");
	}
	
	public void tick (Entorno entorno) {
		for (CartaPlanta carta : this.mazo) {
			if (carta != null) {
				carta.tick(entorno);
			}
		}
	}
	
	public void dibujar (Entorno entorno) {
		for (CartaPlanta carta : this.mazo) {
			if (carta != null) {
				carta.dibujar(entorno);
			}
		}
	}
	
	/**
	 * Revisa todas las cartas en el mazo y devuelve la carta que 
	 * fue clickeada según las coordenadas (mx, my) del mouse.
	 * Devuelve null si no se clickeó ninguna.
	 */
	public CartaPlanta getCartaClickeada(int mx, int my) {
		for (CartaPlanta carta : this.mazo) {
			if (carta != null && carta.estaClickeada(mx, my) && carta.estaLista) {
				return carta;
			}
		}
		return null; // No se encontró ninguna carta en esa posición
	}
	
	/**
	 * Actualiza el estado de selección de todas las cartas.
	 * Pone en 'true' la carta seleccionada y en 'false' todas las demás.
	 * Si se pasa 'null', deselecciona todas.
	 */
	public void seleccionarCarta(CartaPlanta cartaSeleccionada) {
		for (CartaPlanta carta : this.mazo) {
			if (carta != null) {
				// La carta estará seleccionada solo si es la misma que la pasada por parámetro
				carta.setSeleccionada(carta == cartaSeleccionada);
			}
		}
	}
	
	public void aplicarEfectoItem(String tipo) {
		for (CartaPlanta carta : this.mazo) {
			if (carta != null) {
				// Llama a un nuevo método en CartaPlanta
				carta.modificarEnfriamiento(tipo);
			}
		}
	}
}