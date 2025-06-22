package umu.pds.modelo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class EstrategiaAleatoriaTest {
	
	private EstrategiaAleatoria estrategia;
	
	private List<Ejercicio> ejercicios;
	
	@BeforeEach
	void setup() {
		Ejercicio ej1 = new EjercicioFlashcard();
		ej1.setId((long) 1);
		Ejercicio ej2 = new EjercicioOpcionMultiple();
		ej2.setId((long) 2);
		Ejercicio ej3 = new EjercicioRellenarHuecos();
		ej3.setId((long) 3);
		ejercicios = List.of(ej1, ej2, ej3);
		estrategia = (EstrategiaAleatoria) EstrategiaFactory.crearEstrategia(TipoEstrategia.ALEATORIA);
	}
	
	@Test
	void testOrdenarEjercicios() {
		List<Ejercicio> ejerciciosOrdenados = estrategia.ordenarEjercicios(ejercicios);
		assertEquals(ejercicios.size(), ejerciciosOrdenados.size());
		assertTrue(ejerciciosOrdenados.containsAll(ejercicios));
	}
	
	@Test
	void testGetTipoEstrategia() {
		assertEquals(TipoEstrategia.ALEATORIA, estrategia.getTipoEstrategia());
	}
	
}
