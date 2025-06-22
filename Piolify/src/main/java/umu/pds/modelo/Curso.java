package umu.pds.modelo;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;

/**
 * Clase que representa los cursos de aprendizaje. Entidad persistente.
 */
@Entity
@Table(name = "cursos")
public class Curso {
	
	/**
	 * Identificador único de un curso. Utilizado para persistencia.
	 */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * Título del curso.
     */
	private String titulo;
	
	/**
	 * Descripción del curso.
	 */
	private String descripcion;
	
	/**
	 * Nivel de dificultad asociada al curso.
	 */
	private String dificultad;
	
	/**
	 * Nombre del autor que ha creado el curso.
	 */
	private String autor;
	
	/**
	 * Lista de instancias {@link Bloque} que conforman el curso, ordenados según el campo {@code orden} de {@link Bloque}
	 * recuperados con la estrategia {@code EAGER} (al cargar en memoria una instancia {@link Curso}, también se cargan estas
	 * instancias {@link Bloque} asociadas).
	 * Relación uno a muchos: un curso puede contener varios bloques.<br>
	 * 
	 */
    @OneToMany(mappedBy = "curso", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @OrderBy("orden ASC")
    private List<Bloque> bloques = new ArrayList<>();
    
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getDificultad() {
		return dificultad;
	}

	public void setDificultad(String dificultad) {
		this.dificultad = dificultad;
	}


	public List<Bloque> getBloques() {
		return bloques;
	}

	public void setBloques(List<Bloque> bloques) {
		this.bloques = bloques;
	}

	public String getAutor() {
		return this.autor;
		}
	
	public void setAutor(String idAutor) {
		this.autor = idAutor;
	}
    	
	
}
