package umu.pds.utils;


/**
 * Clase destinada a transportar la información recogida en el formulario de registro de un usuario ( Data Transfer Object).
 */
public class RegistroUsuarioDTO {
    private String nombre;
    private String apellidos;
    private String genero;
    private String email;
    private String password;
    private String confirmar;
    private String rutaImagenPerfil;

    private RegistroUsuarioDTO() {}

    public String getNombre() {
		return nombre;
	}

	public String getApellidos() {
		return apellidos;
	}

	public String getGenero() {
		return genero;
	}

	public String getEmail() {
		return email;
	}

	public String getPassword() {
		return password;
	}

	public String getConfirmar() {
		return confirmar;
	}

	public String getRutaImagenPerfil() {
		return rutaImagenPerfil;
	}

	/**
	 * Clase estática destinada a la creación de instancias {@link RegistroUsuarioDTO} (Patrón Builder).
	 */
	public static class Builder {
        private final RegistroUsuarioDTO dto = new RegistroUsuarioDTO();

        public Builder nombre(String nombre) {
            dto.nombre = nombre;
            return this;
        }

        public Builder apellidos(String apellidos) {
            dto.apellidos = apellidos;
            return this;
        }

        public Builder genero(String genero) {
            dto.genero = genero;
            return this;
        }

        public Builder email(String email) {
            dto.email = email;
            return this;
        }

        public Builder password(String password) {
            dto.password = password;
            return this;
        }

        public Builder confirmar(String confirmar) {
            dto.confirmar = confirmar;
            return this;
        }

        public Builder rutaImagenPerfil(String ruta) {
            dto.rutaImagenPerfil = ruta;
            return this;
        }

        public RegistroUsuarioDTO build() {
            return dto;
        }
    }
}