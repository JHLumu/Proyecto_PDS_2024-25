package umu.pds.modelo;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UsuarioTest {

    private Usuario usuario;
    private Usuario amigo1;
    private Usuario amigo2;
    
    @BeforeEach
    void setUp() {
        usuario = new Usuario("Juan", "Pérez", "Hombre", "juan@test.com", "password123", "/foto.jpg");
        amigo1 = new Usuario("Ana", "García", "Mujer", "ana@test.com", "pass", "/ana.jpg");
        amigo2 = new Usuario("Luis", "Martín", "Hombre", "luis@test.com", "pass", "/luis.jpg");
    }
    
    
    @Test
    void testSettersYGetters() {
        Usuario usuario = new Usuario();
        Long id = 1L;
        List<Logro> logros = new ArrayList<>();
        Set<Amistad> amistades = new HashSet<>();
        List<Curso> biblioteca = new ArrayList<>();
        List<String> cursosCreados = new ArrayList<>();
        Estadisticas estadisticas = new Estadisticas();

        usuario.setId(id);
        usuario.setNombre("Carlos");
        usuario.setApellidos("López");
        usuario.setGenero("Hombre");
        usuario.setEmail("carlos@test.com");
        usuario.setPassword("password456");
        usuario.setImagenPerfil("/carlos.jpg");
        usuario.setLogros(logros);
        usuario.setAmistadesEnviadas(amistades);
        usuario.setAmistadesRecibidas(amistades);
        usuario.setBiblioteca(biblioteca);
        usuario.setEstadisticas(estadisticas);

        assertEquals(id, usuario.getId());
        assertEquals("Carlos", usuario.getNombre());
        assertEquals("López", usuario.getApellidos());
        assertEquals("Hombre", usuario.getGenero());
        assertEquals("carlos@test.com", usuario.getEmail());
        assertEquals("password456", usuario.getPassword());
        assertEquals("/carlos.jpg", usuario.getImagenPerfil());
        assertEquals(logros, usuario.getLogros());
        assertEquals(amistades, usuario.getAmistadesEnviadas());
        assertEquals(amistades, usuario.getAmistadesRecibidas());
        assertEquals(biblioteca, usuario.getBiblioteca());
        assertEquals(estadisticas, usuario.getEstadisticas());
    }
    
    
    @Test
    void testObtenerAmigos() {
        Usuario amigo1 = new Usuario("Ana", "García", "Mujer", "ana@test.com", "pass", "/ana.jpg");
        Usuario amigo2 = new Usuario("Luis", "Martín", "Hombre", "luis@test.com", "pass", "/luis.jpg");
      
        Amistad amistad1 = new Amistad(usuario, amigo1);
        amistad1.setEstado(EstadoAmistad.ACEPTADA);
        
        Amistad amistad2 = new Amistad(amigo2, usuario);
        amistad2.setEstado(EstadoAmistad.ACEPTADA);
        
        usuario.getAmistadesEnviadas().add(amistad1);
        usuario.getAmistadesRecibidas().add(amistad2);
        
        List<Usuario> amigos = usuario.getAmigos();
        
        assertEquals(2, amigos.size());
        assertTrue(amigos.contains(amigo1));
        assertTrue(amigos.contains(amigo2));
        assertFalse(amigos.contains(usuario)); // el usuario no puede estar en su lista de amigos
    }
    
    @Test
    void testGetAmigosConDiferentesEstados() {
        // amistades con diferentes estados
        Amistad amistadAceptada1 = new Amistad(usuario, amigo1);
        amistadAceptada1.setEstado(EstadoAmistad.ACEPTADA);
        
        Amistad amistadAceptada2 = new Amistad(amigo2, usuario);
        amistadAceptada2.setEstado(EstadoAmistad.ACEPTADA);
        
        Amistad amistadPendiente = new Amistad(usuario, new Usuario("Pendiente", "Test", "H", "pendiente@test.com", "pass", "/p.jpg"));
        amistadPendiente.setEstado(EstadoAmistad.PENDIENTE);
        
        Amistad amistadRechazada = new Amistad(new Usuario("Rechazado", "Test", "H", "rechazado@test.com", "pass", "/r.jpg"), usuario);
        amistadRechazada.setEstado(EstadoAmistad.RECHAZADA);
        
        usuario.getAmistadesEnviadas().add(amistadAceptada1);
        usuario.getAmistadesEnviadas().add(amistadPendiente);
        usuario.getAmistadesRecibidas().add(amistadAceptada2);
        usuario.getAmistadesRecibidas().add(amistadRechazada);
        
        List<Usuario> amigos = usuario.getAmigos();
        
        assertEquals(2, amigos.size());
        assertTrue(amigos.contains(amigo1));
        assertTrue(amigos.contains(amigo2));
        
        Set<Amistad> amistadesEnviadas = usuario.getAmistadesEnviadas();
        Set<Amistad> amistadesRecibidas = usuario.getAmistadesRecibidas();
        
        // solo se incluyen amistades ACEPTADAS
        assertTrue(amistadesEnviadas.stream().anyMatch(a -> a.getEstado() == EstadoAmistad.ACEPTADA));
        assertTrue(amistadesRecibidas.stream().anyMatch(a -> a.getEstado() == EstadoAmistad.ACEPTADA));

    }
    
    @Test
    void testSolicitudesEnviadasYPendientes() {
        Usuario solicitante = new Usuario("Pedro", "López", "Hombre", "pedro@test.com", "pass", "/pedro.jpg");
        
        Amistad solicitudPendiente = new Amistad(solicitante, usuario);
        solicitudPendiente.setEstado(EstadoAmistad.PENDIENTE);
        
        Amistad solicitudAceptada = new Amistad(solicitante, usuario);
        solicitudAceptada.setEstado(EstadoAmistad.ACEPTADA);
        
		usuario.getAmistadesEnviadas().add(solicitudPendiente);
		usuario.getAmistadesEnviadas().add(solicitudAceptada);
		
        usuario.getAmistadesRecibidas().add(solicitudPendiente);
        usuario.getAmistadesRecibidas().add(solicitudAceptada);
       
        
        List<Amistad> enviadas = usuario.getSolicitudesEnviadas();
        List<Amistad> pendientes = usuario.getSolicitudesPendientes();
        
        assertEquals(1, pendientes.size());
        assertEquals(1, enviadas.size());
        assertEquals(solicitudPendiente, pendientes.get(0));
        assertEquals(EstadoAmistad.PENDIENTE, pendientes.get(0).getEstado());
    }
    

}
