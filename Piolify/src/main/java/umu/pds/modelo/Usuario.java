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

@Entity
@Table(name = "usuarios")
public class Usuario {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String nombre;
	private String apellidos;
	private String genero;
	private String email;
	private String password;
	private String imagenPerfil;
	
	// Relaciones
	@OneToMany(mappedBy = "usuario1", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Set<Amistad> amistadesEnviadas = new HashSet<>();

	@OneToMany(mappedBy = "usuario2", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Set<Amistad> amistadesRecibidas = new HashSet<>();

    
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Logro> logros = new ArrayList<>();
   
    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Estadisticas estadisticas;
	
    @OneToMany(mappedBy = "autor", fetch = FetchType.EAGER)
    private List<Curso> cursosCreados = new ArrayList<>();

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Curso> biblioteca = new ArrayList<>(); // Cursos que el usuario tiene en su biblioteca

    
    public Usuario() {
        
    }
    
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
    
	public List<Curso> getCursosCreados() {
		return cursosCreados;
	}
	
	public void setCursosCreados(List<Curso> cursosCreados) {
		this.cursosCreados = cursosCreados;
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

	public List<Amistad> getSolicitudesPendientes() {
	    return amistadesRecibidas.stream()
	        .filter(a -> a.getEstado() == EstadoAmistad.PENDIENTE)
	        .collect(Collectors.toList());
	}

	public List<Amistad> getSolicitudesEnviadas() {
	    return amistadesEnviadas.stream()
	        .filter(a -> a.getEstado() == EstadoAmistad.PENDIENTE)
	        .collect(Collectors.toList());
	}

	

}
