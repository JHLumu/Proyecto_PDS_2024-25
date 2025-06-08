package umu.pds.modelo;

import java.util.Collections;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;

@Entity
@Table(name = "bloques")
public class Bloque {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	private String titulo;
	private String descripcion;
	
	
    @OneToMany(mappedBy = "bloque", cascade = CascadeType.ALL)
    @OrderBy("orden ASC")
	private List<Ejercicio> ejercicios;
	

    @ManyToOne
    @JoinColumn(name = "curso_id", nullable = false)
    private Curso curso;
    
    private int orden; // Orden del bloque dentro del curso
    
	
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
	
	public int getOrden() {
		return this.orden;
	}
	
	public void setOrden(int orden) {
		this.orden = orden;
	}

	public Curso getCurso() 
	{return this.curso;
	}
	
	public void setCurso(Curso curso) {
		this.curso = curso;
	}
	
	
}
