package umu.pds.persistencia;

import java.util.List;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import umu.pds.modelo.Curso;
import umu.pds.modelo.SesionAprendizaje;
import umu.pds.modelo.Usuario;

public class AdaptadorSesionAprendizajeDAO implements SesionAprendizajeDAO {

    private final EntityManagerFactory emf;

    public AdaptadorSesionAprendizajeDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public void guardarSesion(SesionAprendizaje sesion) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(sesion);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Error al guardar sesión: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }

    @Override
    public void actualizarSesion(SesionAprendizaje sesion) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(sesion);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Error al actualizar sesión: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }

    @Override
    public SesionAprendizaje buscarPorId(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(SesionAprendizaje.class, id);
        } finally {
            em.close();
        }
    }

    @Override
    public List<SesionAprendizaje> buscarSesionesPorUsuario(Usuario usuario) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<SesionAprendizaje> query = em.createQuery(
                "SELECT s FROM SesionAprendizaje s WHERE s.usuario = :usuario ORDER BY s.fechaInicio DESC", 
                SesionAprendizaje.class);
            query.setParameter("usuario", usuario);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<SesionAprendizaje> buscarSesionesPorUsuarioYCurso(Usuario usuario, Curso curso) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<SesionAprendizaje> query = em.createQuery(
                "SELECT s FROM SesionAprendizaje s WHERE s.usuario = :usuario AND s.curso = :curso ORDER BY s.fechaInicio DESC", 
                SesionAprendizaje.class);
            query.setParameter("usuario", usuario);
            query.setParameter("curso", curso);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<SesionAprendizaje> buscarSesionesCompletadas(Usuario usuario) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<SesionAprendizaje> query = em.createQuery(
                "SELECT s FROM SesionAprendizaje s WHERE s.usuario = :usuario AND s.completada = true ORDER BY s.fechaInicio DESC", 
                SesionAprendizaje.class);
            query.setParameter("usuario", usuario);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public void eliminarSesion(SesionAprendizaje sesion) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            SesionAprendizaje sesionAEliminar = em.find(SesionAprendizaje.class, sesion.getId());
            if (sesionAEliminar != null) {
                em.remove(sesionAEliminar);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Error al eliminar sesión: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }
}