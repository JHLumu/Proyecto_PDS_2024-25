package umu.pds.servicios;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.net.MalformedURLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import umu.pds.modelo.Bloque;
import umu.pds.modelo.Curso;
import umu.pds.modelo.Ejercicio;
import umu.pds.modelo.EjercicioOpcionMultiple;
import umu.pds.modelo.Estadisticas;
import umu.pds.modelo.SesionAprendizaje;
import umu.pds.modelo.Usuario;
import umu.pds.persistencia.SesionAprendizajeDAO;
import umu.pds.persistencia.UsuarioDAO;
import umu.pds.servicios.ServicioEstadisticas.EstadisticasCurso;

class ServicioEstadisticasTest {

    private SesionAprendizajeDAO mockSesionDAO;
    private UsuarioDAO mockUsuarioDAO;
    private ServicioEstadisticas servicioEstadisticas;
    private Usuario usuario;
    private Curso curso;
    private Ejercicio ejercicio;
    private SesionAprendizaje sesion;

    @BeforeEach
    void setUp() {
        mockSesionDAO = mock(SesionAprendizajeDAO.class);
        mockUsuarioDAO = mock(UsuarioDAO.class);
        servicioEstadisticas = new ServicioEstadisticas(mockSesionDAO, mockUsuarioDAO);
        
        setupTestObjects();
    }
    
    private void setupTestObjects() {
        usuario = new Usuario("María", "García", "M", "maria.garcia@um.es", "password123", "/fotoUser.png");
        usuario.setId(1L);
        
        curso = new Curso();
        curso.setId(1L);
        curso.setTitulo("Programación en Java");
        
        Bloque bloque = new Bloque();
        bloque.setTitulo("Fundamentos de Java");
        
        ejercicio = new EjercicioOpcionMultiple("¿Cuál es la palabra clave para definir una clase en Java?", "class");
        ejercicio.setId(1L);
        ejercicio.setBloque(bloque);
        
        bloque.setEjercicios(Arrays.asList(ejercicio));
        curso.setBloques(Arrays.asList(bloque));
        
        Estadisticas estadisticas = new Estadisticas();
        estadisticas.setUsuario(usuario);
        usuario.setEstadisticas(estadisticas);
        
        sesion = new SesionAprendizaje(usuario, curso, ejercicio);
        sesion.setId(1L);
    }

    @Test
    void testIniciarSesion() {
        SesionAprendizaje resultado = servicioEstadisticas.iniciarSesion(usuario, curso, ejercicio);
        
        assertNotNull(resultado);
        assertEquals(usuario, resultado.getUsuario());
        assertEquals(curso, resultado.getCurso());
        assertEquals(ejercicio, resultado.getEjercicio());
        assertFalse(resultado.isCompletada());
        
        verify(mockSesionDAO).guardarSesion(any(SesionAprendizaje.class));
    }

    @Test
    void testFinalizarSesion() throws MalformedURLException {
        when(mockUsuarioDAO.recuperarUsuario(usuario.getId())).thenReturn(usuario);
        when(mockSesionDAO.buscarSesionesPorUsuario(usuario)).thenReturn(Arrays.asList(sesion));
        
        servicioEstadisticas.finalizarSesion(sesion);
        
        assertTrue(sesion.isCompletada());
        assertNotNull(sesion.getFechaFin());
        
        verify(mockSesionDAO).actualizarSesion(sesion);
        verify(mockUsuarioDAO).recuperarUsuario(usuario.getId());
    }

    @Test
    void testRegistrarAcierto() {
        int aciertosPrevios = sesion.getAciertos();
        int ejerciciosPrevios = sesion.getEjerciciosCompletados();
        
        servicioEstadisticas.registrarAcierto(sesion);
        
        assertEquals(aciertosPrevios + 1, sesion.getAciertos());
        assertEquals(ejerciciosPrevios + 1, sesion.getEjerciciosCompletados());
        verify(mockSesionDAO).actualizarSesion(sesion);
    }

