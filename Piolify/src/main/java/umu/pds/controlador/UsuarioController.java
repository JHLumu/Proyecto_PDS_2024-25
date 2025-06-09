package umu.pds.controlador;

import umu.pds.modelo.Usuario;
import umu.pds.servicios.UsuarioService;
import umu.pds.utils.RegistroUsuarioDTO;
import umu.pds.utils.UsuarioValidador;

public class UsuarioController {
	
	private Piolify controlador;
	private UsuarioService usuarioService;
	private UsuarioValidador usuarioValidador;
	
	/**
	 * Constructor por defecto que inicializa el controlador y los servicios necesarios.
	 * @param piolify Instancia del controlador principal
	 */
	public UsuarioController(Piolify piolify) {
		this.controlador = piolify;
		this.usuarioService = new UsuarioService();
		this.usuarioValidador = new UsuarioValidador();
	}

	/**
	 * Constructor para inyección de dependencias
	 * @param piolify Instancia del controlador principal
	 * @param usuarioService Servicio de usuario
	 * @param usuarioValidador Validador de usuario
	 */
	public UsuarioController(Piolify piolify, UsuarioService usuarioService, UsuarioValidador usuarioValidador) {
		this.controlador = piolify;
		this.usuarioService = usuarioService;
		this.usuarioValidador = usuarioValidador;
	}
	
	/**
	 * Inicia sesión en el sistema.
	 * @param email Email del usuario
	 * @param password Contraseña del usuario
	 * @return true si la sesión se inició correctamente, false en caso contrario
	 */
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
	
	/**
	 * Registra un nuevo usuario en el sistema.
	 * @param dto Datos del usuario a registrar
	 * @return true si el registro fue exitoso, false en caso contrario
	 */
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
	
	/**
	 * Modifica los datos de un usuario.
	 * @param usuario Usuario a modificar
	 */
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

	/**
	 * Actualiza los datos de un usuario.
	 * @param usuario Usuario a actualizar
	 * @param nombre Nuevo nombre del usuario
	 * @param apellidos Nuevos apellidos del usuario
	 * @param genero Nuevo género del usuario
	 * @param nuevaRutaImagen Nueva ruta de la imagen de perfil
	 * @param passwordNueva Nueva contraseña del usuario
	 * @param imagenCambiada Indica si la imagen de perfil ha sido cambiada
	 * @param cambiarPassword Indica si la contraseña debe ser cambiada
	 */
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

	/**
	 * Busca un usuario por su email.
	 * @param email Email del usuario a buscar
	 * @return Usuario encontrado o null si no existe
	 */
	public Usuario buscarUsuarioPorEmail(String email) {
	    return usuarioService.buscarUsuarioPorEmail(email);
	}
	
	/**
	 * Envía una solicitud de amistad a otro usuario.
	 * @param emailDestinatario Email del usuario al que se le envía la solicitud
	 * @return true si la solicitud se envió correctamente, false en caso contrario
	 */
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

	/**
	 * Procesa una solicitud de amistad, aceptándola o rechazándola.
	 * @param id ID de la solicitud de amistad a procesar
	 * @param aceptar true para aceptar la solicitud, false para rechazarla
	 * @return true si la operación fue exitosa, false en caso contrario
	 */
	public boolean procesarSolicitudAmistad(Long id, boolean aceptar) {
		Usuario usuarioActual = controlador.getUsuarioActual();
        return usuarioService.procesarSolicitudAmistad(id, usuarioActual, aceptar);
    }

	
}