package umu.pds.modelo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EstrategiaAleatoria implements Estrategia {
	
	public EstrategiaAleatoria() {
	}

	@Override
	public List<Ejercicio> ordenarEjercicios(List<Ejercicio> ejercicios) {
		List<Ejercicio> lista =  new ArrayList<Ejercicio>(ejercicios);
		Collections.shuffle(lista);
		return lista;
	}

	@Override
	public TipoEstrategia getTipoEstrategia() {
		return TipoEstrategia.ALEATORIA;
	}
	
	
	
}
