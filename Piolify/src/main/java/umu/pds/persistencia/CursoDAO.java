package umu.pds.persistencia;

import java.net.MalformedURLException;
import java.util.List;

import umu.pds.modelo.Curso;

/**
 * Interfaz DAO (Data Access Object) para la separación de la lógica de negocio
 * de los cursos con su lógica de almacenamiento.
 */
public interface CursoDAO {

	/**
     * Método que persiste un curso en la base de datos.
     * @param curso Instancia {@link Curso} a registrar.
     */
	public void registrarCurso(Curso curso);
	
	 /**
     * Método que recupera un curso por su identificador.
     * @param id Identificador del curso.
     * @return Instancia {@link Curso} correspondiente al identificador.
     */
	public Curso recuperarCurso(int id);
	
	/**
     * Método que recupera todos los cursos existentes en la base de datos.
     * @return Lista de instancias {@link Curso} registrados en el sistema.
     * @throws MalformedURLException en caso de que alguna URL asociada a un curso no sea válida.
     */
	public List<Curso> recuperarTodosCursos() throws MalformedURLException;
	
}
