package umu.pds.servicios;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import umu.pds.modelo.Amistad;
import umu.pds.modelo.CatalogoUsuarios;
import umu.pds.modelo.Curso;
import umu.pds.modelo.Estadisticas;
import umu.pds.modelo.EstadoAmistad;
import umu.pds.modelo.TipoLogro;
import umu.pds.modelo.Usuario;
import umu.pds.persistencia.AmistadDAO;
import umu.pds.persistencia.UsuarioDAO;
import umu.pds.utils.RegistroUsuarioDTO;

class UsuarioServiceTest {
    
    private CatalogoUsuarios mockCatalogo;
    private UsuarioDAO mockUsuarioDAO;
    private AmistadDAO mockAmistadDAO;
    
    private UsuarioService usuarioService;
    private Usuario usuario;
    private Usuario receptor;
    
    @BeforeEach
    void setUp() {
        mockCatalogo = mock(CatalogoUsuarios.class);
        mockUsuarioDAO = mock(UsuarioDAO.class);
        mockAmistadDAO = mock(AmistadDAO.class);
        
        usuario = new Usuario("Juan", "Pérez", "H", "juan@um.es", "miPassword123", "/foto1.jpg");
        usuario.setId(1L);
        
        receptor = new Usuario("Ana", "García", "M", "ana@um.es", "claveSecreta456", "/foto2.jpg"); 
        receptor.setId(2L);
        
        usuario.setLogros(new ArrayList<>());
        usuario.setBiblioteca(new ArrayList<>());
        usuario.setAmistadesEnviadas(new HashSet<>());
        usuario.setAmistadesRecibidas(new HashSet<>());
        
        when(mockCatalogo.buscarPorEmail(usuario.getEmail())).thenReturn(Optional.of(usuario));
        when(mockCatalogo.buscarPorEmail(receptor.getEmail())).thenReturn(Optional.of(receptor));
        
        usuarioService = new UsuarioService(mockCatalogo, mockUsuarioDAO, mockAmistadDAO);
    }
    
    @Test
    void testRegistrarUsuarioExitoso() {
        RegistroUsuarioDTO dto = new RegistroUsuarioDTO.Builder()
            .nombre("Ana")
            .apellidos("García")
            .genero("Mujer")
            .email("ana@um.es")
            .password("contraseñaSegura")
            .confirmar("contraseñaSegura")
            .rutaImagenPerfil("/ana.jpg")
            .build();
        
        when(mockCatalogo.existeEmail("ana@um.es")).thenReturn(false);
        
        boolean resultado = usuarioService.registrarUsuario(dto);
        
        assertTrue(resultado);
        verify(mockUsuarioDAO).registrarUsuario(any(Usuario.class));
        verify(mockCatalogo).nuevoUsuario(any(Usuario.class));
    }
    
    @Test
    void testRegistrarUsuarioEmailExistente() {
        RegistroUsuarioDTO dto = new RegistroUsuarioDTO.Builder()
            .nombre("Juan")
            .apellidos("Pérez")
            .genero("Hombre")
            .email("juan@um.es")
            .password("miPassword123")
            .confirmar("miPassword123")
            .rutaImagenPerfil("/juan.jpg")
            .build();
        
        when(mockCatalogo.existeEmail("juan@um.es")).thenReturn(true);
        
        boolean resultado = usuarioService.registrarUsuario(dto);
        
        assertFalse(resultado);
        verify(mockUsuarioDAO, never()).registrarUsuario(any(Usuario.class));
        verify(mockCatalogo, never()).nuevoUsuario(any(Usuario.class));
    }
    
    @Test
    void testIniciarSesionExitoso() {
        String email = "juan@um.es";
        String password = "miPassword123";
        when(mockCatalogo.buscarPorEmail(email)).thenReturn(Optional.of(usuario));
        
        Usuario resultado = usuarioService.iniciarSesion(email, password);
        
        assertNotNull(resultado);
        assertEquals(usuario, resultado);
        verify(mockCatalogo).buscarPorEmail(email);
    }
    
