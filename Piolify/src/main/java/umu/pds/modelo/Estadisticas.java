package umu.pds.modelo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

/**
 * Clase que representa las estadísticas asociadas a un usuario. Entidad persistente.
 */
@Entity
@Table(name = "estadisticas")
public class Estadisticas {
	
	/**
	 * Identificador único de las estadísticas. Utilizado para persistencia.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	/**
	 * Tiempo total, expresado en segundos, acumulado en todas las sesiones completadas por el usuario.
	 */
	private int tiempoTotal;
	
	/**
	 * Racha de días consecutivos en la que el usuario ha completado al menos una sesión.
	 */
	private int rachaDias;
	
	/**
	 * Mejor racha de días consecutivas en la que el usuario ha completado al menos una sesión.
	 */
	private int mejorRacha;
	
	/**
	 * Número total de ejercicios completados por el usuario.
	 */
	private int totalEjerciciosCompletados;
	
	/**
	 * Precisión de aciertos del usuario.
	 */
	private double precision;
	
	@OneToOne
	@JoinColumn(name = "usuario_id", unique = true)
	private Usuario usuario;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getTiempoTotal() {
		return tiempoTotal;
	}

	public void setTiempoTotal(int tiempoTotal) {
		this.tiempoTotal = tiempoTotal;
	}

	public int getRachaDias() {
		return rachaDias;
	}

	public void setRachaDias(int rachaDias) {
		this.rachaDias = rachaDias;
	}

	public int getMejorRacha() {
		return mejorRacha;
	}

	public void setMejorRacha(int mejorRacha) {
		this.mejorRacha = mejorRacha;
	}

	public int getTotalEjerciciosCompletados() {
		return totalEjerciciosCompletados;
	}

	public void setTotalEjerciciosCompletados(int totalEjerciciosCompletados) {
		this.totalEjerciciosCompletados = totalEjerciciosCompletados;
	}

	public double getPrecision() {
		return precision;
	}

	public void setPrecision(double precision) {
		this.precision = precision;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
	
}
