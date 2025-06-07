package umu.pds.servicios;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import umu.pds.modelo.Amistad;
import umu.pds.modelo.CatalogoUsuarios;
import umu.pds.modelo.Estadisticas;
import umu.pds.modelo.EstadoAmistad;
import umu.pds.modelo.Logro;
import umu.pds.modelo.Usuario;
import umu.pds.persistencia.AmistadDAO;
import umu.pds.persistencia.JPAFactoriaDAO;
import umu.pds.persistencia.UsuarioDAO;
import umu.pds.utils.RegistroUsuarioDTO;

public class UsuarioService {
	
	private final CatalogoUsuarios catalogoUsuarios;
	private final UsuarioDAO repoUsuarios;
	private final AmistadDAO repoAmistades;
	
	public UsuarioService () {
		this.catalogoUsuarios = CatalogoUsuarios.getInstancia();
		this.repoUsuarios = JPAFactoriaDAO.getInstancia().getUsuarioDAO();
		this.repoAmistades = JPAFactoriaDAO.getInstancia().getAmistadDAO();
	}
	
	public boolean registrarUsuario(RegistroUsuarioDTO dto) {
        if (catalogoUsuarios.existeEmail(dto.getEmail())) {
        	return false;
            
        } 
        Usuario usuario = new Usuario(dto.getNombre(), dto.getApellidos(), dto.getGenero(), dto.getEmail(), dto.getPassword(), dto.getRutaImagenPerfil());
        repoUsuarios.registrarUsuario(usuario);
        catalogoUsuarios.nuevoUsuario(usuario);
        return true;
    }
    

    public Usuario iniciarSesion(String email, String password) {
        Optional<Usuario> usuarioOpt = catalogoUsuarios.buscarPorEmail(email);
        if (usuarioOpt.isPresent() && usuarioOpt.get().getPassword().equals(password)) {
            return usuarioOpt.get();
        }
        return null;
    }
    
    public boolean modificarUsuario(Usuario usuario) {
        if (usuario == null || usuario.getEmail() == null) {
        	return false;
        }
        
        // guardar en base de datos
        repoUsuarios.modificarUsuario(usuario);
        catalogoUsuarios.actualizarUsuario(usuario);
        return true;   
    }
    
    public Usuario obtenerUsuarioPorEmail(String email) {
        return catalogoUsuarios.buscarPorEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }
    
    public void agregarLogro(Usuario usuario, Logro logro) {
		if (usuario == null || logro == null) {
			throw new IllegalArgumentException("Usuario o logro no pueden ser nulos");
		}

		usuario.getLogros().add(logro);
		repoUsuarios.modificarUsuario(usuario);
		catalogoUsuarios.actualizarUsuario(usuario);
		
	}

	public Estadisticas obtenerEstadisticas(Usuario usuario) {
		if (usuario == null) {
			throw new IllegalArgumentException("Usuario no puede ser nulo");
		}
		return usuario.getEstadisticas();
		
	}
	
	public Usuario buscarUsuarioPorEmail(String email) {
	    return catalogoUsuarios.buscarPorEmail(email).orElse(null);
	}
	
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
}
