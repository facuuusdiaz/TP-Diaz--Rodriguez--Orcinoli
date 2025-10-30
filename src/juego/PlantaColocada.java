package juego;

import java.awt.Color;
import entorno.Entorno;
import java.awt.Image;
import entorno.Herramientas;

public class PlantaColocada {

    double x, y;
    String nombre;

    private boolean estaViva;
    private int disparoRetraso = 2000; // En milisegundos para lógica de planta
    private long proximoDisparo; // Usar long aquí porque viene de entorno.tiempo()

    private int vida;
    private int vidaMaxima;

    private Image img;

    private int fila;
    private int col;
    private boolean estaSeleccionadaParaMover;

    // Constructor CORRECTO (5 argumentos)
    public PlantaColocada(double x, double y, int fila, int col, String nombre) {
        this.x = x;
        this.y = y;
        this.fila = fila;
        this.col = col;
        this.nombre = nombre;
        this.estaViva = true;
        this.proximoDisparo = 0; // Se inicializa bien en el primer tick de planta
        this.estaSeleccionadaParaMover = false;

        if (this.nombre.equals("Rose Blade")) {
            this.img = Herramientas.cargarImagen("RoseBlade.png");
            this.vida = 100;
            this.vidaMaxima = 100;
        } else if (this.nombre.equals("Wall-nut")) {
            this.img = Herramientas.cargarImagen("Wall-nutt.png"); // Asumo 2 't'
            this.vida = 1000;
            this.vidaMaxima = 1000;
        } else if (this.nombre.equals("Rose-Bomba")) {
            this.img = Herramientas.cargarImagen("Rose-Bombaa.png"); // Re-usar imagen
            this.vida = 1; // ¡Vida mínima para que explote al primer toque!
            this.vidaMaxima = 1;
        } else {
             // Default por si acaso
            this.vida = 50;
            this.vidaMaxima = 50;
        }
    }

    // Tick de la planta (disparar)
public Disparo tick(Entorno e, ZombiesManejo movZombies, ZombieGrinch jefeFinal) {
        
        // --- ¡ESTA LÍNEA ES LA MÁS IMPORTANTE! ---
        // Se asegura de que SOLO la "Rose Blade" entre en la lógica de disparo.
        if (this.nombre.equals("Rose Blade")) {
             
             // Inicializa el timer de disparo si es necesario
             if (this.proximoDisparo == 0) { 
                 this.proximoDisparo = e.tiempo() + disparoRetraso;
             }
             
             // Si la planta está viva y lista para disparar...
             if (this.estaViva && e.tiempo() >= this.proximoDisparo) {
                
                // --- Lógica de amenaza (para disparar al jefe o zombies) ---
                
                // 1. Revisa si el jefe final está vivo
                boolean jefeEstaVivo = (jefeFinal != null && jefeFinal.estaVivo());
            
                // 2. Revisa si hay una amenaza normal en la fila
                boolean hayAmenazaNormal = movZombies.hayZombieEnFila(this.y, 800.0);
            
                // 3. Dispara si CUALQUIERA de las dos es verdadera
                if (hayAmenazaNormal || jefeEstaVivo) {
                    this.proximoDisparo = e.tiempo() + this.disparoRetraso;
                    return new Disparo(this.x, this.y); // ¡Dispara!
                }
            }
        }
        
        // Si la planta NO es "Rose Blade" (es Wall-nut o Rose-Bomba),
        // salta el 'if' y devuelve null (no dispara).
        return null; 
    }

    // Daño MELEE (mordida) - Correcto sin 'entorno'
    public void recibirContacto() {
        this.vida--;
        if (this.vida <= 0) {
            this.estaViva = false;
        }
    }

    // Daño A DISTANCIA (bola de nieve)
    public void recibirDisparoZombie() {
        this.vida -= 5; // Daño más fuerte
        if (this.vida <= 0) {
            this.estaViva = false;
        }
    }

    // Hitbox de la planta
    public boolean estaTocando(double otroX, double otroY, double otroAncho, double otroAlto) {
        double miAncho = 40;
        double miAlto = 40;
        double miIzquierda = this.x - miAncho / 2;
        double miDerecha = this.x + miAncho / 2;
        double miArriba = this.y - miAlto / 2;
        double miAbajo = this.y + miAlto / 2;
        double otroIzquierda = otroX - otroAncho / 2;
        double otroDerecha = otroX + otroAncho / 2;
        double otroArriba = otroY - otroAncho / 2;
        double otroAbajo = otroY + otroAlto / 2;
        return miIzquierda < otroDerecha && miDerecha > otroIzquierda && miArriba < otroAbajo && miAbajo > otroArriba;
    }

    public void dibujar(Entorno e) {
        if (!this.estaViva) { return; }

        double escala;
        if (this.nombre.equals("Rose Blade")){ escala = 0.3; }
        else if (this.nombre.equals("Wall-nut")) { escala = 0.09; }
        else { escala = 0.1; }

        if (this.img != null) {
            e.dibujarImagen(this.img, this.x, this.y, 0, escala);
        } else {
             e.dibujarRectangulo(this.x, this.y, 30, 30, 0, Color.MAGENTA); // Dibujo alternativo si falla la imagen
        }


        if (this.vida < this.vidaMaxima) {
            Color rojoTransparente = new Color(255, 0, 0, 100);
            e.dibujarCirculo(this.x, this.y, 40, rojoTransparente);
        }
        // Quitamos el círculo de selección
    }

    // Getters y Setters para movimiento y estado
    public int getFila() { return this.fila; }
    public int getCol() { return this.col; }
    public void setSeleccionadaParaMover(boolean sel) { this.estaSeleccionadaParaMover = sel; }
    public boolean estaClickeada(double mx, double my) {
        double dist = Math.sqrt(Math.pow(mx - this.x, 2) + Math.pow(my - this.y, 2));
        return dist < 20;
    }
    public void setCasilla(int fila, int col, Tablero tablero) {
        this.fila = fila;
        this.col = col;
        double[] nuevoCentro = tablero.getCentroCasilla(fila, col);
        this.x = nuevoCentro[0];
        this.y = nuevoCentro[1];
    }
    public boolean estaViva() { return this.estaViva; }
    public double getX() { return this.x; }
    public double getY() { return this.y; }

public String getNombre() {
    return this.nombre;
}
}
