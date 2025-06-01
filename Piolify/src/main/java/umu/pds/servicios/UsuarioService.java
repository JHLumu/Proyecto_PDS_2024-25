package umu.pds.servicios;

import java.util.Optional;
import umu.pds.modelo.Estadisticas;
import umu.pds.modelo.Logro;
import umu.pds.modelo.Usuario;
import umu.pds.persistencia.UsuarioDAO;
import umu.pds.utils.RegistroUsuarioDTO;

public class UsuarioService {
	
	private UsuarioDAO repo;
	
	public UsuarioService () {
		this.repo = new UsuarioDAO();
	}
	
	public boolean registrarUsuario(RegistroUsuarioDTO dto) {
        if (repo.existeEmail(dto.getEmail())) {
        	return false;
            
        } 
        Usuario usuario = new Usuario(dto.getNombre(), dto.getApellidos(), dto.getGenero(), dto.getEmail(), dto.getPassword(), dto.getRutaImagenPerfil());
        repo.guardar(usuario);
        return true;
    }
    

    public Usuario iniciarSesion(String email, String password) {
        Optional<Usuario> usuarioOpt = repo.buscarPorEmail(email);
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
        repo.modificar(usuario);
        return true;   
    }
    
    public Usuario obtenerUsuarioPorEmail(String email) {
        return repo.buscarPorEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }
    
    public void agregarLogro(Usuario usuario, Logro logro) {
		if (usuario == null || logro == null) {
			throw new IllegalArgumentException("Usuario o logro no pueden ser nulos");
		}

		usuario.getLogros().add(logro);
		repo.modificar(usuario);
	}

	public Estadisticas obtenerEstadisticas(Usuario usuario) {
		if (usuario == null) {
			throw new IllegalArgumentException("Usuario no puede ser nulo");
		}

		return usuario.getEstadisticas();
		
	}

}