    @Test
    void testIniciarSesionPasswordIncorrecta() {
        String email = "juan@um.es";
        String passwordIncorrecta = "passwordIncorrecta";
        
        when(mockCatalogo.buscarPorEmail(email)).thenReturn(Optional.of(usuario));
        
        Usuario resultado = usuarioService.iniciarSesion(email, passwordIncorrecta);
        
        assertNull(resultado);
        verify(mockCatalogo).buscarPorEmail(email);
    }
    
    @Test
    void testIniciarSesionUsuarioNoEncontrado() {
        String email = "noexiste@um.es";
        String password = "cualquierClave";
        
        when(mockCatalogo.buscarPorEmail(email)).thenReturn(Optional.empty());
        
        Usuario resultado = usuarioService.iniciarSesion(email, password);
        
        assertNull(resultado);
        verify(mockCatalogo).buscarPorEmail(email);
    }
    
    @Test
    void testModificarUsuarioExitoso() {
        when(mockCatalogo.buscarPorEmail(usuario.getEmail())).thenReturn(Optional.of(usuario));
        
        boolean resultado = usuarioService.modificarUsuario(usuario);
        
        assertTrue(resultado);
        verify(mockUsuarioDAO).modificarUsuario(usuario);
        verify(mockCatalogo).actualizarUsuario(usuario);
    }
    
    @Test
    void testModificarUsuarioNull() {
        boolean resultado = usuarioService.modificarUsuario(null);
        
        assertFalse(resultado);
        verify(mockUsuarioDAO, never()).modificarUsuario(any());
        verify(mockCatalogo, never()).actualizarUsuario(any());
    }
    
    @Test
    void testModificarUsuarioEmailNull() {
        Usuario usuarioSinEmail = new Usuario();
        usuarioSinEmail.setEmail(null);
        
        boolean resultado = usuarioService.modificarUsuario(usuarioSinEmail);
        
        assertFalse(resultado);
        verify(mockUsuarioDAO, never()).modificarUsuario(any());
        verify(mockCatalogo, never()).actualizarUsuario(any());
    }
    
    @Test
    void testObtenerUsuarioPorEmailExitoso() {
        String email = "juan@um.es";
        when(mockCatalogo.buscarPorEmail(email)).thenReturn(Optional.of(usuario));
        
        Usuario resultado = usuarioService.obtenerUsuarioPorEmail(email);
        
        assertEquals(usuario, resultado);
        verify(mockCatalogo).buscarPorEmail(email);
    }
    
    @Test
    void testObtenerUsuarioPorEmailNoEncontrado() {
        String email = "noexiste@um.es";
        when(mockCatalogo.buscarPorEmail(email)).thenReturn(Optional.empty());
        
        assertThrows(RuntimeException.class, () -> {
            usuarioService.obtenerUsuarioPorEmail(email);
        });
        verify(mockCatalogo).buscarPorEmail(email);
    }
    
    @Test
    void testAgregarLogroExitoso() {
        TipoLogro tipoLogro = TipoLogro.CINCO_CURSOS;
        int logrosIniciales = usuario.getLogros().size();
        
        usuarioService.agregarLogro(usuario, tipoLogro);
        
        assertTrue(usuario.getLogros().size() > logrosIniciales);
        verify(mockUsuarioDAO).modificarUsuario(usuario);
        verify(mockCatalogo).actualizarUsuario(usuario);
    }
    
    @Test
    void testAgregarLogroUsuarioNull() {
        TipoLogro tipoLogro = TipoLogro.CINCO_CURSOS;
        
        assertThrows(IllegalArgumentException.class, () -> {
            usuarioService.agregarLogro(null, tipoLogro);
        });
    }
    
