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

@Entity
@Table(name = "sesiones_aprendizaje")
public class SesionAprendizaje {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	private Usuario usuario;
	
	@ManyToOne
	private Curso curso;
	
	@ManyToOne
	private Ejercicio ejercicio; // Ejercicio específico realizado
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date fechaInicio;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date fechaFin;
	
	private int ejerciciosCompletados;
	private int aciertos;
	private int fallos;
	private int tiempoTotal; // en segundos
	private boolean completada; // Si la sesión fue completada exitosamente
	
	// Constructores
	public SesionAprendizaje() {
		this.fechaInicio = new Date();
		this.completada = false;
	}
	
	/**
	 * Constructor para crear una sesión de aprendizaje asociada a un usuario, curso y ejercicio.
	 * @param usuario Usuario que realiza la sesión
	 * @param curso Curso al que pertenece la sesión
	 * @param ejercicio Ejercicio específico realizado en la sesión
	 */
	public SesionAprendizaje(Usuario usuario, Curso curso, Ejercicio ejercicio) {
		this();
		this.usuario = usuario;
		this.curso = curso;
		this.ejercicio = ejercicio;
	}
	
	// Getters y Setters
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
	 * Inicia una nueva sesión de aprendizaje.
	 * Se establece la fecha de inicio y se reinician los contadores.
	 */
	public void finalizarSesion() {
		this.fechaFin = new Date();
		this.completada = true;
		calcularTiempoTotal();
	}

	/**
	 * Calcula el tiempo total de la sesión en segundos.
	 * Se llama al finalizar la sesión.
	 */
	private void calcularTiempoTotal() {
		if (fechaInicio != null && fechaFin != null) {
			this.tiempoTotal = (int) ((fechaFin.getTime() - fechaInicio.getTime()) / 1000);
		}
	}
	
	/**
	 * Registra un acierto en la sesión.
	 * Incrementa el contador de aciertos y ejercicios completados.
	 */
	public void registrarAcierto() {
		this.aciertos++;
		this.ejerciciosCompletados++;
	}

	/**
	 * Registra un fallo en la sesión.
	 * Incrementa el contador de fallos.
	 */
	public void registrarFallo() {
		this.fallos++;
	}
	
	/**
	 * Calcula el porcentaje de aciertos en la sesión.
	 * @return Porcentaje de aciertos (0.0 si no hay intentos).
	 */
	public double getPorcentajeAciertos() {
		int totalIntentos = aciertos + fallos;
		if (totalIntentos == 0) return 0.0;
		return (double) aciertos / totalIntentos * 100.0;
	}
}