package umu.pds.persistencia;

import umu.pds.modelo.Usuario;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import java.util.Optional;

public class UsuarioDAO {
    
    /**
     * Guarda un usuario en la base de datos
     */
    public Usuario guardar(Usuario usuario) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(usuario);
            em.getTransaction().commit();
            return usuario;
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
	public Usuario modificar(Usuario usuario) {
		EntityManager em = JPAUtil.getEntityManager();
		try {
			em.getTransaction().begin();
			Usuario usuarioActualizado = em.merge(usuario);
			em.getTransaction().commit();
			return usuarioActualizado;
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
    public Optional<Usuario> buscarPorId(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            Usuario usuario = em.find(Usuario.class, id);
            return Optional.ofNullable(usuario);
        } finally {
            em.close();
        }
    }
    
    /**
     * Busca un usuario por email
     */
    public Optional<Usuario> buscarPorEmail(String email) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            String jpql = "SELECT u FROM Usuario u WHERE u.email = :email";
            TypedQuery<Usuario> query = em.createQuery(jpql, Usuario.class);
            query.setParameter("email", email);
            return Optional.of(query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        } finally {
            em.close();
        }
    }
    
    /**
     * Verifica si existe un email
     */
    public boolean existeEmail(String email) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            String jpql = "SELECT COUNT(u) FROM Usuario u WHERE u.email = :email";
            TypedQuery<Long> query = em.createQuery(jpql, Long.class);
            query.setParameter("email", email);
            return query.getSingleResult() > 0;
        } finally {
            em.close();
        }
    }
}