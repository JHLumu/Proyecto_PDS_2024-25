package umu.pds.modelo;

import java.util.Arrays;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

/**
* Subclase de {@link Ejercicio} que representa un ejercicio de tipo opción multiple. Entidad persistente.
*/
@Entity
@DiscriminatorValue("OPCION_MULTIPLE")
public class EjercicioOpcionMultiple extends Ejercicio {
	
	/**
	 * Opciones disponibles para este ejercicio, separada por comas.
	 */
    @Column(name = "opciones", columnDefinition = "TEXT")
    private String opciones; 
    
    /**
     * Constructor por defecto para crear un ejercicio de opción múltiple.
     */
	public EjercicioOpcionMultiple() {
		super();
		this.setTipo(TipoEjercicio.OPCION_MULTIPLE);
	}
	
    /**
     * Constructor para crear un ejercicio de opción múltiple con un contenido y respuesta específicos.
     * @param contenido Contenido del ejercicio.
     * @param respuesta Respuesta esperada, que debe coincidir con una de las opciones.
     */
	public EjercicioOpcionMultiple(String contenido, String respuesta) {
		super(contenido, respuesta);
	}
	
    /**
     * Método que obtiene las opciones del ejercicio de opción múltiple.
     * @return Lista de opciones disponibles.
     */
    public List<String> getOpciones() {
        if (opciones == null || opciones.isEmpty()) {
            return Arrays.asList();
        }
        return Arrays.asList(opciones.split("\\|"));
    }
    
    /**
     * Método que establece las opciones del ejercicio de opción múltiple.
     * @param opciones Lista de opciones a establecer.
     */
    public void setOpciones(List<String> opciones) {
        this.opciones = String.join("|", opciones);
    }

    /**
     * Método que establece las opciones del ejercicio de opción múltiple como un String separado por comas.
     * @param opciones String con las opciones separadas por comas.
     */
    @Override
    public void renderEjercicio() {
        System.out.println("Pregunta: " + getContenido());
        List<String> opciones = getOpciones();
        for (int i = 0; i < opciones.size(); i++) {
            System.out.println((char)('A' + i) + ") " + opciones.get(i));
        }
    }

    /** 
     * {@inheritDoc}
     * 
     */
    @Override
    public boolean validarRespuesta(String respuestaUsuario) {
        return getRespuesta().equalsIgnoreCase(respuestaUsuario.trim());
    }
    

}
