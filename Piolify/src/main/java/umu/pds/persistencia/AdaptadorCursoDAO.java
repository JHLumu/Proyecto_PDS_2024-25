package umu.pds.persistencia;

import java.util.List;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import umu.pds.modelo.Curso;

public class AdaptadorCursoDAO implements CursoDAO{

		private final EntityManagerFactory emf;
		public AdaptadorCursoDAO(EntityManagerFactory emf) {this.emf = emf;}
		
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
		
		@Override
		public Curso recuperarCurso(int id) {
			 EntityManager em = emf.createEntityManager();
		        try {
		            Curso curso = em.find(Curso.class, id);
		            return curso;
		        } finally {
		            em.close();
		        }
			
		} 
		
		@Override
		public List<Curso> recuperarTodosCursos() {
			
			EntityManager em = emf.createEntityManager();
			try {
				List<Curso> lista = em.createQuery("SELECT c FROM Curso c", Curso.class).getResultList();
				return lista;
			}finally {
				em.close();
			}
		}

	    
}
