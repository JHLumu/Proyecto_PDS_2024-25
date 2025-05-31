package umu.pds.controlador;

import java.util.Optional;

import umu.pds.modelo.Estadisticas;
import umu.pds.modelo.Logro;
import umu.pds.modelo.Usuario;
import umu.pds.persistencia.UsuarioDAO;

public class Piolify {
    
    private final UsuarioDAO usuarioDAO;
    
    public Piolify() {
        this.usuarioDAO = new UsuarioDAO();
    }
    
    public boolean registrarUsuario(String nombre, String apellidos, String genero, String email, String password, String rutaImagenPefil) {
        // Validar que el email no exista
        if (usuarioDAO.existeEmail(email)) {
            throw new RuntimeException("El email ya está registrado");
        }
        
        // Crear y guardar usuario
        Usuario usuario = new Usuario(nombre, apellidos, genero, email, password, rutaImagenPefil);
        usuarioDAO.guardar(usuario);
        return true;
    }
    

    public boolean autenticarUsuario(String email, String password) {
        Optional<Usuario> usuarioOpt = usuarioDAO.buscarPorEmail(email);
        return usuarioOpt.isPresent() && usuarioOpt.get().getPassword().equals(password);
    }

    public Usuario obtenerUsuarioPorEmail(String email) {
        return usuarioDAO.buscarPorEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }
    
}