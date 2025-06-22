package umu.pds.servicios;

import umu.pds.modelo.CatalogoCursos;
import umu.pds.modelo.Curso;
import umu.pds.persistencia.CursoDAO;
import umu.pds.persistencia.JPAFactoriaDAO;

/**
 * Clase de apoyo para el controlador, utilizada para la gestión
 * de la persistencia de los cursos.
 */
public class CursoService {

	/**
	 * Repositorio de cursos, utilizado para la persistencia de los cursos.
	 */
	private final CursoDAO repoCursos;
	/**
	 * Catálogo de cursos, utilizado para la gestión en memoria de los cursos.
	 */
	private final CatalogoCursos catalogoCurso;
	
	/**
	 * Constructor para crear una instancia CursoService.
	 */
	public CursoService() {
		this.repoCursos = JPAFactoriaDAO.getInstancia().getCursoDAO();
		this.catalogoCurso = CatalogoCursos.getInstancia();
	}
	
	/**
	 * Método encargado de guardar tanto en la base de 
	 * datos de la aplicación cómo en la memoria un curso
	 * importado por un usuario.
	 * @param curso Curso que se acaba de importar.
	 */
	public void guardarCurso(Curso curso) {
		repoCursos.registrarCurso(curso);
		catalogoCurso.nuevoCurso(curso);
	}
	
	/**
	 * Método encargado de recuperar de la memoria de
	 * la aplicación un curso dado su ID.
	 * @param id identificador de un curso.
	 * @return Curso correspondiente al identificador pasado como parámetro.
	 */
	public Curso recuperarCurso(Long id) {
		return catalogoCurso.obtenerCursoPorID(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
	}

}
