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
	private String nombre;
	private String descripcion;
	private String imagen;
	private Date fecha;
	private TipoLogro tipo;
	
	@ManyToOne
	private Usuario usuario;
	
	public Logro(){
		
	}
	
	public Logro(TipoLogro tipo) {
		this.nombre = tipo.getNombre();
		this.descripcion = tipo.getDescripcion();
		this.fecha = new Date();
	}
	
	public Logro(String nombre, String descripcion, String imagen, Date fecha, TipoLogro tipo) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.imagen = imagen;
        this.fecha = fecha;
        this.tipo = tipo;
    }
	
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public String getImagen() {
		return imagen;
	}
	public void setImagen(String imagen) {
		this.imagen = imagen;
	}
	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	public TipoLogro getTipo() {
		return this.tipo;
	}
	
	
	

}
