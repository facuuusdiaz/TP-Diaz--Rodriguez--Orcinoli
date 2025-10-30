package juego;

import entorno.Entorno;

public class Colisiones {

    public boolean verificar(ManejoPlantas movPlantas, ZombiesManejo movZombies,Tablero tablero, ZombieGrinch jefeFinal, ManejoTumbas manejoTumbas,ManejoItems manejoItems, double xRegalo, Entorno entorno) {

        ZombieGrinch[] zombies = movZombies.getZombies();
        PlantaColocada[] plantas = movPlantas.getPlantas();
        Disparo[] disparos = movPlantas.getDisparos();
        ProyectilZombie[] proyectilesZombies = movZombies.getProyectiles();
        Tumba[] tumbas = manejoTumbas.getTumbas();

        // --- Bucle de Zombies (Secciones 1, 2, 3) ---
        for (int i = 0; i < zombies.length; i++) {
            ZombieGrinch z = zombies[i];
            if (z == null || !z.estaVivo()) { continue; }

            // 1. ZOMBIE VS REGALO
            if (z.getX() <= xRegalo) {
                return true; // Game Over
            }

            // 2. ZOMBIE VS PLANTA (Mordida)
            boolean estaTocandoPlanta = false;
            for (int j = 0; j < plantas.length; j++) {
                PlantaColocada p = plantas[j];
                if (p == null || !p.estaViva()) { continue; }

                if (z.estaTocando(p.getX(), p.getY(), 40, 40)) {
                    estaTocandoPlanta = true;
                    z.setAtacando(true);
                    p.recibirContacto(); // La planta recibe daño
                    
                    if (!p.estaViva()) { // Si la planta muere
                        
                        // Si era una bomba, explota
                        if (p.getNombre().equals("Rose-Bomba")) {
                            // ¡CAMBIO ACÁ! Pasamos 'movZombies' en lugar de 'zombies'
                        	this.explotar(p.getFila(), p.getCol(), movZombies, tumbas, tablero, jefeFinal);
                        }
                        
                        movPlantas.removerPlanta(j); // Se remueve la planta
                    }
                    break;
                }
            }
            if (!estaTocandoPlanta) {
                z.setAtacando(false);
            }

            // 3. DISPARO (PLANTA) VS ZOMBIE
            for (int k = 0; k < disparos.length; k++) {
                Disparo d = disparos[k];
                if (d == null) { continue; }

                if (z.estaTocando(d.getX(), d.getY(), d.getAncho(), d.getAlto())) {
                    z.recibirDisparo();
                    movPlantas.removerDisparo(k);
                    
                    if (z.estaVivo() != true) { // <-- Si el zombie muere...
                        
                        if (jefeFinal != null && z == jefeFinal) {
                            // Lógica del Jefe (sin cambios)
                            manejoItems.agregarItem(z.getX(), z.getY());
                            manejoItems.agregarItem(z.getX() - 40, z.getY());
                            manejoItems.agregarItem(z.getX() + 40, z.getY());
                            manejoItems.agregarItem(z.getX(), z.getY() - 40);
                            manejoItems.agregarItem(z.getX(), z.getY() + 40);
                            
                        } else {
                            // Lógica de Zombie Normal (sin cambios)
	                    	double CHANCE_TUMBA = 0.45; 
	                        if (Math.random() < CHANCE_TUMBA) {
	                            int[] casilla = tablero.getCasilla((int)z.getX(), (int)z.getY());
	                            if (casilla != null) {
	                                int fila = casilla[0];
	                                int col = casilla[1];
	                                if (!movPlantas.hayPlantaEn(fila, col)) {
	                                    manejoTumbas.agregarTumba(fila, col);
	                                }
	                            }
	                        }
	                        
	                        double CHANCE_ITEM = 0.50; 
	                        if (Math.random() < CHANCE_ITEM) {
	                            manejoItems.agregarItem(z.getX(), z.getY());
	                        }
                    	}
                        
                        movZombies.removerZombie(i); // El contador suma aquí
                    }
                    break;
                }
            }
        } // --- Fin del bucle FOR de zombies ---


     // --- 4. PROYECTIL (ZOMBIE) VS PLANTA ---
        for (int k = 0; k < proyectilesZombies.length; k++) {
            ProyectilZombie proy = proyectilesZombies[k];
            if (proy == null) { continue; }

            for (int j = 0; j < plantas.length; j++) {
                PlantaColocada p = plantas[j];
                if (p == null || !p.estaViva()) { continue; }

                if (p.estaTocando(proy.getX(), proy.getY(), proy.getDiametro(), proy.getDiametro())) {
                    
                    p.recibirDisparoZombie();
                    movZombies.removerProyectil(k);
                    
                    if (!p.estaViva()) {
                    	
                    	if (p.getNombre().equals("Rose-Bomba")) {
                            // ¡CAMBIO ACÁ! Pasamos 'movZombies' en lugar de 'zombies'
                    		this.explotar(p.getFila(), p.getCol(), movZombies, tumbas, tablero, jefeFinal);
                        }
                    	
                        movPlantas.removerPlanta(j);
                    }
                    break;
                }
            }
        }

        // --- 5. DISPARO (PLANTA) VS TUMBA ---
        for (int k = 0; k < disparos.length; k++) {
            Disparo d = disparos[k];
            if (d == null) { continue; }

            for (int tIdx = 0; tIdx < tumbas.length; tIdx++) {
                Tumba t = tumbas[tIdx];
                if (t == null || !t.estaViva()) { continue; }

                if (t.estaTocando(d.getX(), d.getY(), d.getAncho(), d.getAlto())) {
                    t.recibirDisparo();
                    movPlantas.removerDisparo(k);
                    if (!t.estaViva()) {
                        manejoTumbas.removerTumba(tIdx);
                    }
                }
            }
        }

        return false; // El juego continúa
    }
    
