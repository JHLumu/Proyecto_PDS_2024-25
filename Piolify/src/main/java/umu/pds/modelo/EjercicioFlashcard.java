package umu.pds.modelo;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

/**
 * Subclase de {@link Ejercicio} que representa un ejercicio de tipo Flashcard. Entidad persistente.
 */
@Entity
@DiscriminatorValue("FLASHCARD")
public class EjercicioFlashcard extends Ejercicio {
	
	public EjercicioFlashcard() {
		super();
		this.setTipo(TipoEjercicio.FLASHCARD);
	}
	
	/**
	 * Constructor para crear un ejercicio flashcard con un contenido y respuesta específicos
	 * @param contenido Contenido de la flashcard.
	 * @param respuesta Respuesta esperada.
	 */
	public EjercicioFlashcard(String contenido, String respuesta) {
		super(contenido, respuesta);
		this.setTipo(TipoEjercicio.FLASHCARD);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void renderEjercicio() {

	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean validarRespuesta(String respuestaUsuario) {
		return false;
	}
	
}
