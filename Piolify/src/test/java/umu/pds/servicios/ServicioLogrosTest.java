package umu.pds.servicios;

import static org.junit.jupiter.api.Assertions.*;


import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import umu.pds.modelo.Estadisticas;
import umu.pds.modelo.TipoLogro;
import umu.pds.modelo.Usuario;
import umu.pds.servicios.ServicioLogros.LogroConEstado;

class ServicioLogrosTest {

    private ServicioLogros servicioLogros;
    private Usuario usuario;
    private Estadisticas estadisticas;

    @BeforeEach
    void setUp() {
        servicioLogros = new ServicioLogros();
        
        // usuario de prueba
        usuario = new Usuario("Hola", "Nose", "H", "hola@um.com", "pass", "/fotoUser.png");
        usuario.setId(1L);
        usuario.setLogros(new ArrayList<>());
        usuario.setBiblioteca(new ArrayList<>());

        estadisticas = new Estadisticas();
        estadisticas.setUsuario(usuario);
        estadisticas.setTotalEjerciciosCompletados(0);
        estadisticas.setRachaDias(0);
        estadisticas.setTiempoTotal(0);
        usuario.setEstadisticas(estadisticas);
        
    }

    @Test
    void testObtenerLogrosConEstadoTodosDesbloqueados() {
        usuario.desbloquearLogro(TipoLogro.PRIMER_EJERCICIO);
        usuario.desbloquearLogro(TipoLogro.CINCO_EJERCICIOS);
        
        List<LogroConEstado> resultado = servicioLogros.obtenerLogrosConEstado(usuario);
        
        assertNotNull(resultado);
        assertEquals(TipoLogro.values().length, resultado.size());
        
        //los logros desbloqueados aparecen
        LogroConEstado primerEjercicio = resultado.stream()
            .filter(l -> l.getTipoLogro() == TipoLogro.PRIMER_EJERCICIO)
            .findFirst()
            .orElse(null);
        assertNotNull(primerEjercicio);
        assertTrue(primerEjercicio.isDesbloqueado());
        
        LogroConEstado cincoEjercicios = resultado.stream()
            .filter(l -> l.getTipoLogro() == TipoLogro.CINCO_EJERCICIOS)
            .findFirst()
            .orElse(null);
        assertNotNull(cincoEjercicios);
        assertTrue(cincoEjercicios.isDesbloqueado());
        
        //un logro no desbloqueado aparece como no desbloqueado
        LogroConEstado diezEjercicios = resultado.stream()
            .filter(l -> l.getTipoLogro() == TipoLogro.DIEZ_EJERCICIOS)
            .findFirst()
            .orElse(null);
        assertNotNull(diezEjercicios);
        assertFalse(diezEjercicios.isDesbloqueado());
    }

    @Test
    void testObtenerLogrosConEstadoNingunDesbloqueado() {
        List<LogroConEstado> resultado = servicioLogros.obtenerLogrosConEstado(usuario);
        
        assertNotNull(resultado);
        assertEquals(TipoLogro.values().length, resultado.size());
        
        //ningún logro está desbloqueado
        for (LogroConEstado logro : resultado) {
            assertFalse(logro.isDesbloqueado());
        }
    }


    @Test
    void testDesbloquearLogro() {
		// El usuario no tiene el logro desbloqueado
		assertFalse(usuario.tieneLogroDesbloqueado(TipoLogro.PRIMER_EJERCICIO));

		// Desbloquear logro
		servicioLogros.desbloquearLogro(usuario, TipoLogro.PRIMER_EJERCICIO);

		// El logro está desbloqueado
		assertTrue(usuario.tieneLogroDesbloqueado(TipoLogro.PRIMER_EJERCICIO));
    }


    @Test
    void testComprobarYDesbloquearLogrosNingunLogroNuevo() {
        usuario.desbloquearLogro(TipoLogro.PRIMER_EJERCICIO);
        estadisticas.setTotalEjerciciosCompletados(1);
        
        List<TipoLogro> logrosDesbloqueados = servicioLogros.comprobarYDesbloquearLogros(usuario);
        
        assertNotNull(logrosDesbloqueados);
        assertTrue(logrosDesbloqueados.isEmpty());
    }



    @Test
    void testLogroConEstadoGetters() {
        LogroConEstado logro = new LogroConEstado(TipoLogro.PRIMER_EJERCICIO, true);
        
        assertEquals(TipoLogro.PRIMER_EJERCICIO, logro.getTipoLogro());
        assertEquals(TipoLogro.PRIMER_EJERCICIO.getNombre(), logro.getNombre());
        assertEquals(TipoLogro.PRIMER_EJERCICIO.getDescripcion(), logro.getDescripcion());
        assertEquals(TipoLogro.PRIMER_EJERCICIO.getCondicion(), logro.getCondicion());
        assertTrue(logro.isDesbloqueado());
    }




    @Test
    void testComprobarYDesbloquearLogrosLimitesExactos() {
        // Exactamente 5 ejercicios
        estadisticas.setTotalEjerciciosCompletados(5);
        List<TipoLogro> logros = servicioLogros.comprobarYDesbloquearLogros(usuario);
        assertTrue(logros.contains(TipoLogro.PRIMER_EJERCICIO));
        assertTrue(logros.contains(TipoLogro.CINCO_EJERCICIOS));
        assertFalse(logros.contains(TipoLogro.DIEZ_EJERCICIOS));
        
        usuario.setLogros(new ArrayList<>());
        
        // Exactamente 15 días de racha
        estadisticas.setRachaDias(15);
        logros = servicioLogros.comprobarYDesbloquearLogros(usuario);
        assertTrue(logros.contains(TipoLogro.RACHA_3_DIAS));
        assertTrue(logros.contains(TipoLogro.RACHA_7_DIAS));
        assertTrue(logros.contains(TipoLogro.RACHA_15_DIAS));
        
        
        usuario.setLogros(new ArrayList<>());
        
        // Exactamente 1 hora (3600 segundos)
        estadisticas.setTiempoTotal(3600);
        logros = servicioLogros.comprobarYDesbloquearLogros(usuario);
        assertTrue(logros.contains(TipoLogro.TIEMPO_30_MIN));
        assertTrue(logros.contains(TipoLogro.TIEMPO_1_HORA));
        assertFalse(logros.contains(TipoLogro.TIEMPO_5_HORAS));
    }

}