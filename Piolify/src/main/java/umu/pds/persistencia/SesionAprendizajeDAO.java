package umu.pds.persistencia;

import java.util.List;
import umu.pds.modelo.Curso;
import umu.pds.modelo.SesionAprendizaje;
import umu.pds.modelo.Usuario;

/**
 * Interfaz DAO (Data Access Object) para la separación de la lógica de negocio
 * de las sesiones de aprendizaje con su lógica de almacenamiento.
 */
public interface SesionAprendizajeDAO {
	
	/**
     * Método que persiste una sesión de aprendizaje en la base de datos.
     * @param sesion Instancia {@link SesionAprendizaje} a guardar.
     */
    void guardarSesion(SesionAprendizaje sesion);
    
    /**
     * Método que actualiza una sesión de aprendizaje existente.
     * @param sesion Instancia {@link SesionAprendizaje} con cambios sin persistir.
     */
    void actualizarSesion(SesionAprendizaje sesion);
    
    /**
     * Método que recupera una sesión de aprendizaje existente.
     * @param id Identificador de la sesión.
     * @return Instancia {@link SesionAprendizaje} correspondiente al identificador.
     */
    SesionAprendizaje buscarPorId(Long id);
    
    /**
     * Método que recupera todas las sesiones de aprendizaje de un usuario.
     * @param usuario Instancia {@link Usuario} cuyas sesiones se quieren recuperar.
     * @return Lista de instancias {@link SesionAprendizaje} asociadas al usuario.
     */
    List<SesionAprendizaje> buscarSesionesPorUsuario(Usuario usuario);
    
    /**
     * Método que recupera todas las sesiones de aprendizaje de un usuario para un curso específico.
     * @param usuario Instancia {@link Usuario} asociado a las sesiones de aprendizaje.
     * @param curso   Instancia {@link Curso} asociado a las sesiones de aprendizaje..
     * @return Lista de instancias {@link SesionAprendizaje} asociadas al usuario y curso.
     */
    List<SesionAprendizaje> buscarSesionesPorUsuarioYCurso(Usuario usuario, Curso curso);
    
    /**
     * Método que recupera únicamente las sesiones completadas de un usuario.
     * @param usuario Instancia {@link Usuario} cuyas sesiones completadas se quieren recuperar.
     * @return Lista de instancias {@link SesionAprendizaje} completadas por el usuario.
     */
    List<SesionAprendizaje> buscarSesionesCompletadas(Usuario usuario);
    
    /**
     * Método que elimina una sesión de aprendizaje de la base de datos.
     * @param sesion Instancia {@link SesionAprendizaje} a eliminar.
     */
    void eliminarSesion(SesionAprendizaje sesion);
}
