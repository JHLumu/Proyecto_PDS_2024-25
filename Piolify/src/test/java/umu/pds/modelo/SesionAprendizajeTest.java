package umu.pds.modelo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Date;
import java.util.Calendar;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SesionAprendizajeTest {

	private SesionAprendizaje sesion;
	
    @BeforeEach
    void setUp() {
        sesion = new SesionAprendizaje();
    }
    
    @Test
    void testSetters() {
    	
    	SesionAprendizaje s = new SesionAprendizaje();

    	Long id = 42L;
    	Usuario usuario = new Usuario();
    	Curso curso = new Curso();
    	Ejercicio ejercicio = new EjercicioFlashcard();

    	Calendar calInicio = Calendar.getInstance();
    	calInicio.set(2025, Calendar.JANUARY, 15, 10, 0, 0);
    	Calendar calFin = Calendar.getInstance();
    	calInicio.set(2025, Calendar.JANUARY, 16, 10, 0, 0);
    	Date inicio = calInicio.getTime();
    	Date fin    = calInicio.getTime();

    	int ejerciciosCompletados = 5;
    	int aciertos = 3;
    	int fallos = 2;
    	int tiempoTotal = 60; 
    	boolean completada = true;

    	s.setId(id);
    	s.setUsuario(usuario);
    	s.setCurso(curso);
    	s.setEjercicio(ejercicio);
    	s.setFechaInicio(inicio);
    	s.setFechaFin(fin);
    	s.setEjerciciosCompletados(ejerciciosCompletados);
    	s.setAciertos(aciertos);
    	s.setFallos(fallos);
    	s.setTiempoTotal(tiempoTotal);
    	s.setCompletada(completada);

    	assertEquals(id, s.getId());
    	assertSame(usuario, s.getUsuario());
    	assertSame(curso, s.getCurso());
    	assertSame(ejercicio, s.getEjercicio());
    	assertEquals(inicio, s.getFechaInicio());
    	assertEquals(fin, s.getFechaFin());
    	assertEquals(ejerciciosCompletados, s.getEjerciciosCompletados());
    	assertEquals(aciertos, s.getAciertos());
    	assertEquals(fallos, s.getFallos());
    	assertEquals(tiempoTotal, s.getTiempoTotal());
    	assertTrue(s.isCompletada());
    	
    }
    
    @Test
    void testFinalizarSesion() {
    	assertFalse(sesion.isCompletada());
    	sesion.finalizarSesion();
    	assertTrue(sesion.isCompletada());
    }
    
    @Test
    void testRegistarAcierto() {
    	sesion.registrarAcierto();
    	assertNotEquals(0, sesion.getAciertos());
    	assertEquals(sesion.getAciertos(), sesion.getEjerciciosCompletados());
    }

    @Test
    void testRegistrarFallo() {
    	sesion.registrarFallo();
    	assertEquals(1, sesion.getFallos());
    	assertNotEquals(sesion.getFallos(), sesion.getEjerciciosCompletados());
    	
    }
    
    @Test
    void testPorcentajeAciertos() {
    	 assertEquals(0.0, sesion.getPorcentajeAciertos());
    	 for (int i = 0; i < 3; i++) sesion.registrarAcierto();
         sesion.registrarFallo();
         assertEquals(75.0, sesion.getPorcentajeAciertos(), 0.0001);
    }
}
