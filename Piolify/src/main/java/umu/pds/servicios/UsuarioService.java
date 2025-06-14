package umu.pds.servicios;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import umu.pds.modelo.Amistad;
import umu.pds.modelo.CatalogoUsuarios;
import umu.pds.modelo.Curso;
import umu.pds.modelo.Estadisticas;
import umu.pds.modelo.EstadoAmistad;
import umu.pds.modelo.Logro;
import umu.pds.modelo.Usuario;
import umu.pds.persistencia.AmistadDAO;
import umu.pds.persistencia.JPAFactoriaDAO;
import umu.pds.persistencia.UsuarioDAO;
import umu.pds.utils.RegistroUsuarioDTO;

/**
 * Clase de apoyo para los controladores, utilizado para la gestión 
 * de la persistencia de los usuarios tanto en la base de datos
 * como en memoria. <br>
 * 
 * Tambien se encarga de la recuperación de usuarios.
 */
public class UsuarioService {
	
	/**
	 * Instancia {@link CatalogoUsuarios} encargada del almacenamiento y recuperación de instancias tipo {@link Usuarios} en la memoria.
	 * Para más información, véase {@link CatalogoUsuarios}.
	 */
	private final CatalogoUsuarios catalogoUsuarios;
	
	/**
	 * Instancia {@link UsuarioDAO} encargada de la persistencia de instancias tipo {@link Usuarios} en la base de datos del sistema.
	 */
	private final UsuarioDAO repoUsuarios;
	
	/**
	 * Instancia {@link AmistadDAO} encargada de la persistencia de instancias tipo {@link Amistades} en la base de datos del sistema.
	 */
	private final AmistadDAO repoAmistades;

	
	/**
	 * Constructor por defecto. Obtiene las instancias {@link CatalogoUsuarios}, {@link UsuarioDAO} y {@link AmistadDAO}.
	 */
	public UsuarioService() {
		this.catalogoUsuarios = CatalogoUsuarios.getInstancia();
		this.repoUsuarios = JPAFactoriaDAO.getInstancia().getUsuarioDAO();
		this.repoAmistades = JPAFactoriaDAO.getInstancia().getAmistadDAO();
	}
	
	/**
	 * Constructor utilizado para Testing.
	 * @param catalogoUsuarios Instancia {@link CatalogoUsuarios}, ya sea una instancia real o un Mock Object.
	 * @param usuarioDAO Instancia {@link UsuarioDAO}, ya sea una instancia real o un Mock Object.
	 * @param amistadDAO Instancia {@link AmistadDAO}, ya sea una instancia real o un Mock Object.
	 */
	public UsuarioService(CatalogoUsuarios catalogoUsuarios, UsuarioDAO usuarioDAO, AmistadDAO amistadDAO) {
		this.catalogoUsuarios = catalogoUsuarios;
		this.repoUsuarios = usuarioDAO;
		this.repoAmistades = amistadDAO;
	}
	
	/**
	 * Método que registra un nuevo usuario en el sistema, tanto en el catálogo de usuarios como en la base de datos del sistema.
	 * @param dto Instancia {@link RegistroUsuarioDTO} que contiene la información necesaria para el registro del usuario.
	 * @return {@code true} si se ha registrado correctamente, {@code false} en caso contrario.
	 */
	public boolean registrarUsuario(RegistroUsuarioDTO dto) {
        if (catalogoUsuarios.existeEmail(dto.getEmail())) {
        	return false;
        } 
        Usuario usuario = new Usuario(dto.getNombre(), dto.getApellidos(), dto.getGenero(), dto.getEmail(), dto.getPassword(), dto.getRutaImagenPerfil());
        repoUsuarios.registrarUsuario(usuario);
        catalogoUsuarios.nuevoUsuario(usuario);
        return true;
    }
    
