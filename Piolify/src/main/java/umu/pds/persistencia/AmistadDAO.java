package umu.pds.persistencia;

import java.util.List;

import umu.pds.modelo.Amistad;
import umu.pds.modelo.Usuario;

/**
 * Interfaz DAO (Data Access Object) para la separación de la lógica de negocio
 * de las amistades entre usuarios con su lógica de almacenamiento.
 */
public interface AmistadDAO {
    
	/**
     * Método que persiste una amistad en la base de datos.
     * @param amistad Instancia {@link Amistad} a guardar.
     */
    void guardarAmistad(Amistad amistad);
    

    /**
     * Método que actualiza una amistad existente.
     * @param amistad Instancia {@link Amistad} con los cambios a persistir.
     */
    void actualizarAmistad(Amistad amistad);
    
    /**
     * Método que recupera una amistad existente.
     * @param id Identificador de la amistad.
     * @return Instancia {@link Amistad} correspondiente al identificador.
     */
    Amistad buscarPorId(Long id);
    
    /**
     * Método que recupera todas las solicitudes de amistad pendientes de un usuario.
     * @param receptor Instancia {@link Usuario} que recibe las solicitudes.
     * @return Lista de instancias {@link Amistad} en estado pendiente.
     */
    List<Amistad> buscarSolicitudesPendientes(Usuario receptor);
    
    /**
     * Método que recupera todas las amistades confirmadas de un usuario.
     * @param usuario Instancia {@link Usuario} cuyas amistades se quieren recuperar.
     * @return Lista de instancias {@link Amistad} existentes.
     */
    List<Amistad> buscarAmistades(Usuario usuario);
    
    /**
     * Método que comprueba si existe una amistad entre dos usuarios.
     * @param usuario1 Instancia {@link Usuario} emisor de la amistad.
     * @param usuario2 Instancia {@link Usuario} receptor de la amistad.
     * @return {@code true} si existe una amistad entre ambos usuarios, {@code false} en caso contrario.
     */
    boolean existeAmistad(Usuario usuario1, Usuario usuario2);
    
    /**
     * Método que elimina una amistad de la base de datos.
     * @param amistad Instancia {@link Amistad} a eliminar.
     */
    void eliminarAmistad(Amistad amistad);
}