package umu.pds.modelo;

import java.util.List;

/**
 * Implementación de estrategia por defecto. Mantiene el orden de los ejericios definido por el archivo JSON que define el curso.
 */
public class EstrategiaSecuencial implements Estrategia{

	
	/** 
	 * {@inheritDoc}
	 * 
	 * Para la implementación de estrategia secuencial. la lista de ejercicios ya está ordenada.
	 */
	@Override
	public List<Ejercicio> ordenarEjercicios(List<Ejercicio> ejercicios) {return ejercicios;}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TipoEstrategia getTipoEstrategia() {
		return TipoEstrategia.SECUENCIAL;
	}

}
