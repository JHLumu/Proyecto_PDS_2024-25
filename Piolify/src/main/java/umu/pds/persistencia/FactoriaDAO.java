package umu.pds.persistencia;

/**
 * Factoría abstracta para la creación de los diferentes DAO (Data Access Object) del sistema.
 */
public abstract class FactoriaDAO {

	/**
	 * Instancía única de {@link FactoriaDAO} (Singleton).
	 */
	protected static FactoriaDAO instancia = null;
	
	/**
	 * Constructor por defecto.
	 */
	protected FactoriaDAO() {};
	
	/**
	 * Método que recupera la instancia de {@link FactoriaDAO} según el nombre de una factoría específica indicada.
	 * @param nombre Nombre de una subclase de {@link FactoriaDAO}.
	 * @return Instancia {@link FactoriaDAO}.
	 */
	public static FactoriaDAO getInstancia(String nombre)  {
		 if (instancia == null)
			try {
				instancia = (FactoriaDAO) Class.forName(nombre).getDeclaredConstructor().newInstance();
			} catch (Exception e) {
				e.printStackTrace();
			}
		 return instancia ;
		 }
	/**
	 * Método que devuelve la instancia {@link FactoriaDAO}
	 * @return instancia {@link FactoriaDAO} si ya ha sido inicializada, {@code null} en caso contrario.
	 */
	public static FactoriaDAO getInstancia() {return instancia;}
	public abstract UsuarioDAO getUsuarioDAO();
	public abstract CursoDAO getCursoDAO();
	public abstract AmistadDAO getAmistadDAO();
	public abstract SesionAprendizajeDAO getSesionAprendizajeDAO();
	
}