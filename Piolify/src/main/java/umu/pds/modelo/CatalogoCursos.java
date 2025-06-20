package umu.pds.modelo;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import umu.pds.persistencia.CursoDAO;
import umu.pds.persistencia.FactoriaDAO;
import umu.pds.persistencia.JPAFactoriaDAO;
import umu.pds.persistencia.UsuarioDAO;

/**
 * Clase que actua como Repositorio/Catálogo que implementa métodos CRUD para 
 * las instancias tipo {@link Curso}. Actua como intermediario entre la capa de
 * dominio y la capa de persistencia, recuperando los cursos almacenados en el sistema
 * al iniciar su ejecución, reduciendo la latencia percibida por el usuario debido a un número elevado de accesos a 
 * la base de datos.
 * 
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
	 * Constructor para inyección de dependencias (para pruebas unitarias).
	 * @param cursoDAO Instancia {@link CursoDAO} para la persistencia de
	 * cursos.
	 * @param cursos Map entre Identificador de curso - Instancia tipo {@link Curso}.
	 */
	public CatalogoCursos(CursoDAO cursoDAO, Map<Long,Curso> cursos) {
		this.cursoDAO = cursoDAO;
		this.cursos = cursos;
		cargarCatalogo();
	}
	
	/**
	 * Método estático para obtener la instancia única del catálogo de cursos
	 * @return Única nstancia del catálogo de cursos.
	 */
	public static CatalogoCursos getInstancia() {return instancia;}
	
	/**
	 * Método que guarda un curso en el catálogo.
	 * 
	 * @param curso Instancia {@link Curso} a guardar en el catálogo.
	 */
	public void nuevoCurso(Curso curso) {this.cursos.put(curso.getId(), curso);}
	
	/**
	 * Método que verifica si existe un curso según un identificador.
	 * 
	 * @param id Identificador del curso.
	 * @return {@code true} si el curso existe en el catálogo de cursos, {@code false} en caso
	 *         contrario.
	 */
	public boolean existeCurso(Long id) {
		return this.cursos.containsKey(id);
	}
	
	/**
	 * Método que obtiene un curso por su ID.
	 * 
	 * @param id Identificador del curso.
	 * @return {@link Optional} que contiene la instancia {@link Curso} si el identificador dado es válido.
	 */
	public Optional<Curso> obtenerCursoPorID(Long id) {return Optional.ofNullable(this.cursos.get(id));}
	
	/**
	 * Método que recupera todos los cursos almacenados en la base de datos del sistema.
	 *
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
