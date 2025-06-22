package umu.pds.servicios;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import umu.pds.modelo.*;
import umu.pds.persistencia.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ServicioProgresoTest {

    @Mock
    private ProgresoBloqueDAO progresoBloqueDAO;
    
    @Mock
    private FactoriaDAO factoriaDAO;
    
    @Mock
    private Usuario usuario;
    
    @Mock
    private Bloque bloque;
    
    @Mock
    private Curso curso;
    
    @Mock
    private Ejercicio ejercicio1;
    
    @Mock
    private Ejercicio ejercicio2;
    
    @Mock
    private Ejercicio ejercicio3;
    
    private ServicioProgreso servicioProgreso;
    private AutoCloseable mockitoCloseable;
    private MockedStatic<FactoriaDAO> factoriaDAOMockedStatic;
    
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    public void setUp() {
        mockitoCloseable = MockitoAnnotations.openMocks(this);
        
        factoriaDAOMockedStatic = mockStatic(FactoriaDAO.class);
        factoriaDAOMockedStatic.when(() -> FactoriaDAO.getInstancia(JPAFactoriaDAO.class.getName()))
                .thenReturn(factoriaDAO);
        
        when(factoriaDAO.getProgresoBloqueDAO()).thenReturn(progresoBloqueDAO);
        
        servicioProgreso = new ServicioProgreso();
        
        when(usuario.getId()).thenReturn(1L);
        when(bloque.getId()).thenReturn(1L);
        when(bloque.getTitulo()).thenReturn("Bloque Test");
        when(curso.getId()).thenReturn(1L);
        when(bloque.getCurso()).thenReturn(curso);
        
        List<Ejercicio> ejercicios = Arrays.asList(ejercicio1, ejercicio2, ejercicio3);
        when(bloque.getEjercicios()).thenReturn(ejercicios);
        
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    public void tearDown() throws Exception {
        System.setOut(originalOut);
        factoriaDAOMockedStatic.close();
        mockitoCloseable.close();
    }

    @Test
    public void testIniciarProgreso_NuevoProgreso() {
        TipoEstrategia estrategia = TipoEstrategia.SECUENCIAL;
        when(progresoBloqueDAO.buscarProgreso(usuario, bloque)).thenReturn(Optional.empty());
        
        ProgresoBloque resultado = servicioProgreso.iniciarProgreso(usuario, bloque, estrategia);
        
        assertNotNull(resultado);
        assertEquals(usuario, resultado.getUsuario());
        assertEquals(bloque, resultado.getBloque());
        assertEquals(estrategia, resultado.getEstrategiaUtilizada());
        verify(progresoBloqueDAO).guardarProgreso(any(ProgresoBloque.class));
        assertTrue(outContent.toString().contains("Nuevo progreso iniciado para bloque: Bloque Test"));
    }

    @Test
    public void testIniciarProgreso_ProgresoExistenteMismaEstrategia() {
        TipoEstrategia estrategia = TipoEstrategia.SECUENCIAL;
        ProgresoBloque progresoExistente = new ProgresoBloque(usuario, bloque, estrategia);
        when(progresoBloqueDAO.buscarProgreso(usuario, bloque)).thenReturn(Optional.of(progresoExistente));
        
        ProgresoBloque resultado = servicioProgreso.iniciarProgreso(usuario, bloque, estrategia);
        
        assertSame(progresoExistente, resultado);
        verify(progresoBloqueDAO, never()).actualizarProgreso(any(ProgresoBloque.class));
    }

    @Test
    public void testIniciarProgreso_ProgresoExistenteDiferenteEstrategia() {
        TipoEstrategia estrategiaOriginal = TipoEstrategia.SECUENCIAL;
        TipoEstrategia nuevaEstrategia = TipoEstrategia.REPETICION_ESPACIADA;
        ProgresoBloque progresoExistente = spy(new ProgresoBloque(usuario, bloque, estrategiaOriginal));
        when(progresoBloqueDAO.buscarProgreso(usuario, bloque)).thenReturn(Optional.of(progresoExistente));
        
        ProgresoBloque resultado = servicioProgreso.iniciarProgreso(usuario, bloque, nuevaEstrategia);
        
        assertSame(progresoExistente, resultado);
        verify(progresoExistente).setEstrategiaUtilizada(nuevaEstrategia);
        verify(progresoBloqueDAO).actualizarProgreso(progresoExistente);
    }

    @Test
    public void testObtenerProgreso_Existente() {
        ProgresoBloque progresoExistente = new ProgresoBloque(usuario, bloque, TipoEstrategia.SECUENCIAL);
        when(progresoBloqueDAO.buscarProgreso(usuario, bloque)).thenReturn(Optional.of(progresoExistente));
        
        ProgresoBloque resultado = servicioProgreso.obtenerProgreso(usuario, bloque);
        
        assertSame(progresoExistente, resultado);
    }

    @Test
    public void testObtenerProgreso_NoExistente() {
        when(progresoBloqueDAO.buscarProgreso(usuario, bloque)).thenReturn(Optional.empty());
        
        ProgresoBloque resultado = servicioProgreso.obtenerProgreso(usuario, bloque);
        
        assertNull(resultado);
    }

    @Test
    public void testGuardarProgreso() {
        ProgresoBloque progreso = spy(new ProgresoBloque(usuario, bloque, TipoEstrategia.SECUENCIAL));
        when(progreso.getPorcentajeCompletado()).thenReturn(50.0);
        
        servicioProgreso.guardarProgreso(progreso);
        
        verify(progreso).actualizarActividad();
        verify(progresoBloqueDAO).actualizarProgreso(progreso);
        assertTrue(outContent.toString().contains("Progreso guardado: 50.0%"));
    }

    @Test
    public void testAvanzarEjercicio_BloqueNoCompletado() {
        ProgresoBloque progreso = spy(new ProgresoBloque(usuario, bloque, TipoEstrategia.SECUENCIAL));
        when(progreso.isCompletado()).thenReturn(false);
        when(progreso.getPorcentajeCompletado()).thenReturn(33.3);
        
        servicioProgreso.avanzarEjercicio(progreso);
        
        verify(progreso).avanzarEjercicio();
        verify(progreso).actualizarActividad();
        verify(progresoBloqueDAO).actualizarProgreso(progreso);
        assertFalse(outContent.toString().contains("¡Bloque completado:"));
    }

    @Test
    public void testAvanzarEjercicio_BloqueCompletado() {
        ProgresoBloque progreso = spy(new ProgresoBloque(usuario, bloque, TipoEstrategia.SECUENCIAL));
        when(progreso.isCompletado()).thenReturn(true);
        when(progreso.getPorcentajeCompletado()).thenReturn(100.0);
        
        servicioProgreso.avanzarEjercicio(progreso);
        
        verify(progreso).avanzarEjercicio();
        verify(progreso).actualizarActividad();
        verify(progresoBloqueDAO).actualizarProgreso(progreso);
        assertTrue(outContent.toString().contains("¡Bloque completado: Bloque Test!"));
    }

    @Test
    public void testReiniciarBloque_ProgresoExistente() {
        ProgresoBloque progreso = spy(new ProgresoBloque(usuario, bloque, TipoEstrategia.SECUENCIAL));
        when(progresoBloqueDAO.buscarProgreso(usuario, bloque)).thenReturn(Optional.of(progreso));
        when(progreso.getPorcentajeCompletado()).thenReturn(0.0);
        
        servicioProgreso.reiniciarBloque(usuario, bloque);
        
        verify(progreso).reiniciar();
        verify(progreso).actualizarActividad();
        verify(progresoBloqueDAO).actualizarProgreso(progreso);
        assertTrue(outContent.toString().contains("Bloque reiniciado: Bloque Test"));
    }

    @Test
    public void testReiniciarBloque_ProgresoNoExistente() {
        when(progresoBloqueDAO.buscarProgreso(usuario, bloque)).thenReturn(Optional.empty());
        
        servicioProgreso.reiniciarBloque(usuario, bloque);
        
        verify(progresoBloqueDAO, never()).actualizarProgreso(any(ProgresoBloque.class));
        assertFalse(outContent.toString().contains("Bloque reiniciado:"));
    }

    @Test
    public void testEliminarProgreso_ProgresoExistente() {
        ProgresoBloque progreso = new ProgresoBloque(usuario, bloque, TipoEstrategia.SECUENCIAL);
        when(progresoBloqueDAO.buscarProgreso(usuario, bloque)).thenReturn(Optional.of(progreso));
        
        servicioProgreso.eliminarProgreso(usuario, bloque);
        
        verify(progresoBloqueDAO).eliminarProgreso(progreso);
        assertTrue(outContent.toString().contains("Progreso eliminado para bloque: Bloque Test"));
    }

    @Test
    public void testEliminarProgreso_ProgresoNoExistente() {
        when(progresoBloqueDAO.buscarProgreso(usuario, bloque)).thenReturn(Optional.empty());
        
        servicioProgreso.eliminarProgreso(usuario, bloque);
        
        verify(progresoBloqueDAO, never()).eliminarProgreso(any(ProgresoBloque.class));
        assertFalse(outContent.toString().contains("Progreso eliminado"));
    }

    @Test
    public void testObtenerProgresosCurso() {
        List<ProgresoBloque> progresos = Arrays.asList(
            new ProgresoBloque(usuario, bloque, TipoEstrategia.SECUENCIAL)
        );
        when(progresoBloqueDAO.buscarProgresosPorCurso(usuario, curso)).thenReturn(progresos);
        
        List<ProgresoBloque> resultado = servicioProgreso.obtenerProgresosCurso(usuario, curso);
        
        assertEquals(progresos, resultado);
        verify(progresoBloqueDAO).buscarProgresosPorCurso(usuario, curso);
    }

    @Test
    public void testObtenerBloquesEnProgreso() {
        List<ProgresoBloque> progresosActivos = Arrays.asList(
            new ProgresoBloque(usuario, bloque, TipoEstrategia.SECUENCIAL)
        );
        when(progresoBloqueDAO.buscarProgresosActivos(usuario)).thenReturn(progresosActivos);
        
        List<ProgresoBloque> resultado = servicioProgreso.obtenerBloquesEnProgreso(usuario);
        
        assertEquals(progresosActivos, resultado);
        verify(progresoBloqueDAO).buscarProgresosActivos(usuario);
    }

    @Test
    public void testObtenerBloquesCompletados() {
        ProgresoBloque progresoCompleto = spy(new ProgresoBloque(usuario, bloque, TipoEstrategia.SECUENCIAL));
        ProgresoBloque progresoIncompleto = spy(new ProgresoBloque(usuario, bloque, TipoEstrategia.SECUENCIAL));
        when(progresoCompleto.isCompletado()).thenReturn(true);
        when(progresoIncompleto.isCompletado()).thenReturn(false);
        
        List<ProgresoBloque> todosLosProgresos = Arrays.asList(progresoCompleto, progresoIncompleto);
        when(progresoBloqueDAO.buscarProgresosPorUsuario(usuario)).thenReturn(todosLosProgresos);
        
        List<ProgresoBloque> resultado = servicioProgreso.obtenerBloquesCompletados(usuario);
        
        assertEquals(1, resultado.size());
        assertTrue(resultado.contains(progresoCompleto));
        assertFalse(resultado.contains(progresoIncompleto));
    }

    @Test
    public void testIsCursoCompletado_CursoCompleto() {
        Bloque bloque2 = mock(Bloque.class);
        when(bloque2.getId()).thenReturn(2L);
        when(bloque2.getCurso()).thenReturn(curso);
        
        List<Bloque> bloques = Arrays.asList(bloque, bloque2);
        when(curso.getBloques()).thenReturn(bloques);
        
        ProgresoBloque progreso1 = spy(new ProgresoBloque(usuario, bloque, TipoEstrategia.SECUENCIAL));
        ProgresoBloque progreso2 = spy(new ProgresoBloque(usuario, bloque2, TipoEstrategia.SECUENCIAL));
        when(progreso1.isCompletado()).thenReturn(true);
        when(progreso2.isCompletado()).thenReturn(true);
        
        when(progresoBloqueDAO.buscarProgreso(usuario, bloque)).thenReturn(Optional.of(progreso1));
        when(progresoBloqueDAO.buscarProgreso(usuario, bloque2)).thenReturn(Optional.of(progreso2));
        
        boolean resultado = servicioProgreso.isCursoCompletado(usuario, curso);
        
        assertTrue(resultado);
    }

    @Test
    public void testIsCursoCompletado_CursoIncompleto() {
        Bloque bloque2 = mock(Bloque.class);
        when(bloque2.getId()).thenReturn(2L);
        when(bloque2.getCurso()).thenReturn(curso);
        
        List<Bloque> bloques = Arrays.asList(bloque, bloque2);
        when(curso.getBloques()).thenReturn(bloques);
        
        ProgresoBloque progreso1 = spy(new ProgresoBloque(usuario, bloque, TipoEstrategia.SECUENCIAL));
        when(progreso1.isCompletado()).thenReturn(true);
        
        when(progresoBloqueDAO.buscarProgreso(usuario, bloque)).thenReturn(Optional.of(progreso1));
        when(progresoBloqueDAO.buscarProgreso(usuario, bloque2)).thenReturn(Optional.empty());
        
        boolean resultado = servicioProgreso.isCursoCompletado(usuario, curso);
        
        assertFalse(resultado);
    }

    @Test
    public void testIsCursoCompletado_CursoSinBloques() {
        when(curso.getBloques()).thenReturn(new ArrayList<>());
        
        boolean resultado = servicioProgreso.isCursoCompletado(usuario, curso);
        
        assertFalse(resultado);
    }

    @Test
    public void testIsCursoCompletado_CursoBloqueNull() {
        when(curso.getBloques()).thenReturn(null);
        
        boolean resultado = servicioProgreso.isCursoCompletado(usuario, curso);
        
        assertFalse(resultado);
    }

    @Test
    public void testCalcularPorcentajeCurso() {
        Bloque bloque2 = mock(Bloque.class);
        when(bloque2.getId()).thenReturn(2L);
        when(bloque2.getCurso()).thenReturn(curso);
        
        List<Ejercicio> ejercicios1 = Arrays.asList(ejercicio1, ejercicio2, ejercicio3);
        when(bloque.getEjercicios()).thenReturn(ejercicios1);
        
        List<Ejercicio> ejercicios2 = Arrays.asList(ejercicio1, ejercicio2);
        when(bloque2.getEjercicios()).thenReturn(ejercicios2);
        
        List<Bloque> bloques = Arrays.asList(bloque, bloque2);
        when(curso.getBloques()).thenReturn(bloques);
        
        ProgresoBloque progreso1 = spy(new ProgresoBloque(usuario, bloque, TipoEstrategia.SECUENCIAL));
        when(progreso1.getEjerciciosCompletados()).thenReturn(2);
        when(progresoBloqueDAO.buscarProgreso(usuario, bloque)).thenReturn(Optional.of(progreso1));
        
        ProgresoBloque progreso2 = spy(new ProgresoBloque(usuario, bloque2, TipoEstrategia.SECUENCIAL));
        when(progreso2.getEjerciciosCompletados()).thenReturn(1);
        when(progresoBloqueDAO.buscarProgreso(usuario, bloque2)).thenReturn(Optional.of(progreso2));
        
        double resultado = servicioProgreso.calcularPorcentajeCurso(usuario, curso);
        
        assertEquals(60.0, resultado, 0.01);
    }

    @Test
    public void testCalcularPorcentajeCurso_CursoSinBloques() {
        when(curso.getBloques()).thenReturn(new ArrayList<>());
        
        double resultado = servicioProgreso.calcularPorcentajeCurso(usuario, curso);
        
        assertEquals(0.0, resultado, 0.01);
    }

    @Test
    public void testCalcularPorcentajeCurso_CursoBloqueNull() {
        when(curso.getBloques()).thenReturn(null);
        
        double resultado = servicioProgreso.calcularPorcentajeCurso(usuario, curso);
        
        assertEquals(0.0, resultado, 0.01);
    }

    @Test
    public void testObtenerSiguienteBloqueRecomendado_PrimerBloqueNoCompletado() {
        Bloque bloque2 = mock(Bloque.class);
        when(bloque2.getId()).thenReturn(2L);
        when(bloque2.getCurso()).thenReturn(curso);
        
        List<Bloque> bloques = Arrays.asList(bloque, bloque2);
        when(curso.getBloques()).thenReturn(bloques);
        
        when(progresoBloqueDAO.buscarProgreso(usuario, bloque)).thenReturn(Optional.empty());
        
        Bloque resultado = servicioProgreso.obtenerSiguienteBloqueRecomendado(usuario, curso);
        
        assertEquals(bloque, resultado);
    }

    @Test
    public void testObtenerSiguienteBloqueRecomendado_SegundoBloqueNoCompletado() {
        Bloque bloque2 = mock(Bloque.class);
        when(bloque2.getId()).thenReturn(2L);
        when(bloque2.getCurso()).thenReturn(curso);
        
        List<Bloque> bloques = Arrays.asList(bloque, bloque2);
        when(curso.getBloques()).thenReturn(bloques);
        
        ProgresoBloque progreso1 = spy(new ProgresoBloque(usuario, bloque, TipoEstrategia.SECUENCIAL));
        when(progreso1.isCompletado()).thenReturn(true);
        when(progresoBloqueDAO.buscarProgreso(usuario, bloque)).thenReturn(Optional.of(progreso1));
        
        when(progresoBloqueDAO.buscarProgreso(usuario, bloque2)).thenReturn(Optional.empty());
        
        Bloque resultado = servicioProgreso.obtenerSiguienteBloqueRecomendado(usuario, curso);
        
        assertEquals(bloque2, resultado);
    }

    @Test
    public void testObtenerSiguienteBloqueRecomendado_TodosCompletados() {
        Bloque bloque2 = mock(Bloque.class);
        when(bloque2.getId()).thenReturn(2L);
        when(bloque2.getCurso()).thenReturn(curso);
        
        List<Bloque> bloques = Arrays.asList(bloque, bloque2);
        when(curso.getBloques()).thenReturn(bloques);
        
        ProgresoBloque progreso1 = spy(new ProgresoBloque(usuario, bloque, TipoEstrategia.SECUENCIAL));
        ProgresoBloque progreso2 = spy(new ProgresoBloque(usuario, bloque2, TipoEstrategia.SECUENCIAL));
        when(progreso1.isCompletado()).thenReturn(true);
        when(progreso2.isCompletado()).thenReturn(true);
        
        when(progresoBloqueDAO.buscarProgreso(usuario, bloque)).thenReturn(Optional.of(progreso1));
        when(progresoBloqueDAO.buscarProgreso(usuario, bloque2)).thenReturn(Optional.of(progreso2));
        
        Bloque resultado = servicioProgreso.obtenerSiguienteBloqueRecomendado(usuario, curso);
        
        assertNull(resultado);
    }

    @Test
    public void testObtenerSiguienteBloqueRecomendado_CursoSinBloques() {
        when(curso.getBloques()).thenReturn(new ArrayList<>());
        
        Bloque resultado = servicioProgreso.obtenerSiguienteBloqueRecomendado(usuario, curso);
        
        assertNull(resultado);
    }

    @Test
    public void testObtenerEstadisticas() {
        Curso curso2 = mock(Curso.class);
        when(curso2.getId()).thenReturn(2L);
        
        Bloque bloque2 = mock(Bloque.class);
        when(bloque2.getId()).thenReturn(2L);
        when(bloque2.getCurso()).thenReturn(curso);
        
        Bloque bloque3 = mock(Bloque.class);
        when(bloque3.getId()).thenReturn(3L);
        when(bloque3.getCurso()).thenReturn(curso2);
        
        ProgresoBloque progreso1 = spy(new ProgresoBloque(usuario, bloque, TipoEstrategia.SECUENCIAL));
        ProgresoBloque progreso2 = spy(new ProgresoBloque(usuario, bloque2, TipoEstrategia.SECUENCIAL));
        ProgresoBloque progreso3 = spy(new ProgresoBloque(usuario, bloque3, TipoEstrategia.SECUENCIAL));
        
        when(progreso1.isCompletado()).thenReturn(true);
        when(progreso2.isCompletado()).thenReturn(true);
        when(progreso3.isCompletado()).thenReturn(false);
        
        List<ProgresoBloque> todosLosProgresos = Arrays.asList(progreso1, progreso2, progreso3);
        when(progresoBloqueDAO.buscarProgresosPorUsuario(usuario)).thenReturn(todosLosProgresos);
        
        ServicioProgreso.EstadisticasUsuario resultado = servicioProgreso.obtenerEstadisticas(usuario);
        
        assertEquals(3, resultado.getBloquesIniciados());
        assertEquals(2, resultado.getBloquesCompletados());
        assertEquals(1, resultado.getBloquesEnProgreso());
        assertEquals(2, resultado.getCursosIniciados()); 
    }

    @Test
    public void testEstadisticasUsuario_Constructor() {
        ServicioProgreso.EstadisticasUsuario estadisticas = 
            new ServicioProgreso.EstadisticasUsuario(5, 3, 2, 2);
        
        assertEquals(5, estadisticas.getBloquesIniciados());
        assertEquals(3, estadisticas.getBloquesCompletados());
        assertEquals(2, estadisticas.getBloquesEnProgreso());
        assertEquals(2, estadisticas.getCursosIniciados());
    }

    @Test
    public void testEstadisticasUsuario_ToString() {
        ServicioProgreso.EstadisticasUsuario estadisticas = 
            new ServicioProgreso.EstadisticasUsuario(5, 3, 2, 2);
        
        String resultado = estadisticas.toString();
        
        String esperado = "Estadísticas: 5 bloques iniciados, 3 completados, 2 en progreso (2 cursos)";
        assertEquals(esperado, resultado);
    }
    
    
}