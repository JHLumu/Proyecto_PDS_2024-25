package umu.pds.controlador;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import umu.pds.modelo.Usuario;
import umu.pds.servicios.UsuarioService;
import umu.pds.utils.RegistroUsuarioDTO;
import umu.pds.utils.UsuarioValidador;

class UsuarioControllerTest {
    
    @Mock 
    private Piolify piolifyMock;
   
    @Mock
    private UsuarioService usuarioServiceMock;
    
    @Mock 
    private UsuarioValidador usuarioValidadorMock;
    
    @Mock 
    private Usuario usuarioMock;
    
    private UsuarioController controller;
    
    private static final String EMAIL_TEST = "test@um.com";
    private static final String PASSWORD_TEST = "pass";
    private static final String EMAIL_AMIGO = "amigo@um.com";
    
    @BeforeEach
    void setUp() {
		piolifyMock = mock(Piolify.class);
		usuarioServiceMock = mock(UsuarioService.class);
    	usuarioValidadorMock = mock(UsuarioValidador.class);
    	usuarioMock = mock(Usuario.class);
    	                                
        controller = new UsuarioController(piolifyMock, usuarioServiceMock, usuarioValidadorMock);
    }
    
    // para crear DTO de registro
    private RegistroUsuarioDTO crearRegistroDTO() {
        return new RegistroUsuarioDTO.Builder()
                .nombre("Test")
                .apellidos("User")
                .genero("H")
                .email(EMAIL_TEST)
                .password(PASSWORD_TEST)
                .confirmar(PASSWORD_TEST)
                .rutaImagenPerfil("/foto.jpg")
                .build();
    }
    
    // para crear usuario
    private Usuario crearUsuario() {
        return new Usuario("Test", "User", "H", EMAIL_TEST, PASSWORD_TEST, "/foto.jpg");
    }
    
    @Test
    void testIniciarSesionExitoso() {
        when(usuarioServiceMock.iniciarSesion(EMAIL_TEST, PASSWORD_TEST)).thenReturn(usuarioMock);
        
        boolean resultado = controller.iniciarSesion(EMAIL_TEST, PASSWORD_TEST);
        
        assertTrue(resultado);
        verify(piolifyMock).setUsuarioActual(usuarioMock);
        verify(piolifyMock).loginExitoso();
        verify(usuarioValidadorMock).validarLogin(EMAIL_TEST, PASSWORD_TEST);
    }
    
    @Test
    void testIniciarSesionFallido() {
        when(usuarioServiceMock.iniciarSesion(EMAIL_TEST, PASSWORD_TEST)).thenReturn(null);
        
        boolean resultado = controller.iniciarSesion(EMAIL_TEST, PASSWORD_TEST);
        
        assertFalse(resultado);
        verify(piolifyMock, never()).setUsuarioActual(any());
        verify(piolifyMock, never()).loginExitoso();
    }
    
    @Test
    void testRegistrarUsuarioExitoso() {
        RegistroUsuarioDTO dto = crearRegistroDTO();
        when(usuarioServiceMock.registrarUsuario(dto)).thenReturn(true);
        
        boolean resultado = controller.registrarUsuario(dto);
        
        assertTrue(resultado);
        verify(piolifyMock).registroExitoso();
        verify(usuarioValidadorMock).validarRegistro(dto);
    }
    
    @Test
    void testRegistrarUsuarioEmailExistente() {
        RegistroUsuarioDTO dto = crearRegistroDTO();
        when(usuarioServiceMock.registrarUsuario(dto)).thenReturn(false);
        
        assertThrows(RuntimeException.class, () -> controller.registrarUsuario(dto));
        verify(piolifyMock, never()).registroExitoso();
    }
    
    @Test
    void testModificarUsuarioExitoso() {
        Usuario usuario = crearUsuario();
        when(usuarioServiceMock.modificarUsuario(usuario)).thenReturn(true);
        when(piolifyMock.getUsuarioActual()).thenReturn(usuario);
        
        assertDoesNotThrow(() -> controller.modificarUsuario(usuario));
        
        verify(piolifyMock).setUsuarioActual(usuario);
        verify(piolifyMock).notificarCambiosUsuario();
    }
    
    @Test
    void testModificarUsuarioNulo() {
        assertThrows(IllegalArgumentException.class, () -> controller.modificarUsuario(null));
        verify(piolifyMock, never()).setUsuarioActual(any());
        verify(piolifyMock, never()).notificarCambiosUsuario();
    }
    
