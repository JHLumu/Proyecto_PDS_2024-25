package umu.pds.servicios.importacion;

public class ImportacionException extends Exception {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final String tipoError;
    private String detalles;
    
    public ImportacionException(String mensaje, String tipoError) {
        super(mensaje);
        this.tipoError = tipoError;
        this.detalles = null;
    }
    
    public ImportacionException(String mensaje, String tipoError, String detalles, Throwable causa) {
        super(mensaje, causa);
        this.tipoError = tipoError;
        this.detalles = detalles;
    }
    
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
