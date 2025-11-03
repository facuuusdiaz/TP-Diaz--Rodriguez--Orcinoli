package juego;

import entorno.Entorno;

public class ManejoTumbas {
    private static final int MAX_TUMBAS = 50; // Máximo de tumbas a la vez
    private Tumba[] tumbasEnTablero;
    private Tablero tablero; // Necesita saber sobre el tablero

    public ManejoTumbas(Tablero tablero) {
        this.tumbasEnTablero = new Tumba[MAX_TUMBAS];
        this.tablero = tablero; // Guarda la referencia al tablero
    }

    /**
     * Intenta agregar una tumba en una casilla específica.
     */
    public boolean agregarTumba(int fila, int col) {
        // No poner tumbas fuera del tablero o en la columna de regalos
        if (fila < 0 || fila >= 5 || col <= 0 || col >= 10) { 
            return false;
        }
        
        // No poner una tumba donde ya hay otra
        if (getTumbaEnCasilla1(fila, col) != null) {
            return false;
        }

        // Busca un espacio libre en el array
        for (int i = 0; i < this.tumbasEnTablero.length; i++) {
            if (this.tumbasEnTablero[i] == null) {
                // Calcula el centro de la casilla para la nueva tumba
                double[] centro = tablero.getCentroCasilla(fila, col);
                this.tumbasEnTablero[i] = new Tumba(centro[0], centro[1], fila, col);
                return true; // Se agregó con éxito
            }
        }
        System.out.println("No hay espacio para más tumbas!");
        return false; // No había espacio
    }

    /**
     * Dibuja todas las tumbas activas.
     */
    public void dibujar(Entorno entorno) {
        for (Tumba t : this.tumbasEnTablero) {
            if (t != null && t.estaViva()) {
                t.dibujar(entorno);
            }
        }
    }
 
    /**
     * Devuelve la tumba en una casilla específica, o null si no hay.
     */
    public Tumba getTumbaEnCasilla1(int fila, int col) {
        for (Tumba t : this.tumbasEnTablero) {
            if (t != null && t.estaViva() && t.getFila() == fila && t.getCol() == col) {
                return t;
            }
        }
        return null;
    }    
    
    /**
     * Marca una tumba como eliminada (la pone en null) por índice.
     */
    public void removerTumba(int indice) {
         if (indice >= 0 && indice < this.tumbasEnTablero.length) {
            this.tumbasEnTablero[indice] = null;
        }
    }
    
    /**
     * Busca una tumba específica y la remueve.
     */
     public void removerTumba(Tumba tumbaARemover) {
        for (int i = 0; i < tumbasEnTablero.length; i++) {
            if (tumbasEnTablero[i] == tumbaARemover) {
                tumbasEnTablero[i] = null;
                return; // Sale una vez que la encuentra y remueve
            }
        }
    }
     
     public Tumba[] getTumbas() {
    	    return this.tumbasEnTablero;
    	}
}