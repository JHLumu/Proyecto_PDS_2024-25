package umu.pds.modelo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "ejercicios")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_ejercicio", discriminatorType = DiscriminatorType.STRING)
public abstract class Ejercicio {
	
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	private final LocalDateTime fechaCreacion;
	private int dificultad;
	private String contenido;
	private String respuesta;
	
	private int orden; // Orden del ejercicio dentro del bloque
	
    @ManyToOne
    @JoinColumn(name = "bloque_id", nullable = false)
    private Bloque bloque;
    
	public Ejercicio() {
		this.fechaCreacion = LocalDateTime.now();
		this.dificultad = 5;
	}
	
	public Ejercicio(String contenido, String respuesta) {
		this.fechaCreacion = LocalDateTime.now();
		this.dificultad = 5;
		this.contenido = contenido;
		this.respuesta = respuesta;
	}
	
	public Long getId() {
		return this.id;
	}
	
	public LocalDateTime getFechaCreacion(){
		return this.fechaCreacion;
	}
	
	public int getDificultad() {
		return this.dificultad;
	}
	
	public void setDificultad(int dificultad) {
		this.dificultad = dificultad;
	}
	
	public String getContenido() {
		return this.contenido;
	}
	
	public String getRespuesta() {
		return this.respuesta;
	}
	
	public void setContenido(String contenido) {
		this.contenido = contenido;
	}
	
	public void setRespuesta(String respuesta) {
		this.respuesta = respuesta;
	}
	
	public abstract void renderEjercicio();
	
	public abstract boolean validarRespuesta(String respuestaUsuario);
	
	
    public TipoEjercicio getTipo() {
        if (this instanceof EjercicioOpcionMultiple) return TipoEjercicio.OPCION_MULTIPLE;
        if (this instanceof EjercicioRellenarHuecos) return TipoEjercicio.COMPLETAR_HUECOS;
        if (this instanceof EjercicioFlashcard) return TipoEjercicio.FLASHCARD;
        return null;
    }
    
}
