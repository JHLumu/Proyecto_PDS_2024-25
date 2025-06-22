package umu.pds.persistencia;

import jakarta.persistence.*;
import umu.pds.modelo.*;
import java.util.List;
import java.util.Optional;

public class AdaptadorProgresoBloqueDAO implements ProgresoBloqueDAO {
    
    private final EntityManagerFactory emf;
    
    public AdaptadorProgresoBloqueDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }
    
    @Override
    public void guardarProgreso(ProgresoBloque progreso) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(progreso);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Error al guardar progreso: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }
    
    @Override
    public void actualizarProgreso(ProgresoBloque progreso) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(progreso);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Error al actualizar progreso: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }
    
    @Override
    public Optional<ProgresoBloque> buscarProgreso(Usuario usuario, Bloque bloque) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<ProgresoBloque> query = em.createQuery(
                "SELECT p FROM ProgresoBloque p WHERE p.usuario = :usuario AND p.bloque = :bloque", 
                ProgresoBloque.class);
            query.setParameter("usuario", usuario);
            query.setParameter("bloque", bloque);
            
            List<ProgresoBloque> resultados = query.getResultList();
            return resultados.isEmpty() ? Optional.empty() : Optional.of(resultados.get(0));
        } finally {
            em.close();
        }
    }
    
    @Override
    public List<ProgresoBloque> buscarProgresosPorCurso(Usuario usuario, Curso curso) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<ProgresoBloque> query = em.createQuery(
                "SELECT p FROM ProgresoBloque p WHERE p.usuario = :usuario AND p.bloque.curso = :curso", 
                ProgresoBloque.class);
            query.setParameter("usuario", usuario);
            query.setParameter("curso", curso);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    @Override
    public List<ProgresoBloque> buscarProgresosPorUsuario(Usuario usuario) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<ProgresoBloque> query = em.createQuery(
                "SELECT p FROM ProgresoBloque p WHERE p.usuario = :usuario ORDER BY p.ultimaActualizacion DESC", 
                ProgresoBloque.class);
            query.setParameter("usuario", usuario);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    @Override
    public void eliminarProgreso(ProgresoBloque progreso) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            ProgresoBloque progresoAEliminar = em.find(ProgresoBloque.class, progreso.getId());
            if (progresoAEliminar != null) {
                em.remove(progresoAEliminar);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Error al eliminar progreso: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }
    
    @Override
    public List<ProgresoBloque> buscarProgresosActivos(Usuario usuario) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<ProgresoBloque> query = em.createQuery(
                "SELECT p FROM ProgresoBloque p WHERE p.usuario = :usuario AND p.bloqueCompletado = false ORDER BY p.ultimaActualizacion DESC", 
                ProgresoBloque.class);
            query.setParameter("usuario", usuario);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
}