package juego;

import entorno.Entorno;

public class ManejoItems {
    private static final int MAX_ITEMS = 30; // Máximo de items en pantalla
    private Item[] itemsEnTablero;

    public ManejoItems() {
        this.itemsEnTablero = new Item[MAX_ITEMS];
    }

    /**
     * Agrega un nuevo item al tablero en la posición (x, y).
     * Decide aleatoriamente si es bueno o malo.
     */
    public void agregarItem(double x, double y) {
        // Busca un espacio libre en el array
        for (int i = 0; i < this.itemsEnTablero.length; i++) {
            if (this.itemsEnTablero[i] == null) {
                
                // 50% de chance de ser bueno, 50% de ser malo
                String tipo = (Math.random() > 0.5) ? "bueno" : "malo";
                
                this.itemsEnTablero[i] = new Item(x, y, tipo);
                return; // Item agregado, salimos
            }
        }
        // Si no hay espacio, simplemente no se agrega
    }

    /**
     * Dibuja todos los items activos.
     */
    public void dibujar(Entorno entorno) {
        for (Item item : this.itemsEnTablero) {
            if (item != null) {
                item.dibujar(entorno);
            }
        }
    }

    /**
     * Revisa si se hizo clic en algún item y lo devuelve.
     * Devuelve null si no se clickeó ninguno.
     */
    public Item getItemClickeado(int mx, int my) {
        for (Item item : this.itemsEnTablero) {
            if (item != null && item.estaClickeado(mx, my)) {
                return item;
            }
        }
        return null; // No se encontró
    }

    /**
     * Remueve un item específico del array (poniéndolo en null).
     */
    public void removerItem(Item itemARemover) {
        for (int i = 0; i < this.itemsEnTablero.length; i++) {
            if (this.itemsEnTablero[i] == itemARemover) {
                this.itemsEnTablero[i] = null;
                return; // Item removido
            }
        }
    }
}