package umu.pds.modelo;

import java.util.Date;
import jakarta.persistence.*;

/**
 * Versión simplificada de ProgresoBloque que maneja todo el progreso de forma
 * individual por bloque.
 */
@Entity
@Table(name = "progreso_bloques")
public class ProgresoBloque {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "usuario_id", nullable = false)
	private Usuario usuario;

	@ManyToOne
	@JoinColumn(name = "bloque_id", nullable = false)
	private Bloque bloque;

	/**
	 * Índice del ejercicio actual (0-based)
	 */
	private int indiceEjercicioActual;

	/**
	 * Número de ejercicios completados correctamente
	 */
	private int ejerciciosCompletados;

	/**
	 * Indica si el bloque está completado
	 */
	private boolean completado;

	/**
	 * Estrategia utilizada para este bloque
	 */
	@Enumerated(EnumType.STRING)
	private TipoEstrategia estrategiaUtilizada;

	/**
	 * Fecha de la última actividad
	 */
	@Temporal(TemporalType.TIMESTAMP)
	private Date ultimaActividad;

	/**
	 * Fecha de inicio del bloque
	 */
	@Temporal(TemporalType.TIMESTAMP)
	private Date fechaInicio;

	/**
	 * Constructor por defecto de la aplicación.
	 */
	public ProgresoBloque() {
		this.fechaInicio = new Date();
		this.ultimaActividad = new Date();
		this.indiceEjercicioActual = 0;
		this.ejerciciosCompletados = 0;
		this.completado = false;
	}

	public ProgresoBloque(Usuario usuario, Bloque bloque, TipoEstrategia estrategia) {
		this();
		this.usuario = usuario;
		this.bloque = bloque;
		this.estrategiaUtilizada = estrategia;
	}

	/**
	 * Avanza al siguiente ejercicio
	 */
	public void avanzarEjercicio() {
		this.indiceEjercicioActual++;
		this.ejerciciosCompletados++;
		this.ultimaActividad = new Date();

		// Verificar si el bloque está completado
		if (bloque != null && bloque.getEjercicios() != null) {
			if (ejerciciosCompletados >= bloque.getEjercicios().size()) {
				this.completado = true;
			}
		}
	}

	/**
	 * Actualiza la última actividad sin avanzar
	 */
	public void actualizarActividad() {
		this.ultimaActividad = new Date();
	}

	/**
	 * Reinicia el progreso del bloque
	 */
	public void reiniciar() {
		this.indiceEjercicioActual = 0;
		this.ejerciciosCompletados = 0;
		this.completado = false;
		this.ultimaActividad = new Date();
		this.fechaInicio = new Date();
	}

	/**
	 * Marca el bloque como completado
	 */
	public void marcarCompletado() {
		this.completado = true;
		this.ultimaActividad = new Date();
		if (bloque != null && bloque.getEjercicios() != null) {
			this.ejerciciosCompletados = bloque.getEjercicios().size();
			this.indiceEjercicioActual = this.ejerciciosCompletados;
		}
	}

	/**
	 * Calcula el porcentaje de progreso
	 */
	public double getPorcentajeCompletado() {
		if (bloque == null || bloque.getEjercicios() == null || bloque.getEjercicios().isEmpty()) {
			return 0.0;
		}

		int totalEjercicios = bloque.getEjercicios().size();
		return ((double) ejerciciosCompletados / totalEjercicios) * 100.0;
	}

	/**
	 * Obtiene el ejercicio actual basado en el índice
	 */
	public Ejercicio getEjercicioActual() {
		if (bloque == null || bloque.getEjercicios() == null
				|| indiceEjercicioActual >= bloque.getEjercicios().size()) {
			return null;
		}
		return bloque.getEjercicios().get(indiceEjercicioActual);
	}

	/**
	 * Verifica si puede continuar (hay más ejercicios)
	 */
	public boolean puedeAvanzar() {
		if (bloque == null || bloque.getEjercicios() == null) {
			return false;
		}
		return indiceEjercicioActual < bloque.getEjercicios().size() - 1;
	}

	// Getters y Setters básicos
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

	public Bloque getBloque() {
		return bloque;
	}

	public void setBloque(Bloque bloque) {
		this.bloque = bloque;
	}

	public int getIndiceEjercicioActual() {
		return indiceEjercicioActual;
	}

	public void setIndiceEjercicioActual(int indice) {
		this.indiceEjercicioActual = indice;
		this.ultimaActividad = new Date();
	}

	public int getEjerciciosCompletados() {
		return ejerciciosCompletados;
	}

	public void setEjerciciosCompletados(int completados) {
		this.ejerciciosCompletados = completados;
		this.ultimaActividad = new Date();
	}

	public boolean isCompletado() {
		return completado;
	}

	public void setCompletado(boolean completado) {
		this.completado = completado;
	}

	public TipoEstrategia getEstrategiaUtilizada() {
		return estrategiaUtilizada;
	}

	public void setEstrategiaUtilizada(TipoEstrategia estrategia) {
		this.estrategiaUtilizada = estrategia;
	}

	public Date getUltimaActividad() {
		return ultimaActividad;
	}

	public void setUltimaActividad(Date fecha) {
		this.ultimaActividad = fecha;
	}

	public Date getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(Date fecha) {
		this.fechaInicio = fecha;
	}

	@Override
	public String toString() {
		return String.format("ProgresoBloque[usuario=%s, bloque=%s, progreso=%.1f%%, completado=%s]",
				usuario != null ? usuario.getNombre() : "null", bloque != null ? bloque.getTitulo() : "null",
				getPorcentajeCompletado(), completado);
	}
}