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

@Entity
@Table(name = "cursos")
public class Curso {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	private String titulo;
	private String descripcion;
	private String dificultad;
	private String autor; //Nombre del autor que ha creado el curso
	
	// Relaciones
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
