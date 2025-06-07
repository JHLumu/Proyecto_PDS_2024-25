package umu.pds.controlador;

import umu.pds.modelo.Usuario;
import umu.pds.servicios.UsuarioService;
import umu.pds.utils.RegistroUsuarioDTO;
import umu.pds.utils.UsuarioValidador;

public class UsuarioController {
	
	private Piolify controlador;
	private UsuarioService usuarioService;
	private UsuarioValidador usuarioValidador;
	
	public UsuarioController(Piolify piolify) {
		this.controlador = piolify;
		this.usuarioService = new UsuarioService();
		this.usuarioValidador = new UsuarioValidador();
	}
	
	public boolean iniciarSesion(String email, String password) {
		usuarioValidador.validarLogin(email, password);
		Usuario usuario = usuarioService.iniciarSesion(email, password);
		if(usuario != null) {
			controlador.setUsuarioActual(usuario);
			controlador.loginExitoso();
			return true;
		}
		return false;
	}
	
	public boolean registrarUsuario(RegistroUsuarioDTO dto) {
		
		usuarioValidador.validarRegistro(dto);
		
		boolean registrado = usuarioService.registrarUsuario(dto);
		if (registrado) {
			controlador.registroExitoso();
		} else {
			throw new RuntimeException("El email ya está registrado");
		}
		
		return registrado;
	}
	
	public void modificarUsuario(Usuario usuario) {
		boolean modificado = usuarioService.modificarUsuario(usuario);
		Usuario usuarioActual = controlador.getUsuarioActual();
		if(modificado) {
			if (usuarioActual != null && usuarioActual.getEmail().equals(usuario.getEmail())) {
	            controlador.setUsuarioActual(usuario);
	            controlador.notificarCambiosUsuario(); //notificar a los observadores
	        }
			
		} else {
			throw new IllegalArgumentException("Usuario o email no pueden ser nulos");
		}
		
	}

	public void actualizarUsuario(Usuario usuario, String nombre, String apellidos, String genero,
			String nuevaRutaImagen, String passwordNueva, boolean imagenCambiada, boolean cambiarPassword) {
		
		usuario.setNombre(nombre);
		usuario.setApellidos(apellidos);
		usuario.setGenero(genero);
		// TODO Auto-generated method stub
		if (imagenCambiada) {
			usuario.setImagenPerfil(nuevaRutaImagen);
		}

		if (cambiarPassword) {
			usuario.setPassword(passwordNueva);
		}
	}

	
	public Usuario buscarUsuarioPorEmail(String email) {
	    return usuarioService.buscarUsuarioPorEmail(email);
	}
	
	public boolean enviarSolicitudAmistad(String emailDestinatario) {
	    Usuario usuarioActual = controlador.getUsuarioActual();
	    Usuario destinatario = usuarioService.buscarUsuarioPorEmail(emailDestinatario);
	    
	    if (destinatario == null) {
	        throw new RuntimeException("Usuario no encontrado");
	    }
	    
	    boolean exito = usuarioService.enviarSolicitudAmistad(usuarioActual, destinatario);
	    
	    if (!exito) {
	        throw new RuntimeException("No se pudo enviar la solicitud. Puede que ya exista una relación.");
	    }
	    
	    return true;
	}

	public boolean procesarSolicitudAmistad(Long id, boolean aceptar) {
		Usuario usuarioActual = controlador.getUsuarioActual();
        return usuarioService.procesarSolicitudAmistad(id, usuarioActual, aceptar);
    }

	
}
