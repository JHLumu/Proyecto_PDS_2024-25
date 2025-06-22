package umu.pds.modelo;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import umu.pds.persistencia.UsuarioDAO;

class CatalogoUsuariosTest {
    
    private CatalogoUsuarios catalogo;
    
    @Mock
    private UsuarioDAO usuarioDAO;
    
    private Usuario usuario1;
    private Usuario usuario2;
    
    @BeforeEach
    void setUp() throws MalformedURLException {
        usuarioDAO = mock(UsuarioDAO.class);
        
        usuario1 = new Usuario("Juan", "Pérez", "H", "juan@um.es", "pass123", "/foto1.jpg");
        usuario2 = new Usuario("Ana", "García", "M", "ana@um.es", "pass456", "/foto2.jpg");

        when(usuarioDAO.recuperarTodosUsuarios())
            .thenReturn(Arrays.asList(usuario1, usuario2));
            
        catalogo = new CatalogoUsuarios(usuarioDAO);
    }
    
    @Test
    void testGetInstancia() {
        CatalogoUsuarios instancia1 = CatalogoUsuarios.getInstancia();
        CatalogoUsuarios instancia2 = CatalogoUsuarios.getInstancia();
        
        assertNotNull(instancia1);
        assertSame(instancia1, instancia2);
    }

    @Test
    void testNuevoUsuario() {
        Usuario nuevoUsuario = new Usuario("Carlos", "López", "H", "carlos@um.es", "pass789", "/foto3.jpg");
        
        catalogo.nuevoUsuario(nuevoUsuario);
        
        // Assert
        Optional<Usuario> usuarioRecuperado = catalogo.buscarPorEmail("carlos@um.es");
        assertTrue(usuarioRecuperado.isPresent());
        assertEquals(nuevoUsuario, usuarioRecuperado.get());
    }

    @Test
    void testBuscarPorEmail() {
        catalogo.nuevoUsuario(usuario1);

        assertTrue(catalogo.buscarPorEmail("juan@um.es").isPresent());
        assertFalse(catalogo.buscarPorEmail("noexiste@um.es").isPresent());
    }

    @Test
    void testExisteEmail() {
        catalogo.nuevoUsuario(usuario1);

        assertTrue(catalogo.existeEmail("juan@um.es"));
        assertFalse(catalogo.existeEmail("noexiste@um.es"));
    }

    @Test
    void testActualizarUsuario() {
        catalogo.nuevoUsuario(usuario1);
        usuario1.setNombre("Juan Actualizado");
        
        catalogo.actualizarUsuario(usuario1);

        Optional<Usuario> usuarioActualizado = catalogo.buscarPorEmail("juan@um.es");
        assertTrue(usuarioActualizado.isPresent());
        assertEquals("Juan Actualizado", usuarioActualizado.get().getNombre());
    }

    @Test
    void testActualizarUsuarioNull() {
        catalogo.actualizarUsuario(null);
        assertTrue(true);
    }

    @Test
    void testActualizarUsuarioNoExistente() {
        Usuario usuarioNoExistente = new Usuario("No", "Existe", "H", "noexiste@um.es", "pass", "/foto.jpg");
        catalogo.actualizarUsuario(usuarioNoExistente);
        assertFalse(catalogo.buscarPorEmail("noexiste@um.es").isPresent());
    }

    @Test
    void testCargarCatalogo() throws MalformedURLException {
        List<Usuario> usuariosEsperados = Arrays.asList(usuario1, usuario2);
        when(usuarioDAO.recuperarTodosUsuarios()).thenReturn(usuariosEsperados);

        assertTrue(catalogo.existeEmail(usuario1.getEmail()));
        assertTrue(catalogo.existeEmail(usuario2.getEmail()));
        
        Usuario usuarioRecuperado1 = catalogo.buscarPorEmail(usuario1.getEmail()).get();
        Usuario usuarioRecuperado2 = catalogo.buscarPorEmail(usuario2.getEmail()).get();

        assertEquals(usuario1.getEmail(), usuarioRecuperado1.getEmail());
        assertEquals(usuario1.getNombre(), usuarioRecuperado1.getNombre());
        assertEquals(usuario1.getApellidos(), usuarioRecuperado1.getApellidos());
        
        assertEquals(usuario2.getEmail(), usuarioRecuperado2.getEmail());
        assertEquals(usuario2.getNombre(), usuarioRecuperado2.getNombre());
        assertEquals(usuario2.getApellidos(), usuarioRecuperado2.getApellidos());
    }
}