    @Test
    void testRegistrarFallo() {
        int fallosPrevios = sesion.getFallos();
        
        servicioEstadisticas.registrarFallo(sesion);
        
        assertEquals(fallosPrevios + 1, sesion.getFallos());
        verify(mockSesionDAO).actualizarSesion(sesion);
    }

    static List<Arguments> casosActualizarEstadisticas() {
        return List.of(
            Arguments.of(Arrays.asList(
                crearDatosSesion(120, 5, 4, 1),
                crearDatosSesion(180, 3, 2, 1)
            ), 300, 8, 75.0),
            Arguments.of(Arrays.asList(
                crearDatosSesion(60, 2, 2, 0),
                crearDatosSesion(90, 3, 1, 2)
            ), 150, 5, 60.0),
            Arguments.of(Arrays.asList(
                crearDatosSesion(200, 4, 4, 0)
            ), 200, 4, 100.0)
        );
    }

    @ParameterizedTest
    @MethodSource("casosActualizarEstadisticas")
    void testActualizarEstadisticasUsuario(List<int[]> datosSesiones, int tiempoEsperado, 
                                         int ejerciciosEsperados, double precisionEsperada) throws MalformedURLException {
        List<SesionAprendizaje> sesiones = datosSesiones.stream()
            .map(datos -> crearSesionCompletada(datos[0], datos[1], datos[2], datos[3]))
            .toList();
        
        when(mockSesionDAO.buscarSesionesPorUsuario(usuario)).thenReturn(sesiones);
        when(mockUsuarioDAO.recuperarUsuario(usuario.getId())).thenReturn(usuario);
        
        servicioEstadisticas.actualizarEstadisticasUsuario(usuario);
        
        Estadisticas stats = usuario.getEstadisticas();
        assertEquals(tiempoEsperado, stats.getTiempoTotal());
        assertEquals(ejerciciosEsperados, stats.getTotalEjerciciosCompletados());
        assertEquals(precisionEsperada, stats.getPrecision(), 0.1);
        
        verify(mockUsuarioDAO).modificarUsuario(usuario);
    }

    @Test
    void testActualizarEstadisticasUsuarioSinEstadisticas() throws MalformedURLException {
        usuario.setEstadisticas(null);
        
        when(mockSesionDAO.buscarSesionesPorUsuario(usuario)).thenReturn(Collections.emptyList());
        when(mockUsuarioDAO.recuperarUsuario(usuario.getId())).thenReturn(usuario);
        when(mockUsuarioDAO.recuperarEstadisticas(usuario.getId())).thenReturn(null);
        
        servicioEstadisticas.actualizarEstadisticasUsuario(usuario);
        
        assertNotNull(usuario.getEstadisticas());
        verify(mockUsuarioDAO).modificarUsuario(usuario);
    }

    @Test
    void testObtenerEstadisticasCurso() {
        List<SesionAprendizaje> sesionesCurso = Arrays.asList(
            crearSesionCompletada(120, 3, 2, 1),
            crearSesionCompletada(180, 2, 2, 0)
        );
        
        when(mockSesionDAO.buscarSesionesPorUsuarioYCurso(usuario, curso)).thenReturn(sesionesCurso);
        
        EstadisticasCurso resultado = servicioEstadisticas.obtenerEstadisticasCurso(usuario, curso);
        
        assertNotNull(resultado);
        assertEquals(curso, resultado.getCurso());
        assertEquals(5, resultado.getTiempoTotalMinutos()); // 300 segundos = 5 minutos
        assertEquals(300, resultado.getTiempoTotalSegundos());
        assertEquals(5, resultado.getEjerciciosCompletados()); // 3 + 2
        assertEquals(80.0, resultado.getPrecision(), 0.1); // 4/(4+1) * 100
        assertEquals(100.0, resultado.getPorcentajeCompletado(), 0.1); // 5/5 * 100
    }

