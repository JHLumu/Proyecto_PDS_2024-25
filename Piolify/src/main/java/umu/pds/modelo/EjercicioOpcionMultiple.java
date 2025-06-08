package umu.pds.modelo;

import java.util.Arrays;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("OPCION_MULTIPLE")
public class EjercicioOpcionMultiple extends Ejercicio {
	
    @Column(name = "opciones", columnDefinition = "TEXT")
    private String opciones; // Almacenar como JSON o separado por comas
    
    
	public EjercicioOpcionMultiple() {
		super();
		this.setTipo(TipoEjercicio.OPCION_MULTIPLE);
	}
	
	public EjercicioOpcionMultiple(String contenido, String respuesta) {
		super(contenido, respuesta);
	}
	
    public List<String> getOpciones() {
        if (opciones == null || opciones.isEmpty()) {
            return Arrays.asList();
        }
        return Arrays.asList(opciones.split("\\|"));
    }
    
    public void setOpciones(List<String> opciones) {
        this.opciones = String.join("|", opciones);
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
    

}
