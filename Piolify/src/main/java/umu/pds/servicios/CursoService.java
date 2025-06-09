package umu.pds.servicios;

import umu.pds.modelo.CatalogoCursos;
import umu.pds.modelo.Curso;
import umu.pds.modelo.EstrategiaFactory;
import umu.pds.persistencia.CursoDAO;
import umu.pds.persistencia.JPAFactoriaDAO;

public class CursoService {

	private final CursoDAO repoCursos;
	private final CatalogoCursos catalogoCurso;
	
	public CursoService() {
		this.repoCursos = JPAFactoriaDAO.getInstancia().getCursoDAO();
		this.catalogoCurso = CatalogoCursos.getInstancia();
	}
	
	public void guardarCurso(Curso curso) {
		repoCursos.registrarCurso(curso);
		catalogoCurso.nuevoCurso(curso);
	}
	
	public Curso recuperarCurso(Long id) {
		return catalogoCurso.obtenerCursoPorID(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
	}

}
