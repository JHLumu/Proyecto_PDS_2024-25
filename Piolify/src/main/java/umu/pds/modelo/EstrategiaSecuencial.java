package umu.pds.modelo;

import java.util.List;

public class EstrategiaSecuencial implements Estrategia{

	@Override
	public List<Ejercicio> ordenarEjercicios(List<Ejercicio> ejercicios) {
		//Al ser secuencial, se mantiene el orden de la lista dada.
		return ejercicios;
	}

	@Override
	public TipoEstrategia getTipoEstrategia() {
		return TipoEstrategia.SECUENCIAL;
	}

}
