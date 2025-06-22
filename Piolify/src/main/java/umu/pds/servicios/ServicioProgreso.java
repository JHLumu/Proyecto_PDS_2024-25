package umu.pds.servicios;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import umu.pds.modelo.*;
import umu.pds.persistencia.*;

/**
 * Servicio simplificado que se enfoca solo en ProgresoBloque.
 * Elimina la complejidad de ProgresoCurso.
 */
public class ServicioProgreso {
    
    private final ProgresoBloqueDAO progresoBloqueDAO;
    
    public ServicioProgreso() {
        FactoriaDAO factoria = FactoriaDAO.getInstancia(JPAFactoriaDAO.class.getName());
        this.progresoBloqueDAO = factoria.getProgresoBloqueDAO();
    }
    
    // ==========================================
    // MÉTODOS PRINCIPALES
    // ==========================================
    
    /**
     * Inicia o recupera el progreso de un bloque
     */
    public ProgresoBloque iniciarProgreso(Usuario usuario, Bloque bloque, TipoEstrategia estrategia) {
        // Buscar progreso existente
        Optional<ProgresoBloque> progresoExistente = progresoBloqueDAO.buscarProgreso(usuario, bloque);
        
        if (progresoExistente.isPresent()) {
            // Ya existe, actualizamos la estrategia si es diferente
            ProgresoBloque progreso = progresoExistente.get();
            if (!progreso.getEstrategiaUtilizada().equals(estrategia)) {
                progreso.setEstrategiaUtilizada(estrategia);
                progresoBloqueDAO.actualizarProgreso(progreso);
            }
            return progreso;
        } else {
            // Crear nuevo progreso
            ProgresoBloque nuevoProgreso = new ProgresoBloque(usuario, bloque, estrategia);
            progresoBloqueDAO.guardarProgreso(nuevoProgreso);
            System.out.println("Nuevo progreso iniciado para bloque: " + bloque.getTitulo());
            return nuevoProgreso;
        }
    }
    
    /**
     * Obtiene el progreso de un bloque específico
     */
    public ProgresoBloque obtenerProgreso(Usuario usuario, Bloque bloque) {
        return progresoBloqueDAO.buscarProgreso(usuario, bloque).orElse(null);
    }
    
    /**
     * Guarda el progreso actual
     */
    public void guardarProgreso(ProgresoBloque progreso) {
        progreso.actualizarActividad();
        progresoBloqueDAO.actualizarProgreso(progreso);
        System.out.println("Progreso guardado: " + progreso.getPorcentajeCompletado() + "%");
    }
    
    /**
     * Avanza al siguiente ejercicio y guarda automáticamente
     */
    public void avanzarEjercicio(ProgresoBloque progreso) {
        progreso.avanzarEjercicio();
        guardarProgreso(progreso);
        
        if (progreso.isCompletado()) {
            System.out.println("¡Bloque completado: " + progreso.getBloque().getTitulo() + "!");
        }
    }
    
    /**
     * Reinicia un bloque completamente
     */
    public void reiniciarBloque(Usuario usuario, Bloque bloque) {
        ProgresoBloque progreso = obtenerProgreso(usuario, bloque);
        if (progreso != null) {
            progreso.reiniciar();
            guardarProgreso(progreso);
            System.out.println("Bloque reiniciado: " + bloque.getTitulo());
        }
    }
    
    /**
     * Elimina el progreso de un bloque (para reinicio completo)
     */
    public void eliminarProgreso(Usuario usuario, Bloque bloque) {
        ProgresoBloque progreso = obtenerProgreso(usuario, bloque);
        if (progreso != null) {
            progresoBloqueDAO.eliminarProgreso(progreso);
            System.out.println("Progreso eliminado para bloque: " + bloque.getTitulo());
        }
    }
    
    // ==========================================
    // MÉTODOS DE CONSULTA
    // ==========================================
    
    /**
     * Obtiene todos los progresos de un usuario en un curso
     */
    public List<ProgresoBloque> obtenerProgresosCurso(Usuario usuario, Curso curso) {
        return progresoBloqueDAO.buscarProgresosPorCurso(usuario, curso);
    }
    
    /**
     * Obtiene todos los bloques en progreso (no completados) de un usuario
     */
    public List<ProgresoBloque> obtenerBloquesEnProgreso(Usuario usuario) {
        return progresoBloqueDAO.buscarProgresosActivos(usuario);
    }
    
