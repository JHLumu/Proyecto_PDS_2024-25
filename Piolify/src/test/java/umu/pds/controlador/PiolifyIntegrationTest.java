package umu.pds.controlador;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import umu.pds.modelo.Usuario;
import umu.pds.utils.RegistroUsuarioDTO;


/* * Clase de pruebas de integración para el sistema Piolify.
 * Realiza pruebas completas del flujo de registro, login, patrón observer,
 * persistencia de datos y manejo de errores.
 */
class PiolifyIntegrationTest {

    private Piolify piolify;
    private UsuarioController usuarioController;
    private ImportacionController importacionController;
    private File dbFile;
    
    @BeforeEach
    void setUp() throws Exception {
        dbFile = new File("testbasedatos.db");
        if (dbFile.exists()) {
            dbFile.delete();
        }
        
        piolify = new Piolify();
        usuarioController = piolify.getUsuarioController();
        importacionController = piolify.getImportacionController();
    }

    @AfterEach
    void cleanUp() throws Exception {
        if (dbFile.exists()) {
            dbFile.delete();
        }
    }

    @Test
    void testIntegracionFlujoRegistroYLogin() {
        String email = "maria.garcia" + System.currentTimeMillis() + "@um.es";
        
        RegistroUsuarioDTO dto = new RegistroUsuarioDTO.Builder()
            .nombre("María")
            .apellidos("García López")
            .genero("Mujer")
            .email(email)
            .password("MiPassword123!")
            .confirmar("MiPassword123!")
            .rutaImagenPerfil("/fotoUser.png")
            .build();

        boolean registroExitoso = usuarioController.registrarUsuario(dto);
        assertTrue(registroExitoso);

        assertThrows(RuntimeException.class, () -> piolify.getUsuarioActual());

        Usuario usuarioRegistrado = usuarioController.buscarUsuarioPorEmail(email);
        assertNotNull(usuarioRegistrado);
        assertEquals("María", usuarioRegistrado.getNombre());
        assertEquals("García López", usuarioRegistrado.getApellidos());
        assertEquals(email, usuarioRegistrado.getEmail());

        Usuario usuarioParaLogin = usuarioController.buscarUsuarioPorEmail(email);
        boolean credencialesCorrectas = usuarioParaLogin != null && 
            usuarioParaLogin.getPassword().equals("MiPassword123!");
        assertTrue(credencialesCorrectas);

        piolify.setUsuarioActual(usuarioParaLogin);
        
        Usuario usuarioActual = piolify.getUsuarioActual();
        assertNotNull(usuarioActual);
        assertEquals(email, usuarioActual.getEmail());
        assertEquals("María", usuarioActual.getNombre());
        assertEquals("García López", usuarioActual.getApellidos());
    }

    @Test
    void testIntegracionPatronObserver() {
        final int[] contadorNotificaciones = {0};
        
        Runnable observador = () -> contadorNotificaciones[0]++;
        
        piolify.añadirObservador(observador);
        
        String email = "carlos.martinez" + System.currentTimeMillis() + "@um.es";
        RegistroUsuarioDTO dto = new RegistroUsuarioDTO.Builder()
            .nombre("Carlos")
            .apellidos("Martínez Ruiz")
            .genero("Hombre")
            .email(email)
            .password("CarlosPass456#")
            .confirmar("CarlosPass456#")
            .rutaImagenPerfil("/fotoUser.png")
            .build();

        usuarioController.registrarUsuario(dto);
        
        Usuario usuario = usuarioController.buscarUsuarioPorEmail(email);
        piolify.setUsuarioActual(usuario);
        
        usuario.setNombre("Carlos Alberto");
        usuarioController.modificarUsuario(usuario);
        
        assertTrue(contadorNotificaciones[0] > 0);
        
        int notificacionesAntes = contadorNotificaciones[0];
        piolify.borrarObservador(observador);
        
        usuario.setApellidos("Martínez Fernández");
        usuarioController.modificarUsuario(usuario);
        
        assertEquals(notificacionesAntes, contadorNotificaciones[0]);
    }

    @Test
    void testIntegracionPersistenciaMultiplesSesiones() {
        String email = "ana.lopez" + System.currentTimeMillis() + "@um.es";
        
        RegistroUsuarioDTO dto = new RegistroUsuarioDTO.Builder()
            .nombre("Ana")
            .apellidos("López Sánchez")
            .genero("Mujer")
            .email(email)
            .password("AnaSecure789$")
            .confirmar("AnaSecure789$")
            .rutaImagenPerfil("/fotoUser.png")
            .build();

        usuarioController.registrarUsuario(dto);
        
        Usuario usuario = usuarioController.buscarUsuarioPorEmail(email);
        piolify.setUsuarioActual(usuario);
        
        usuarioController.actualizarUsuario(usuario, "Ana María", "López Jiménez", 
            "Mujer", "/fotoUsernew.png", "NewPassword321@", true, true);
        usuarioController.modificarUsuario(usuario);

        Piolify nuevoPiolify = new Piolify();
        UsuarioController nuevoUsuarioController = nuevoPiolify.getUsuarioController();
        
        Usuario usuarioRecuperado = nuevoUsuarioController.buscarUsuarioPorEmail(email);
        
        assertNotNull(usuarioRecuperado);
        assertEquals("Ana María", usuarioRecuperado.getNombre());
        assertEquals("López Jiménez", usuarioRecuperado.getApellidos());
        assertEquals("/fotoUsernew.png", usuarioRecuperado.getImagenPerfil());
        assertEquals("NewPassword321@", usuarioRecuperado.getPassword());
    }

    @Test
    void testIntegracionManejoErrores() {
        boolean loginSinRegistro = usuarioController.iniciarSesion("usuario.inexistente@um.es", "password");
        assertFalse(loginSinRegistro);
        
        assertThrows(RuntimeException.class, () -> piolify.getUsuarioActual());
        
        boolean importacionSinUsuario = importacionController.importarCursosDesdeArchivo(
            "cursos_informatica.json", null);
        assertFalse(importacionSinUsuario);
        
        String email = "pedro.gonzalez" + System.currentTimeMillis() + "@um.es";
        RegistroUsuarioDTO dto = new RegistroUsuarioDTO.Builder()
            .nombre("Pedro")
            .apellidos("González Martín")
            .genero("Hombre")
            .email(email)
            .password("PedroPass654!")
            .confirmar("PedroPass654!")
            .rutaImagenPerfil("/fotoUser.png")
            .build();

        usuarioController.registrarUsuario(dto);
        
        Usuario usuarioRegistrado = usuarioController.buscarUsuarioPorEmail(email);
        assertNotNull(usuarioRegistrado);
        
        piolify.setUsuarioActual(usuarioRegistrado);
        
        boolean importacionArchivoInexistente = importacionController.importarCursosDesdeArchivo(
            "archivo_inexistente.json", usuarioRegistrado);
        assertFalse(importacionArchivoInexistente);
        
        assertEquals(0, usuarioRegistrado.getBiblioteca().size());
    }
}