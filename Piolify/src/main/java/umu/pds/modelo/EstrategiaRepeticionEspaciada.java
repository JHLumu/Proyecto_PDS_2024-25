package umu.pds.modelo;

import java.util.ArrayList;
import java.util.List;

public class EstrategiaRepeticionEspaciada implements Estrategia{

	//Intervalo de preguntas entre una pregunta y una repetición de esta.
	private static final int REPETICIONES_POR_DEFECTO = 2;
	
	@Override
	public List<Ejercicio> ordenarEjercicios(List<Ejercicio> ejercicios) {
		if (ejercicios == null || ejercicios.isEmpty()) return List.of();
		int ultimoEjercicioRepetido = 0;
		int contador = 0;
		List<Ejercicio> listaConRepeticiones = new ArrayList<Ejercicio>();
	
		for (Ejercicio ejer : ejercicios) {
			listaConRepeticiones.add(ejer);
			contador++;
			if (contador == REPETICIONES_POR_DEFECTO+1) {
				listaConRepeticiones.add(ejercicios.get(ultimoEjercicioRepetido));
				ultimoEjercicioRepetido++;
				contador=0;
			}
		}
		return listaConRepeticiones;
	}

	@Override
	public TipoEstrategia getTipoEstrategia() {
		return TipoEstrategia.REPETICION_ESPACIADA;
	}

}
