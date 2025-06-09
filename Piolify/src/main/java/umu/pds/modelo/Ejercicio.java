package umu.pds.modelo;

import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "ejercicios")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_ejercicio", discriminatorType = DiscriminatorType.STRING)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,include = JsonTypeInfo.As.PROPERTY,property="tipo",visible=true)

@JsonSubTypes({
	
	@JsonSubTypes.Type(name="OPCION_MULTIPLE",value=EjercicioOpcionMultiple.class),
	@JsonSubTypes.Type(name="COMPLETAR_HUECOS",value=EjercicioRellenarHuecos.class),
	@JsonSubTypes.Type(name="FLASHCARD",value=EjercicioFlashcard.class),
})
public abstract class Ejercicio {
	
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	private final LocalDateTime fechaCreacion;
	private int dificultad;
	private String contenido;
	private String respuesta;
	private int orden; // Orden del ejercicio dentro del bloque
	private TipoEjercicio tipo;
	
    @ManyToOne
    @JoinColumn(name = "bloque_id", nullable = false)
    private Bloque bloque;
    
	public Ejercicio() {
		this.fechaCreacion = LocalDateTime.now();
		this.dificultad = 5;
	}
	
	/**
	 * Constructor para crear un ejercicio con un contenido y respuesta específicos
	 * @param contenido Contenido del ejercicio
	 * @param respuesta Respuesta esperada
	 */
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
	
	public int getOrden() {
		return this.orden;
	}
	
	public void setOrden(int orden) {
		this.orden = orden;
	}
	
	public Bloque getBloque() {
		return this.bloque;
	}
	
	public void setBloque(Bloque bloque) {
		this.bloque = bloque;
	}
	
	/**
	 * Método abstracto para renderizar el ejercicio.
	 * Cada tipo de ejercicio implementará su propia lógica de renderizado.
	 */
	public abstract void renderEjercicio();
	
	/**
	 * Método abstracto para validar la respuesta del usuario.
	 * Cada tipo de ejercicio implementará su propia lógica de validación.
	 * @param respuestaUsuario Respuesta proporcionada por el usuario
	 * @return true si la respuesta es correcta, false en caso contrario
	 */
	public abstract boolean validarRespuesta(String respuestaUsuario);
	
    public TipoEjercicio getTipo() {
    	return this.tipo;
    	}
    
    public void setTipo(TipoEjercicio tipo) {
    	this.tipo = tipo;
    }


}
