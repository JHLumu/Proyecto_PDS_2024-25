package umu.pds.modelo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

/**
 * Clase que representa a un usuario registrado en el sistema. Entidad persistente.
 */
@Entity
@Table(name = "usuarios")
public class Usuario {
	
	/**
	 * Identificador único del usuario. Utilizado para persistencia.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	/**
	 * Nombre real del usuario.
	 */
	private String nombre;
	
	/**
	 * Apellidos del usuario.
	 */
	private String apellidos;
	
	/**
	 * Genero del usuario. Sólo puede ser Hombre o Mujer.
	 */
	private String genero;
	
	/**
	 * Correo electrónico del usuario. Único para cada usuario.
	 */
	private String email;
	
	/**
	 * Contraseña del usuario.
	 */
	private String password;
	
	/**
	 * Ruta de la imagen de perfil del usuario.
	 */
	private String imagenPerfil;
	
	/**
	 * Lista de instancias {@link Amistad} enviadas por el usuario
	 * ,recuperados con la estrategia {@code LAZY} (al cargar en memoria una instancia {@link Usuario}, se cargarán en memoria
	 * estas instancias {@link Amistad} asociadas sólo cuando sea necesario).
	 * Relación uno a muchos: un usuario puede enviar varias solicitudes de amistad.
	 */
	@OneToMany(mappedBy = "usuario1", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Set<Amistad> amistadesEnviadas = new HashSet<>();

	/**
	 * Lista de instancias {@link Amistad} recibidas por el usuario, recuperados con la estrategia {@code LAZY}
	 * (al cargar en memoria una instancia {@link Usuario}, se cargarán en memoria
	 * estas instancias {@link Amistad} asociadas sólo cuando sea necesario).
	 * Relacíón uno a muchos: un usuario puede recibir varias solicitudes de amistad. 
	 */
	@OneToMany(mappedBy = "usuario2", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Set<Amistad> amistadesRecibidas = new HashSet<>();

    /**
     * Lista de instancias {@link Logro} desbloqueados por el usuario, recuperados con la estrategia
     * {@code EAGER} (al cargar en memoria una instancia {@link Usuario}, se cargarán también estas
     * instancias {@link Logro} asociadas). 
     * Relación uno a muchos: un usuario puede tener desbloqueado varios logros.
     */
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Logro> logros = new ArrayList<>();
   
    /**
     * Instancia {@link Estadisticas} asociadas al usuario, recuperado con la estrategia {@code EAGER} 
     * (al cargar una instancia {@link Usuario}, se carga también su instancia {@link Estrategia} asociada).
     * Relación uno a uno.
     */
	@OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
	private Estadisticas estadisticas;
	
	/**
	 * Lista de instancias {@link Curso} que el usuario tiene en su biblioteca interna, recuperados con la 
	 * estrategia {@code EAGER} (al cargar una instancia {@link Usuario}, se cargan también estas instancias
	 * {@link Curso} asociadas.
	 */
    @ManyToMany(fetch = FetchType.EAGER)
    private List<Curso> biblioteca = new ArrayList<>(); 

	/**
	 * Lista de instancias {@link ProgresoBloque} asociadas al usuario, recuperados
	 * con la estrategia {@code LAZY} (al cargar en memoria una instancia
	 * {@link Usuario}, se cargarán en memoria estas instancias
	 * {@link ProgresoBloque} asociadas sólo cuando sea necesario)*/
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ProgresoBloque> progresosBloques = new ArrayList<>();
    
    /**
     * Constructor necesario para JPA.
     */
    public Usuario() {

    }

	/**
	 * Constructor para crear un usuario con todos los campos necesarios.
	 * 
	 * @param nombre        Nombre del usuario
	 * @param apellidos     Apellidos del usuario
	 * @param genero        Género del usuario
	 * @param email         Email del usuario
	 * @param password      Contraseña del usuario
	 * @param imagenPerfil  URL de la imagen de perfil del usuario
	 */
	public Usuario(String nombre, String apellidos, String genero, String email, String password, String imagenPerfil) {
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.genero = genero;
		this.email = email;
		this.password = password;
		this.imagenPerfil = imagenPerfil;
	}


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellidos() {
		return apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public String getGenero() {
		return genero;
	}

	public void setGenero(String genero) {
		this.genero = genero;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getImagenPerfil() {
		return imagenPerfil;
	}

	public void setImagenPerfil(String imagenPerfil) {
		this.imagenPerfil = imagenPerfil;
	}

	public List<Logro> getLogros() {
		return logros;
	}

	public void setLogros(List<Logro> logros) {
		this.logros = logros;
	}

	public Estadisticas getEstadisticas() {
		return estadisticas;
	}

	public void setEstadisticas(Estadisticas estadisticas) {
		this.estadisticas = estadisticas;
	}

	public List<Curso> getBiblioteca() {
		return biblioteca;
	}

	public void setBiblioteca(List<Curso> biblioteca) {
		this.biblioteca = biblioteca;
	}
    

	public Set<Amistad> getAmistadesEnviadas() {
		return amistadesEnviadas;
	}
	
	public void setAmistadesEnviadas(Set<Amistad> amistadesEnviadas) {
		this.amistadesEnviadas = amistadesEnviadas;
	}
	
	public Set<Amistad> getAmistadesRecibidas() {
		return amistadesRecibidas;
	}
	
	public void setAmistadesRecibidas(Set<Amistad> amistadesRecibidas) {
		this.amistadesRecibidas = amistadesRecibidas;
	}
	
	public List<ProgresoBloque> getProgresosBloques() {
		return progresosBloques;
	}
	
	public void setProgresosBloques(List<ProgresoBloque> progresosBloques) {
		this.progresosBloques = progresosBloques;
	}
	
	/**
	 * Devuelve una lista de amigos para este usuario.
	 * <p>
	 * Este método recopila todos los usuarios que tienen una amistad aceptada (EstadoAmistad.ACEPTADA)
	 * con este usuario, ya sea a través de solicitudes enviadas o recibidas.
	 * </p>
	 *
	 * @return una lista de objetos {@link Usuario} que representan los amigos del usuario.
	 */
	public List<Usuario> getAmigos() {
	    List<Usuario> amigos = new ArrayList<>();
	    
	    // Amigos de solicitudes enviadas que fueron aceptadas
	    for (Amistad amistad : amistadesEnviadas) {
	        if (amistad.getEstado() == EstadoAmistad.ACEPTADA) {
	            amigos.add(amistad.getUsuario2());
	        }
	    }
	    
	    // Amigos de solicitudes recibidas que fueron aceptadas
	    for (Amistad amistad : amistadesRecibidas) {
	        if (amistad.getEstado() == EstadoAmistad.ACEPTADA) {
	            amigos.add(amistad.getUsuario1());
	        }
	    }
	    
	    return amigos;
	}

	/**
	 * Devuelve una lista de solicitudes de amistad pendientes recibidas por este usuario.
	 * <p>
	 * Este método filtra las amistades recibidas para devolver solo aquellas que están en estado
	 * PENDIENTE.
	 * </p>
	 *
	 * @return una lista de objetos {@link Amistad} que representan las solicitudes pendientes.
	 */
	public List<Amistad> getSolicitudesPendientes() {
	    return amistadesRecibidas.stream()
	        .filter(a -> a.getEstado() == EstadoAmistad.PENDIENTE)
	        .collect(Collectors.toList());
	}

	/**
	 * Devuelve una lista de solicitudes de amistad enviadas por este usuario que están pendientes.
	 * <p>
	 * Este método filtra las amistades enviadas para devolver solo aquellas que están en estado
	 * PENDIENTE.
	 * </p>
	 *
	 * @return una lista de objetos {@link Amistad} que representan las solicitudes enviadas pendientes.
	 */
	public List<Amistad> getSolicitudesEnviadas() {
	    return amistadesEnviadas.stream()
	        .filter(a -> a.getEstado() == EstadoAmistad.PENDIENTE)
	        .collect(Collectors.toList());
	}

	/**
	 * Método que comprueba si este usuario tiene un logro en específico desbloqueado.
	 * @param tipo Constante {@link TipoLogro} (logro en específico).
	 * @return {@code true} si el usuario tiene dicho logro desbloqueado, {@code false} en caso contrario.
	 */
	public boolean tieneLogroDesbloqueado(TipoLogro tipo) {
		
		return this.logros.stream().anyMatch(l -> l.getTipo().equals(tipo));
	}

	/**
	 * Método que desbloquea un logro en específico para este usuario.
	 * @param tipo Constante {@link TipoLogro} (logro en específico).
	 */
	public void desbloquearLogro(TipoLogro tipo) {
		Logro logro = new Logro( this, tipo);
		this.logros.add(logro);
	}
	

	/**
	 * Método que desbloquea un logro para este usuario.
	 * 
	 * @param logro Instancia {@link Logro} a desbloquear.
	 */
	public void desbloquearLogro(Logro logro) {
		this.logros.add(logro);
	}

	/**
	 * Método que devuelve el número de cursos empezados por el usuario
	 * @return Número de cursos en la biblioteca del usuario
	 */
	public int getCursosComenzados() {
		
		return this.biblioteca.size();
	}
	

}
