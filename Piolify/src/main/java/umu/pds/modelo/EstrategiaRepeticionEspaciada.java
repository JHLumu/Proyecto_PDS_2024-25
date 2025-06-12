package umu.pds.modelo;

import java.util.ArrayList;
import java.util.List;

/** 
 * Implementación de {@link Estrategia} que, cada cierto número determinado de ejercicios, 
 * repeite un ejercicio anterior.
 *
 */
public class EstrategiaRepeticionEspaciada implements Estrategia{

	/**
	 * Constante que contiene el intervalo (número de ejercicios) entre un ejercicio y su repetición.
	 */
	private static final int INTERVALO_EJERCICIOS_POR_DEFECTO = 3;
	
	/**
	 * {@inheritDoc}
	 * <br> Para la estrategia de repetición espaciada, respeta el orden secuencial de los ejercicios, insertando un ejercicio ya visto
	 * <br> (empezando según el orden secuencial) cada {@code INTERVALO_EJERCICIOS_POR_DEFECTO}.
	 * 
	 */
	@Override
	public List<Ejercicio> ordenarEjercicios(List<Ejercicio> ejercicios) {
		if (ejercicios == null || ejercicios.isEmpty()) return List.of();
		int ultimoEjercicioRepetido = 0;
		int contador = 0;
		List<Ejercicio> listaConRepeticiones = new ArrayList<Ejercicio>();
	
		for (Ejercicio ejer : ejercicios) {
			listaConRepeticiones.add(ejer);
			contador++;
			if (contador == INTERVALO_EJERCICIOS_POR_DEFECTO+1) {
				listaConRepeticiones.add(ejercicios.get(ultimoEjercicioRepetido));
				ultimoEjercicioRepetido++;
				contador=0;
			}
		}
		return listaConRepeticiones;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TipoEstrategia getTipoEstrategia() {
		return TipoEstrategia.REPETICION_ESPACIADA;
	}

}
