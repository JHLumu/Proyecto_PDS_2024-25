package umu.pds.modelo;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import umu.pds.persistencia.CursoDAO;
import umu.pds.persistencia.FactoriaDAO;
import umu.pds.persistencia.JPAFactoriaDAO;

/**
 * Clase que actua como Repositorio/Catálogo que implementa métodos CRUD para 
 * las instancias tipo {@link Curso}. Actua como intermediario entre la capa de
 * dominio y la capa de persistencia, recuperando los cursos almacenados en el sistema
 * al iniciar su ejecución, reduciendo la latencia percibida por el usuario debido a un número elevado de accesos a 
 * la base de datos.
 * 
 * <br> Además, se ha implementado el Patrón Singleton.
 */
public class CatalogoCursos {
	
	/**
	 * Instancia única de {@link CatalogoCursos} (Singleton).
	 */
	private static final CatalogoCursos instancia = new CatalogoCursos();
	
	/**
	 * Instancia {@link CursoDAO} para la recuperación de los cursos desde la base de datos.
	 */
	private final CursoDAO cursoDAO;
	
	/**
	 * Map entre Identificador de curso - Instancia tipo {@link Curso}.
	 */
	private final Map<Long, Curso> cursos;
	
	/**
	 * Constructor privado para la inicialización de la instancia única de {@link CatalogoCursos}
	 */
	private CatalogoCursos() {
		this.cursoDAO  = FactoriaDAO.getInstancia(JPAFactoriaDAO.class.getName()).getCursoDAO();
		this.cursos = new HashMap<Long,Curso>();
		cargarCatalogo();
	}
	
	/**
	 * Método estático para obtener la instancia única del catálogo de cursos
	 * @return Única nstancia del catálogo de cursos.
	 */
	public static CatalogoCursos getInstancia() {return instancia;}
	
	/**
	 * Guarda un curso en el catálogo.
	 * 
	 * @param curso Curso a guardar en el catálogo.
	 */
	public void nuevoCurso(Curso curso) {this.cursos.put(curso.getId(), curso);}
	
	/**
	 * Verifica si existe un curso según un identificador.
	 * 
	 * @param id ID del curso a verificar.
	 * @return true si el curso existe en el catálogo de cursos, false en caso
	 *         contrario.
	 */
	public boolean existeCurso(Long id) {
		return this.cursos.containsKey(id);
	}
	
	/**
	 * Obtiene un curso por su ID.
	 * 
	 * @param id ID del curso a obtener.
	 * @return Optional que contiene el curso si se encuentra, o vacío si no existe.
	 */
	public Optional<Curso> obtenerCursoPorID(Long id) {return Optional.ofNullable(this.cursos.get(id));}
	
	/**
	 * Recupera todos los cursos almacenados en la base de datos del sistema..
	 * */
	private void cargarCatalogo() {
		try {
			List<Curso> listaCursos = cursoDAO.recuperarTodosCursos();
			for (Curso curso : listaCursos) {
				this.cursos.put(curso.getId(), curso);
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
	}
}
