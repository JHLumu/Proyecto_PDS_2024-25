package umu.pds.modelo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Implementación de estrategia que ordena los ejercicios de un
 * bloque de manera aleatoria.
 * 
 * 
 */
public class EstrategiaAleatoria implements Estrategia {

	/**
	 * {@inheritDoc}
	 * <br> Para el tipo de estrategia aleatoria, copia la lista de ejercicios que recibe como parámetro y
	 * la reordena según el <br> algoritmo de Fisher-Yates.
	 */
	@Override
	public List<Ejercicio> ordenarEjercicios(List<Ejercicio> ejercicios) {
		List<Ejercicio> lista =  new ArrayList<Ejercicio>(ejercicios);
		Collections.shuffle(lista);
		return lista;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TipoEstrategia getTipoEstrategia() {
		return TipoEstrategia.ALEATORIA;
	}
	
	
	
}
