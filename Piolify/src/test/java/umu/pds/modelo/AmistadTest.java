package umu.pds.modelo;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AmistadTest {
	
	private Usuario usuario1; // solicitante
	private Usuario usuario2; // receptor
	private Amistad amistad;
	
	@BeforeEach
	void setUp() {
        usuario1 = new Usuario("Juan", "Pérez", "H", "juan@test.com", "pass123", "foto.jpg");
        usuario2 = new Usuario("Ana", "García", "M", "ana@test.com", "pass456", "foto2.jpg");
        amistad = new Amistad(usuario1, usuario2);
	}
	
	@Test
	void testSetters() {
	    Long id = 1L;
	    Date fecha = new Date();
	    Usuario nuevoSolicitante = new Usuario("Carlos", "López", "H", "carlos@test.com", "pass789", "foto3.jpg");
	    Usuario nuevoReceptor = new Usuario("Maria", "Ruiz", "M", "maria@test.com", "pass101", "foto4.jpg");
	    
	    amistad.setId(id);
	    amistad.setEstado(EstadoAmistad.ACEPTADA);
	    amistad.setFecha(fecha);
	    amistad.setUsuario1(nuevoSolicitante);
	    amistad.setUsuario2(nuevoReceptor);

	    assertEquals(id, amistad.getId());
	    assertEquals(EstadoAmistad.ACEPTADA, amistad.getEstado());
	    assertEquals(fecha, amistad.getFecha());
	    assertEquals(nuevoSolicitante, amistad.getUsuario1());
	    assertEquals(nuevoReceptor, amistad.getUsuario2());
	}

    @Test
    void testGetOtroUsuario() {

        assertEquals(usuario2, amistad.getOtroUsuario(usuario1));
        assertEquals(usuario1, amistad.getOtroUsuario(usuario2));

        Usuario usuario3 = new Usuario("Pedro", "López", "H", "pedro@test.com", "pass789", "foto3.jpg");
        assertNull(amistad.getOtroUsuario(usuario3));
    }
    
    @Test
    void testEsSolicitante() {
        assertTrue(amistad.esSolicitante(usuario1));
        assertFalse(amistad.esSolicitante(usuario2));
        
        Usuario usuario3 = new Usuario("Pedro", "López", "H", "pedro@test.com", "pass789", "foto3.jpg");
        assertFalse(amistad.esSolicitante(usuario3));
    }
    
    @Test
    void testEsReceptor() {
        assertFalse(amistad.esReceptor(usuario1));
        assertTrue(amistad.esReceptor(usuario2));
        
        Usuario usuario3 = new Usuario("Pedro", "López", "H", "pedro@test.com", "pass789", "foto3.jpg");
        assertFalse(amistad.esReceptor(usuario3));
    }
    
    @Test
    void testEquals() {
        Amistad amistad2 = new Amistad(usuario1, usuario2);
        amistad2.setId(amistad.getId());
        amistad2.setFecha(amistad.getFecha());
        
        assertTrue(amistad.equals(amistad));
        assertTrue(amistad.equals(amistad2));
        
        Amistad amistadDiferente = new Amistad(usuario2, usuario1);
        assertFalse(amistad.equals(amistadDiferente));
        assertFalse(amistad.equals(null));
    }
    
    @Test
    void testHashCode() {
        Amistad amistad2 = new Amistad(usuario1, usuario2);
        amistad2.setId(amistad.getId());
        amistad2.setFecha(amistad.getFecha());
        
        assertEquals(amistad.hashCode(), amistad2.hashCode());
    }
}
