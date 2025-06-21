package umu.pds.controlador;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import umu.pds.modelo.Estadisticas;
import umu.pds.modelo.TipoLogro;
import umu.pds.modelo.Usuario;
import umu.pds.utils.RegistroUsuarioDTO;

/**
 * * Clase de pruebas de integración para el controlador de usuarios. Realiza
 * pruebas completas del flujo de registro, modificación y validación de
 * usuarios.
 */
class UsuarioControllerIntegrationTest {

    private UsuarioController usuarioController;
    private Piolify piolify;
    private File dbFile;

    @BeforeEach
    void setUp() throws Exception {
        dbFile = new File("testbasedatos.db");
        if (dbFile.exists()) {
            dbFile.delete();
        }
        
        piolify = new Piolify();
        usuarioController = piolify.getUsuarioController();
    }

    @AfterEach
    void cleanUp() throws Exception {
        if (dbFile.exists()) {
            dbFile.delete();
        }
    }

    @Test
    void testIntegracionRegistroCompleto() {
        String email = "lucia.rodriguez" + System.currentTimeMillis() + "@um.es";
        RegistroUsuarioDTO dto = new RegistroUsuarioDTO.Builder()
            .nombre("Lucía")
            .apellidos("Rodríguez Martín")
            .genero("Mujer")
            .email(email)
            .password("LuciaPassword123!")
            .confirmar("LuciaPassword123!")
            .rutaImagenPerfil("/fotoUser.png")
            .build();

        boolean registroExitoso = usuarioController.registrarUsuario(dto);
        assertTrue(registroExitoso);

        Usuario usuarioRegistrado = usuarioController.buscarUsuarioPorEmail(email);
        assertNotNull(usuarioRegistrado);
        assertEquals("Lucía", usuarioRegistrado.getNombre());
        assertEquals("Rodríguez Martín", usuarioRegistrado.getApellidos());
        assertEquals("Mujer", usuarioRegistrado.getGenero());
        assertEquals(email, usuarioRegistrado.getEmail());
        assertEquals("LuciaPassword123!", usuarioRegistrado.getPassword());
        assertEquals("/fotoUser.png", usuarioRegistrado.getImagenPerfil());
    }

    @Test
    void testIntegracionValidacionCredenciales() {
        String email = "alberto.sanchez" + System.currentTimeMillis() + "@um.es";
        RegistroUsuarioDTO dto = new RegistroUsuarioDTO.Builder()
            .nombre("Alberto")
            .apellidos("Sánchez Pérez")
            .genero("Hombre")
            .email(email)
            .password("AlbertoSecure456#")
            .confirmar("AlbertoSecure456#")
            .rutaImagenPerfil("/fotoUser.png")
            .build();

        usuarioController.registrarUsuario(dto);

        Usuario usuario = usuarioController.buscarUsuarioPorEmail(email);
        assertNotNull(usuario);
        assertEquals("AlbertoSecure456#", usuario.getPassword());

        assertNotEquals("passwordIncorrecta", usuario.getPassword());
    }

    @Test
    void testIntegracionModificarUsuarioCompleto() {
        String email = "carmen.jimenez" + System.currentTimeMillis() + "@um.es";
        RegistroUsuarioDTO dto = new RegistroUsuarioDTO.Builder()
            .nombre("Carmen")
            .apellidos("Jiménez López")
            .genero("Mujer")
            .email(email)
            .password("CarmenOriginal789$")
            .confirmar("CarmenOriginal789$")
            .rutaImagenPerfil("/fotoUser.png")
            .build();

        usuarioController.registrarUsuario(dto);
        Usuario usuario = usuarioController.buscarUsuarioPorEmail(email);
        piolify.setUsuarioActual(usuario);

        usuarioController.actualizarUsuario(usuario, 
            "Carmen Isabel", 
            "Jiménez Fernández", 
            "Mujer", 
            "/fotoUserupdated.png", 
            "CarmenNueva123@", 
            true,
            true
        );

        usuarioController.modificarUsuario(usuario);

        Usuario usuarioActualizado = usuarioController.buscarUsuarioPorEmail(email);
        assertEquals("Carmen Isabel", usuarioActualizado.getNombre());
        assertEquals("Jiménez Fernández", usuarioActualizado.getApellidos());
        assertEquals("/fotoUserupdated.png", usuarioActualizado.getImagenPerfil());
        assertEquals("CarmenNueva123@", usuarioActualizado.getPassword());

        assertEquals(usuario.getId(), piolify.getUsuarioActual().getId());
    }

    @Test
    void testIntegracionVerificarLogros() {
        String email = "diego.morales" + System.currentTimeMillis() + "@um.es";
        RegistroUsuarioDTO dto = new RegistroUsuarioDTO.Builder()
            .nombre("Diego")
            .apellidos("Morales García")
            .genero("Hombre")
            .email(email)
            .password("DiegoLogros456!")
            .confirmar("DiegoLogros456!")
            .rutaImagenPerfil("/fotoUser.png")
            .build();

        usuarioController.registrarUsuario(dto);
        Usuario usuario = usuarioController.buscarUsuarioPorEmail(email);
        piolify.setUsuarioActual(usuario);

        assertFalse(usuario.tieneLogroDesbloqueado(TipoLogro.PRIMER_EJERCICIO));

        Estadisticas stats = usuario.getEstadisticas();
        if (stats == null) {
            stats = new Estadisticas();
            stats.setUsuario(usuario);
            usuario.setEstadisticas(stats);
        }
        
        stats.setTotalEjerciciosCompletados(1);
        
        usuarioController.verificarYDesbloquearLogros(usuario);
        usuarioController.modificarUsuario(usuario);

        Usuario usuarioActualizado = usuarioController.buscarUsuarioPorEmail(email);
        assertTrue(usuarioActualizado.tieneLogroDesbloqueado(TipoLogro.PRIMER_EJERCICIO));

        assertNotNull(usuarioActualizado.getLogros());
        assertTrue(usuarioActualizado.getLogros().size() > 0);
        assertNotNull(usuarioActualizado.getLogros().get(0).getFecha());
    }

    @Test
    void testIntegracionErrores() {
        String emailDuplicado = "elena.ruiz" + System.currentTimeMillis() + "@um.es";
        
        RegistroUsuarioDTO dto1 = new RegistroUsuarioDTO.Builder()
            .nombre("Elena").apellidos("Ruiz Martínez").genero("Mujer")
            .email(emailDuplicado).password("ElenaPass123!").confirmar("ElenaPass123!")
            .rutaImagenPerfil("/fotoUser.png").build();

        RegistroUsuarioDTO dto2 = new RegistroUsuarioDTO.Builder()
            .nombre("Elena").apellidos("Ruiz García").genero("Mujer")
            .email(emailDuplicado).password("OtraElena456#").confirmar("OtraElena456#")
            .rutaImagenPerfil("/fotoUser.png").build();

        assertTrue(usuarioController.registrarUsuario(dto1));

        assertThrows(RuntimeException.class, () -> {
            usuarioController.registrarUsuario(dto2);
        });

        assertThrows(RuntimeException.class, () -> {
            usuarioController.buscarUsuarioPorEmail("usuario.inexistente@um.es");
        });

        Usuario usuario = usuarioController.buscarUsuarioPorEmail(emailDuplicado);
        piolify.setUsuarioActual(usuario);
        
        assertThrows(RuntimeException.class, () -> {
            usuarioController.enviarSolicitudAmistad("amigo.inexistente@um.es");
        });
    }
}