    /**
     * Obtiene todos los bloques completados de un usuario
     */
    public List<ProgresoBloque> obtenerBloquesCompletados(Usuario usuario) {
        return progresoBloqueDAO.buscarProgresosPorUsuario(usuario)
            .stream()
            .filter(ProgresoBloque::isCompletado)
            .collect(Collectors.toList());
    }
    
    /**
     * Verifica si un curso está completado (todos sus bloques)
     */
    public boolean isCursoCompletado(Usuario usuario, Curso curso) {
        List<Bloque> bloques = curso.getBloques();
        if (bloques == null || bloques.isEmpty()) {
            return false;
        }
        
        return bloques.stream().allMatch(bloque -> {
            ProgresoBloque progreso = obtenerProgreso(usuario, bloque);
            return progreso != null && progreso.isCompletado();
        });
    }
    
    /**
     * Calcula el porcentaje de progreso total en un curso
     */
    public double calcularPorcentajeCurso(Usuario usuario, Curso curso) {
        List<Bloque> bloques = curso.getBloques();
        if (bloques == null || bloques.isEmpty()) {
            return 0.0;
        }
        
        int totalEjercicios = 0;
        int ejerciciosCompletados = 0;
        
        for (Bloque bloque : bloques) {
            if (bloque.getEjercicios() != null) {
                totalEjercicios += bloque.getEjercicios().size();
                
                ProgresoBloque progreso = obtenerProgreso(usuario, bloque);
                if (progreso != null) {
                    ejerciciosCompletados += progreso.getEjerciciosCompletados();
                }
            }
        }
        
        return totalEjercicios > 0 ? (double) ejerciciosCompletados / totalEjercicios * 100.0 : 0.0;
    }
    
    /**
     * Obtiene el siguiente bloque recomendado en un curso
     */
    public Bloque obtenerSiguienteBloqueRecomendado(Usuario usuario, Curso curso) {
        List<Bloque> bloques = curso.getBloques();
        if (bloques == null || bloques.isEmpty()) {
            return null;
        }
        
        // Buscar el primer bloque no completado
        for (Bloque bloque : bloques) {
            ProgresoBloque progreso = obtenerProgreso(usuario, bloque);
            if (progreso == null || !progreso.isCompletado()) {
                return bloque;
            }
        }
        
        return null; // Todos completados
    }
    
    // ==========================================
    // ESTADÍSTICAS SIMPLIFICADAS
    // ==========================================
    
    /**
     * Obtiene estadísticas resumidas de un usuario
     */
    public EstadisticasUsuario obtenerEstadisticas(Usuario usuario) {
        List<ProgresoBloque> todosLosProgresos = progresoBloqueDAO.buscarProgresosPorUsuario(usuario);
        
        int bloquesIniciados = todosLosProgresos.size();
        int bloquesCompletados = (int) todosLosProgresos.stream()
            .filter(ProgresoBloque::isCompletado)
            .count();
        int bloquesEnProgreso = bloquesIniciados - bloquesCompletados;
        
        // Calcular cursos únicos
        int cursosIniciados = (int) todosLosProgresos.stream()
            .map(p -> p.getBloque().getCurso().getId())
            .distinct()
            .count();
            
        return new EstadisticasUsuario(bloquesIniciados, bloquesCompletados, 
                                     bloquesEnProgreso, cursosIniciados);
    }
    
    /**
     * Clase para estadísticas de usuario
     */
    public static class EstadisticasUsuario {
        private final int bloquesIniciados;
        private final int bloquesCompletados;
        private final int bloquesEnProgreso;
        private final int cursosIniciados;
        
        public EstadisticasUsuario(int bloquesIniciados, int bloquesCompletados, 
                                 int bloquesEnProgreso, int cursosIniciados) {
            this.bloquesIniciados = bloquesIniciados;
            this.bloquesCompletados = bloquesCompletados;
            this.bloquesEnProgreso = bloquesEnProgreso;
            this.cursosIniciados = cursosIniciados;
        }
        
        // Getters
        public int getBloquesIniciados() { return bloquesIniciados; }
        public int getBloquesCompletados() { return bloquesCompletados; }
        public int getBloquesEnProgreso() { return bloquesEnProgreso; }
        public int getCursosIniciados() { return cursosIniciados; }
        
        @Override
        public String toString() {
            return String.format("Estadísticas: %d bloques iniciados, %d completados, %d en progreso (%d cursos)",
                bloquesIniciados, bloquesCompletados, bloquesEnProgreso, cursosIniciados);
        }
    }
}