package umu.pds.servicios;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import umu.pds.modelo.*;
import umu.pds.persistencia.*;

/**
 * Servicio simplificado que se enfoca solo en ProgresoBloque.
 */
public class ServicioProgreso {

    private final ProgresoBloqueDAO progresoBloqueDAO;

    /**
     * Constructor para ServicioProgreso.
     * Inicializa el DAO de ProgresoBloque utilizando la factoría DAO.
     */
    public ServicioProgreso() {
        FactoriaDAO factoria = FactoriaDAO.getInstancia(JPAFactoriaDAO.class.getName());
        this.progresoBloqueDAO = factoria.getProgresoBloqueDAO();
    }

    /**
     * Inicia o recupera el progreso de un bloque específico para un usuario dado.
     * Si ya existe un progreso para el usuario y el bloque, se recupera y se actualiza
     * la estrategia si es diferente. Si no existe, se crea un nuevo progreso.
     *
     * @param usuario El usuario para el que se gestiona el progreso.
     * @param bloque El bloque cuyo progreso se desea iniciar o recuperar.
     * @param estrategia La estrategia de aprendizaje a utilizar para este progreso.
     * @return El objeto ProgresoBloque existente o recién creado.
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
     * Obtiene el progreso de un bloque específico para un usuario.
     *
     * @param usuario El usuario cuyo progreso se busca.
     * @param bloque El bloque del que se desea obtener el progreso.
     * @return El objeto ProgresoBloque si existe, o null si no se encuentra.
     */
    public ProgresoBloque obtenerProgreso(Usuario usuario, Bloque bloque) {
        return progresoBloqueDAO.buscarProgreso(usuario, bloque).orElse(null);
    }

    /**
     * Guarda el progreso actual de un bloque.
     * Antes de guardar, actualiza la actividad del progreso para reflejar la última interacción.
     *
     * @param progreso El objeto ProgresoBloque a guardar.
     */
    public void guardarProgreso(ProgresoBloque progreso) {
        progreso.actualizarActividad();
        progresoBloqueDAO.actualizarProgreso(progreso);
        System.out.println("Progreso guardado: " + progreso.getPorcentajeCompletado() + "%");
    }

    /**
     * Avanza al siguiente ejercicio dentro de un progreso de bloque y guarda automáticamente los cambios.
     * Si el bloque se completa al avanzar, imprime un mensaje de felicitación.
     *
     * @param progreso El objeto ProgresoBloque en el que se desea avanzar.
     */
    public void avanzarEjercicio(ProgresoBloque progreso) {
        progreso.avanzarEjercicio();
        guardarProgreso(progreso);

        if (progreso.isCompletado()) {
            System.out.println("¡Bloque completado: " + progreso.getBloque().getTitulo() + "!");
        }
    }

    /**
     * Reinicia completamente el progreso de un bloque para un usuario dado.
     * Si el progreso existe, se reinicia y se guarda.
     *
     * @param usuario El usuario cuyo progreso se desea reiniciar.
     * @param bloque El bloque que se desea reiniciar.
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
     * Elimina el progreso de un bloque específico para un usuario.
     * Este método se utiliza para reiniciar completamente el seguimiento de un bloque.
     *
     * @param usuario El usuario cuyo progreso se desea eliminar.
     * @param bloque El bloque del que se desea eliminar el progreso.
     */
    public void eliminarProgreso(Usuario usuario, Bloque bloque) {
        ProgresoBloque progreso = obtenerProgreso(usuario, bloque);
        if (progreso != null) {
            progresoBloqueDAO.eliminarProgreso(progreso);
            System.out.println("Progreso eliminado para bloque: " + bloque.getTitulo());
        }
    }

    /**
     * Obtiene una lista de todos los progresos de bloques que un usuario tiene dentro de un curso específico.
     *
     * @param usuario El usuario cuyos progresos se desean obtener.
     * @param curso El curso del que se desean obtener los progresos de los bloques.
     * @return Una lista de objetos ProgresoBloque correspondientes al usuario y curso.
     */
    public List<ProgresoBloque> obtenerProgresosCurso(Usuario usuario, Curso curso) {
        return progresoBloqueDAO.buscarProgresosPorCurso(usuario, curso);
    }

    /**
     * Obtiene una lista de todos los bloques que un usuario tiene en progreso (no completados).
     *
     * @param usuario El usuario cuyos bloques en progreso se desean obtener.
     * @return Una lista de objetos ProgresoBloque que representan los bloques activos del usuario.
     */
    public List<ProgresoBloque> obtenerBloquesEnProgreso(Usuario usuario) {
        return progresoBloqueDAO.buscarProgresosActivos(usuario);
    }

