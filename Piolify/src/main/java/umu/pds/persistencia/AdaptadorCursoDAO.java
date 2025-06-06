package umu.pds.persistencia;

import jakarta.persistence.EntityManagerFactory;

public class AdaptadorCursoDAO implements CursoDAO{

		private final EntityManagerFactory emf;
		public AdaptadorCursoDAO(EntityManagerFactory emf) {this.emf = emf;} 
		
}
