package umu.pds.modelo;


import java.net.MalformedURLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import umu.pds.persistencia.FactoriaDAO;
import umu.pds.persistencia.JPAFactoriaDAO;
import umu.pds.persistencia.UsuarioDAO;

public class CatalogoUsuarios {

		//Patron Singleton: Instancia única
		private static CatalogoUsuarios instancia = new CatalogoUsuarios();
		//Mapa que asocia correos electrónicos con usuarios
		private final Map<String,Usuario> usuarios;
		//Usuario DAO para la persistencia de usuarios
		private final UsuarioDAO usuarioDAO;
		

		private CatalogoUsuarios() {
			this.usuarios = new HashMap<String,Usuario>();
			usuarioDAO  = FactoriaDAO.getInstancia(JPAFactoriaDAO.class.getName()).getUsuarioDAO();
			cargarCatalogo();		
		}
		
		public static CatalogoUsuarios getInstancia() {return instancia;}
		
		/**
		 * Guarda un usuario ya registrado en la base de datos en el catálogo de usuarios 
		 * */
		public void nuevoUsuario(Usuario usuario) {this.usuarios.put(usuario.getEmail(), usuario);}
		
		 /**
	     * Busca un usuario por email
	     */
	    public Optional<Usuario> buscarPorEmail(String email) {return Optional.ofNullable(this.usuarios.get(email));}
	    
	    /**
	     * Verifica si existe un email
	     */
	    public boolean existeEmail(String email) {return this.usuarios.containsKey(email);}
		
	    public void actualizarUsuario(Usuario usuario) {
	    	if (usuario == null || !this.usuarios.containsKey(usuario.getEmail())) return;
	    	this.usuarios.put(usuario.getEmail(), usuario);
	  
	    }
	    
		private void cargarCatalogo() {
			try {
				List<Usuario> listaUsuarios = usuarioDAO.recuperarTodosUsuarios();
				for (Usuario usuario : listaUsuarios) {
					this.usuarios.put(usuario.getEmail(), usuario);
				}
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				System.err.println();
				e.printStackTrace();
			}
			
			
			}
		
}
