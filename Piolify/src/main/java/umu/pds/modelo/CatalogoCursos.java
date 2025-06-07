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
	
	private CatalogoCursos() {
		this.cursoDAO  = FactoriaDAO.getInstancia(JPAFactoriaDAO.class.getName()).getCursoDAO();
		this.cursos = new HashMap<Long,Curso>();
		cargarCatalogo();
	}
	
	public static CatalogoCursos getInstancia() {return instancia;}
	
	public void nuevoCurso(Curso curso) {this.cursos.put(curso.getId(), curso);}
	
	public boolean existeCurso(Long id) {
		return this.cursos.containsKey(id);
	}
	
	public Optional<Curso> obtenerCursoPorID(Long id) {return Optional.ofNullable(this.cursos.get(id));}
	
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