	/**
	 * Método que autentica el inicio de sesión de un usuario.
	 * @param email Correo electrónico del usuario.
	 * @param password Contraseña del usuario.
	 * @return {@code true} si las credenciales son válidas, {@code false} en caso contrario.
	 */
    public Usuario iniciarSesion(String email, String password) {
        Optional<Usuario> usuarioOpt = catalogoUsuarios.buscarPorEmail(email);
        if (usuarioOpt.isPresent() && usuarioOpt.get().getPassword().equals(password)) {
            return usuarioOpt.get();
        }
        return null;
    }
    
    /**
     * Método que actualiza a un usuario tanto en el catálogo de usuarios como en la base de datos del sistema.
     * @param usuario Instancia {@link Usuario} modificada a actualizar.
     * @return {@code true} si se ha actualizado correctamente, {@code false} si la instancia {@link Usuario} es igual a {@code null}.
     */
    public boolean modificarUsuario(Usuario usuario) {
        if (usuario == null || usuario.getEmail() == null) {
        	return false;
        }
        
        repoUsuarios.modificarUsuario(usuario);
        catalogoUsuarios.actualizarUsuario(usuario);
        return true;   
    }
    
    /**
     * Método que recupera un usuario por su correo electrónico.
     * @param email Correo electrónico del usuario a buscar.
     * @return Instancia {@link Usuario} correspondiente a dicho correo electrónico.
     * @throws RuntimeException si el correo electrónico dado no está asociado a ningún usuario registrado en el sistema.
     */
    public Usuario obtenerUsuarioPorEmail(String email) {
        return catalogoUsuarios.buscarPorEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }
    
    /**
     * Método que asocia un logro con el usuario que lo ha obtenido, persistiendo dicha asociación.
     * @param usuario Instancia {@link Usuario} que ha obtenido el logro.
     * @param logro Instancia {@link Logro} a añadir.
     */
    public void agregarLogro(Usuario usuario, Logro logro) {
		if (usuario == null || logro == null) throw new IllegalArgumentException("Usuario o logro no pueden ser nulos");
		usuario.getLogros().add(logro);
		this.modificarUsuario(usuario);
	}

    
    /**
     * Método que recupera las estádisticas de un usuario.
     * @param usuario Instancia {@link Usuario} del que recuperar sus estadísticas.
     * @return Instancia {@link Estadísticas} asociada con el usuario.
     * @throws IllegalArgumentException si instancia {@link Usuario} es igual a {@code null}.
     */
	public Estadisticas obtenerEstadisticas(Usuario usuario) {
		if (usuario == null) {
			throw new IllegalArgumentException("Usuario no puede ser nulo");
		}
		return usuario.getEstadisticas();
	}
	
	/**
	 * Método que envía una solicitud de amistad de un usuario a otro.
	 * @param solicitante Instancia {@link Usuario} que envía la solicitud.
	 * @param receptor Instancia {@link Usuario} que recibe la solicitud.
	 * @return {@code true} si la solicitud se ha enviado correctamente, {@code false} si tanto {@code solicitante} como {@code receptor} son iguales, o si ya existe amistad entre ellos.
	 */
	public boolean enviarSolicitudAmistad(Usuario solicitante, Usuario receptor) {
		if (solicitante.getId().equals(receptor.getId())) {
			return false;
		}
		if (repoAmistades.existeAmistad(solicitante, receptor)) {
			return false;
		}
		Amistad amistad = new Amistad(solicitante, receptor);
		repoAmistades.guardarAmistad(amistad);
		
		return true;
	}
	
