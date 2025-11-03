package juego;

import entorno.Entorno;

/**
 * Administra y dibuja las cartas de los zombies en el lado derecho.
 */
public class BarraZombies {
	
	private CartaZombie[] mazoZombies;

	public BarraZombies() {
		this.mazoZombies = new CartaZombie[2]; // Un espacio para cada tipo de zombie

		// Misma altura que las cartas de plantas
		double y = 60; 
		// A 60px del borde derecho (740)
		double xZombieNormal = 800 - 60;  
		// A 160px del borde derecho (640)
		double xZombieRapido = 800 - 160; 

		
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
	
	//No necesitamos un 'tick()' porque estas cartas no tienen enfriamiento.
}