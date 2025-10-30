package juego;

import java.awt.Color;
import java.awt.Image;
import entorno.Entorno;
import entorno.Herramientas;
import entorno.InterfaceJuego;
// ¡Ya no se necesita JOptionPane!

public class Juego extends InterfaceJuego {
    // El objeto Entorno que controla el tiempo y otros
    private Entorno entorno;

    // (Variables de dificultad eliminadas)

    // Variables y métodos propios de cada grupo
    private Tablero miTablero;
    private BarraCartas Barrita;
    private BarraZombies barraZombies;
    private CartaPlanta cartaSeleccionada;

    private ManejoPlantas movPlantas;
    private ZombiesManejo movZombies;
    private ManejoTumbas manejoTumbas;
    private ManejoItems manejoItems;
    private PlantaColocada plantaEnMovimiento;

    private Colisiones col;

    private boolean juegoTerminado;
    private boolean juegoGanado;
    private double xRegalo; // Posición 'x' de la zona de "regalos"

    private int zombiesParaGanar;

    private int contadorTicks;
    private int segundosJuego;
    
    private boolean jefeHaAparecido;
    private ZombieGrinch jefeFinal; // Para rastrear al jefe

   // private Image fondo;

    Juego() {
        // (Lógica de JOptionPane eliminada)

        // Inicializa el objeto entorno
        this.entorno = new Entorno(this, "Proyecto para TP", 800, 600);

        // Inicializar lo que haga falta para el juego
        this.miTablero = new Tablero(entorno);
        this.Barrita = new BarraCartas();
        this.barraZombies = new BarraZombies();
       // this.fondo = Herramientas.cargarImagen("Tablerooo.jpg"); // Asegúrate que el archivo exista

        this.cartaSeleccionada = null;
        this.plantaEnMovimiento = null;
        this.movPlantas = new ManejoPlantas();
        this.movZombies = new ZombiesManejo(this.miTablero); // Pasa el tablero al constructor
        this.manejoTumbas = new ManejoTumbas(this.miTablero); // Pasa el tablero al constructor
        this.manejoItems = new ManejoItems();
        this.col = new Colisiones();

        this.juegoTerminado = false;
        this.juegoGanado = false;
        
        this.jefeHaAparecido = false;
        this.jefeFinal = null;
        
        this.xRegalo = this.miTablero.getAnchoCasilla() / 2.0;
        this.zombiesParaGanar = 50; // Objetivo de zombies a matar

        this.contadorTicks = 0;
        this.segundosJuego = 0;

        // Inicia el juego!
        this.entorno.iniciar();
    }

