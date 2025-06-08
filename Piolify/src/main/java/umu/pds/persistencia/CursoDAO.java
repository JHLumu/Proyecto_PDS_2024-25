package umu.pds.persistencia;

import java.net.MalformedURLException;
import java.util.List;

import umu.pds.modelo.Curso;

public interface CursoDAO {

	public void registrarCurso(Curso curso);
	public Curso recuperarCurso(int id);
	public List<Curso> recuperarTodosCursos() throws MalformedURLException;
	
}
