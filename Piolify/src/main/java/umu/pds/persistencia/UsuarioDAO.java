package umu.pds.persistencia;

import java.net.MalformedURLException;
import java.util.List;
import umu.pds.modelo.Usuario;;

public interface UsuarioDAO {

	public void registrarUsuario(Usuario usuario);
	public void modificarUsuario(Usuario usuario);
	public Usuario recuperarUsuario(Long id) throws MalformedURLException;
	public List<Usuario> recuperarTodosUsuarios() throws MalformedURLException;
	
}
