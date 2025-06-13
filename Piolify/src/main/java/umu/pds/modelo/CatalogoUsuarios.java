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

		//Instancia única de {@link CatalogoUsuarios} (Singleton).
		private static CatalogoUsuarios instancia = new CatalogoUsuarios();
		//Mapa que asocia correos electrónicos con las instancias {@link Usuario} correspondientes.
		private final Map<String,Usuario> usuarios;
		
		//Instancia {@link UsuarioDAO} para la persistencia de usuarios.
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
		 * Método que guarda un usuario ya registrado en la base de datos en el catálogo de usuarios. 
		 * * @param usuario Instancia {@link Usuario} a guardar en el catálogo.
		 * */
		public void nuevoUsuario(Usuario usuario) {this.usuarios.put(usuario.getEmail(), usuario);}
		
		 /**
	     * Método que busca un usuario por email.
	     * @param email Correo electrónico del usuario.
	     * @return {@link Optional} que contiene la instancia {@link Usuario} correspondiente.
	     */
	    public Optional<Usuario> buscarPorEmail(String email) {return Optional.ofNullable(this.usuarios.get(email));}
	    
	    /**
	     * Método que verifica si existe un email.
	     * @param email Correo electrónico a verificar.
	     * @return {@code true} si el email existe en el catálogo de usuarios, {@code false} en caso contrario.
	     */
	    public boolean existeEmail(String email) {return this.usuarios.containsKey(email);}
		
		/**
		 * Método que actualiza un usuario en el catálogo de usuarios.
		 * @param usuario Instancia {@link Usuario} a actualizar.
		    */
	    public void actualizarUsuario(Usuario usuario) {
	    	if (usuario == null || !this.usuarios.containsKey(usuario.getEmail())) return;
	    	this.usuarios.put(usuario.getEmail(), usuario);
	  
	    }
	    
        
	    /**
	     * Método que carga el catálogo de usuarios desde la base de datos.
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