package umu.pds.servicios.importacion;

import java.util.List;


/**
 *  Clase destinada a transportar la información recogida en la importación de un curso ( Data Transfer Object).
 */
public class BloqueDTO {

    /**
     *  Título del bloque.
     */
    private String titulo;
    /** 
     * Descripción del bloque. 
     */
    private String descripcion;
    /**
     *  Orden del bloque en el curso.
     */
    private int orden;
    /**
     *  Lista de ejercicios asociados al bloque.
     */
    private List<EjercicioDTO> ejercicios;
    
    /**
     * Constructor por defecto para crear un bloque.
     */
    public BloqueDTO() {}
    
    /**
     * Constructor para crear un bloque con título, descripción, orden y lista de ejercicios.
     * @param titulo Título del bloque.
     * @param descripcion Descripción del bloque.
     * @param orden Orden del bloque en el curso.
     * @param ejercicios Lista de ejercicios asociados al bloque.
     */
    public BloqueDTO(String titulo, String descripcion, int orden, List<EjercicioDTO> ejercicios) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.orden = orden;
        this.ejercicios = ejercicios;
    }
    
    // Getters y Setters
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    
    public int getOrden() { return orden; }
    public void setOrden(int orden) { this.orden = orden; }
    
    public List<EjercicioDTO> getEjercicios() { return ejercicios; }
    public void setEjercicios(List<EjercicioDTO> ejercicios) { this.ejercicios = ejercicios; }
}