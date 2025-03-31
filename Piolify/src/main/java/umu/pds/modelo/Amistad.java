package umu.pds.modelo;

import java.util.Date;

public class Amistad {
	
	private EstadoAmistad estado;
	private Usuario usuario1;
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
