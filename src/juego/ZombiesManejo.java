package juego;
import entorno.Entorno;

public class ZombiesManejo {

	private static int max_zombies = 50;
	private ZombieGrinch jefeFinalEnManejo;
	private ZombieGrinch [] zombiesEnJuego;

	// Intervalo de tiempo para generar zombies
	private static final int TIEMPO_ENTRE_ZOMBIES = 3000; // 3 segundos
	private int tiempoProximoZombie; 

	private int zombiesMuertos;
	private static int MAX_PROYECTILES = 200;
	private ProyectilZombie[] proyectiles;
	private Tablero tablero;
	private boolean spawnsDetenidos = false;
	
	
	public ZombiesManejo (Tablero tablero) {
		this.tablero = tablero;
		this.jefeFinalEnManejo = null; // Inicializar a null
		this.zombiesEnJuego = new ZombieGrinch [max_zombies];
		this.proyectiles = new ProyectilZombie[MAX_PROYECTILES];
		this.zombiesMuertos = 0;

		// El primer zombie aparece a los 5 segundos
		this.tiempoProximoZombie = 5000; 
	}

	public void tick (Entorno entorno, Tablero tablero, ManejoPlantas movPlantas) {

		// Genera un nuevo zombie si ha pasado el tiempo
		if (entorno.tiempo() >= this.tiempoProximoZombie && !this.spawnsDetenidos) {
			this.generarZombie(this.tablero); 
			this.tiempoProximoZombie = entorno.tiempo() + TIEMPO_ENTRE_ZOMBIES;
		}
		
		// Actualiza el estado de cada zombie y genera sus proyectiles
		for (int i = 0; i < this.zombiesEnJuego.length; i++) {
			if (this.zombiesEnJuego[i] != null) {
				ProyectilZombie p = this.zombiesEnJuego[i].tick(entorno, movPlantas);
				if (p != null) {
					this.agregarProyectil(p);
				}
			}
		}

		// Mueve los proyectiles de los zombies
		for (int i = 0; i < this.proyectiles.length; i++) {
			if (this.proyectiles[i] != null) {
				this.proyectiles[i].mover();
				if (this.proyectiles[i].getX() < 0) { // Si sale de pantalla
					this.proyectiles[i] = null;
				}
			}
		}
	}

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

	/**
	 * Genera un zombie (70% normal, 30% rápido) en una fila aleatoria.
	 */
	private void generarZombie (Tablero tablero) {
		int filaAleatoria = (int) (Math.random() * 5);
		double y = tablero.getCentroCasilla (filaAleatoria, 0)[1];
		double x = 850.0; // Fuera de pantalla
		ZombieGrinch nuevoZombie;
		double chance = Math.random();
		if (chance < 0.7) { // Normal
			nuevoZombie = new ZombieGrinch(x, y, 0.25, 10, "ZombieNormal.png", 50.0, 80.0, 0.1, 240);
		} else { // Rápido
			nuevoZombie = new ZombieGrinch(x, y, 0.45, 5, "Grinchh.png", 30.0, 60.0, 0.075, 150);
		}
		this.agregarZombie(nuevoZombie);
	}

	/**
	 * Agrega un zombie al array en el primer espacio disponible.
	 */
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
	
	/**
	 * Crea y añade al jefe final al juego.
	 */
	public ZombieGrinch generarJefeFinal(Tablero tablero) {
		// El jefe aparece centrado verticalmente en el tablero
		double yCentroAreaJuego = tablero.getMenuHeight() + (tablero.getLargoCasilla() * tablero.getFilas() / 2.0);
		double x = 850.0; // Fuera de pantalla
		
		// El hitbox del jefe ocupa toda el área vertical del tablero
		double altoZonaJuego = tablero.getLargo_pantalla() - tablero.getMenuHeight();

		String imagenJefe = "ZombieFinall.png"; 
		
		double anchoBaseHitbox = 100.0; 
		double altoBaseHitbox = altoZonaJuego;
		double escalaDibujo = 0.5; // Ajustar escala de la imagen

		// Vida: 300, Velocidad: 0.10, Retraso de disparo: 999999 (no dispara)
		ZombieGrinch jefe = new ZombieGrinch(x, yCentroAreaJuego, 0.10, 300, imagenJefe, anchoBaseHitbox, altoBaseHitbox, escalaDibujo, 999999);
		
		this.agregarZombie(jefe);
		return jefe; // Devuelve la instancia del jefe
	}
	
	public ZombieGrinch[] getZombies() { return this.zombiesEnJuego; }

	/**
	 * Remueve un zombie del array y suma al contador de muertes.
	 */
	public void removerZombie(int indice) {
		if (indice >= 0 && indice < this.zombiesEnJuego.length) {
			if (this.zombiesEnJuego[indice] != null) {
				this.zombiesEnJuego[indice] = null;
				this.zombiesMuertos++;
			}
		}
	}

	// --- Métodos de Proyectiles ---

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
	
	public void setJefeFinal(ZombieGrinch jefe) {
	    this.jefeFinalEnManejo = jefe;
	}

	// --- Métodos de Estado ---
	
	public int getTotalAsesinados() { return this.zombiesMuertos; }

	/**
	 * Verifica si hay una amenaza (zombie o jefe) en una fila o área.
	 * El jefe se considera una amenaza para todas las filas.
	 */
	public boolean hayZombieEnFila(double yFila, double limiteX) {
        for (int i = 0; i < this.zombiesEnJuego.length; i++) {
	        ZombieGrinch z = this.zombiesEnJuego[i];
	        if (z != null && z.estaVivo()) { 
                // Si el zombie actual es el jefe final Y está dentro del límite X
                if (this.jefeFinalEnManejo != null && z == this.jefeFinalEnManejo && z.getX() <= limiteX) {
                    return true; // El jefe está presente y es una amenaza
                }
                // Si es un zombie normal y está en la fila y dentro del límite X
                else if (z != this.jefeFinalEnManejo && z.getY() == yFila && z.getX() <= limiteX) {
	            	return true; // Un zombie normal en esta fila es una amenaza
	            }
	        }
        }
        return false;
    }
}