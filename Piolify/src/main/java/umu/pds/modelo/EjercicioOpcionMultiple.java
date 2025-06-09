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
    
    
    /**
     * Constructor por defecto para crear un ejercicio de opción múltiple
     */
	public EjercicioOpcionMultiple() {
		super();
		this.setTipo(TipoEjercicio.OPCION_MULTIPLE);
	}
	
    /**
     * Constructor para crear un ejercicio de opción múltiple con un contenido y respuesta específicos
     * @param contenido Contenido del ejercicio
     * @param respuesta Respuesta esperada, que debe coincidir con una de las opciones
     */
	public EjercicioOpcionMultiple(String contenido, String respuesta) {
		super(contenido, respuesta);
	}
	
    /**
     * Obtiene las opciones del ejercicio de opción múltiple
     * @return Lista de opciones disponibles
     */
    public List<String> getOpciones() {
        if (opciones == null || opciones.isEmpty()) {
            return Arrays.asList();
        }
        return Arrays.asList(opciones.split("\\|"));
    }
    
    /**
     * Establece las opciones del ejercicio de opción múltiple
     * @param opciones Lista de opciones a establecer
     */
    public void setOpciones(List<String> opciones) {
        this.opciones = String.join("|", opciones);
    }

    /**
     * Establece las opciones del ejercicio de opción múltiple como un String separado por comas
     * @param opciones String con las opciones separadas por comas
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
     * Valida la respuesta del usuario comparándola con la respuesta esperada
     * @param respuestaUsuario Respuesta proporcionada por el usuario
     * @return true si la respuesta es correcta, false en caso contrario
     */
    @Override
    public boolean validarRespuesta(String respuestaUsuario) {
        return getRespuesta().equalsIgnoreCase(respuestaUsuario.trim());
    }
    

}
