package umu.pds.persistencia;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import umu.pds.modelo.Bloque;
import umu.pds.modelo.Curso;
import umu.pds.modelo.Ejercicio;
import umu.pds.modelo.EjercicioFlashcard;
import umu.pds.modelo.EjercicioOpcionMultiple;
import umu.pds.modelo.EjercicioRellenarHuecos;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AdaptadorCursoDAOTest {

	private  EntityManagerFactory emf;
	
	private  AdaptadorCursoDAO cursoDAO;

	
	@BeforeAll
	void setUp() { 
		this.emf = Persistence.createEntityManagerFactory("Piolify");
		this.cursoDAO = new AdaptadorCursoDAO(emf);
	}
	
	@BeforeEach
	void clean() {
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		em.createQuery("DELETE FROM Ejercicio").executeUpdate();
		em.createQuery("DELETE FROM Bloque").executeUpdate();
		em.createQuery("DELETE FROM Curso").executeUpdate();
		em.getTransaction().commit();
		em.close();
	}
	
	@AfterAll
	void closeEmf(){
		emf.close();
	}
	
	void inicializarEjercicio(Ejercicio ej, Bloque b) {
		ej.setContenido("ejemplo");
		ej.setRespuesta("ejemplo");
		ej.setBloque(b);
	}
	
	Curso inicializarCurso(String titulo) {
		
		Ejercicio ejercicio1 = new EjercicioFlashcard();
		Ejercicio ejercicio2 = new EjercicioOpcionMultiple();
		Ejercicio ejercicio3 = new EjercicioRellenarHuecos();
		
		Bloque bloque = new Bloque();
		bloque.setTitulo(titulo);
		bloque.setDescripcion("ejemplo");
		bloque.setEjercicios(List.of(ejercicio1, ejercicio2, ejercicio3));
		
		inicializarEjercicio(ejercicio1, bloque);
		inicializarEjercicio(ejercicio2, bloque);
		inicializarEjercicio(ejercicio3, bloque);

		Curso curso = new Curso();
		curso.setTitulo("Ejemplo");
		curso.setDescripcion("ejemplo");
		curso.setDificultad("5");
		curso.setBloques(List.of(bloque));
		
		bloque.setCurso(curso);
		return curso;
		
	}
	
	@Test
	void testRegistrarCurso() {
		Curso curso = inicializarCurso("ejemplo");
		this.cursoDAO.registrarCurso(curso);
		assertNotNull(curso.getId());
	}
	
	void assertRecuperarCurso(Curso curso, Curso cursoRecuperado) {
		Bloque bloque = curso.getBloques().get(0);
		Bloque bloqueRecuperado = cursoRecuperado.getBloques().get(0);
		
		List<Ejercicio> ejercicios = bloque.getEjercicios();
		List<Ejercicio> ejerciciosRecuperados = bloqueRecuperado.getEjercicios();
		
		assertEquals(curso.getId(), cursoRecuperado.getId());
		assertEquals(curso.getTitulo(), cursoRecuperado.getTitulo());
		assertEquals(curso.getDificultad(), cursoRecuperado.getDificultad());
		assertEquals(bloque.getId(), bloqueRecuperado.getId());
		assertEquals(bloque.getTitulo(), bloqueRecuperado.getTitulo());
		assertEquals(bloque.getDescripcion(), bloqueRecuperado.getDescripcion());
		
		for(int i = 0; i < ejercicios.size(); i++) {
			Ejercicio ejercicio = ejercicios.get(i);
			Ejercicio ejercicioRecuperado = ejerciciosRecuperados.get(i);
			assertEquals(ejercicio.getId(), ejercicioRecuperado.getId());
			assertEquals(ejercicio.getContenido(), ejercicio.getRespuesta());
		}
		
	}
	
	@Test
	void testRecuperarCurso() {
		Curso curso = inicializarCurso("ejemplo");
		this.cursoDAO.registrarCurso(curso);
		Curso cursoRecuperado = this.cursoDAO.recuperarCurso(curso.getId().intValue());
		assertRecuperarCurso(curso, cursoRecuperado);
		
		
		
	}
	
	@Test
	void testRecuperarTodosCursos() {
		List<Curso> cursos = List.of(inicializarCurso("ejemplo"), inicializarCurso("ejemplo2"));
		for (Curso curso : cursos) this.cursoDAO.registrarCurso(curso);
		List<Curso> cursosRecuperados = this.cursoDAO.recuperarTodosCursos();
		assertEquals(cursos.size(), cursosRecuperados.size());
		for (int i = 0; i < cursos.size(); i++) assertRecuperarCurso(cursos.get(i), cursosRecuperados.get(i));
	}
	
}
