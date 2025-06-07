package umu.pds.servicios.importacion;

import java.util.Map;

public class EjercicioDTO {
    private String tipo; // "OPCION_MULTIPLE", "COMPLETAR_HUECOS", "FLASHCARD"
    private String contenido;
    private String respuesta;
    private int dificultad;
    private int orden;
    private Map<String, Object> propiedadesEspecificas; // Para opciones múltiples, etc.
    
    // Constructors
    public EjercicioDTO() {}
    
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
    
    public Map<String, Object> getPropiedadesEspecificas() { return propiedadesEspecificas; }
    public void setPropiedadesEspecificas(Map<String, Object> propiedadesEspecificas) { 
        this.propiedadesEspecificas = propiedadesEspecificas; 
    }
}
