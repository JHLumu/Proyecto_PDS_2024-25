package umu.pds.persistencia;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.Comparator;
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
import umu.pds.modelo.SesionAprendizaje;
import umu.pds.modelo.Usuario;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AdaptadorSesionAprendizajeDAOTest {

	private EntityManagerFactory emf;
	private AdaptadorSesionAprendizajeDAO sesionDAO;
	
	
	@BeforeAll
	void setUp() { 
		this.emf = Persistence.createEntityManagerFactory("Piolify");
		this.sesionDAO = new AdaptadorSesionAprendizajeDAO(emf);
	}
	
	@BeforeEach
	void clean() {
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		em.createQuery("DELETE FROM Usuario").executeUpdate();
		em.createQuery("DELETE FROM Ejercicio").executeUpdate();
		em.createQuery("DELETE FROM Curso").executeUpdate();
		em.createQuery("DELETE FROM SesionAprendizaje").executeUpdate();
		em.getTransaction().commit();
		em.close();
	}
	
	@AfterAll
	void closeEmf(){
		emf.close();
	}
	
	SesionAprendizaje inicializarSesionAprendizaje(Usuario usuario, Curso curso) {

		SesionAprendizaje sesion = new SesionAprendizaje();
		sesion.setUsuario(usuario);
		sesion.setCurso(curso);
		Ejercicio ejercicio = curso.getBloques().get(0).getEjercicios().get(0);
		sesion.setEjercicio(ejercicio);
		
		return sesion;
	}
	
	Usuario inicializarUsuario() {
		UsuarioDAO usuarioDAO = new AdaptadorUsuarioDAO(emf);
		Usuario usuario = new Usuario();
		usuarioDAO.registrarUsuario(usuario);
		return usuario;
	}
	
	Curso inicializarCurso() {
		CursoDAO cursoDAO = new AdaptadorCursoDAO(emf);
		Curso curso = new Curso();
		Bloque bloque = new Bloque();
		Ejercicio ejercicio = new EjercicioFlashcard();		
		bloque.setEjercicios(List.of(ejercicio));
		ejercicio.setBloque(bloque);
		bloque.setCurso(curso);
		curso.setBloques(List.of(bloque));
		cursoDAO.registrarCurso(curso);
		return curso;
		
	}
	
	void assertRecuperarCurso(SesionAprendizaje sesion, SesionAprendizaje sesionRecuperada) {
		assertEquals(sesion.getId(), sesionRecuperada.getId());
		assertEquals(sesion.getUsuario().getId(), sesionRecuperada.getUsuario().getId());
		assertEquals(sesion.getCurso().getId(), sesionRecuperada.getCurso().getId());
		assertEquals(sesion.getEjercicio().getId(), sesionRecuperada.getEjercicio().getId());
	}
	
	@Test
	void testGuardarSesion() {
		SesionAprendizaje sesion = new SesionAprendizaje();
		this.sesionDAO.guardarSesion(sesion);
		assertNotNull(sesion.getId());
	}
	
	@Test
	void testActualizarSesion() {
		
		SesionAprendizaje sesion = new SesionAprendizaje();
		this.sesionDAO.guardarSesion(sesion);
		sesion.setAciertos(5);
		this.sesionDAO.actualizarSesion(sesion);
		SesionAprendizaje sesionRecuperada = this.sesionDAO.buscarPorId(sesion.getId());
		assertEquals(sesion.getAciertos(), sesionRecuperada.getAciertos());
	}
	
	@Test
	void testBuscarPorId() {
		SesionAprendizaje sesion = inicializarSesionAprendizaje(inicializarUsuario(), inicializarCurso());
		this.sesionDAO.guardarSesion(sesion);
		SesionAprendizaje sesionRecuperada = this.sesionDAO.buscarPorId(sesion.getId());
		assertRecuperarCurso(sesion, sesionRecuperada);
	}
	
	@Test
	void testBuscarSesionesPorUsuario() {
		Usuario usuario = inicializarUsuario();
		SesionAprendizaje sesion1 = inicializarSesionAprendizaje(usuario, inicializarCurso());
		SesionAprendizaje sesion2 = inicializarSesionAprendizaje(usuario, inicializarCurso());
		List<SesionAprendizaje> sesiones = List.of(sesion1, sesion2);
		this.sesionDAO.guardarSesion(sesion1);
		this.sesionDAO.guardarSesion(sesion2);
		List<SesionAprendizaje> sesionesRecuperadas = this.sesionDAO.buscarSesionesPorUsuario(usuario).stream().sorted(Comparator.comparing(SesionAprendizaje::getId)).toList();
		assertEquals(sesiones.size(), sesionesRecuperadas.size());
		for (int i = 0; i < sesiones.size(); i++) assertRecuperarCurso(sesiones.get(i), sesionesRecuperadas.get(i));
	}
	
	
	
	@Test
	void testBuscarSesionesPorUsuarioYCurso() {
		Usuario usuario = inicializarUsuario();
		Curso curso = inicializarCurso();
		SesionAprendizaje sesion1 = inicializarSesionAprendizaje(usuario, curso);
		SesionAprendizaje sesion2 = inicializarSesionAprendizaje(usuario, curso);
		List<SesionAprendizaje> sesiones = List.of(sesion1, sesion2);
		this.sesionDAO.guardarSesion(sesion1);
		this.sesionDAO.guardarSesion(sesion2);
		List<SesionAprendizaje> sesionesRecuperadas = this.sesionDAO.buscarSesionesPorUsuarioYCurso(usuario, curso);
		assertEquals(sesiones.size(), sesionesRecuperadas.size());
		for (int i = 0; i < sesiones.size(); i++) assertRecuperarCurso(sesiones.get(i), sesionesRecuperadas.get(i));
	
	}
	
	@Test
	void testBuscarSesionesCompletadas() {
	Usuario usuario = inicializarUsuario();
	Curso curso = inicializarCurso();
	SesionAprendizaje sesion1 = inicializarSesionAprendizaje(usuario, curso);
	SesionAprendizaje sesion2 = inicializarSesionAprendizaje(usuario, curso);
	sesion1.setCompletada(true);
	this.sesionDAO.guardarSesion(sesion1);
	this.sesionDAO.guardarSesion(sesion2);
	List<SesionAprendizaje> sesionesRecuperadas = this.sesionDAO.buscarSesionesCompletadas(usuario);
	assertEquals(1, sesionesRecuperadas.size());
	assertRecuperarCurso(sesion1, sesionesRecuperadas.get(0));
	
	}
	
	@Test
	void testEliminarSesion() {
		SesionAprendizaje sesion = new SesionAprendizaje();
		this.sesionDAO.guardarSesion(sesion);
		Long id = sesion.getId();
		this.sesionDAO.eliminarSesion(sesion);
		assertNull(this.sesionDAO.buscarPorId(id));
		
	}
}