    public void tick() {
        // --- 1. CHEQUEO DE ESTADO (GANAR/PERDER) ---
        if (!this.jefeHaAparecido && this.movZombies.getTotalAsesinados() >= this.zombiesParaGanar) {
            
            // ¡Es hora del jefe!
            this.jefeFinal = this.movZombies.generarJefeFinal(this.miTablero);
            this.jefeHaAparecido = true;
            this.movZombies.detenerSpawns(); // Detiene los spawns normales
            this.movZombies.setJefeFinal(this.jefeFinal);
        }
        
        // B. Nueva condición de victoria: El jefe apareció Y está muerto
        if (this.jefeHaAparecido && this.jefeFinal != null && !this.jefeFinal.estaVivo()) {
            this.juegoGanado = true;
        }

        if (this.juegoGanado) {
            entorno.cambiarFont("Arial", 50, Color.GREEN);
            entorno.escribirTexto("¡GANASTE!", 280, 300);
            return;
        }

        if (this.juegoTerminado) {
            entorno.cambiarFont("Arial", 50, Color.RED);
            entorno.escribirTexto("¡PERDISTE!", 280, 300);
            return;
        }

        // --- Temporizador Manual ---
        this.contadorTicks++;
        if (this.contadorTicks >= 60) {
            this.segundosJuego++;
            this.contadorTicks = 0;
        }

        // --- 2. LÓGICA DEL JUEGO ---
        this.Barrita.tick(this.entorno); // Actualiza enfriamiento de cartas
        this.manejarMouse(); // Revisa clics del mouse
        this.manejarTeclado(); // Revisa teclas para mover planta

        // Actualizar estados internos (movimiento, disparos, generación)
        this.movZombies.tick(this.entorno, this.miTablero, this.movPlantas); // Correcto (4 args)
        this.movPlantas.tick(this.entorno, this.movZombies, this.jefeFinal); // Correcto (2 args)

      
        boolean debeTerminar = this.col.verificar(
                this.movPlantas,
                this.movZombies,
                this.miTablero,
                this.jefeFinal,
                this.manejoTumbas,
                this.manejoItems,
                this.xRegalo,
                this.entorno
            );

        if (debeTerminar) {
            this.juegoTerminado = true;
        }

        // --- 3. DIBUJAR EL JUEGO (EN ORDEN CORRECTO) ---
        // (El código de dibujar no cambia)

        // Tablero encima del fondo
        this.miTablero.dibujarTablero();

        // HUD encima del tablero
        this.dibujarHUD();

        // Barras de cartas encima del HUD
        this.Barrita.dibujar(this.entorno);
        this.barraZombies.dibujar(this.entorno);

        // Tumbas encima del tablero
        this.manejoTumbas.dibujar(this.entorno);
        this.manejoItems.dibujar(this.entorno);

        // Plantas y Zombies encima de las tumbas
        this.movPlantas.dibujar(this.entorno);
        this.movZombies.dibujar(this.entorno);

        // Círculo de arrastre (si hay carta seleccionada)
        if (this.cartaSeleccionada != null) {
            int mx = this.entorno.mouseX();
            int my = this.entorno.mouseY();
            entorno.dibujarCirculo(mx, my, 40, Color.CYAN);
        }

        // Regalos al final (encima de todo en la columna 0)
        this.miTablero.dibujarRegalos();
    }


    private void manejarMouse() {
        // A. CUANDO SE PRESIONA
        if (this.entorno.sePresionoBoton(entorno.BOTON_IZQUIERDO)) {
            int mx = this.entorno.mouseX();
            int my = this.entorno.mouseY();
            
            // 1. Revisa el item PRIMERO
            Item itemClickeado = this.manejoItems.getItemClickeado(mx, my);

            if (itemClickeado != null) {
                // ... (lógica de items sin cambios)
                this.Barrita.aplicarEfectoItem(itemClickeado.getTipo());
                this.manejoItems.removerItem(itemClickeado);
                this.cartaSeleccionada = null;
                this.Barrita.seleccionarCarta(null);
                if (this.plantaEnMovimiento != null) {
                    this.plantaEnMovimiento.setSeleccionadaParaMover(false);
                    this.plantaEnMovimiento = null;
                }
                return;
            }

            // 2. Si no fue un item, revisa la barra de cartas
            CartaPlanta cartaClickeada = this.Barrita.getCartaClickeada(mx, my);

            if (cartaClickeada != null) { // Clic en carta de la barra
                this.cartaSeleccionada = cartaClickeada;
                this.Barrita.seleccionarCarta(this.cartaSeleccionada);
                if (this.plantaEnMovimiento != null) {
                    this.plantaEnMovimiento.setSeleccionadaParaMover(false);
                    this.plantaEnMovimiento = null;
                }
            } 
            // 3. Si no, revisa el tablero
            else if (this.miTablero.estaEnTablero(mx, my)) { // Clic en el tablero
                PlantaColocada plantaClickeada = this.movPlantas.getPlantaClickeada(mx, my);
                if (this.plantaEnMovimiento != null) {
                    this.plantaEnMovimiento.setSeleccionadaParaMover(false);
                }
                this.plantaEnMovimiento = plantaClickeada;
                if (this.plantaEnMovimiento != null) {
                    this.plantaEnMovimiento.setSeleccionadaParaMover(true);
                    this.cartaSeleccionada = null;
                    this.Barrita.seleccionarCarta(null);
                }
            } 
            // 4. Clic fuera de todo
            else { 
                this.cartaSeleccionada = null;
                this.Barrita.seleccionarCarta(null);
                if (this.plantaEnMovimiento != null) {
                    this.plantaEnMovimiento.setSeleccionadaParaMover(false);
                    this.plantaEnMovimiento = null;
                }
            }
        }

        // B. CUANDO SE SUELTA (Plantar carta nueva)
        // --- CÓDIGO RESTAURADO (SIN LÍMITE DE DIFICULTAD) ---
        if (this.entorno.seLevantoBoton(entorno.BOTON_IZQUIERDO)) {
            if (this.cartaSeleccionada != null) {
                int mx = this.entorno.mouseX();
                int my = this.entorno.mouseY();
                if (this.miTablero.estaEnTablero(mx, my)) {
                    boolean plantacion = this.movPlantas.plantar(this.cartaSeleccionada, mx, my, this.miTablero, this.manejoTumbas); // Pasa manejoTumbas
                    if (plantacion) {
                        this.cartaSeleccionada.enfriarPlanta(this.entorno);
                    }
                }
                this.cartaSeleccionada = null;
                this.Barrita.seleccionarCarta(null);
            }
        }
        // --- FIN DEL BLOQUE RESTAURADO ---
    }

