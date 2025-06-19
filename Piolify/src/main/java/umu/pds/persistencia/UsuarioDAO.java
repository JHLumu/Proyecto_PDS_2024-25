package umu.pds.persistencia;

import java.net.MalformedURLException;
import java.util.List;

import umu.pds.modelo.Estadisticas;
import umu.pds.modelo.Usuario;;

/**
 * Interfaz DAO (Data Access Object) para la separación de la lógica de negocio
 * de los usuarios con su lógica de almacenamiento.
 */
public interface UsuarioDAO {

	/**
	 * Método que persiste un usuario en la base de datos.
	 * @param usuario Instancia {@link Usuario} a registrar.
	 */
	public void registrarUsuario(Usuario usuario);
	
	/**
	 * Método que modifica un usuario existente.
	 * @param usuario Instancia {@link Usuario} con cambios sin persistir.
	 */
	public void modificarUsuario(Usuario usuario);
	
	/**
	 * Método que recupera un usuario existente.
	 * @param id Identificador del usuario.
	 * @throws MalformedURLException en el caso de que la URL asociada al usuario no es válida.
	 */
	public Usuario recuperarUsuario(Long id) throws MalformedURLException;
	
	/**
	 * Método que recupera todos los usuarios existentes en la base de datos.
	 * @return Lista de instancias {@link Usuario} registrados en la aplicación.
	 * @throws MalformedURLException en el caso de que la URL asociada a algún usuario no es válida.
	 */
	public List<Usuario> recuperarTodosUsuarios() throws MalformedURLException;
	
	/**
	 * Método que recupera las estadísticas de un un usuario existente.
	 * @param id Identiicador del usuario.
	 * @return Instancia {@link Estadísticas} asociada al usuario.
	 */
	public Estadisticas recuperarEstadisticas(Long id);
	
	/*
	 * * Método que recupera un usuario por su email.
	 * 
	 * @param email Email del usuario.
	 * 
	 * @return Instancia {@link Usuario} asociada al email.
	 */
	public Usuario recuperarUsuarioPorEmailConLogrosYEstadisticas(String email);
	
}
