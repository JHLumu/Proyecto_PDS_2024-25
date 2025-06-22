package umu.pds.modelo;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;

/**
 * Clase que representa un bloque de ejercicios dentro de un curso. Entidad persistente.
 */
@Entity
@Table(name = "bloques")
public class Bloque {
	
	/**
	 * Identificador único del bloque. Utilizado para persistencia.
	 */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
	 * Título del bloque.
	 */
	private String titulo;
	
	/**
	 * Descripción del bloque.
	 */
	private String descripcion;
	
	/**
	 * Lista de instancias {@link Ejercicio} pertenecientes al bloque, ordenados
	 * según el campo {@code orden} de {@link Ejercicio}, recuperados con la estrategia {@code EAGER} (al cargar 
	 * en memoria una instancia {@link Bloque}, tambien se cargan estas instancias {@link Ejercicio} asociadas).
	 * Relación uno a muchos: un bloque puede contener varios ejercicios.
	 */
    @OneToMany(mappedBy = "bloque", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @OrderBy("orden ASC")
	private List<Ejercicio> ejercicios;

    /**
     * Instancia {@link Curso} al que pertenece el bloque.
     * Relación muchos a uno: varios bloques pertenecen al mismo curso.
     */
    @ManyToOne
    @JoinColumn(name = "curso_id", nullable = false)
    private Curso curso;
    
	/**
	 * Orden del bloque dentro del curso, utilizado para definir la secuencia de
	 * bloques.
	 */
    private int orden;
	
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
	
	public List<Ejercicio> getEjercicios() {
		return Collections.unmodifiableList(ejercicios);
	}
	
	public void setEjercicios(List<Ejercicio> listaEjercicios) {
		this.ejercicios = listaEjercicios;
	}
	

	public Curso getCurso() 
	{return this.curso;
	}
	
	public void setCurso(Curso curso) {
		this.curso = curso;
	}

	public int getOrden() {
		return orden;
	}

	public void setOrden(int orden) {
		this.orden = orden;
	}

	@Override
	public int hashCode() {
		return Objects.hash(curso, descripcion, ejercicios, id, orden, titulo);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Bloque other = (Bloque) obj;
		return Objects.equals(curso, other.curso) && Objects.equals(descripcion, other.descripcion)
				&& Objects.equals(ejercicios, other.ejercicios) && Objects.equals(id, other.id) && orden == other.orden
				&& Objects.equals(titulo, other.titulo);
	}
	
	
}
