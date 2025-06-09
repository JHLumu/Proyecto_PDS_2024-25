package umu.pds.modelo;


import java.net.MalformedURLException;
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
		
		/**
		 * Constructor privado para inicializar el catálogo de usuarios y cargar los usuarios desde la base de datos
		 */
		private CatalogoUsuarios() {
			this.usuarios = new HashMap<String,Usuario>();
			usuarioDAO  = FactoriaDAO.getInstancia(JPAFactoriaDAO.class.getName()).getUsuarioDAO();
			cargarCatalogo();		
		}
		
		/**
		 * Método estático para obtener la instancia única del catálogo de usuarios
		 * @return Instancia del catálogo de usuarios
		 */
		public static CatalogoUsuarios getInstancia() {return instancia;}
		
		/**
		 * Guarda un usuario ya registrado en la base de datos en el catálogo de usuarios 
		 * * @param usuario Usuario a guardar en el catálogo
		 * */
		public void nuevoUsuario(Usuario usuario) {this.usuarios.put(usuario.getEmail(), usuario);}
		
		 /**
	     * Busca un usuario por email
	     * @param email Email del usuario a buscar
	     * @return Un Optional que contiene el usuario si se encuentra, o vacío si no existe
	     */
	    public Optional<Usuario> buscarPorEmail(String email) {return Optional.ofNullable(this.usuarios.get(email));}
	    
	    /**
	     * Verifica si existe un email
	     * @param email Email a verificar
	     * @return true si el email existe en el catálogo de usuarios, false en caso contrario
	     */
	    public boolean existeEmail(String email) {return this.usuarios.containsKey(email);}
		
		/**
		 * actualiza un usuario en el catálogo de usuarios
		 * @param usuario Usuario a actualizar
		    */
	    public void actualizarUsuario(Usuario usuario) {
	    	if (usuario == null || !this.usuarios.containsKey(usuario.getEmail())) return;
	    	this.usuarios.put(usuario.getEmail(), usuario);
	  
	    }
	    
        
	    /**
	     * carga el catálogo de usuarios desde la base de datos
	     * 
	     */
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