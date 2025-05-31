package umu.pds.persistencia;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class JPAUtil {
    
    private static EntityManagerFactory emf;
    
	static {
		try {
			emf = Persistence.createEntityManagerFactory("Piolify");
		} catch (Throwable ex) {
			System.err.println("Initial EntityManagerFactory creation failed." + ex);
			throw new ExceptionInInitializerError(ex);
		}
	}
    
    public static EntityManager getEntityManager() {
        return emf.createEntityManager();
    }
    
    public static void cerrarEntityManager() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}
