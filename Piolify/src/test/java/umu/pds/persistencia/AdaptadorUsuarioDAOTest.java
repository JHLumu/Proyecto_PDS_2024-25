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
import umu.pds.modelo.Estadisticas;
import umu.pds.modelo.Usuario;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AdaptadorUsuarioDAOTest {

	private EntityManagerFactory emf;
	
	private AdaptadorUsuarioDAO usuarioDAO;
	

	
	@BeforeAll
	void setUp() { 
		this.emf = Persistence.createEntityManagerFactory("Piolify");
		this.usuarioDAO = new AdaptadorUsuarioDAO(emf);
	}
	
	@BeforeEach
	void clean() {
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		//Borrar recursivamente para no romper las reglas de integridad referencial
		em.createQuery("DELETE FROM Estadisticas").executeUpdate();
		em.createQuery("DELETE FROM Usuario").executeUpdate();
		em.getTransaction().commit();
		em.close();
	}
	
	@AfterAll
	void closeEmf(){
		emf.close();
	}
	
	
	@Test
	void testRegistrarUsuario() {
		Usuario usuarioPruebas = new Usuario("Jose", "Pérez", "Hombre", "jose@ejemplo.com", "123123", null);
		this.usuarioDAO.registrarUsuario(usuarioPruebas);
		assertNotNull(usuarioPruebas.getId()); //Si tiene una id es que ha sido persistido correctamente
	}
	
	@Test
	void testModificarUsuario() {
		Usuario usuarioPruebas = new Usuario("Jose", "Pérez", "Hombre", "jose@ejemplo.com", "123123", null);
		this.usuarioDAO.registrarUsuario(usuarioPruebas);
		usuarioPruebas.setNombre("Pablo");
		this.usuarioDAO.modificarUsuario(usuarioPruebas);
		Usuario usuarioRecuperado = this.usuarioDAO.recuperarUsuario(usuarioPruebas.getId());
		assertEquals("Pablo", usuarioRecuperado.getNombre());		
		
	}
	
	void assertRecuperarUsuario(Usuario usuarioPruebas, Usuario usuarioRecuperado) {
		assertEquals(usuarioPruebas.getId(), usuarioRecuperado.getId());
		assertEquals(usuarioPruebas.getNombre(), usuarioRecuperado.getNombre());
		assertEquals(usuarioPruebas.getApellidos(), usuarioRecuperado.getApellidos());
		assertEquals(usuarioPruebas.getGenero(), usuarioRecuperado.getGenero());
		assertEquals(usuarioPruebas.getEmail(), usuarioRecuperado.getEmail());
		assertEquals(usuarioPruebas.getPassword(), usuarioRecuperado.getPassword());
		assertEquals(usuarioPruebas.getImagenPerfil(), usuarioRecuperado.getImagenPerfil());
	}
	
	@Test
	void testRecuperarUsuario() {
		Usuario usuarioPruebas = new Usuario("Jose", "Pérez", "Hombre", "jose@ejemplo.com", "123123", null);
		this.usuarioDAO.registrarUsuario(usuarioPruebas);
		Usuario usuarioRecuperado = this.usuarioDAO.recuperarUsuario(usuarioPruebas.getId());
		assertRecuperarUsuario(usuarioPruebas, usuarioRecuperado);
	}
	
	@Test
	void testRecuperarTodosUsuarios() {
		Usuario usuario1 = new Usuario("Luis", "Pérez", "Hombre", "jose@ejemplo.com", "123123", null);
		Usuario usuario2 = new Usuario("Antonio", "Pérez", "Hombre", "luis@ejemplo.com", "321321", null);
	    
	    List<Usuario> usuarios = List.of(usuario1,usuario2);
	    this.usuarioDAO.registrarUsuario(usuario1);
	    this.usuarioDAO.registrarUsuario(usuario2);
	    
	    List<Usuario> usuariosRecuperados = this.usuarioDAO.recuperarTodosUsuarios();
	    assertEquals(usuarios.size(), usuariosRecuperados.size());
	    for (int i=0; i < usuarios.size(); i++) assertRecuperarUsuario(usuarios.get(i), usuariosRecuperados.get(i));
	    
	}
	
	@Test
	void testRecuperarEstadisticas() {
		Usuario usuarioPruebas = new Usuario("Jose", "Pérez", "Hombre", "jose@ejemplo.com", "123123", null);
		Estadisticas estadisticas = new Estadisticas();
		estadisticas.setUsuario(usuarioPruebas);
		usuarioPruebas.setEstadisticas(estadisticas);
		this.usuarioDAO.registrarUsuario(usuarioPruebas);
		assertNotNull(estadisticas.getId());
		Estadisticas estadisticasRecuperadas = this.usuarioDAO.recuperarEstadisticas(estadisticas.getId());
		assertEquals(estadisticas.getId(), estadisticasRecuperadas.getId());
		
	}
	
}
