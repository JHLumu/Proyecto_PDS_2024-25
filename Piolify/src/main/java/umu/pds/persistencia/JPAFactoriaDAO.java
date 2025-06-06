package umu.pds.persistencia;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class JPAFactoriaDAO extends FactoriaDAO {

	 
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
    
  
	
}
