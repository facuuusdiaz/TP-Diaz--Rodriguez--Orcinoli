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

                // Usamos hitbox fija de planta para mordida
                if (z.estaTocando(p.getX(), p.getY(), 40, 40)) {
                    estaTocandoPlanta = true;
                    z.setAtacando(true);
                    p.recibirContacto(); // Correcto (sin entorno)
                    if (!p.estaViva()) {
                        movPlantas.removerPlanta(j);
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

                // Colisión con hitbox del zombie
                if (z.estaTocando(d.getX(), d.getY(), d.getAncho(), d.getAlto())) {
                    z.recibirDisparo();
                    movPlantas.removerDisparo(k); // Disparo desaparece
                    if (z.estaVivo() != true) { // <-- Si el zombie muere...
                    	
                    	if (jefeFinal != null && z == jefeFinal) {
                            // --- LÓGICA DEL JEFE ---
                            // ¡Es el jefe! Suelta 5 pociones en cruz
                            manejoItems.agregarItem(z.getX(), z.getY()); // Centro
                            manejoItems.agregarItem(z.getX() - 40, z.getY()); // Izquierda
                            manejoItems.agregarItem(z.getX() + 40, z.getY()); // Derecha
                            manejoItems.agregarItem(z.getX(), z.getY() - 40); // Arriba
                            manejoItems.agregarItem(z.getX(), z.getY() + 40); // Abajo
                            
                    	} else {
                    		// --- LÓGICA DE ZOMBIE NORMAL ---
	                    	double CHANCE_TUMBA = 0.45; 
	                        if (Math.random() < CHANCE_TUMBA) {
	                            
	                            // 1. Obtener la casilla donde murió el zombie
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
	                            // Agrega un item en la posición donde murió el zombie
	                            manejoItems.agregarItem(z.getX(), z.getY());
	                        }
                    	}
                        
                        movZombies.removerZombie(i); // Esto queda fuera del else
                    }
                    break; // Disparo ya golpeó
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

                // --- ¡ESTA ES LA LÍNEA CORREGIDA! ---
                // Comprueba si la planta (p) toca el proyectil (proy)
                if (p.estaTocando(proy.getX(), proy.getY(), proy.getDiametro(), proy.getDiametro())) {
                    
                    // --- ESTA ES LA LÓGICA CORREGIDA ---
                    p.recibirDisparoZombie(); // La planta recibe daño
                    movZombies.removerProyectil(k); // El proyectil desaparece
                    
                    if (!p.estaViva()) {
                    	
                    	// Lógica de explosión (esto estaba bien)
                    	if (p.getNombre().equals("Rose-Bomba")) {
                            this.explotar(p.getFila(), p.getCol(), zombies, tumbas, tablero);
                        }
                    	
                        movPlantas.removerPlanta(j);
                    }
                    break; // Proyectil ya golpeó
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

                // Usamos el hitbox de la tumba
                if (t.estaTocando(d.getX(), d.getY(), d.getAncho(), d.getAlto())) {
                    t.recibirDisparo();
                    movPlantas.removerDisparo(k); // Disparo desaparece
                    if (!t.estaViva()) {
                        manejoTumbas.removerTumba(tIdx); // Remueve por índice
                    }
                    // No ponemos break aquí; un disparo podría seguir
                }
            }
        }

        return false; // El juego continúa
    }
    
private void explotar(int filaCentro, int colCentro, ZombieGrinch[] zombies, Tumba[] tumbas, Tablero tablero) {
        
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
                for (ZombieGrinch z : zombies) {
                    if (z != null && z.estaVivo()) {
                        // Si el zombie está tocando esta casilla del 3x3...
                        if (z.estaTocando(xCasilla, yCasilla, anchoCasilla, altoCasilla)) {
                            z.recibirDisparo(danoExplosion); // ¡Lo mata!
                        }
                    }
                }
                
                // 2. Revisa todas las TUMBAS
                for (Tumba t : tumbas) {
                    if (t != null && t.estaViva()) {
                        // Si la tumba está EN esta casilla del 3x3...
                        if (t.getFila() == f && t.getCol() == c) {
                            t.recibirDisparo(danoExplosion); // ¡La destruye!
                        }
                    }
                }
            }
        }
    }
    
}