    /**
     * Obtiene una lista de todos los bloques que un usuario ha completado.
     *
     * @param usuario El usuario cuyos bloques completados se desean obtener.
     * @return Una lista de objetos ProgresoBloque que representan los bloques completados del usuario.
     */
    public List<ProgresoBloque> obtenerBloquesCompletados(Usuario usuario) {
        return progresoBloqueDAO.buscarProgresosPorUsuario(usuario)
                .stream()
                .filter(ProgresoBloque::isCompletado)
                .collect(Collectors.toList());
    }

    /**
     * Verifica si un curso específico ha sido completamente terminado por un usuario.
     * Un curso se considera completado si todos sus bloques han sido completados.
     *
     * @param usuario El usuario para el que se verifica el estado del curso.
     * @param curso El curso que se desea verificar.
     * @return true si el curso está completado por el usuario, false en caso contrario.
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
     * Calcula el porcentaje de progreso total de un usuario en un curso determinado.
     * El porcentaje se basa en la cantidad de ejercicios completados en relación con el total de ejercicios en el curso.
     *
     * @param usuario El usuario para el que se calcula el porcentaje de progreso.
     * @param curso El curso del que se desea calcular el porcentaje.
     * @return Un valor double que representa el porcentaje de progreso del curso (0.0 a 100.0).
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
     * Obtiene el siguiente bloque recomendado para un usuario en un curso.
     * El "siguiente" bloque es el primer bloque no completado en la secuencia del curso.
     *
     * @param usuario El usuario para el que se busca el siguiente bloque.
     * @param curso El curso en el que se busca el siguiente bloque.
     * @return El objeto Bloque recomendado si existe uno no completado, o null si todos los bloques están completados o el curso no tiene bloques.
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
     * Obtiene estadísticas resumidas del progreso de un usuario a través de todos sus bloques.
     * Incluye el número de bloques iniciados, completados, en progreso y la cantidad de cursos iniciados.
     *
     * @param usuario El usuario del que se desean obtener las estadísticas.
     * @return Un objeto EstadisticasUsuario que contiene el resumen del progreso del usuario.
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
     * Clase interna estática para almacenar y representar las estadísticas resumidas de un usuario.
     */
    public static class EstadisticasUsuario {
        private final int bloquesIniciados;
        private final int bloquesCompletados;
        private final int bloquesEnProgreso;
        private final int cursosIniciados;

        /**
         * Constructor para EstadisticasUsuario.
         *
         * @param bloquesIniciados El número total de bloques que el usuario ha iniciado.
         * @param bloquesCompletados El número de bloques que el usuario ha completado.
         * @param bloquesEnProgreso El número de bloques que el usuario tiene en progreso (iniciados pero no completados).
         * @param cursosIniciados El número de cursos diferentes en los que el usuario ha iniciado al menos un bloque.
         */
        public EstadisticasUsuario(int bloquesIniciados, int bloquesCompletados,
                                   int bloquesEnProgreso, int cursosIniciados) {
            this.bloquesIniciados = bloquesIniciados;
            this.bloquesCompletados = bloquesCompletados;
            this.bloquesEnProgreso = bloquesEnProgreso;
            this.cursosIniciados = cursosIniciados;
        }

        // Getters
        /**
         * Obtiene el número de bloques que el usuario ha iniciado.
         * @return El número de bloques iniciados.
         */
        public int getBloquesIniciados() { return bloquesIniciados; }
        /**
         * Obtiene el número de bloques que el usuario ha completado.
         * @return El número de bloques completados.
         */
        public int getBloquesCompletados() { return bloquesCompletados; }
        /**
         * Obtiene el número de bloques que el usuario tiene en progreso.
         * @return El número de bloques en progreso.
         */
        public int getBloquesEnProgreso() { return bloquesEnProgreso; }
        /**
         * Obtiene el número de cursos únicos en los que el usuario ha iniciado al menos un bloque.
         * @return El número de cursos iniciados.
         */
        public int getCursosIniciados() { return cursosIniciados; }

        /**
         * Retorna una representación en cadena de las estadísticas del usuario.
         * @return Una cadena formateada con las estadísticas de bloques y cursos.
         */
        @Override
        public String toString() {
            return String.format("Estadísticas: %d bloques iniciados, %d completados, %d en progreso (%d cursos)",
                    bloquesIniciados, bloquesCompletados, bloquesEnProgreso, cursosIniciados);
        }
    }
}