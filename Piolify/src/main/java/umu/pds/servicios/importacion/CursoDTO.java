package umu.pds.servicios.importacion;

import java.util.List;


/**
 *  Clase destinada a transportar la información recogida en la importación de un curso ( Data Transfer Object).
 */
public class CursoDTO {
	
	 /**
	  * Título del curso.
	  */
	 private String titulo;
	 
	 /**
	  * Descripción del curso.
	  */
	 private String descripcion;
	 
	 /*
	  * Nivel de dificultad asociada al curso.
	  */
	 private String dificultad;
	 
	 /**
	  * Autor del curso.
	  */
	 private String autor;
	 
	 /**
	  * Lista de bloques que contiene el curso.
	  */
	 private List<BloqueDTO> bloques;
	 
	 // Constructor por defecto.
	 public CursoDTO() {}
	 
	 /**
	  * Constructor para crear un curso con título, descripción, dificultad, autor y lista de bloques.
	  * @param titulo Título del curso.
	  * @param descripcion Descripción del curso.
	  * @param dificultad Dificultad del curso.
	  * @param autor Autor del curso.
	  * @param bloques Lista de bloques asociados al curso.
	  */
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