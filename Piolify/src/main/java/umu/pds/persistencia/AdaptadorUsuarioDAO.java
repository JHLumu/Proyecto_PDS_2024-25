package umu.pds.persistencia;

import umu.pds.modelo.Estadisticas;
import umu.pds.modelo.Usuario;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import java.util.List;

/**
 * Adaptador que implementa {@link UsuarioDAO} mediante JPA.
 */
public class AdaptadorUsuarioDAO implements UsuarioDAO{
   
	/**
	 * Instancia {@link EntityManagerFactory} asociado, utilizado para crear {@link EntityManager}.
	 */
	private final EntityManagerFactory emf;
	
	/**
	 * Constructor por defecto.
	 * @param emf Instancia {@link EntityManagerFactory}.
	 */
	public AdaptadorUsuarioDAO(EntityManagerFactory emf) {this.emf = emf;}
	
	/**
     * {@inheritDoc}
     */
	@Override
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
     * {@inheritDoc}
     */
    @Override
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
     * {@inheritDoc}
     */
	@Override
    public Usuario recuperarUsuario(Long id) {
    	 EntityManager em = emf.createEntityManager();
        try {
            Usuario usuario = em.find(Usuario.class, id);
            return usuario;
        } finally {
            em.close();
        }
    }
    
    /**
     * {@inheritDoc}
     */
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
     * {@inheritDoc}
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
