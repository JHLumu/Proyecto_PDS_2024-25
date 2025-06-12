package umu.pds.servicios.importacion;

import java.util.List;


/**
 *  Clase destinada a transportar la información recogida en la importación de un curso ( Data Transfer Object).
 */
public class CursoDTO {
 private String titulo;
 private String descripcion;
 private String dificultad;
 private String autor;
 private List<BloqueDTO> bloques;
 
 // Constructores
 public CursoDTO() {}
 
 public CursoDTO(String titulo, String descripcion, String dificultad, String autor, List<BloqueDTO> bloques) {
     this.titulo = titulo;
     this.descripcion = descripcion;
     this.dificultad = dificultad;
     this.autor = autor;
     this.bloques = bloques;
 }
 
 // Getters y Setters
 public String getTitulo() { return titulo; }
 public void setTitulo(String titulo) { this.titulo = titulo; }
 
 public String getDescripcion() { return descripcion; }
 public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
 
 public String getDificultad() { return dificultad; }
 public void setDificultad(String dificultad) { this.dificultad = dificultad; }
 
 public String getAutor() { return autor; }
 public void setAutor(String autor) { this.autor = autor; }
 
 public List<BloqueDTO> getBloques() { return bloques; }
 public void setBloques(List<BloqueDTO> bloques) { this.bloques = bloques; }
}