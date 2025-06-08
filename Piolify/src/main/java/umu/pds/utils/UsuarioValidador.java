package umu.pds.utils;

public class UsuarioValidador {
	
	public UsuarioValidador() {
	}

	
	
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

	public void validarLogin(String email, String password) {
		// TODO Auto-generated method stub
		if (email.isEmpty() || password.isEmpty()) {
			throw new IllegalArgumentException("Por favor, complete todos los campos.");
	    }
	}
}
