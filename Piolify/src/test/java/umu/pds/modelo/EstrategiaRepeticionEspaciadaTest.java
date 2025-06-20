package umu.pds.modelo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class EstrategiaRepeticionEspaciadaTest {

private EstrategiaRepeticionEspaciada estrategia;
	
	private List<Ejercicio> ejercicios;
	
	@BeforeEach
	void setup() {
		ejercicios = new ArrayList<Ejercicio>();
		estrategia = (EstrategiaRepeticionEspaciada) EstrategiaFactory.crearEstrategia(TipoEstrategia.REPETICION_ESPACIADA);
	}
	
	@Test
	void testOrdenarEjercicios() {
		for (int i = 0; i < 8; i++) ejercicios.add(new EjercicioFlashcard());
		int[ ] orden = {0, 1, 2, 3, 0, 4, 5, 6, 7, 1};
		List<Ejercicio> ejerciciosOrdenados = estrategia.ordenarEjercicios(ejercicios);
		assertEquals(ejercicios.size()+(ejercicios.size()/3), ejerciciosOrdenados.size());
		for (int i = 0; i < ejerciciosOrdenados.size(); i++) {
			assertSame(ejercicios.get(orden[i]), ejerciciosOrdenados.get(i));
		}
		
	}
	
	@Test
	void testGetTipoEstrategia() {
		assertEquals(TipoEstrategia.REPETICION_ESPACIADA, estrategia.getTipoEstrategia());
	}
	
}
