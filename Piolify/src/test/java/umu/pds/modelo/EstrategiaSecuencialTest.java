package umu.pds.modelo;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class EstrategiaSecuencialTest {

	private EstrategiaSecuencial estrategia;
	
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
		estrategia = (EstrategiaSecuencial) EstrategiaFactory.crearEstrategia(TipoEstrategia.SECUENCIAL);
	}
	
	@Test
	void testOrdenarEjercicios() {
		List<Ejercicio> ejerciciosOrdenados = estrategia.ordenarEjercicios(ejercicios);
		assertEquals(ejercicios.size(), ejerciciosOrdenados.size());
		for(int i = 0 ; i < ejercicios.size() ; i++) assertEquals(ejercicios.get(i).getId(), ejerciciosOrdenados.get(i).getId());
	}
	
	@Test
	void testGetTipoEstrategia() {
		assertEquals(TipoEstrategia.SECUENCIAL, estrategia.getTipoEstrategia());
	}
	
}
