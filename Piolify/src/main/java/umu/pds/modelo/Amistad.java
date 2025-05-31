package umu.pds.modelo;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "amistades")
public class Amistad {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private EstadoAmistad estado;
	@ManyToOne
	private Usuario usuario1;

	@ManyToOne
	private Usuario usuario2;
	private Date fecha;
	
	public EstadoAmistad getEstado() {
		return estado;
	}
	
	public void setEstado(EstadoAmistad estado) {
		this.estado = estado;
	}
	
	public Usuario getUsuario1() {
		return usuario1;
	}
	
	public void setUsuario1(Usuario usuario1) {
		this.usuario1 = usuario1;
	}
	
	public Usuario getUsuario2() {
		return usuario2;
	}
	
	public void setUsuario2(Usuario usuario2) {
		this.usuario2 = usuario2;
	}
	
	public Date getFecha() {
		return fecha;
	}
	
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	
	
}