    @Test
    void testActualizarUsuario() {
        String nuevoNombre = "Nuevo Nombre";
        String nuevoApellido = "Nuevo Apellido";
        String nuevoGenero = "H";
        String nuevaRutaImagen = "/nueva/ruta.jpg";
        String nuevaPassword = "nuevaPass123";

        controller.actualizarUsuario(usuarioMock, nuevoNombre, nuevoApellido, nuevoGenero, 
            nuevaRutaImagen, nuevaPassword, true, true);

        verify(usuarioMock).setNombre(nuevoNombre);
        verify(usuarioMock).setApellidos(nuevoApellido);
        verify(usuarioMock).setGenero(nuevoGenero);
        verify(usuarioMock).setImagenPerfil(nuevaRutaImagen);
        verify(usuarioMock).setPassword(nuevaPassword);
    }

    @Test
    void testBuscarUsuarioPorEmail() {
        when(usuarioServiceMock.obtenerUsuarioPorEmail(EMAIL_TEST)).thenReturn(usuarioMock);

        Usuario resultado = controller.buscarUsuarioPorEmail(EMAIL_TEST);

        assertEquals(usuarioMock, resultado);
        verify(usuarioServiceMock).obtenerUsuarioPorEmail(EMAIL_TEST);
    }
    
    @Test
    void testEnviarSolicitudAmistadExitoso() {
        when(piolifyMock.getUsuarioActual()).thenReturn(usuarioMock);
        when(usuarioServiceMock.obtenerUsuarioPorEmail(EMAIL_AMIGO)).thenReturn(usuarioMock);
        when(usuarioServiceMock.enviarSolicitudAmistad(usuarioMock, usuarioMock)).thenReturn(true);
        
        boolean resultado = controller.enviarSolicitudAmistad(EMAIL_AMIGO);
        
        assertTrue(resultado);
        verify(usuarioServiceMock).enviarSolicitudAmistad(usuarioMock, usuarioMock);
    }
    
    @Test
    void testEnviarSolicitudAmistadDestinatarioNull() {
        when(piolifyMock.getUsuarioActual()).thenReturn(usuarioMock);
        when(usuarioServiceMock.obtenerUsuarioPorEmail(EMAIL_AMIGO)).thenReturn(null);
        
        assertThrows(RuntimeException.class, () -> controller.enviarSolicitudAmistad(EMAIL_AMIGO));
        verify(usuarioServiceMock, never()).enviarSolicitudAmistad(any(), any());
    }
    
    @Test
    void testEnviarSolicitudAmistadSolicitudExistente() {
        when(piolifyMock.getUsuarioActual()).thenReturn(usuarioMock);
        when(usuarioServiceMock.obtenerUsuarioPorEmail(EMAIL_AMIGO)).thenReturn(usuarioMock);
        when(usuarioServiceMock.enviarSolicitudAmistad(usuarioMock, usuarioMock)).thenReturn(false);
        
        Exception exception = assertThrows(RuntimeException.class, 
            () -> controller.enviarSolicitudAmistad(EMAIL_AMIGO));
        assertEquals("No se pudo enviar la solicitud. Puede que ya exista una relación.", 
            exception.getMessage());
        
        verify(usuarioServiceMock).enviarSolicitudAmistad(usuarioMock, usuarioMock);
    }
    
    @Test
    void testProcesarSolicitudAmistad() {
        Long solicitudId = 1L;
        when(piolifyMock.getUsuarioActual()).thenReturn(usuarioMock);
        when(usuarioServiceMock.procesarSolicitudAmistad(solicitudId, usuarioMock, true))
            .thenReturn(true);
        
        boolean resultado = controller.procesarSolicitudAmistad(solicitudId, true);
        
        assertTrue(resultado);
        verify(usuarioServiceMock).procesarSolicitudAmistad(solicitudId, usuarioMock, true);
    }
    

    @Test
    void testActualizarUsuarioSinCambiarImagenNiPassword() {
        String nuevoNombre = "Nuevo Nombre";
        String nuevoApellido = "Nuevo Apellido";
        String nuevoGenero = "M";
        String nuevaRutaImagen = "/nueva/ruta.jpg";
        String nuevaPassword = "nuevaPass123";

        controller.actualizarUsuario(usuarioMock, nuevoNombre, nuevoApellido, nuevoGenero, 
            nuevaRutaImagen, nuevaPassword, false, false);

        verify(usuarioMock).setNombre(nuevoNombre);
        verify(usuarioMock).setApellidos(nuevoApellido);
        verify(usuarioMock).setGenero(nuevoGenero);
        verify(usuarioMock, never()).setImagenPerfil(anyString());
        verify(usuarioMock, never()).setPassword(anyString());
    }
    


    
    
    
}