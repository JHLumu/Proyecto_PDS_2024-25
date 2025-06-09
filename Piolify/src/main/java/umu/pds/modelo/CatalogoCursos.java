package umu.pds.modelo;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import umu.pds.persistencia.CursoDAO;
import umu.pds.persistencia.FactoriaDAO;
import umu.pds.persistencia.JPAFactoriaDAO;

public class CatalogoCursos {

	private static final CatalogoCursos instancia = new CatalogoCursos();
	private final CursoDAO cursoDAO;
	private final Map<Long, Curso> cursos;//Mapping entre ID Curso - Instancia Curso
	
	/**
	 * Constructor privado para inicializar el catálogo de cursos y cargar los cursos desde la base de datos
	 */
	private CatalogoCursos() {
		this.cursoDAO  = FactoriaDAO.getInstancia(JPAFactoriaDAO.class.getName()).getCursoDAO();
		this.cursos = new HashMap<Long,Curso>();
		cargarCatalogo();
	}
	
	/**
	 * Método estático para obtener la instancia única del catálogo de cursos
	 * @return Instancia del catálogo de cursos
	 */
	public static CatalogoCursos getInstancia() {return instancia;}
	
	/**
	 * Guarda un curso en el catálogo de cursos
	 * 
	 * @param curso Curso a guardar en el catálogo
	 */
	
	public void nuevoCurso(Curso curso) {this.cursos.put(curso.getId(), curso);}
	
	/**
	 * Verifica si existe un curso por su ID
	 * 
	 * @param id ID del curso a verificar
	 * @return true si el curso existe en el catálogo de cursos, false en caso
	 *         contrario
	 */
	public boolean existeCurso(Long id) {
		return this.cursos.containsKey(id);
	}
	
	/**
	 * Obtiene un curso por su ID
	 * 
	 * @param id ID del curso a obtener
	 * @return Un Optional que contiene el curso si se encuentra, o vacío si no
	 *         existe
	 */
	public Optional<Curso> obtenerCursoPorID(Long id) {return Optional.ofNullable(this.cursos.get(id));}
	
	/**
	 * carga el catálogo de cursos desde la base de datos
	 * */
	private void cargarCatalogo() {
		try {
			List<Curso> listaCursos = cursoDAO.recuperarTodosCursos();
			for (Curso curso : listaCursos) {
				this.cursos.put(curso.getId(), curso);
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			System.err.println();
			e.printStackTrace();
		}
		
	}
}