	/**
	 * Método que procesa una solicitud de amistad recibida.
	 * @param solicitudId Identificador de la solicitud de amistad a procesar.
	 * @param receptor Instancia {@link Usuario} del usuario que ha recibido la solicitud.
	 * @param aceptar {@code true} si el usuario receptor ha aceptado la solicitud, {@code false} si la ha rechazado.
	 * @return {@code true} si se ha procesado correctamente, {@code false} en caso de que la solicitud sea igual a {@code null} <br>
	 * el estado de la solicitud sea distinto a {@code PENDIENTE}, o si emisor y receptor son iguales.
	 */
	public boolean procesarSolicitudAmistad(Long solicitudId, Usuario receptor, boolean aceptar) {
		Amistad solicitud = repoAmistades.buscarPorId(solicitudId);
		
		if (solicitud == null || 
			solicitud.getEstado() != EstadoAmistad.PENDIENTE || 
			!solicitud.getUsuario2().getId().equals(receptor.getId())) {
			return false;
		}
		EstadoAmistad nuevoEstado = aceptar ? EstadoAmistad.ACEPTADA : EstadoAmistad.RECHAZADA;
		solicitud.setEstado(nuevoEstado);
		repoAmistades.actualizarAmistad(solicitud);
		
		return true;
	}
	
	/**
	 * Método que recupera la lista de amigos de un usuario.
	 * @param usuario Instancia {@link Usuario} del que se recupera la lista de amigos.
	 * @return Lista de Instancias {@link Usuario} que mantienen una amistad con usuario.
	 */
	public List<Usuario> obtenerAmigos(Usuario usuario) {
	    if (repoAmistades == null) {
	        System.err.println("repoAmistades es null");
	        return new ArrayList<>();
	    }
	    
	    try {
	        List<Amistad> amistades = repoAmistades.buscarAmistades(usuario);
	        List<Usuario> amigos = new ArrayList<>();
	        
	        for (Amistad amistad : amistades) {
	            if (amistad.getUsuario1().getId().equals(usuario.getId())) {
	                amigos.add(amistad.getUsuario2());
	            } else {
	                amigos.add(amistad.getUsuario1());
	            }
	        }
	        
	        return amigos;
	    } catch (Exception e) {
	        System.err.println("Error al obtener amigos: " + e.getMessage());
	        e.printStackTrace();
	        return new ArrayList<>();
	    }
	}

	/**
	 * Método que recupera las solicitudes pendientes de un usuario.
	 * @param usuario Instancia {@link Usuario} del que se recupera la lista de solicitudes pendientes.
	 * @return Lista de Instancias {@link Amistad} en estado {@code PENDIENTE}.
	 */
	public List<Amistad> obtenerSolicitudesPendientes(Usuario usuario) {
	    if (repoAmistades == null) {
	        System.err.println("repoAmistades es null");
	        return new ArrayList<>();
	    }
	    
	    try {
	        List<Amistad> solicitudes = repoAmistades.buscarSolicitudesPendientes(usuario);
	        return solicitudes;
	    } catch (Exception e) {
	        System.err.println("Error al obtener solicitudes pendientes: " + e.getMessage());
	        e.printStackTrace();
	        return new ArrayList<>();
	    }
	}
	

	/**
	 * Método que limpia cursos duplicados de la biblioteca de un usuario
	 * @param usuario Usuario cuya biblioteca se quiere limpiar
	 * @return true si se encontraron y eliminaron duplicados
	 */
	public boolean limpiarCursosDuplicados(Usuario usuario) {
	    if (usuario == null || usuario.getBiblioteca() == null) {
	        return false;
	    }
	    
	    List<Curso> cursos = usuario.getBiblioteca();
	    Map<String, Curso> cursosUnicos = new LinkedHashMap<>();
	    
	    // Eliminar duplicados basándose en el título
	    for (Curso curso : cursos) {
	        if (curso != null && curso.getTitulo() != null) {
	            cursosUnicos.putIfAbsent(curso.getTitulo(), curso);
	        }
	    }
	    
	    List<Curso> cursosLimpios = new ArrayList<>(cursosUnicos.values());
	    
	    // Si había duplicados, actualizar
	    if (cursosLimpios.size() != cursos.size()) {
	        usuario.setBiblioteca(cursosLimpios);
	        modificarUsuario(usuario);
	        
	        System.out.println("Se eliminaron " + (cursos.size() - cursosLimpios.size()) + 
	                          " cursos duplicados de la biblioteca de " + usuario.getNombre());
	        return true;
	    }
	    
	    return false;
	}
}