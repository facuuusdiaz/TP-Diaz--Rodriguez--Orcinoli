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
     * Devuelve la carta clickeada solo si está lista (sin chequear el límite).
     */
    public CartaPlanta getCartaClickeada(int mx, int my) {
        for (CartaPlanta carta : this.mazo) {
            // Chequea si se clickeó Y si está lista (cooldown terminado)
            if (carta != null && 
                carta.estaClickeada(mx, my) && 
                carta.estaLista) {
                
                return carta;
            }
        }
        return null; // No se encontró ninguna carta válida
    }
    
    public void seleccionarCarta(CartaPlanta cartaSeleccionada) {
        for (CartaPlanta carta : this.mazo) {
            if (carta != null) {
                carta.setSeleccionada(carta == cartaSeleccionada);
            }
        }
    }
    
    public void aplicarEfectoItem(String tipo) {
        for (CartaPlanta carta : this.mazo) {
            if (carta != null) {
                carta.modificarEnfriamiento(tipo);
            }
        }
    }
    
    
}