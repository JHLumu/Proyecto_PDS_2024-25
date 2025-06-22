package umu.pds.persistencia;

import java.util.List;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import umu.pds.modelo.Curso;

/**
 * Adaptador que implementa {@link CursoDAO} mediante JPA.
 */
public class AdaptadorCursoDAO implements CursoDAO{
	
	/**
	 * Instancia {@link EntityManagerFactory} asociado, utilizado para crear {@link EntityManager}.
	 */
	private final EntityManagerFactory emf;
	
	/**
	 * Constructor por defecto.
	 * @param emf Instancia {@link EntityManagerFactory}.
	 */
	public AdaptadorCursoDAO(EntityManagerFactory emf) {this.emf = emf;}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void registrarCurso(Curso curso) {
		EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(curso);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Error al guardar curso: " + e.getMessage(), e);
        } finally {
            em.close();
        }
		
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Curso> recuperarTodosCursos() {
	    EntityManager em = emf.createEntityManager();
	    try {
	        return em.createQuery("SELECT DISTINCT c FROM Curso c", Curso.class)
		                 .getResultList();
		    } finally {
		        em.close();
		    }
		}
	
	/**
	 * {@inheritDoc}
	 */	
	@Override
	public Curso recuperarCurso(int id) {
	    EntityManager em = emf.createEntityManager();
	    try {
	        return em.find(Curso.class, id);
	    } catch (Exception e) {
	        return null;
	    } finally {
	        em.close();
	    }
	}

	    
}
