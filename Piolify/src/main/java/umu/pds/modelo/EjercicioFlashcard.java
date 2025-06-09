package umu.pds.modelo;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("FLASHCARD")
public class EjercicioFlashcard extends Ejercicio {
	
	public EjercicioFlashcard() {
		super();
		this.setTipo(TipoEjercicio.FLASHCARD);
	}
	
	/**
	 * Constructor para crear un ejercicio flashcard con un contenido y respuesta específicos
	 * @param contenido Contenido de la flashcard
	 * @param respuesta Respuesta esperada
	 */
	public EjercicioFlashcard(String contenido, String respuesta) {
		super(contenido, respuesta);
		this.setTipo(TipoEjercicio.FLASHCARD);
	}
	
	@Override
	public void renderEjercicio() {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * Muestra el contenido de la flashcard y espera la respuesta del usuario.
	 */
	@Override
	public boolean validarRespuesta(String respuestaUsuario) {
		// TODO Auto-generated method stub
		return false;
		
	}
	
}
