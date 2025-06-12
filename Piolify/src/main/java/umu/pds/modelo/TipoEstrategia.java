package umu.pds.modelo;
import java.util.Arrays;

/**
 * Enum que define los tipos de estrategias de aprendizaje disponibles.
 */
public enum TipoEstrategia {
	SECUENCIAL,
	REPETICION_ESPACIADA,
	ALEATORIA;

	/**
	 * Método encargado de la conversión de String a TipoEstrategia.
	 * @param nombre String que contiene el nombre de un tipo de estrategia.
	 * @return Instancia {@link TipoEstrategia} correspondiente.
	 */
	public static TipoEstrategia from(String nombre){
		String nombreFormateado = nombre.toUpperCase().replace(" ", "_");
		return Arrays.stream(values())
				.filter(t -> t.name().equals(nombreFormateado))
				.findFirst().get();
	}
	
	@Override
	public String toString() {
		String nombre = name().toLowerCase().replace("_", " ");
		return nombre.toUpperCase().charAt(0) + nombre.substring(1, nombre.length());
		
	}

}