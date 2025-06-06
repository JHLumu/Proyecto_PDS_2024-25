package umu.pds.servicios;

import java.util.Optional;

import umu.pds.modelo.CatalogoUsuarios;
import umu.pds.modelo.Estadisticas;
import umu.pds.modelo.Logro;
import umu.pds.modelo.Usuario;
import umu.pds.persistencia.JPAFactoriaDAO;
import umu.pds.persistencia.UsuarioDAO;
import umu.pds.utils.RegistroUsuarioDTO;

public class UsuarioService {
	
	private final CatalogoUsuarios catalogoUsuarios;
	private final UsuarioDAO repoUsuarios;
	
	public UsuarioService () {
		this.catalogoUsuarios = CatalogoUsuarios.getInstancia();
		this.repoUsuarios = JPAFactoriaDAO.getInstancia().getUsuarioDAO();
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

}
