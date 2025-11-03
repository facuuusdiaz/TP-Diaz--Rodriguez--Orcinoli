package juego;

import java.awt.Color;
import entorno.Entorno;
import java.awt.Image;
import entorno.Herramientas;

public class CartaPlanta {

    private double x,y;
    private double ancho, largo;
    private boolean seleccionada;
    private String nombreCarta;
    private int enfriamiento;
    private int tiempoProxDisponible;
    boolean estaLista;
    private Image imgCarta;
    
    public CartaPlanta (double x, double y, String nombreCarta) {
        this.x = x;
        this.y = y;
        this.ancho = 80.0;
        this.largo = 100.0;
        this.nombreCarta = nombreCarta;
        this.seleccionada = false;
        this.estaLista = true;
        this.tiempoProxDisponible = 0;

        // Carga de imágenes
        if (this.nombreCarta.equals("Rose Blade")) {
            this.imgCarta = Herramientas.cargarImagen("RoseBlade.png");
            this.enfriamiento = 4000;
        } else if (this.nombreCarta.equals("Wall-nut")) {
            this.imgCarta = Herramientas.cargarImagen("Wall-nutt.png"); 
            this.enfriamiento = 10000;
        } else if (this.nombreCarta.equals("Rose-Bomba")) {
            this.imgCarta = Herramientas.cargarImagen("Rose-Bombaa.png");
            this.enfriamiento = 15000;
        }
    }
    
    public void tick (Entorno entorno) {
        if (this.estaLista != true) {
            if (entorno.tiempo() >= this.tiempoProxDisponible) {
                this.estaLista = true;
            }
        }
    }
    
    public void enfriarPlanta(Entorno entorno) {
        this.estaLista = false;
        this.tiempoProxDisponible = entorno.tiempo() + enfriamiento;
    }
    
    public void dibujar(Entorno entorno) {
        
        // 1. Dibujar el fondo/borde de la carta
        Color colorBorde;
        if (this.seleccionada) {
            colorBorde = Color.GREEN;
        } else {
            colorBorde = new Color(80, 80, 80);
        }
        entorno.dibujarRectangulo(this.x, this.y, this.ancho, this.largo, 0, Color.DARK_GRAY);
        entorno.dibujarRectangulo(this.x, this.y, this.ancho, this.largo, 0, colorBorde);
        
        
        // 2. Dibujar la imagen de la planta ENCIMA del fondo
        if (this.imgCarta != null) {
            double escala = 0;
            if (this.nombreCarta.equals("Rose Blade")) {
                 escala = 0.2;
            }
            if (this.nombreCarta.equals("Wall-nut")) {
                escala = 0.1;
            }
            if (this.nombreCarta.equals("Rose-Bomba")) {
                escala = 0.1;
            }                                                                                               
            entorno.dibujarImagen(this.imgCarta, this.x, this.y - 10, 0, escala);
        } else {
            entorno.dibujarRectangulo(this.x, this.y, this.ancho - 5, this.largo - 5, 0, Color.ORANGE);
        }
        
        // 3. Dibujar el nombre de la planta debajo
        entorno.cambiarFont("Arial", 12, Color.WHITE);
        entorno.escribirTexto(this.nombreCarta, this.x - 30, this.y + this.largo/2 - 10);
        
        
        // 4. Dibuja la barra de recarga (si no está lista)
        if (this.estaLista != true) {
            long tiempoActual = entorno.tiempo();
            long tiempoRestante = this.tiempoProxDisponible - tiempoActual;
            if (tiempoRestante < 0) { tiempoRestante = 0; }
            double progresoRestante = (double)tiempoRestante / (double)this.enfriamiento;
            progresoRestante = Math.min(progresoRestante, 1.0);
            double alturaBarra = this.largo * progresoRestante;
            double yBarra = (this.y - this.largo / 2) + (alturaBarra / 2);
            Color colorRecarga = new Color(40, 40, 40, 200);
            entorno.dibujarRectangulo(this.x, yBarra, this.ancho, alturaBarra, 0, colorRecarga);
        }
    }
    
    public boolean estaClickeada(int mx, int my) {
        double minX = this.x - this.ancho / 2;
        double maxX = this.x + this.ancho / 2;
        double minY = this.y - this.largo / 2;
        double maxY = this.y + this.largo / 2;
        
        return (mx >= minX && mx <= maxX && my >= minY && my <= maxY);
    }
    
    public void modificarEnfriamiento(String tipo) {
        if (!this.estaLista) {
            int modificador = 3000;
            if (tipo.equals("bueno")) {
                this.tiempoProxDisponible -= modificador;
            } else if (tipo.equals("malo")) {
                this.tiempoProxDisponible += modificador;
            }
        }
    }
    
    public void setSeleccionada(boolean seleccionada) {
        this.seleccionada = seleccionada;
    }
    
    public boolean estaSeleccionada() {
        return this.seleccionada;
    }
    
    public String getNombreCarta() {
        return this.nombreCarta;
    }
    
   
}
