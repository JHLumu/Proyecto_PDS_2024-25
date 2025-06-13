package umu.pds.modelo;

import java.util.List;
import java.util.Map;

/**Factoría encargada de instanciar las diferentes estrategias
 * de aprendizaje implementadas en la aplicación.
 * */
public class EstrategiaFactory {
	
	/**
	 * Map que almacena para constante {@link TipoEstrategia} definida, una instancia de su implementación 
	 * por defecto. Utilizado para el método crearEstrategia(). 
	 */
	private static final Map<TipoEstrategia,Estrategia> ESTRATEGIAS_DEFINIDAS = Map.of(
			TipoEstrategia.SECUENCIAL,new EstrategiaSecuencial(),
			TipoEstrategia.ALEATORIA, new EstrategiaAleatoria(),
			TipoEstrategia.REPETICION_ESPACIADA, new EstrategiaRepeticionEspaciada()
			);
	
	/**
	 * Método estático para obtener los tipos de estrategias definidos en el sistema.
	 * @returns Lista de las constantes actuales del enumerado {@link TipoEstrategia}.
	 * */
	public static List<TipoEstrategia> getEstrategiasDefinidas(){
		return List.of(TipoEstrategia.values());
	} 
	
	/**
	 * Método estático para la creación de una instancia Estrategia a partir
	 * de su tipo.
	 *  @param tipo Constante del enum {@link TipoEstrategia}.
	 */
	public static Estrategia crearEstrategia(TipoEstrategia tipo) {
		return ESTRATEGIAS_DEFINIDAS.get(tipo);
	}
	
	/**
	 * Método estático para la creación de una instancia Estrategia a partir
	 * de su tipo.
	 *  @param tipo String que contiene el nombre del tipo de estrategia.
	 */
	public static Estrategia crearEstrategia(String tipo){
		return crearEstrategia(TipoEstrategia.from(tipo));
	}
	
}