    @Test
    void testObtenerEstadisticasCursoSinSesiones() {
        when(mockSesionDAO.buscarSesionesPorUsuarioYCurso(usuario, curso)).thenReturn(Collections.emptyList());
        
        EstadisticasCurso resultado = servicioEstadisticas.obtenerEstadisticasCurso(usuario, curso);
        
        assertEquals(0, resultado.getTiempoTotalMinutos());
        assertEquals(0.0, resultado.getPrecision());
        assertEquals(0.0, resultado.getPorcentajeCompletado());
    }

    @Test
    void testObtenerSesionesUsuario() {
        List<SesionAprendizaje> sesionesEsperadas = Arrays.asList(sesion);
        when(mockSesionDAO.buscarSesionesPorUsuario(usuario)).thenReturn(sesionesEsperadas);
        
        List<SesionAprendizaje> resultado = servicioEstadisticas.obtenerSesionesUsuario(usuario);
        
        assertEquals(sesionesEsperadas, resultado);
        verify(mockSesionDAO).buscarSesionesPorUsuario(usuario);
    }

    @Test
    void testObtenerEstadisticasTodosCursos() {
        Curso curso2 = new Curso();
        curso2.setId(2L);
        curso2.setTitulo("Bases de Datos");
        
        List<SesionAprendizaje> todasLasSesiones = Arrays.asList(
            crearSesionCompletada(curso, 120, 3, 2, 1),
            crearSesionCompletada(curso2, 180, 2, 1, 1)
        );
        
        when(mockSesionDAO.buscarSesionesPorUsuario(usuario)).thenReturn(todasLasSesiones);
        when(mockSesionDAO.buscarSesionesPorUsuarioYCurso(eq(usuario), any(Curso.class)))
            .thenAnswer(invocation -> {
                Curso cursoParam = invocation.getArgument(1);
                return todasLasSesiones.stream()
                    .filter(s -> s.getCurso().getId().equals(cursoParam.getId()))
                    .toList();
            });
        
        List<EstadisticasCurso> resultado = servicioEstadisticas.obtenerEstadisticasTodosCursos(usuario);
        
        assertEquals(2, resultado.size());
    }

    @Test
    void testEstadisticasCursoGetters() {
        EstadisticasCurso estadisticas = new EstadisticasCurso(curso, 5, 300, 10, 85.5, 75.0);
        
        assertEquals(curso, estadisticas.getCurso());
        assertEquals(5, estadisticas.getTiempoTotalMinutos());
        assertEquals(300, estadisticas.getTiempoTotalSegundos());
        assertEquals(10, estadisticas.getEjerciciosCompletados());
        assertEquals(85.5, estadisticas.getPrecision(), 0.1);
        assertEquals(75.0, estadisticas.getPorcentajeCompletado(), 0.1);
    }

    static List<Arguments> casosCalcularMejorRacha() {
        LocalDate hoy = LocalDate.now();
        return List.of(
            Arguments.of(Collections.emptyList(), 0, "sin sesiones"),
            Arguments.of(Arrays.asList(hoy), 1, "un solo día"),
            Arguments.of(Arrays.asList(hoy.minusDays(2), hoy.minusDays(1), hoy), 3, "tres días consecutivos"),
            Arguments.of(Arrays.asList(hoy.minusDays(5), hoy.minusDays(2), hoy), 1, "días no consecutivos"),
            Arguments.of(Arrays.asList(hoy.minusDays(4), hoy.minusDays(3), hoy.minusDays(2), hoy), 3, "racha interrumpida")
        );
    }

    @ParameterizedTest
    @MethodSource("casosCalcularMejorRacha")
    void testCalcularMejorRacha(List<LocalDate> fechas, int rachaEsperada, String descripcion) throws MalformedURLException {
        List<SesionAprendizaje> sesiones = fechas.stream()
            .map(this::crearSesionEnFecha)
            .toList();
        
        when(mockSesionDAO.buscarSesionesPorUsuario(usuario)).thenReturn(sesiones);
        when(mockUsuarioDAO.recuperarUsuario(usuario.getId())).thenReturn(usuario);
        
        servicioEstadisticas.actualizarEstadisticasUsuario(usuario);
        
        assertEquals(rachaEsperada, usuario.getEstadisticas().getMejorRacha(), 
                    "Fallo en caso: " + descripcion);
    }

