package umu.pds.modelo;

public class Usuario {
	
	private String nombre;
	private String apellidos;
	private String genero;
	private String email;
	private String password;
	private String imagenPerfil;
	
	public Usuario(String nombre, String apellidos, String genero, String email, String password) {
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.genero = genero;
		this.email = email;
		this.password = password;
		this.imagenPerfil = "/fotoUser.png"; // foto por defecto
	}
	
	public Usuario(String nombre, String apellidos, String genero, String email, String password, String imagenPerfil) {
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.genero = genero;
		this.email = email;
		this.password = password;
		this.imagenPerfil = imagenPerfil;
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
	
	public String getImagenPerfil() {
		return imagenPerfil;
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
	
	public void setImagenPerfil(String imagenPerfil) {
		this.imagenPerfil = imagenPerfil;
	}
	
	
}
