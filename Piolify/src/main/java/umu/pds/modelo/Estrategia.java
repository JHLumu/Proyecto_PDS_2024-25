package umu.pds.modelo;

import java.util.List;

/**
 * Interfaz utilizada para definir una estrategia de aprendizaje que el usuario puede elegir 
 * a la hora de realizar un curso. Cada implementación concreta debe proporcionar un algoritmo
 * para ordenar los ejercicios y una forma de identificar el tipo de estrategia aplicada.
 */
public interface Estrategia {
	/**
	 * Método que realiza la ordenación de los ejercicios
	 * según la lógica definida por la implementación
	 * de la estrategia.
	 * @param ejercicios Lista de ejercicios a ordenar.
	 * @return Lista ordenada de ejercicios según la estrategia.
	 */
	public List<Ejercicio> ordenarEjercicios(List<Ejercicio> ejercicios);
	
	/**
	 * Método que identifica el tipo de estrategia utilizada.
	 * @return {@link TipoEstrategia} que identifica a esta estrategia.
	 */
	public TipoEstrategia getTipoEstrategia();
	
}
