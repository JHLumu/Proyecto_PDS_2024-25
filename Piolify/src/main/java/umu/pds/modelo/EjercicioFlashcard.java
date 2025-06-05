package umu.pds.modelo;

public class EjercicioFlashcard extends Ejercicio {
	
	public EjercicioFlashcard() {
		super();
	}
	
	public EjercicioFlashcard(String contenido, String respuesta) {
		super(contenido, respuesta);
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
	
	@Override
	public TipoEjercicio getTipo() {
		return TipoEjercicio.FLASHCARD;
	}

}
