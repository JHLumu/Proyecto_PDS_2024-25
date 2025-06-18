package umu.pds.controlador;


import umu.pds.modelo.Estadisticas;
import umu.pds.modelo.TipoLogro;
import umu.pds.modelo.Usuario;
import umu.pds.servicios.UsuarioService;
import umu.pds.utils.RegistroUsuarioDTO;
import umu.pds.utils.UsuarioValidador;

/**
 * Controlador encargado del registro de un usuario en el sistema, la autenticación
 * de un usuario, la modificación de su información personales, la gestión de amistades
 * y la asignación de logros.
 */
public class UsuarioController {
	
	/**
	 * Instancia {@link Piolify}, utilizado para notificar cambios y asignar un usuario actual.
	 */
	private Piolify controlador;
	
	/**
	 * Instancia {@link UsuarioService}, servicio encargado de la persistencia de los usuarios. 
	 */
	private UsuarioService usuarioService;
	
	/**
	 * Instancia {@link UsuarioValidador} para la validación de los datos de usuario.
	 */
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
	 * Método que inicia sesión en el sistema dada unas credenciales.
	 * @param email Email del usuario.
	 * @param password Contraseña del usuario.
	 * @return {@code true} si la sesión se inició correctamente, {@code false} en caso contrario.
	 */
	public boolean iniciarSesion(String email, String password) {
		usuarioValidador.validarLogin(email, password);
		Usuario usuario = usuarioService.iniciarSesion(email, password);
		if(usuario != null) {
			//limpiar cursos duplicados al iniciar sesión
			usuarioService.limpiarCursosDuplicados(usuario);
			
			controlador.setUsuarioActual(usuario);
			controlador.loginExitoso();
			return true;
		}
		return false;
	}
	
	/**
	 * Método que registra un nuevo usuario en el sistema.
	 * @param dto Datos del usuario a registrar.
	 * @return {@code true} si el registro fue exitoso, {@code false} en caso contrario
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
	 * Método que persiste un usuario modificado.
	 * @param usuario Instancia {@link Usuario} con cambios pendientes de persistir.
	 * @throws IllegalArgumentException si {@code usuario} no ha sido modificado, es {@code null} o contiene campos nulos.
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
	 * Método que actualiza los datos de una Instancia {@link Usuario}.
	 * @param usuario Instancia {@link Usuario} a actualizar.
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
	 * Método que busca un usuario por su email.
	 * @param email Email del usuario a buscar.
	 * @return Instancia {@link Usuario} si existe, {@code null} en caso contrario.
	 */
	public Usuario buscarUsuarioPorEmail(String email) {
	    return usuarioService.obtenerUsuarioPorEmail(email);
	}
	
	/**
	 * Método que envía una solicitud de amistad a otro usuario.
	 * @param emailDestinatario Email del usuario al que se le envía la solicitud.
	 * @return {@code true} si la solicitud se envió correctamente, {@code false} en caso contrario.
	 */
	public boolean enviarSolicitudAmistad(String emailDestinatario) {
	    Usuario usuarioActual = controlador.getUsuarioActual();
	    Usuario destinatario = usuarioService.obtenerUsuarioPorEmail(emailDestinatario);
	    
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
	 * Método que procesa una solicitud de amistad, aceptándola o rechazándola.
	 * @param id ID de la solicitud de amistad a procesar.
	 * @param aceptar {@code true} para aceptar la solicitud, {@code false} para rechazarla.
	 * @return {@code true} si la operación fue exitosa, {@code false} en caso contrario.
	 */
	public boolean procesarSolicitudAmistad(Long id, boolean aceptar) {
		Usuario usuarioActual = controlador.getUsuarioActual();
        return usuarioService.procesarSolicitudAmistad(id, usuarioActual, aceptar);
    }
	
	/**
	 * Método que verifica y desbloquea automáticamente todos los logros que el usuario haya conseguido
	 * basándose en sus estadísticas actuales.
	 * 
	 * @param usuario Instancia {@link Usuario} para el que se verificarán sus logros sin desbloquear.
	 * @return Lista de logros desbloqueados en esta verificación.
	 */
	public void verificarYDesbloquearLogros(Usuario usuario) {
	    
	    Estadisticas estadisticas = usuario.getEstadisticas();
	    if (estadisticas == null) {
	        return;
	    }
	    
	    int cursosComenzados = usuario.getCursosComenzados();
	    
	    // Verificar cada tipo de logro
	    for (TipoLogro tipoLogro : TipoLogro.values()) {
	        // Solo verificar si el logro no está ya desbloqueado
	        if (!usuario.tieneLogroDesbloqueado(tipoLogro)) {
	            // Verificar si se cumple la condición para este logro
	            if (tipoLogro.seCumpleCondicion(estadisticas, cursosComenzados)) {
	                // Desbloquear el logro
	                usuario.desbloquearLogro(tipoLogro);

	            }
	        }
	    }
	    
	}
	
	public void desbloquearLogro(Usuario usuario, TipoLogro logro) {
		this.usuarioService.agregarLogro(usuario, logro);
	}

	
}