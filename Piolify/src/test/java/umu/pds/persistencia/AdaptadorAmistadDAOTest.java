package umu.pds.persistencia;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import umu.pds.modelo.Amistad;
import umu.pds.modelo.EstadoAmistad;
import umu.pds.modelo.Usuario;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AdaptadorAmistadDAOTest {

	private EntityManagerFactory emf;
	
	private AdaptadorAmistadDAO amistadDAO;
	
	
	@BeforeAll
	void setUp() { 
		this.emf = Persistence.createEntityManagerFactory("Piolify");
		this.amistadDAO = new AdaptadorAmistadDAO(emf);
	}
	
	@BeforeEach
	void clean() {
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		em.createQuery("DELETE FROM Usuario").executeUpdate();
		em.createQuery("DELETE FROM Amistad").executeUpdate();
		em.getTransaction().commit();
		em.close();
	}
	
	@AfterAll
	void closeEmf(){
		emf.close();
	}
	

	Amistad inicializarAmistadPendiente() {
		Usuario emisor = new Usuario();
		emisor.setNombre("Emisor");
		Usuario receptor = new Usuario();
		receptor.setNombre("Receptor");
		UsuarioDAO usuarioDAO = new AdaptadorUsuarioDAO(emf);
		usuarioDAO.registrarUsuario(emisor);
		usuarioDAO.registrarUsuario(receptor);
		Amistad amistad = new Amistad(emisor,receptor);
		return amistad;
	}
	
	@Test
	void testGuardarAmistad() {
		Amistad amistad = inicializarAmistadPendiente();
		this.amistadDAO.guardarAmistad(amistad);
		assertNotNull(amistad.getId());
	}
	
	@Test
	void testActualizarAmistad() {
		Amistad amistad = inicializarAmistadPendiente();
		this.amistadDAO.guardarAmistad(amistad);
		amistad.setEstado(EstadoAmistad.ACEPTADA);
		this.amistadDAO.actualizarAmistad(amistad);
		Amistad amistadRecuperada = this.amistadDAO.buscarPorId(amistad.getId());

		assertEquals(amistad.getEstado(), amistadRecuperada.getEstado());
			
	}
	
	@Test
	void testBuscarPorId() {
		Amistad amistad = inicializarAmistadPendiente();
		this.amistadDAO.guardarAmistad(amistad);
		Amistad amistadRecuperada = this.amistadDAO.buscarPorId(amistad.getId());
		assertEquals(amistad.getId(), amistadRecuperada.getId());
		assertEquals(amistad.getEstado(), amistadRecuperada.getEstado());
		assertEquals(amistad.getFecha(), amistadRecuperada.getFecha());
		assertEquals(amistad.getUsuario1().getId(), amistadRecuperada.getUsuario1().getId());
		assertEquals(amistad.getUsuario2().getId(), amistadRecuperada.getUsuario2().getId());
	}
	
	void assertsAmistadesRecuperadas(List<Amistad> amistades, List<Amistad> amistadesRecuperadas, EstadoAmistad estado) {
		
		assertEquals(amistades.size(), amistadesRecuperadas.size());
		for (int i = 0; i < amistades.size(); i++) {
			Amistad amistad = amistades.get(i);
			Amistad amistadRecuperada = amistadesRecuperadas.get(i);
			assertEquals(estado, amistad.getEstado());
			assertEquals(estado, amistadRecuperada.getEstado());
			assertEquals(amistad.getId(), amistadRecuperada.getId());
			assertEquals(amistad.getUsuario2().getId(), amistadRecuperada.getUsuario2().getId());
		}
		
	}
	
	@Test
	void testBuscarSolicitudesPendientes() {
		Amistad amistad1 = inicializarAmistadPendiente();
		Amistad amistad2 = inicializarAmistadPendiente();
		Amistad amistad3 = inicializarAmistadPendiente();
		Usuario receptor = amistad1.getUsuario2();
		amistad2.setUsuario2(receptor);
		amistad3.setUsuario2(receptor);
	
		this.amistadDAO.guardarAmistad(amistad1);
		this.amistadDAO.guardarAmistad(amistad2);
		this.amistadDAO.guardarAmistad(amistad3);
		
		List<Amistad> amistades = List.of(amistad1,amistad2,amistad3);
		List<Amistad> amistadesRecuperadas = this.amistadDAO.buscarSolicitudesPendientes(receptor);
		assertsAmistadesRecuperadas(amistades, amistadesRecuperadas, EstadoAmistad.PENDIENTE);
		
	}
	
	Amistad inicializarAmistadAceptada(Usuario emisor, UsuarioDAO usuarioDAO) {
		Usuario receptor = new Usuario();
		receptor.setNombre("Receptor");
		usuarioDAO.registrarUsuario(receptor);
		Amistad amistad = new Amistad(emisor,receptor);
		amistad.setEstado(EstadoAmistad.ACEPTADA);
		this.amistadDAO.guardarAmistad(amistad);
		return amistad;
	}
	
	@Test
	void testBuscarAmistades() {
		UsuarioDAO usuarioDAO = new AdaptadorUsuarioDAO(emf);
		Usuario emisor = new Usuario();
		emisor.setNombre("Emisor");
		usuarioDAO.registrarUsuario(emisor);
		List<Amistad> amistades = List.of(inicializarAmistadAceptada(emisor, usuarioDAO ), inicializarAmistadAceptada(emisor, usuarioDAO), inicializarAmistadAceptada(emisor, usuarioDAO));
		List<Amistad> amistadesRecuperadas = this.amistadDAO.buscarAmistades(emisor);
		assertsAmistadesRecuperadas(amistades, amistadesRecuperadas, EstadoAmistad.ACEPTADA);

	}
	
	@Test
	void testExisteAmistad() {
		Amistad amistad = inicializarAmistadPendiente();
		this.amistadDAO.guardarAmistad(amistad);
		assertTrue(this.amistadDAO.existeAmistad(amistad.getUsuario1(), amistad.getUsuario2()));
	}
	
	@Test
	void testEliminarAmistad() {
		Amistad amistad = inicializarAmistadPendiente();
		this.amistadDAO.guardarAmistad(amistad);
		Long id = amistad.getId();
		this.amistadDAO.eliminarAmistad(amistad);
		assertNull(this.amistadDAO.buscarPorId(id));
	}
	
	
	
	
}
