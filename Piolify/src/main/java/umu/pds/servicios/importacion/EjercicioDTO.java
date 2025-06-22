package umu.pds.servicios.importacion;

import java.util.List;

/**
 *  Clase destinada a transportar la información recogida en la importación de un curso ( Data Transfer Object).
 */
public class EjercicioDTO {
    
    /**
     *  Tipo de ejercicio. Puede ser "OPCION_MULTIPLE", "COMPLETAR_HUECOS" o "FLASHCARD".
     */
    private String tipo;
    /**
     *  Contenido del ejercicio, que puede ser una pregunta, texto o imagen.
     */
    private String contenido;
    /**
     *  Respuesta esperada del ejercicio. Puede ser una respuesta única o una lista de respuestas separadas por comas.
     */
    private String respuesta;
    /**
     * Dificultad del ejercicio, representada por un número entero.
     */
    private int dificultad;
    /**
     * Orden del ejercicio dentro de un bloque o curso.
     */
    private int orden;
    /**
     *  Opciones específicas para ejercicios de opción múltiple.
     */
    private List<String> opciones;
    
    /**
     * Constructor por defecto para crear un ejercicio.
     */
    public EjercicioDTO() {}
    
    /**
     * Constructor para crear un ejercicio con tipo, contenido, respuesta, dificultad y orden.
     * @param tipo Tipo de ejercicio (OPCION_MULTIPLE, COMPLETAR_HUECOS, FLASHCARD).
     * @param contenido Contenido del ejercicio.
     * @param respuesta Respuesta esperada del ejercicio.
     * @param dificultad Dificultad del ejercicio.
     * @param orden Orden del ejercicio en el bloque o curso.
     */
    public EjercicioDTO(String tipo, String contenido, String respuesta, int dificultad, int orden) {
        this.tipo = tipo;
        this.contenido = contenido;
        this.respuesta = respuesta;
        this.dificultad = dificultad;
        this.orden = orden;
    }
    
    // Getters y Setters
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    
    public String getContenido() { return contenido; }
    public void setContenido(String contenido) { this.contenido = contenido; }
    
    public String getRespuesta() { return respuesta; }
    public void setRespuesta(String respuesta) { this.respuesta = respuesta; }
    
    public int getDificultad() { return dificultad; }
    public void setDificultad(int dificultad) { this.dificultad = dificultad; }
    
    public int getOrden() { return orden; }
    public void setOrden(int orden) { this.orden = orden; }
    
    public List<String> getOpciones() { return opciones; }

	public void setOpciones(List<String> opciones) {
	    this.opciones = opciones;
	}

    

    /**
     * Método que establece las propiedades específicas del ejercicio, como las opciones para ejercicios de opción múltiple.
     * @param propiedadesEspecificas Lista de opciones específicas del ejercicio.
     */
    public void setPropiedadesEspecificas(List<String> propiedadesEspecificas) { 
        this.opciones = propiedadesEspecificas; 
    }
}