    private void manejarTeclado() {
        if (this.plantaEnMovimiento == null) { return; }

        int filaActual = this.plantaEnMovimiento.getFila();
        int colActual = this.plantaEnMovimiento.getCol();
        int nuevaFila = filaActual;
        int nuevaCol = colActual;
        boolean seMovio = false;

        if (entorno.sePresiono(entorno.TECLA_ARRIBA)) { nuevaFila--; seMovio = true; }
        else if (entorno.sePresiono(entorno.TECLA_ABAJO)) { nuevaFila++; seMovio = true; }
        else if (entorno.sePresiono(entorno.TECLA_IZQUIERDA)) { nuevaCol--; seMovio = true; }
        else if (entorno.sePresiono(entorno.TECLA_DERECHA)) { nuevaCol++; seMovio = true; }

        if (seMovio) {
            boolean dentroLimites = nuevaFila >= 0 && nuevaFila < 5 && nuevaCol >= 0 && nuevaCol < 10;
            if (dentroLimites) {
                if (nuevaCol == 0) { return; } // No ir a columna de regalos
                if (this.movPlantas.hayPlantaEn(nuevaFila, nuevaCol)) { return; } // No pisar otra planta
                if (this.manejoTumbas.getTumbaEnCasilla1(nuevaFila, nuevaCol) != null) { return; } // No pisar tumba

                this.plantaEnMovimiento.setCasilla(nuevaFila, nuevaCol, this.miTablero);
            }
        }
    }

    private void dibujarHUD() {
        entorno.cambiarFont("Arial", 18, Color.WHITE);
        int x = 350;
        entorno.escribirTexto("Tiempo: " + this.segundosJuego + "s", x, 40);
        
        // Si el jefe no ha aparecido, muestra el conteo normal
        if (!this.jefeHaAparecido) {
            int asesinados = this.movZombies.getTotalAsesinados();
            int zombiesRestantes = this.zombiesParaGanar - asesinados;
            if (zombiesRestantes < 0) { zombiesRestantes = 0; }
            
            entorno.escribirTexto("Asesinados: " + asesinados, x, 65);
            entorno.escribirTexto("Faltan para ganar: " + zombiesRestantes, x, 90);
        } else {
            // ¡Si el jefe apareció, muestra un mensaje de JEFE!
            int asesinados = this.movZombies.getTotalAsesinados();
            entorno.escribirTexto("Asesinados: " + asesinados, x, 65);
            
            entorno.cambiarFont("Arial", 20, Color.RED); // Letra más grande y roja
            entorno.escribirTexto("¡¡JEFE FINAL!!", x, 90);
        }
    }
    
    @SuppressWarnings("unused")
    public static void main(String[] args) {
        Juego juego = new Juego();
    }
}
