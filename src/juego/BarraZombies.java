package juego;

import entorno.Entorno;

/**
 * Administra y dibuja las cartas de los zombies en el lado derecho.
 */
public class BarraZombies {
	
	private CartaZombie[] mazoZombies;

	public BarraZombies() {
		this.mazoZombies = new CartaZombie[2]; // Un espacio para cada tipo de zombie

		// --- Posiciones en el lado DERECHO de la pantalla ---
		double y = 60; // Misma altura que las cartas de plantas
		double xZombieNormal = 800 - 60;  // A 60px del borde derecho (740)
		double xZombieRapido = 800 - 160; // A 160px del borde derecho (640)

		// --- Crear las cartas ---
		// (Usa los mismos nombres de archivo y escalas que definiste en ZombiesManejo)
		
		// Carta 1: Zombie Normal
		// (Nombre visual, Archivo de imagen, Escala de dibujo)
		this.mazoZombies[0] = new CartaZombie(xZombieNormal, y, "Normal", "ZombieNormal.png", 0.08); 
		
		// Carta 2: Zombie Rápido
		this.mazoZombies[1] = new CartaZombie(xZombieRapido, y, "Rápido", "Grinchh.png", 0.075); 
	}

	/**
	 * Dibuja todas las cartas de zombies.
	 */
	public void dibujar(Entorno entorno) {
		for (CartaZombie carta : this.mazoZombies) {
			if (carta != null) {
				carta.dibujar(entorno);
			}
		}
	}
	
	// Nota: No necesitamos un 'tick()' porque estas cartas no tienen enfriamiento.
}