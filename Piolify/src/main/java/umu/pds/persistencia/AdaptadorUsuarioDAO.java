package umu.pds.persistencia;

import umu.pds.modelo.Estadisticas;
import umu.pds.modelo.Usuario;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import java.util.List;


public class AdaptadorUsuarioDAO implements UsuarioDAO{
   
	private final EntityManagerFactory emf;
	
	public AdaptadorUsuarioDAO(EntityManagerFactory emf) {this.emf = emf;}
	
	/**
	     * Guarda un usuario en la base de datos
	     */
	    public void registrarUsuario(Usuario usuario) {
	        EntityManager em = emf.createEntityManager();
	        try {
	            em.getTransaction().begin();
	            em.persist(usuario);
	            em.getTransaction().commit();
	        } catch (Exception e) {
	            if (em.getTransaction().isActive()) {
	                em.getTransaction().rollback();
	            }
	            throw new RuntimeException("Error al guardar usuario: " + e.getMessage(), e);
	        } finally {
	            em.close();
	        }
	    }
	    
		/**
		 * Modifica un usuario en la base de datos
		 */
		public void modificarUsuario(Usuario usuario) {
			EntityManager em = emf.createEntityManager();
			try {
				em.getTransaction().begin();
				em.merge(usuario);
				em.getTransaction().commit();
			} catch (Exception e) {
				if (em.getTransaction().isActive()) {
					em.getTransaction().rollback();
				}
				throw new RuntimeException("Error al actualizar usuario: " + e.getMessage(), e);
			} finally {
				em.close();
			}
		}
	    
	    /**
	     * Busca un usuario por ID
	     */
		//TODO: Cargar usuarios en catálogo e implementar función en catálogo
	    public Usuario recuperarUsuario(Long id) {
	    	 EntityManager em = emf.createEntityManager();
	        try {
	            Usuario usuario = em.find(Usuario.class, id);
	            return usuario;
	        } finally {
	            em.close();
	        }
	    }
	    
		@Override
		public List<Usuario> recuperarTodosUsuarios() {
			
			EntityManager em = emf.createEntityManager();
			try {
				List<Usuario> lista = em.createQuery("SELECT u FROM Usuario u", Usuario.class).getResultList();
				return lista;
			}finally {
				em.close();
			}
		}
		
		/**
		 * Recupera las estadísticas de un usuario por su ID
		 * @param id El ID del usuario
		 * @return Las estadísticas del usuario, o null si no se encuentran
		 */
		@Override
		public Estadisticas recuperarEstadisticas(Long id) {
			EntityManager em = emf.createEntityManager();
			try {
				Estadisticas estadisticas = em.find(Estadisticas.class, id);
				return estadisticas;
			} finally {
				em.close();
			}
		}
	
	
}
