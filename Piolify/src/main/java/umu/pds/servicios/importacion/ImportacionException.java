package umu.pds.servicios.importacion;


/**
 * Excepción defnida para errores transcurridos durante la importación de un curso.
 */
public class ImportacionException extends Exception {

	private static final long serialVersionUID = 1L;

    /**
     * Tipo de error que se ha producido durante la importación.
     */
	private final String tipoError;
    /**
     * Detalles adicionales sobre el error, si los hay.
     */
    private String detalles;
    
    /**
     * Constructor por defecto para crear una excepción de importación.
     * @param mensaje Mensaje de error.
     * @param tipoError Tipo de error que se ha producido.
     */
    public ImportacionException(String mensaje, String tipoError) {
        super(mensaje);
        this.tipoError = tipoError;
        this.detalles = null;
    }
    
    /**
     * Constructor para crear una excepción de importación con detalles adicionales.
     * @param mensaje Mensaje de error.
     * @param tipoError Tipo de error que se ha producido.
     * @param detalles Detalles adicionales sobre el error.
     */
    public ImportacionException(String mensaje, String tipoError, String detalles, Throwable causa) {
        super(mensaje, causa);
        this.tipoError = tipoError;
        this.detalles = detalles;
    }
    
    /**
     * Constructor para crear una excepción de importación con un mensaje genérico.
     * @param string Mensaje de error genérico.
     */
    public ImportacionException(String string) {
		super(string);
		this.tipoError = new String("Error Importación");
		this.detalles = null;
	}

	public String getTipoError() { return tipoError; }
    public String getDetalles() { return detalles; }
    public void setDetalles(String detalles) {
    	this.detalles = detalles;
    }
}
