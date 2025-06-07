package umu.pds.servicios.importacion;

import java.util.List;

public class BloqueDTO {
    private String titulo;
    private String descripcion;
    private int orden;
    private List<EjercicioDTO> ejercicios;
    
    // Constructors
    public BloqueDTO() {}
    
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