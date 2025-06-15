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

    /**
     * Constructor privado para evitar la instanciación directa.
     * Utilizar el patrón Builder para crear instancias de esta clase.
     */
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
     * Clase Builder para construir instancias de RegistroUsuarioDTO.
     * Utiliza el patrón Builder para facilitar la creación de objetos inmutables.
     */
	public static class Builder {

        /**
         * Instancia de RegistroUsuarioDTO que se va a construir.
         */
        private final RegistroUsuarioDTO dto = new RegistroUsuarioDTO();
        /**
         * Método para establecer el nombre del usuario.
         * @param nombre Nombre del usuario.
         * @return Instancia del Builder para encadenar llamadas.
         */
        public Builder nombre(String nombre) {
            dto.nombre = nombre;
            return this;
        }
        /**
         * Método para establecer el nombre del usuario.
         * @param nombre Nombre del usuario.
         * @return Instancia del Builder para encadenar llamadas.
         */
        public Builder apellidos(String apellidos) {
            dto.apellidos = apellidos;
            return this;
        }
        /**
         * Método para establecer el género del usuario.
         * @param genero Género del usuario.
         * @return Instancia del Builder para encadenar llamadas.
         */
        public Builder genero(String genero) {
            dto.genero = genero;
            return this;
        }
        /**
         * Método para establecer el email del usuario.
         * @param email Email del usuario.
         * @return Instancia del Builder para encadenar llamadas.
         */
        public Builder email(String email) {
            dto.email = email;
            return this;
        }   
        /**
         * Método para establecer la contraseña del usuario.
         * @param password Contraseña del usuario.
         * @return Instancia del Builder para encadenar llamadas.
         */
        public Builder password(String password) {
            dto.password = password;
            return this;
        }
        /**
         * Método para establecer la confirmación de la contraseña del usuario.
         * @param confirmar Confirmación de la contraseña.
         * @return Instancia del Builder para encadenar llamadas.
         */
        public Builder confirmar(String confirmar) {
            dto.confirmar = confirmar;
            return this;
        }

        /**
         * Método para establecer la ruta de la imagen de perfil del usuario.
         * @param ruta Ruta de la imagen de perfil.
         * @return Instancia del Builder para encadenar llamadas.
         */
        public Builder rutaImagenPerfil(String ruta) {
            dto.rutaImagenPerfil = ruta;
            return this;
        }
        /**
         * Método para construir la instancia de RegistroUsuarioDTO.
         * @return Instancia de RegistroUsuarioDTO construida.
         */
        public RegistroUsuarioDTO build() {
            return dto;
        }
    }
}