package umu.pds.modelo;

import java.util.List;
import java.util.Map;

import umu.pds.utils.EjercicioRenderer;

/**Factoría encargada de instanciar las diferentes estrategias
 * de aprendizaje implementadas en la aplicación.
 * */
public class EstrategiaFactory {
	
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
	 *  @return una clase Estrategia del tipo que se ha solicitado.
	 */
	public static Estrategia crearEstrategia(TipoEstrategia tipo) {
		String className = "umu.pds.modelo." + tipo.getClase();
        try {
            Class<?> clazz = Class.forName(className);
            if (Estrategia.class.isAssignableFrom(clazz)) {
                return (Estrategia) clazz.getDeclaredConstructor().newInstance();
            } else {
                throw new IllegalArgumentException("Clase " + className + " no es una Estrategia válido.");
            }
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("No se encontró la clase para el tipo: " + tipo, e);
        } catch (Exception e) {
            throw new RuntimeException("Error al instanciar la clase para el tipo: " + tipo, e);
        }
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
