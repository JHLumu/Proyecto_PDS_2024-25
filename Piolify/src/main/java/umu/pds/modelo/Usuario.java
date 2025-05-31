package umu.pds.modelo;

import java.util.ArrayList;
import java.util.List;


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
    @OneToMany(mappedBy = "usuario1", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Amistad> amistadesSolicitadas = new ArrayList<>();
    
    @OneToMany(mappedBy = "usuario2", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Amistad> amistadesRecibidas = new ArrayList<>();
    
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

	public List<Amistad> getAmistadesSolicitadas() {
		return amistadesSolicitadas;
	}

	public void setAmistadesSolicitadas(List<Amistad> amistadesSolicitadas) {
		this.amistadesSolicitadas = amistadesSolicitadas;
	}

	public List<Amistad> getAmistadesRecibidas() {
		return amistadesRecibidas;
	}

	public void setAmistadesRecibidas(List<Amistad> amistadesRecibidas) {
		this.amistadesRecibidas = amistadesRecibidas;
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
    
	@Override
	public String toString() {
		return "Usuario [nombre=" + nombre + ", apellidos=" + apellidos + ", genero=" + genero + ", email=" + email
				+ ", password=" + password + ", imagenPerfil=" + imagenPerfil + "]";
	}


	
	
	
}