    @Test
    void testAgregarLogroNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            usuarioService.agregarLogro(usuario, null);
        });
    }
    
    @Test
    void testObtenerEstadisticas() {
        Estadisticas estadisticas = new Estadisticas();
        usuario.setEstadisticas(estadisticas);
        
        Estadisticas resultado = usuarioService.obtenerEstadisticas(usuario);
        
        assertEquals(estadisticas, resultado);
    }
    
    @Test
    void testObtenerEstadisticasUsuarioNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            usuarioService.obtenerEstadisticas(null);
        });
    }
    
    @Test
    void testEnviarSolicitudAmistadExitosa() {
        when(mockAmistadDAO.existeAmistad(usuario, receptor)).thenReturn(false);
        
        boolean resultado = usuarioService.enviarSolicitudAmistad(usuario, receptor);
        
        assertTrue(resultado);
        verify(mockAmistadDAO).existeAmistad(usuario, receptor);
        verify(mockAmistadDAO).guardarAmistad(any(Amistad.class));
    }
    
    @Test
    void testEnviarSolicitudAmistadASiMismo() {
        boolean resultado = usuarioService.enviarSolicitudAmistad(usuario, usuario);
        
        assertFalse(resultado);
        verify(mockAmistadDAO, never()).existeAmistad(any(), any());
        verify(mockAmistadDAO, never()).guardarAmistad(any());
    }
    
    @Test
    void testEnviarSolicitudAmistadYaExiste() {
        when(mockAmistadDAO.existeAmistad(usuario, receptor)).thenReturn(true);
        
        boolean resultado = usuarioService.enviarSolicitudAmistad(usuario, receptor);
        
        assertFalse(resultado);
        verify(mockAmistadDAO).existeAmistad(usuario, receptor);
        verify(mockAmistadDAO, never()).guardarAmistad(any());
    }
    
    @Test
    void testProcesarSolicitudAmistadAceptar() {
        Long solicitudId = 1L;
        Amistad solicitud = mock(Amistad.class);
        
        when(mockAmistadDAO.buscarPorId(solicitudId)).thenReturn(solicitud);
        when(solicitud.getEstado()).thenReturn(EstadoAmistad.PENDIENTE);
        when(solicitud.getUsuario2()).thenReturn(receptor);
        
        boolean resultado = usuarioService.procesarSolicitudAmistad(solicitudId, receptor, true);
        
        assertTrue(resultado);
        verify(solicitud).setEstado(EstadoAmistad.ACEPTADA);
        verify(mockAmistadDAO).actualizarAmistad(solicitud);
    }
    
    @Test
    void testProcesarSolicitudAmistadRechazar() {
        Long solicitudId = 1L;
        Amistad solicitud = mock(Amistad.class);
        
        when(mockAmistadDAO.buscarPorId(solicitudId)).thenReturn(solicitud);
        when(solicitud.getEstado()).thenReturn(EstadoAmistad.PENDIENTE);
        when(solicitud.getUsuario2()).thenReturn(receptor);
        
        boolean resultado = usuarioService.procesarSolicitudAmistad(solicitudId, receptor, false);
        
        assertTrue(resultado);
        verify(solicitud).setEstado(EstadoAmistad.RECHAZADA);
        verify(mockAmistadDAO).actualizarAmistad(solicitud);
    }
    
    @Test
    void testProcesarSolicitudAmistadNoExiste() {
        Long solicitudId = 999L;
        when(mockAmistadDAO.buscarPorId(solicitudId)).thenReturn(null);
        
        boolean resultado = usuarioService.procesarSolicitudAmistad(solicitudId, receptor, true);
        
        assertFalse(resultado);
        verify(mockAmistadDAO, never()).actualizarAmistad(any());
    }
    
    @Test
    void testProcesarSolicitudAmistadEstadoIncorrecto() {
        Long solicitudId = 1L;
        Amistad solicitud = mock(Amistad.class);
        
        when(mockAmistadDAO.buscarPorId(solicitudId)).thenReturn(solicitud);
        when(solicitud.getEstado()).thenReturn(EstadoAmistad.ACEPTADA);
        
        boolean resultado = usuarioService.procesarSolicitudAmistad(solicitudId, receptor, true);
        
        assertFalse(resultado);
        verify(mockAmistadDAO, never()).actualizarAmistad(any());
    }
    
    @Test
    void testObtenerAmigos() {
        Usuario amigo1 = new Usuario("Carlos", "López", "Hombre", "carlos@um.es", "contraseñaCarlos", "/carlos.jpg");
        amigo1.setId(3L);
        Usuario amigo2 = new Usuario("María", "Rodríguez", "Mujer", "maria@um.es", "claveMaria", "/maria.jpg");
        amigo2.setId(4L);
        
        Amistad amistad1 = new Amistad(usuario, amigo1);
        amistad1.setEstado(EstadoAmistad.ACEPTADA);
        
        Amistad amistad2 = new Amistad(amigo2, usuario);
        amistad2.setEstado(EstadoAmistad.ACEPTADA);
        
        List<Amistad> amistades = Arrays.asList(amistad1, amistad2);
        when(mockAmistadDAO.buscarAmistades(usuario)).thenReturn(amistades);
        
        List<Usuario> amigos = usuarioService.obtenerAmigos(usuario);
        
        assertEquals(2, amigos.size());
        assertTrue(amigos.contains(amigo1));
        assertTrue(amigos.contains(amigo2));
        verify(mockAmistadDAO).buscarAmistades(usuario);
    }
    
    @Test
    void testObtenerAmigosSinAmistades() {
        when(mockAmistadDAO.buscarAmistades(usuario)).thenReturn(new ArrayList<>());
        
        List<Usuario> amigos = usuarioService.obtenerAmigos(usuario);
        
        assertTrue(amigos.isEmpty());
        verify(mockAmistadDAO).buscarAmistades(usuario);
    }
    
    @Test
    void testObtenerSolicitudesPendientes() {
        Amistad solicitud1 = mock(Amistad.class);
        Amistad solicitud2 = mock(Amistad.class);
        List<Amistad> solicitudes = Arrays.asList(solicitud1, solicitud2);
        
        when(mockAmistadDAO.buscarSolicitudesPendientes(usuario)).thenReturn(solicitudes);
        
        List<Amistad> resultado = usuarioService.obtenerSolicitudesPendientes(usuario);
        
        assertEquals(2, resultado.size());
        assertTrue(resultado.contains(solicitud1));
        assertTrue(resultado.contains(solicitud2));
        verify(mockAmistadDAO).buscarSolicitudesPendientes(usuario);
    }
    
    @Test
    void testLimpiarCursosDuplicados() {
        Curso curso1 = new Curso();
        curso1.setTitulo("Programación en Java");
        
        Curso curso2 = new Curso();
        curso2.setTitulo("Bases de Datos");
        
        Curso curso3 = new Curso();
        curso3.setTitulo("Programación en Java");
        
        List<Curso> bibliotecaConDuplicados = new ArrayList<>();
        bibliotecaConDuplicados.add(curso1);
        bibliotecaConDuplicados.add(curso2);
        bibliotecaConDuplicados.add(curso3);
        
        usuario.setBiblioteca(bibliotecaConDuplicados);
        
        boolean resultado = usuarioService.limpiarCursosDuplicados(usuario);
        
        assertTrue(resultado);
        assertEquals(2, usuario.getBiblioteca().size());
        verify(mockUsuarioDAO).modificarUsuario(usuario);
        verify(mockCatalogo).actualizarUsuario(usuario);
    }
    
    @Test
    void testLimpiarCursosDuplicadosSinDuplicados() {
        Curso curso1 = new Curso();
        curso1.setTitulo("Programación en Java");
        
        Curso curso2 = new Curso();
        curso2.setTitulo("Bases de Datos");
        
        List<Curso> bibliotecaSinDuplicados = new ArrayList<>();
        bibliotecaSinDuplicados.add(curso1);
        bibliotecaSinDuplicados.add(curso2);
        
        usuario.setBiblioteca(bibliotecaSinDuplicados);
        
        boolean resultado = usuarioService.limpiarCursosDuplicados(usuario);
        
        assertFalse(resultado);
        verify(mockUsuarioDAO, never()).modificarUsuario(any());
        verify(mockCatalogo, never()).actualizarUsuario(any());
    }
    
    @Test
    void testLimpiarCursosDuplicadosUsuarioNull() {
        boolean resultado = usuarioService.limpiarCursosDuplicados(null);
        
        assertFalse(resultado);
    }
    
    @Test
    void testLimpiarCursosDuplicadosBibliotecaNull() {
        usuario.setBiblioteca(null);
        
        boolean resultado = usuarioService.limpiarCursosDuplicados(usuario);
        
        assertFalse(resultado);
    }
}