    static List<Arguments> casosCalcularRachaActual() {
        LocalDate hoy = LocalDate.now();
        return List.of(
            Arguments.of(Collections.emptyList(), 0, "sin actividad"),
            Arguments.of(Arrays.asList(hoy, hoy.minusDays(1)), 2, "actividad hoy y ayer"),
            Arguments.of(Arrays.asList(hoy.minusDays(1), hoy.minusDays(2)), 2, "actividad ayer"),
            Arguments.of(Arrays.asList(hoy.minusDays(2)), 0, "racha rota")
        );
    }

    @ParameterizedTest
    @MethodSource("casosCalcularRachaActual")
    void testCalcularRachaActual(List<LocalDate> fechas, int rachaEsperada, String descripcion) throws MalformedURLException {
        List<SesionAprendizaje> sesiones = fechas.stream()
            .map(this::crearSesionEnFecha)
            .toList();
        
        when(mockSesionDAO.buscarSesionesPorUsuario(usuario)).thenReturn(sesiones);
        when(mockUsuarioDAO.recuperarUsuario(usuario.getId())).thenReturn(usuario);
        
        servicioEstadisticas.actualizarEstadisticasUsuario(usuario);
        
        assertEquals(rachaEsperada, usuario.getEstadisticas().getRachaDias(), 
                    "Fallo en caso: " + descripcion);
    }

    @Test
    void testCalcularMejorRachaSinSesionesCompletadas() throws MalformedURLException {
        SesionAprendizaje sesionIncompleta = new SesionAprendizaje(usuario, curso, ejercicio);
        sesionIncompleta.setCompletada(false);
        
        when(mockSesionDAO.buscarSesionesPorUsuario(usuario)).thenReturn(Arrays.asList(sesionIncompleta));
        when(mockUsuarioDAO.recuperarUsuario(usuario.getId())).thenReturn(usuario);
        
        servicioEstadisticas.actualizarEstadisticasUsuario(usuario);
        
        assertEquals(0, usuario.getEstadisticas().getMejorRacha());
    }

    
    // --- Métodos auxiliares para crear datos de prueba ---

    private static int[] crearDatosSesion(int tiempo, int ejercicios, int aciertos, int fallos) {
        return new int[]{tiempo, ejercicios, aciertos, fallos};
    }

    private SesionAprendizaje crearSesionCompletada(int tiempoTotal, int ejercicios, int aciertos, int fallos) {
        return crearSesionCompletada(curso, tiempoTotal, ejercicios, aciertos, fallos);
    }
    
    private SesionAprendizaje crearSesionCompletada(Curso curso, int tiempoTotal, int ejercicios, int aciertos, int fallos) {
        SesionAprendizaje sesion = new SesionAprendizaje(usuario, curso, ejercicio);
        sesion.setCompletada(true);
        sesion.setTiempoTotal(tiempoTotal);
        sesion.setEjerciciosCompletados(ejercicios);
        sesion.setAciertos(aciertos);
        sesion.setFallos(fallos);
        sesion.setFechaInicio(new Date());
        return sesion;
    }

    private SesionAprendizaje crearSesionEnFecha(LocalDate fecha) {
        SesionAprendizaje sesion = new SesionAprendizaje(usuario, curso, ejercicio);
        sesion.setCompletada(true);
        sesion.setTiempoTotal(60);
        sesion.setEjerciciosCompletados(1);
        sesion.setAciertos(1);
        sesion.setFallos(0);
        
        Date fechaDate = Date.from(fecha.atStartOfDay(ZoneId.systemDefault()).toInstant());
        sesion.setFechaInicio(fechaDate);
        
        return sesion;
    }
}