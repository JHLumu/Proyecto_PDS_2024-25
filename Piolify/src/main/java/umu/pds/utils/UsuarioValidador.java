package umu.pds.utils;

/**
 * Clase encargada de la validación de los datos de usuario en el registro
 * y en el inicio de sesion.
 */
public class UsuarioValidador {
	
	/**
	 * Método que valida los datos de registro de un usuario.
	 * @param dto Instancia {@link RegistroUsuarioDTO} que contiene los datos de registro.
	 */
	public void validarRegistro (RegistroUsuarioDTO dto){
		if (dto.getNombre().isEmpty() || dto.getApellidos().isEmpty() || dto.getEmail().isEmpty() || dto.getPassword().isEmpty() || dto.getConfirmar().isEmpty()) {
			throw new IllegalArgumentException("Por favor, rellene todos los campos.");
	    }

	    if (!dto.getPassword().equals(dto.getConfirmar())) {
	    	throw new IllegalArgumentException("Las contraseñas no coinciden.");
	    }
	    
	    if (dto.getGenero().equals("")) {
	    	throw new IllegalArgumentException("Seleccione un género.");
	    }
	    
	    
	    
	}
	/**
	 * Método que valida los datos de inicio de sesión de un usuario.
	 * @param email Correo electronico del usuario.
	 * @param password Contraseña del usuario.
	 * @throws IllegalArgumentException si el correo electrónico o el password están vacíos.
	 */
	public void validarLogin(String email, String password) {
		// TODO Auto-generated method stub
		if (email.isEmpty() || password.isEmpty()) {
			throw new IllegalArgumentException("Por favor, complete todos los campos.");
	    }
	}
}
