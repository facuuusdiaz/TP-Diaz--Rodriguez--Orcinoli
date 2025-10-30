package juego;

import entorno.Entorno;

public class ManejoPlantas {

    private static final int MAX_PLANTAS = 50;
    private PlantaColocada[] plantasEnTablero;

    private static final int MAX_DISPAROS = 200;
    private Disparo[] disparos;

    public ManejoPlantas() {
        this.plantasEnTablero = new PlantaColocada[MAX_PLANTAS];
        this.disparos = new Disparo[MAX_DISPAROS];
    }

    public void tick(Entorno e, ZombiesManejo movZombies, ZombieGrinch jefeFinal) { // Correcto (2 args)
        // Pensar en plantas y generar disparos
        for (int i = 0; i < this.plantasEnTablero.length; i++) {
            PlantaColocada planta = this.plantasEnTablero[i];
            if (planta != null && planta.estaViva()) {
                Disparo d = planta.tick(e, movZombies, jefeFinal); // Pasa movZombies
                if (d != null) {
                    this.agregarDisparo(d);
                }
            }
        }

        // Mover disparos
        for (int i = 0; i < this.disparos.length; i++) {
            if (this.disparos[i] != null) {
                this.disparos[i].mover();
                if (this.disparos[i].getX() > e.ancho()) {
                    this.disparos[i] = null;
                }
            }
        }
    }

    private void agregarDisparo(Disparo disparo) {
        for (int i = 0; i < this.disparos.length; i++) {
            if (this.disparos[i] == null) {
                this.disparos[i] = disparo;
                return;
            }
        }
    }

    private boolean agregarPlanta(PlantaColocada planta) {
        for (int i = 0; i < this.plantasEnTablero.length; i++) {
            if (this.plantasEnTablero[i] == null) {
                this.plantasEnTablero[i] = planta;
                return true;
            }
        }
        System.out.println("¡No hay más espacio para plantar!");
        return false;
    }

    /**
     * CAMBIO: Ahora también recibe manejoTumbas para validar
     */
    public boolean plantar(CartaPlanta carta, int mx, int my, Tablero tablero, ManejoTumbas manejoTumbas) { // Recibe 5 args
        if (tablero.estaEnTablero(mx, my)) {

            int[] casilla = tablero.getCasilla(mx, my);
            if (casilla == null) return false; // Clic fuera de la grilla

            // Validaciones
            if (casilla[1] == 0) { return false; } // Columna regalos
            if (this.hayPlantaEn(casilla[0], casilla[1])) { return false; } // Ya hay planta
            if (manejoTumbas.getTumbaEnCasilla1(casilla[0], casilla[1]) != null) { return false; } // Hay tumba

            double[] centroCasilla = tablero.getCentroCasilla(casilla[0], casilla[1]);
            String nombre = carta.getNombreCarta();

            // Llama al constructor de 5 argumentos
            PlantaColocada nuevaPlanta = new PlantaColocada(centroCasilla[0], centroCasilla[1], casilla[0], casilla[1], nombre);

            return this.agregarPlanta(nuevaPlanta);
        }
        return false;
    }


    public void dibujar(Entorno entorno) {
        // Dibuja plantas
        for (int i = 0; i < this.plantasEnTablero.length; i++) {
            if (this.plantasEnTablero[i] != null) {
                this.plantasEnTablero[i].dibujar(entorno);
            }
        }
        // Dibuja disparos
        for (int i = 0; i < this.disparos.length; i++) {
            if (this.disparos[i] != null) {
                this.disparos[i].dibujar(entorno);
            }
        }
    }

    public PlantaColocada getPlantaClickeada(double mx, double my) {
        for (PlantaColocada p : this.plantasEnTablero) {
            if (p != null && p.estaViva() && p.estaClickeada(mx, my)) {
                return p;
            }
        }
        return null;
    }

    public boolean hayPlantaEn(int fila, int col) {
        for (PlantaColocada p : this.plantasEnTablero) {
            if (p != null && p.estaViva() && p.getFila() == fila && p.getCol() == col) {
                return true;
            }
        }
        return false;
    }

    // --- Método usado por el zombie ---
	public boolean hayPlantaEnFila(double yFila) {
	    for (PlantaColocada p : this.plantasEnTablero) {
	        if (p != null && p.estaViva() && p.getY() == yFila) {
	            return true;
	        }
	    }
	    return false;
	}

    // --- Getters y Setters para COLISIONES ---
    public PlantaColocada[] getPlantas() { return this.plantasEnTablero; }
    public Disparo[] getDisparos() { return this.disparos; }

    public void removerPlanta(int indice) {
         if (indice >= 0 && indice < this.plantasEnTablero.length) {
            this.plantasEnTablero[indice] = null;
        }
    }
    public void removerDisparo(int indice) {
        if (indice >= 0 && indice < this.disparos.length) {
            this.disparos[indice] = null;
        }
    }
    
   
}

