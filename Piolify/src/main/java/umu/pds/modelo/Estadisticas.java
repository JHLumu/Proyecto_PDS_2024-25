package umu.pds.modelo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "estadisticas")
public class Estadisticas {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private int tiempoTotal;
	private int rachaDias;
	private int mejorRacha;
	private int totalEjerciciosCompletados;
	private double precision; // podría ser calculable (?)
	
	@OneToOne
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
