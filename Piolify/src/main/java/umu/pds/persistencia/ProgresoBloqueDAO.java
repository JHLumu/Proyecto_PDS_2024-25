package umu.pds.persistencia;

import java.util.List;
import java.util.Optional;
import umu.pds.modelo.ProgresoBloque;
import umu.pds.modelo.Usuario;
import umu.pds.modelo.Bloque;
import umu.pds.modelo.Curso;

public interface ProgresoBloqueDAO {
    
    /**
     * Guarda un nuevo progreso de bloque
     */
    void guardarProgreso(ProgresoBloque progreso);
    
    /**
     * Actualiza un progreso existente
     */
    void actualizarProgreso(ProgresoBloque progreso);
    
    /**
     * Busca el progreso de un usuario en un bloque específico
     */
    Optional<ProgresoBloque> buscarProgreso(Usuario usuario, Bloque bloque);
    
    /**
     * Obtiene todos los progresos de un usuario en un curso
     */
    List<ProgresoBloque> buscarProgresosPorCurso(Usuario usuario, Curso curso);
    
    /**
     * Obtiene todos los progresos de un usuario
     */
    List<ProgresoBloque> buscarProgresosPorUsuario(Usuario usuario);
    
    /**
     * Elimina el progreso de un bloque específico
     */
    void eliminarProgreso(ProgresoBloque progreso);
    
    /**
     * Busca bloques con progreso activo (no completados) para un usuario
     */
    List<ProgresoBloque> buscarProgresosActivos(Usuario usuario);
}