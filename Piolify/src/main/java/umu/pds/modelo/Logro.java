package umu.pds.modelo;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "logros")
public class Logro {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Date fecha;
	private TipoLogro tipo;
	
	@ManyToOne
	private Usuario usuario;
	
	public Logro(){
		
	}
	
	public Logro(Usuario usuario, TipoLogro tipo) {
		this.usuario = usuario;
		this.fecha = new Date();
		this.tipo = tipo;
	}
	
	public String getNombre() {
		return this.tipo.getNombre();
	}

	public String getDescripcion() {
		return this.tipo.getDescripcion();
	}

	public String getImagen() {
		return this.tipo.getImagePath();
	}

	public Date getFecha() {
		return fecha;
	}

	public TipoLogro getTipo() {
		return this.tipo;
	}
	
	
	

}
