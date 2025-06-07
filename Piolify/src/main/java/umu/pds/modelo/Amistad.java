package umu.pds.modelo;

import java.util.Date;
import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "amistades")
public class Amistad {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Enumerated(EnumType.STRING)
	private EstadoAmistad estado;
	
	@ManyToOne
	@JoinColumn(name = "usuario_solicitante_id")
	private Usuario usuario1;
	
	@ManyToOne
	@JoinColumn(name = "usuario_receptor_id")
	private Usuario usuario2;
	private Date fecha;
	

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
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
	
	public Amistad() {
		// Constructor por defecto necesario para JPA
	}
	
    public Amistad(Usuario solicitante, Usuario receptor) {
        this.usuario1 = solicitante;
        this.usuario2 = receptor;
        this.estado = EstadoAmistad.PENDIENTE;
        this.fecha = new Date();
    }
	
	@Override
	public int hashCode() {
		return Objects.hash(estado, fecha, id, usuario1, usuario2);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Amistad other = (Amistad) obj;
		return estado == other.estado && Objects.equals(fecha, other.fecha) && Objects.equals(id, other.id)
				&& Objects.equals(usuario1, other.usuario1) && Objects.equals(usuario2, other.usuario2);
	}
	
	public Usuario getOtroUsuario(Usuario usuario) {
		if (usuario1.equals(usuario)) {
			return usuario2;
		} else if (usuario2.equals(usuario)) {
			return usuario1;
		}
		return null;
	}
	
	public boolean esSolicitante(Usuario usuario) {
		return usuario1.equals(usuario);
	}
	
	public boolean esReceptor(Usuario usuario) {
		return usuario2.equals(usuario);
	}

	
}
