package umu.pds.modelo;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * Clase que representa un logro definido en el sistema, obtenible por usuarios. Entidad persistente.
 */
@Entity
@Table(name = "logros")
public class Logro {
	
	/**
	 * Identificador único de logro. Utilizado para persistencia.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	/**
	 * Fecha de obtención del logro por parte de un usuario.
	 */
	private Date fecha;
	
	/**
	 * Logro en específico, definido por una constante {@link TipoLogro}.
	 */
	private TipoLogro tipo;
	
	/**
	 * Instancia {@link Usuario} que ha obtenido el logro. <br>
	 * Relación muchos a uno: Varios logros pueden haber sido obtenidos por el mismo usuario.
	 */
	@ManyToOne
	private Usuario usuario;
	
	
	/**
	 * Constructor necesario para JPA.
	 */
	public Logro(){}
	
	/**
	 * Constructor que crea una instancia {@link Logro } según el tipo de logro, asociado a un usuario.
	 * @param usuario Instancia {@link Usuario} asociada al logro.
	 * @param tipo Constante {@link TipoLogro} que define el logro en específico.
	 */
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
