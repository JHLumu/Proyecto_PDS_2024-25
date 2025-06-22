package umu.pds.persistencia;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

/**
 * Implementación de {@link FactoriaDAO} basada en JPA.
 */
public class JPAFactoriaDAO extends FactoriaDAO {

	 /**
	  * Instancia {@link EntityManagerFactory}, común para cada DAO definido por JPA.
	  */
    private static EntityManagerFactory emf;
    
	static {
		try {
			emf = Persistence.createEntityManagerFactory("Piolify");
		} catch (Throwable ex) {
			System.err.println("Initial EntityManagerFactory creation failed." + ex);
			throw new ExceptionInInitializerError(ex);
		}
	}
	

	@Override
	public UsuarioDAO getUsuarioDAO() {
		return new AdaptadorUsuarioDAO(emf);
	}

	@Override
	public CursoDAO getCursoDAO() {
		return new AdaptadorCursoDAO(emf);
	}
    
	@Override
	public AmistadDAO getAmistadDAO() {
		return new AdaptadorAmistadDAO(emf);
	}

	@Override
	public SesionAprendizajeDAO getSesionAprendizajeDAO() {
		return new AdaptadorSesionAprendizajeDAO(emf);
	}

	@Override
	public ProgresoBloqueDAO getProgresoBloqueDAO() {
		return new AdaptadorProgresoBloqueDAO(emf);
	}
	
}
