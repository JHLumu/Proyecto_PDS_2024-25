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
	
	public EjercicioFlashcard(String contenido, String respuesta) {
		super(contenido, respuesta);
		this.setTipo(TipoEjercicio.FLASHCARD);
	}
	
	@Override
	public void renderEjercicio() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean validarRespuesta(String respuestaUsuario) {
		// TODO Auto-generated method stub
		return false;
		
	}
	
}
