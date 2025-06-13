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

/**
 * Clase abstracta que representa los diferentes tipos de ejercicios (entidades persistentes) que puede contener un bloque. <br>
 * Al tratarse de una jerarquía de clases, se especifica mediante @JsonTypeInfo un atributo discriminante
 * para diferenciar subtipos a la hora de importar cursos.
 */
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
	
	public final static int DIFICULTAD_POR_DEFECTO = 5; 
	/**
	 * Identificador único del ejercicio. Utilizado para persistencia.
	 */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * Fecha y hora de la creación del ejercicio.
     */
	private final LocalDateTime fechaCreacion;
	
	/**
	 * Nivel de dificultad asociada al ejercicio.
	 */
	private int dificultad;
	
	/**
	 * Contenido del ejercicio, dependiendo del tipo de ejercicio que sea.
	 */
	private String contenido;
	
	/**
	 * Respuesta correcta esperada para el ejercicio. Varía según el tipo de ejercicio.
	 */
	private String respuesta;
	
	/**
	 * Orden del ejercicio dentro del bloque.
	 */
	private int orden;
	
	/**
	 * Tipo de ejercicio, representado mediante una constante {@link TipoEjercicio}. Atributo discriminante.
	 */
	private TipoEjercicio tipo;
	
	/**
	 * Instancia {@link Bloque} al que pertenece el ejercicio.<br>
	 * Relación muchos a uno: varios ejercicios pueden pertenecer a un mismo bloque.
	 */
    @ManyToOne
    @JoinColumn(name = "bloque_id", nullable = false)
    private Bloque bloque;
    
    /**
     * Constructor por defecto. Inicializa la fecha de creación y le asigna una dificultad por defecto ({@code DIFICULTAD_POR_DEFECTO}).
     */
	public Ejercicio() {
		this.fechaCreacion = LocalDateTime.now();
		this.dificultad = DIFICULTAD_POR_DEFECTO;
	}
	
	/**
	 * Constructor para crear un ejercicio con un contenido y respuesta específicos.
	 * @param contenido Contenido del ejercicio.
	 * @param respuesta Respuesta esperada.
	 */
	public Ejercicio(String contenido, String respuesta) {
		this.fechaCreacion = LocalDateTime.now();
		this.dificultad = DIFICULTAD_POR_DEFECTO;
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
	 * Método para renderizar el ejercicio.
	 * Cada tipo de ejercicio implementará su propia lógica de renderizado.
	 */
	public abstract void renderEjercicio();
	
	/**
	 * Método para validar la respuesta del usuario.
	 * Cada tipo de ejercicio implementará su propia lógica de validación.
	 * @param respuestaUsuario Respuesta proporcionada por el usuario.
	 * @return {@code true} si la respuesta es correcta, {@code false} en caso contrario.
	 */
	public abstract boolean validarRespuesta(String respuestaUsuario);
	
    public TipoEjercicio getTipo() {
    	return this.tipo;
    	}
    
    public void setTipo(TipoEjercicio tipo) {
    	this.tipo = tipo;
    }


}
