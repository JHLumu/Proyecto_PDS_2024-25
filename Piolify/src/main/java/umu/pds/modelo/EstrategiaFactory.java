package umu.pds.modelo;

import java.util.List;
import java.util.Map;


public class EstrategiaFactory {

	private static final Map<TipoEstrategia,Estrategia> ESTRATEGIAS_DEFINIDAS = Map.of(
			TipoEstrategia.SECUENCIAL,new EstrategiaSecuencial(),
			TipoEstrategia.ALEATORIA, new EstrategiaAleatoria(),
			TipoEstrategia.REPETICION_ESPACIADA, new EstrategiaRepeticionEspaciada()
			);
	
	
	public static List<TipoEstrategia> getEstrategiasDefinidas(){
		return List.of(TipoEstrategia.values());
	} 
	
	public static Estrategia crearEstrategia(TipoEstrategia nombre) {
		return ESTRATEGIAS_DEFINIDAS.get(nombre);
	}
	
	public static Estrategia crearEstrategia(String nombre){
		return crearEstrategia(TipoEstrategia.from(nombre));
	}
	
}
