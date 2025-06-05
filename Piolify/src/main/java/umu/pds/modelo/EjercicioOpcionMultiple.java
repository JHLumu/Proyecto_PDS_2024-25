package umu.pds.modelo;

import java.util.Arrays;
import java.util.List;

import jakarta.persistence.Column;

public class EjercicioOpcionMultiple extends Ejercicio {
	
    @Column(name = "opciones", columnDefinition = "TEXT")
    private String opcionesJson; // Almacenar como JSON o separado por comas
    
    
	public EjercicioOpcionMultiple() {
		super();
	}
	
	public EjercicioOpcionMultiple(String contenido, String respuesta) {
		super(contenido, respuesta);
	}
	
    public List<String> getOpciones() {
        if (opcionesJson == null || opcionesJson.isEmpty()) {
            return Arrays.asList();
        }
        return Arrays.asList(opcionesJson.split("\\|"));
    }
    
    public void setOpciones(List<String> opciones) {
        this.opcionesJson = String.join("|", opciones);
    }

    @Override
    public void renderEjercicio() {
        System.out.println("Pregunta: " + getContenido());
        List<String> opciones = getOpciones();
        for (int i = 0; i < opciones.size(); i++) {
            System.out.println((char)('A' + i) + ") " + opciones.get(i));
        }
    }

    @Override
    public boolean validarRespuesta(String respuestaUsuario) {
        return getRespuesta().equalsIgnoreCase(respuestaUsuario.trim());
    }
    
    @Override
    public TipoEjercicio getTipo() {
    	return TipoEjercicio.OPCION_MULTIPLE;

    }

}
