package juego;
import entorno.Entorno;

public class ZombiesManejo {

	private static int max_zombies = 50;
	private ZombieGrinch [] zombiesEnJuego;

	// --- 1. LÓGICA DE TIEMPO "MÍA" (LA QUE QUERÍAS CONSERVAR) ---
	// Usamos un 'static final' para el intervalo
	private static final int TIEMPO_ENTRE_ZOMBIES = 3000; // 6 segundos entre zombies
	// 'int' para el tiempo, como en el PDF
	private int tiempoProximoZombie; 
	// (Se eliminaron 'tiempoInicialJuego' e 'inicializado')
	// -----------------------------------------------------------

	private int zombiesMuertos;
	private static int MAX_PROYECTILES = 200;
	private ProyectilZombie[] proyectiles;
	private Tablero tablero;
	private boolean spawnsDetenidos = false;
	
	
	public ZombiesManejo (Tablero tablero) {
		this.tablero = tablero;
		this.zombiesEnJuego = new ZombieGrinch [max_zombies];
		this.proyectiles = new ProyectilZombie[MAX_PROYECTILES];
		this.zombiesMuertos = 0;

		// --- 2. INICIALIZACIÓN DE TIEMPO "MÍA" ---
		// El primer zombie aparece de inmediato (o tras un breve retraso)
		this.tiempoProximoZombie = 5000; 
		// (Se eliminó toda la lógica de 'inicializado = false', etc.)
		// ------------------------------------
	}

	public void tick (Entorno entorno, Tablero tablero, ManejoPlantas movPlantas) {

		// --- 3. LÓGICA DE 'TICK' "MÍA" ---
		// (Se eliminó el bloque 'if (!this.inicializado)')
		// --------------------------------------------------------------

		if (entorno.tiempo() >= this.tiempoProximoZombie && !this.spawnsDetenidos) {
			this.generarZombie(this.tablero); 
			this.tiempoProximoZombie = entorno.tiempo() + TIEMPO_ENTRE_ZOMBIES;
		}
		
		// 1. Mover zombies y generar disparos (Lógica "TUYA" conservada)
		for (int i = 0; i < this.zombiesEnJuego.length; i++) {
			if (this.zombiesEnJuego[i] != null) {
				ProyectilZombie p = this.zombiesEnJuego[i].tick(entorno, movPlantas);
				if (p != null) {
					this.agregarProyectil(p);
				}
			}
		}

		// 2. GENERAR NUEVOS ZOMBIES (Lógica de tiempo "MÍA")
		// Comprueba si ya pasó el tiempo programado
		if (entorno.tiempo() >= this.tiempoProximoZombie) {
			this.generarZombie(this.tablero); // Usa tu método de generar

			// Programa el siguiente spawn
			this.tiempoProximoZombie = entorno.tiempo() + TIEMPO_ENTRE_ZOMBIES;
		}
		// -------------------------------------------------

		// 3. MOVER PROYECTILES (Lógica "TUYA" conservada)
		for (int i = 0; i < this.proyectiles.length; i++) {
			if (this.proyectiles[i] != null) {
				this.proyectiles[i].mover();
				if (this.proyectiles[i].getX() < 0) {
					this.proyectiles[i] = null;
				}
			}
		}
	}

	// --- 4. TODO EL RESTO DE TU CÓDIGO SE CONSERVA INTACTO ---

	public void dibujar(Entorno entorno) {
		// Dibuja zombies
		for (int i = 0; i < this.zombiesEnJuego.length; i++) {
			if (this.zombiesEnJuego[i] != null && this.zombiesEnJuego[i].estaVivo()) {
				this.zombiesEnJuego[i].dibujar(entorno);
			}
		}
		// Dibuja proyectiles
		for (int i = 0; i < this.proyectiles.length; i++) {
			if (this.proyectiles[i] != null) {
				this.proyectiles[i].dibujar(entorno);
			}
		}
	}

	// Tu método para generar zombies de diferentes tipos
	private void generarZombie (Tablero tablero) {
		int filaAleatoria = (int) (Math.random() * 5);
		double y = tablero.getCentroCasilla (filaAleatoria, 0)[1];
		double x = 850.0;
		ZombieGrinch nuevoZombie;
		double chance = Math.random();
		if (chance < 0.7) { // Normal
			nuevoZombie = new ZombieGrinch(x, y, 0.25, 10, "ZombieNormal.png", 50.0, 80.0, 0.1, 240);
		} else { // Rápido
			nuevoZombie = new ZombieGrinch(x, y, 0.45, 5, "Grinchh.png", 30.0, 60.0, 0.075, 150);
		}
		this.agregarZombie(nuevoZombie);
	}

	private boolean agregarZombie (ZombieGrinch zombie) {
		for (int i = 0; i < this.zombiesEnJuego.length; i++){
			if (this.zombiesEnJuego[i] == null) {
				this.zombiesEnJuego[i] = zombie;
				return true;
			}
		}
		return false;
	}

	public void detenerSpawns() {
		this.spawnsDetenidos = true;
	}
	
	public ZombieGrinch generarJefeFinal(Tablero tablero) {
		// Fila 2 (la del medio) para más drama
		int filaJefe = 2; 
		double y = tablero.getCentroCasilla(filaJefe, 0)[1];
		double x = 850.0;
		
		// Vida: 300, Velocidad: 0.10, Escala: 0.25, Hitbox: 100x150
		ZombieGrinch jefe = new ZombieGrinch(x, y, 0.10, 100, "ZombieNormal.png", 100.0, 150.0, 0.25, 999999);
		
		this.agregarZombie(jefe);
		return jefe; // Devuelve la instancia del jefe
	}
	
	public ZombieGrinch[] getZombies() { return this.zombiesEnJuego; }

	// Tu método que cuenta las muertes
	public void removerZombie(int indice) {
		if (indice >= 0 && indice < this.zombiesEnJuego.length) {
			if (this.zombiesEnJuego[indice] != null) {
				this.zombiesEnJuego[indice] = null;
				this.zombiesMuertos++;
			}
		}
	}

	// Tus métodos para proyectiles
	private void agregarProyectil(ProyectilZombie p) {
		for (int i = 0; i < this.proyectiles.length; i++) {
			if (this.proyectiles[i] == null) {
				this.proyectiles[i] = p;
				return;
			}
		}
	}

	public ProyectilZombie[] getProyectiles() { return this.proyectiles; }

	public void removerProyectil(int indice) {
		if (indice >= 0 && indice < this.proyectiles.length) {
			this.proyectiles[indice] = null;
		}
	}

	// Tus métodos de estado
	public int getTotalAsesinados() { return this.zombiesMuertos; }

    public boolean hayZombieEnFila(double yFila, double limiteX) {
        for (int i = 0; i < this.zombiesEnJuego.length; i++) {
	        ZombieGrinch z = this.zombiesEnJuego[i];
	        if (z != null && z.estaVivo() && z.getY() == yFila) {
	            if (z.getX() <= limiteX) {
	            	return true;
	            }
	        }
        }
    return false;
}
}