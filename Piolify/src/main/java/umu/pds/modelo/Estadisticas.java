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
	
	
}