    // --- MÉTODO EXPLOTAR (CORREGIDO) ---
 // Archivo: Colisiones.java

    private void explotar(int filaCentro, int colCentro, ZombiesManejo movZombies, Tumba[] tumbas, Tablero tablero, ZombieGrinch jefeFinal) {
            
        // Itera en un cuadrado de 3x3 alrededor del centro
        for (int f = filaCentro - 1; f <= filaCentro + 1; f++) {
            for (int c = colCentro - 1; c <= colCentro + 1; c++) {
                
                // No explotar fuera del tablero (filas 0-4, cols 0-9)
                if (f < 0 || f >= 5 || c < 0 || c >= 10) { 
                    continue; 
                }

                // Obtiene las coordenadas de la casilla a dañar
                double[] centroCasilla = tablero.getCentroCasilla(f, c);
                if (centroCasilla == null) { continue; }
                
                double xCasilla = centroCasilla[0];
                double yCasilla = centroCasilla[1];
                double anchoCasilla = tablero.getAnchoCasilla();
                double altoCasilla = tablero.getLargoCasilla();
                
                // Daño de la explosión (un número muy alto)
                int danoExplosion = 1000;

                // 1. Revisa todos los ZOMBIES
                ZombieGrinch[] zombies = movZombies.getZombies(); // Obtenemos el array
                for (int i = 0; i < zombies.length; i++) { // Bucle por índice 'i'
                    ZombieGrinch z = zombies[i];
                    
                    if (z != null && z == jefeFinal) {
                        continue; // No le aplica daño de explosión
                    }
                    
                    if (z != null && z.estaVivo()) {
                        // Si el zombie está tocando esta casilla del 3x3...
                        if (z.estaTocando(xCasilla, yCasilla, anchoCasilla, altoCasilla)) {
                            
                            // --- ¡ESTA ES LA LÍNEA QUE FALTABA! ---
                            // 1. Matamos al zombi (esto pone estaVivo = false)
                            z.recibirDisparo(danoExplosion); 
                            // 2. Lo removemos para que cuente en el score
                            movZombies.removerZombie(i);
                            // ------------------------------------
                        }
                    }
                }
                
                // 2. Revisa todas las TUMBAS (sin cambios)
                for (Tumba t : tumbas) {
                    if (t != null && t.estaViva()) {
                        if (t.getFila() == f && t.getCol() == c) {
                            t.recibirDisparo(danoExplosion); // ¡La destruye!
                        }
                    }
                }
            }
        }
    }
}
