package umu.pds.persistencia;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

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
import umu.pds.modelo.ProgresoBloque;
import umu.pds.modelo.TipoEstrategia;
import umu.pds.modelo.Usuario;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AdaptadorProgresoBloqueDAOTest {

	private EntityManagerFactory emf;
	
	private UsuarioDAO usuarioDAO;
	
	private CursoDAO cursoDAO;
	
	private AdaptadorProgresoBloqueDAO progresoBloqueDAO;
	
	@BeforeAll
	void setUp() { 
	
		this.emf = Persistence.createEntityManagerFactory("Piolify");
		this.progresoBloqueDAO = new AdaptadorProgresoBloqueDAO(emf);
		this.usuarioDAO = new AdaptadorUsuarioDAO(emf);
		this.cursoDAO = new AdaptadorCursoDAO(emf);
	}
	
	@BeforeEach
	void clean() {
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		em.createQuery("DELETE FROM Usuario").executeUpdate();
		em.createQuery("DELETE FROM Ejercicio").executeUpdate();
		em.createQuery("DELETE FROM Curso").executeUpdate();
		em.createQuery("DELETE FROM Bloque").executeUpdate();
		em.createQuery("DELETE FROM ProgresoBloque").executeUpdate();
		em.getTransaction().commit();
		em.close();
	}
	
	@AfterAll
	void closeEmf(){
		emf.close();
	}
	
	
	ProgresoBloque inicializarProgresoBloque(Usuario ...usuario) {
		Usuario user;
		if (usuario.length == 0) {
			user = new Usuario();
			this.usuarioDAO.registrarUsuario(user);
		}
		else user=usuario[0];
		Curso curso = new Curso();
		Bloque bloque = new Bloque();
		Ejercicio ejercicio = new EjercicioFlashcard();
		ejercicio.setBloque(bloque);
		bloque.setEjercicios(List.of(ejercicio));
		bloque.setCurso(curso);
		curso.setBloques(List.of(bloque));
		
		this.cursoDAO.registrarCurso(curso);
		ProgresoBloque res = new ProgresoBloque(user, bloque, TipoEstrategia.SECUENCIAL);
		return res;
	}
		
	
	@Test
	void testGuardarProgreso() {
		
		ProgresoBloque progreso = inicializarProgresoBloque();
		this.progresoBloqueDAO.guardarProgreso(progreso);
		assertNotNull(progreso.getId());
		
	}
	
	@Test
	void testActualizarProgreso() {
		
		ProgresoBloque progreso = inicializarProgresoBloque();
		this.progresoBloqueDAO.guardarProgreso(progreso);
		progreso.setCompletado(true);
		this.progresoBloqueDAO.actualizarProgreso(progreso);
		Optional<ProgresoBloque> optionalProgreso = this.progresoBloqueDAO.buscarProgreso(progreso.getUsuario(), progreso.getBloque());
		assertTrue(optionalProgreso.isPresent());
		assertEquals(progreso.getId(), optionalProgreso.get().getId());
		assertEquals(progreso.isCompletado(), optionalProgreso.get().isCompletado());
					
	}
	
	void assertBuscarProgreso(ProgresoBloque progreso, ProgresoBloque progresoRecuperado) {
		assertEquals(progreso.getId(), progresoRecuperado.getId(), "Progresos no tienen la misma ID");
		assertEquals(progreso.getBloque().getId(), progresoRecuperado.getBloque().getId(), "Bloques no tienen la misma ID");
		assertEquals(progreso.getUsuario().getId(), progresoRecuperado.getUsuario().getId(), "Usuarios no tienen la misma ID");
		assertEquals(progreso.getBloque().getCurso().getId(), progresoRecuperado.getBloque().getCurso().getId(), "Cursos no tienen la misma ID");
		assertEquals(progreso.getBloque().getEjercicios().size(), progresoRecuperado.getBloque().getEjercicios().size(), "Número de ejercicios no son iguales");
		assertEquals(progreso.getBloque().getEjercicios().get(0).getId(), progresoRecuperado.getBloque().getEjercicios().get(0).getId(), "Ejercicios no tienen la misma ID");
	}
	
	@Test 
	void testBuscarProgreso() {
		ProgresoBloque progreso = inicializarProgresoBloque();
		this.progresoBloqueDAO.guardarProgreso(progreso);
		Optional<ProgresoBloque> optionalProgreso = this.progresoBloqueDAO.buscarProgreso(progreso.getUsuario(), progreso.getBloque());
		assertTrue(optionalProgreso.isPresent());
		ProgresoBloque progresoRecuperado = optionalProgreso.get();
		assertBuscarProgreso(progreso, progresoRecuperado);
		
	}
	
	@Test
	void testBuscarProgresoPorCursoYPorUsuario() {
		Usuario usuario1 = new Usuario();
		Usuario usuario2 = new Usuario();
		
		Curso curso1 = new Curso();
		Curso curso2 = new Curso();
		
		Bloque bloque1 = new Bloque();
		Bloque bloque2 = new Bloque();
		Bloque bloque3 = new Bloque();

		Ejercicio ejercicio1 = new EjercicioFlashcard();
		Ejercicio ejercicio2 = new EjercicioFlashcard();
		Ejercicio ejercicio3 = new EjercicioFlashcard();
		
		ejercicio1.setBloque(bloque1);
		ejercicio2.setBloque(bloque2);
		ejercicio3.setBloque(bloque3);
		
		bloque1.setEjercicios(List.of(ejercicio1));
		bloque2.setEjercicios(List.of(ejercicio2));
		bloque3.setEjercicios(List.of(ejercicio3));
		
		bloque1.setCurso(curso1);
		bloque2.setCurso(curso1);
		bloque3.setCurso(curso2);
		
		curso1.setBloques(List.of(bloque1,bloque2));
		curso2.setBloques(List.of(bloque3));
		
		this.usuarioDAO.registrarUsuario(usuario1);
		this.usuarioDAO.registrarUsuario(usuario2);
		this.cursoDAO.registrarCurso(curso1);
		this.cursoDAO.registrarCurso(curso2);
		
		
		ProgresoBloque progreso1 = new ProgresoBloque(usuario1, bloque1, TipoEstrategia.SECUENCIAL);
		ProgresoBloque progreso2 = new ProgresoBloque(usuario2, bloque2, TipoEstrategia.SECUENCIAL);
		ProgresoBloque progreso3 = new ProgresoBloque(usuario1, bloque3, TipoEstrategia.SECUENCIAL);
		
		this.progresoBloqueDAO.guardarProgreso(progreso1);
		this.progresoBloqueDAO.guardarProgreso(progreso2);
		this.progresoBloqueDAO.guardarProgreso(progreso3);
		
		List<ProgresoBloque> progresosRecuperadosPorCurso = this.progresoBloqueDAO.buscarProgresosPorCurso(usuario1, curso1);
		assertEquals(1, progresosRecuperadosPorCurso.size());
		assertBuscarProgreso(progreso1, progresosRecuperadosPorCurso.get(0));
		
		List<ProgresoBloque> progresosRecuperadosPorUsuario = this.progresoBloqueDAO.buscarProgresosPorUsuario(usuario1);
		assertEquals(2, progresosRecuperadosPorUsuario.size());
		assertBuscarProgreso(progreso1, progresosRecuperadosPorUsuario.get(0));
		assertBuscarProgreso(progreso3, progresosRecuperadosPorUsuario.get(1));
			
	}
	
	@Test
	void testEliminarProgreso() {
		ProgresoBloque progreso = inicializarProgresoBloque();
		Usuario usuario = progreso.getUsuario();
		Bloque bloque = progreso.getBloque();
		this.progresoBloqueDAO.guardarProgreso(progreso);
		this.progresoBloqueDAO.eliminarProgreso(progreso);
		assertTrue(this.progresoBloqueDAO.buscarProgreso(usuario, bloque).isEmpty());
		
	}
	
	@Test 
	void testBuscarProgresosActivos() {
	
		Usuario usuario = new Usuario();
		this.usuarioDAO.registrarUsuario(usuario);
		ProgresoBloque progreso1 = inicializarProgresoBloque(usuario);
		ProgresoBloque progreso2 = inicializarProgresoBloque(usuario);
		progreso2.setCompletado(true);
		ProgresoBloque progreso3 = inicializarProgresoBloque();
		
		this.progresoBloqueDAO.guardarProgreso(progreso1);
		this.progresoBloqueDAO.guardarProgreso(progreso2);
		this.progresoBloqueDAO.guardarProgreso(progreso3);
		
		List<ProgresoBloque> progresosRecuperados = this.progresoBloqueDAO.buscarProgresosActivos(usuario);
		assertEquals(1, progresosRecuperados.size());
		assertBuscarProgreso(progreso1, progresosRecuperados.get(0));
		
		
	}
}
