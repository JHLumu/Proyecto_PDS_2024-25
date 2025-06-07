package umu.pds.servicios.importacion;

public class ImportacionException extends Exception {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final String tipoError;
    private final String detalles;
    
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
    
    public String getTipoError() { return tipoError; }
    public String getDetalles() { return detalles; }
}
