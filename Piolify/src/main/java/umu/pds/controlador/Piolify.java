package umu.pds.controlador;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import umu.pds.modelo.Estadisticas;
import umu.pds.modelo.Logro;
import umu.pds.modelo.Usuario;
import umu.pds.persistencia.UsuarioDAO;

public class Piolify {
    
	private Usuario usuarioActual;
    private final UsuarioDAO usuarioDAO;
    private static Piolify unicaInstancia = null;
    
    // patrón observer
    private List<Runnable> notificarCambiosUsuario = new ArrayList<>();
    
    public Piolify() {
        this.usuarioDAO = new UsuarioDAO();
    }
    
	public static Piolify getUnicaInstancia() {
		if (unicaInstancia == null) {
			unicaInstancia = new Piolify();
		}
		return unicaInstancia;
	}
	
    public boolean registrarUsuario(String nombre, String apellidos, String genero, String email, String password, String rutaImagenPefil) {
        if (usuarioDAO.existeEmail(email)) {
            throw new RuntimeException("El email ya está registrado");
        }
       
        Usuario usuario = new Usuario(nombre, apellidos, genero, email, password, rutaImagenPefil);
        usuarioDAO.guardar(usuario);
        return true;
    }
    

    public boolean iniciarSesion(String email, String password) {
        Optional<Usuario> usuarioOpt = usuarioDAO.buscarPorEmail(email);
        if (usuarioOpt.isPresent() && usuarioOpt.get().getPassword().equals(password)) {
            this.usuarioActual = usuarioOpt.get();
            return true;
        }
        return false;
    }
    
    public void añadirObservador(Runnable callback) {
    	notificarCambiosUsuario.add(callback);
    }
    
    public void borrarObservador(Runnable callback) {
    	notificarCambiosUsuario.remove(callback);
    }

    private void notificarCambiosUsuario() {
    	notificarCambiosUsuario.forEach(Runnable::run);
    }
    
    public Usuario obtenerUsuarioPorEmail(String email) {
        return usuarioDAO.buscarPorEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }
    
	public Usuario getUsuarioActual() {
		if (usuarioActual == null) {
			throw new RuntimeException("No hay usuario autenticado");
		}
		return usuarioActual;
	}

    public void modificarUsuario(Usuario usuario) {
        if (usuario == null || usuario.getEmail() == null) {
            throw new IllegalArgumentException("Usuario o email no pueden ser nulos");
        }
        
        // guardar en base de datos
        usuarioDAO.modificar(usuario);
        
        if (usuarioActual != null && usuarioActual.getEmail().equals(usuario.getEmail())) {
            this.usuarioActual = usuario;
            notificarCambiosUsuario(); //notificar a los observadores
        }
    }

	public void agregarLogro(Usuario usuario, Logro logro) {
		if (usuario == null || logro == null) {
			throw new IllegalArgumentException("Usuario o logro no pueden ser nulos");
		}

		usuario.getLogros().add(logro);
		usuarioDAO.modificar(usuario);
	}

	public Estadisticas obtenerEstadisticas(Usuario usuario) {
		if (usuario == null) {
			throw new IllegalArgumentException("Usuario no puede ser nulo");
		}

		return usuario.getEstadisticas();
		
	}
    
}