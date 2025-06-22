package umu.pds.modelo;

/**
 * Enumerado que define los tipos de ejercicios disponibles en la aplicación.
 */
public enum TipoEjercicio {
	OPCION_MULTIPLE("OpcionMultipleRenderer"),
	RELLENAR_HUECOS("RellenarHuecosRenderer"),
	FLASHCARD("FlashcardRenderer");

	private final String clase;

	TipoEjercicio(String clase) {
		this.clase = clase;
	}

	public String getClase() {
		
		return this.clase;
	}
}