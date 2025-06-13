package umu.pds.modelo;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

/**
 * Clase que representa una sesión de aprendizaje que un usuario realiza en un curso en específico. Entidad persistente.
 */
@Entity
@Table(name = "sesiones_aprendizaje")
public class SesionAprendizaje {
	
	/**
	 * Identificador único de la sesión. Utilizado para persistencia.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	/**
	 * Instancia {@link Usuario} que realiza la sesión. <br>
	 * Relación muchos a uno: Varias sesiones pueden ser realizadas por el mismo usuario.
	 */
	@ManyToOne
	private Usuario usuario;
	
	/**
	 * Instancia {@link Curso} en el que se realiza la sesión. <br>
	 * Relación muchos a uno: Varias sesiones pueden realizarse en el mismo curso.
	 */
	@ManyToOne
	private Curso curso;
	
	/**
	 * Instancia {@link Ejercicio} que se refiere al primer ejercicio realizado durante la sesión.
	 * Relación muchos a uno: Varias sesiones pueden empezar por el mismo ejercicio.
	 */
	@ManyToOne
	private Ejercicio ejercicio;
	
	/**
	 * Fecha y hora en el que se inició la sesión de aprendizaje.
	 */
	@Temporal(TemporalType.TIMESTAMP)
	private Date fechaInicio;
	
	/**
	 *  Fecha y hora en el que se finalizó la sesión de aprendizaje.
	 */
	@Temporal(TemporalType.TIMESTAMP)
	private Date fechaFin;
	
	/**
	 * Número de ejercicios completados durante la sesión de aprendizaje.
	 */
	private int ejerciciosCompletados;
	
	/**
	 * Aciertos conseguidos durante la sesión de aprendizaje.
	 */
	private int aciertos;
	
	/**
	 * Fallos obtenidos durante la sesión de aprendizaje.
	 */
	private int fallos;
	
	/**
	 * Tiempo total que ha durado la sesión de aprendizaje.
	 */
	private int tiempoTotal; 
	
	/**
	 * Booleano que indica si la sesión de aprendizaje ha sido completada (se han respondido todos los ejercicios de la sesión).
	 */
	private boolean completada;
	
	/**
	 * Constructor por defecto.
	 */
	public SesionAprendizaje() {
		this.fechaInicio = new Date();
		this.completada = false;
	}
	
	/**
	 * Constructor para crear una sesión de aprendizaje asociada a un usuario, curso y ejercicio.
	 * @param usuario Instancia {@link Usuario} que realiza la sesión.
	 * @param curso Instancia {@link Curso} al que pertenece la sesión.
	 * @param ejercicio Instancia {@link Ejercicio} que empieza la sesión.
	 */
	public SesionAprendizaje(Usuario usuario, Curso curso, Ejercicio ejercicio) {
		this();
		this.usuario = usuario;
		this.curso = curso;
		this.ejercicio = ejercicio;
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public Usuario getUsuario() {
		return usuario;
	}
	
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
	public Curso getCurso() {
		return curso;
	}
	
	public void setCurso(Curso curso) {
		this.curso = curso;
	}
	
	public Ejercicio getEjercicio() {
		return ejercicio;
	}
	
	public void setEjercicio(Ejercicio ejercicio) {
		this.ejercicio = ejercicio;
	}
	
	public Date getFechaInicio() {
		return fechaInicio;
	}
	
	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}
	
	public Date getFechaFin() {
		return fechaFin;
	}
	
	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}
	
	public int getEjerciciosCompletados() {
		return ejerciciosCompletados;
	}
	
	public void setEjerciciosCompletados(int ejerciciosCompletados) {
		this.ejerciciosCompletados = ejerciciosCompletados;
	}
	
	public int getAciertos() {
		return aciertos;
	}
	
	public void setAciertos(int aciertos) {
		this.aciertos = aciertos;
	}
	
	public int getFallos() {
		return fallos;
	}
	
	public void setFallos(int fallos) {
		this.fallos = fallos;
	}
	
	public int getTiempoTotal() {
		return tiempoTotal;
	}
	
	public void setTiempoTotal(int tiempoTotal) {
		this.tiempoTotal = tiempoTotal;
	}
	
	public boolean isCompletada() {
		return completada;
	}
	
	public void setCompletada(boolean completada) {
		this.completada = completada;
	}
	
	/**
	 * Método que inicia una nueva sesión de aprendizaje.
	 * Se establece la fecha de inicio y se reinician los contadores.
	 */
	public void finalizarSesion() {
		this.fechaFin = new Date();
		this.completada = true;
		calcularTiempoTotal();
	}

	/**
	 * Método que calcula el tiempo total de la sesión en segundos.
	 * Se llama al finalizar la sesión.
	 */
	private void calcularTiempoTotal() {
		if (fechaInicio != null && fechaFin != null) {
			this.tiempoTotal = (int) ((fechaFin.getTime() - fechaInicio.getTime()) / 1000);
		}
	}
	
	/**
	 * Método que registra un acierto en la sesión.
	 * Incrementa el contador de aciertos y ejercicios completados.
	 */
	public void registrarAcierto() {
		this.aciertos++;
		this.ejerciciosCompletados++;
	}

	/**
	 * Método que registra un fallo en la sesión.
	 * Incrementa el contador de fallos.
	 */
	public void registrarFallo() {
		this.fallos++;
	}
	
	/**
	 * Método que calcula el porcentaje de aciertos en la sesión.
	 * @return Porcentaje de aciertos (0.0 si no hay intentos).
	 */
	public double getPorcentajeAciertos() {
		int totalIntentos = aciertos + fallos;
		if (totalIntentos == 0) return 0.0;
		return (double) aciertos / totalIntentos * 100.0;
	}
}