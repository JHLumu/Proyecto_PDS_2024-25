package umu.pds.persistencia;

import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import umu.pds.modelo.Amistad;
import umu.pds.modelo.EstadoAmistad;
import umu.pds.modelo.Usuario;

public class AdaptadorAmistadDAO implements AmistadDAO {

    private final EntityManagerFactory emf;

    public AdaptadorAmistadDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public void guardarAmistad(Amistad amistad) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(amistad);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Error al guardar amistad: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }

    @Override
    public void actualizarAmistad(Amistad amistad) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(amistad);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Error al actualizar amistad: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }

    @Override
    public Amistad buscarPorId(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(Amistad.class, id);
        } finally {
            em.close();
        }
    }

    @Override
    public List<Amistad> buscarSolicitudesPendientes(Usuario receptor) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Amistad> query = em.createQuery(
                "SELECT a FROM Amistad a WHERE a.usuario2 = :receptor AND a.estado = :estado", 
                Amistad.class);
            query.setParameter("receptor", receptor);
            query.setParameter("estado", EstadoAmistad.PENDIENTE);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Amistad> buscarAmistades(Usuario usuario) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Amistad> query = em.createQuery(
                "SELECT a FROM Amistad a WHERE (a.usuario1 = :usuario OR a.usuario2 = :usuario) AND a.estado = :estado", 
                Amistad.class);
            query.setParameter("usuario", usuario);
            query.setParameter("estado", EstadoAmistad.ACEPTADA);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public boolean existeAmistad(Usuario usuario1, Usuario usuario2) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Long> query = em.createQuery(
                "SELECT COUNT(a) FROM Amistad a WHERE " +
                "(a.usuario1 = :u1 AND a.usuario2 = :u2) OR " +
                "(a.usuario1 = :u2 AND a.usuario2 = :u1)", 
                Long.class);
            query.setParameter("u1", usuario1);
            query.setParameter("u2", usuario2);
            return query.getSingleResult() > 0;
        } finally {
            em.close();
        }
    }

    @Override
    public void eliminarAmistad(Amistad amistad) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Amistad amistadAEliminar = em.find(Amistad.class, amistad.getId());
            if (amistadAEliminar != null) {
                em.remove(amistadAEliminar);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Error al eliminar amistad: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }
}