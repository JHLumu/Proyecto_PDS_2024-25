package umu.pds.servicios;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

import umu.pds.modelo.Curso;
import umu.pds.servicios.importacion.CursoDTO;
import umu.pds.servicios.importacion.CursoMapper;
import umu.pds.servicios.importacion.ImportacionException;
import umu.pds.servicios.importacion.ImportadorFactory;
import umu.pds.servicios.importacion.ImportadorStrategy;
import umu.pds.servicios.importacion.ValidadorCurso;
import umu.pds.servicios.importacion.ValidadorCurso.ResultadoValidacion;

/**
 * Clase de apoyo utilizado por {@link ImportacionController}, encargado para la importación de cursos.
 */
public class ServicioImportacion {
    
	/**
    * Factoría que proporciona {@link ImportadorStrategy} según la extensión.
    */
    private final ImportadorFactory importadorFactory;
    
   /**
    * Map de datos de transferencia ({@link CursoDTO}) a entidades {@link Curso}.
    */
    private final CursoMapper cursoMapper;
    
    /**
     * Constructor por defecto. Inicializa un {@link CursoMapper} y una {@link ImportadorFactory}.
     */
    public ServicioImportacion() {
        this.cursoMapper = new CursoMapper();
        this.importadorFactory = new ImportadorFactory(cursoMapper);
    }
    
    /**
     * Constructor utilizado para Testing.
     * @param cursoMapper       Instancia de {@link CursoMapper}, ya sea real o un Mock Object.
     * @param importadorFactory Instancia de {@link ImportadorFactory}, ya sea real o un Mock Object.
     */
    public ServicioImportacion(CursoMapper cursoMapper, ImportadorFactory importadorFactory) {
        this.cursoMapper = cursoMapper;
        this.importadorFactory = importadorFactory;
    }
    
    /**
     * Método que Importa cursos desde un archivo, dado su ruta.
     * @param rutaArchivo Ruta completa del archivo de entrada
     * @return Instancia {@link ResultadoImportacion} con la lista de cursos importados y el formato usado.
     * @throws ImportacionException si el archivo no existe, no es legible, o se produce un error de E/S durante la lectura o importación
     */
    public ResultadoImportacion importarDesdeArchivo(String rutaArchivo) throws ImportacionException {
        File archivo = new File(rutaArchivo);
        
        if (!archivo.exists()) {
            throw new ImportacionException("El archivo no existe: " + rutaArchivo, "FILE_NOT_FOUND");
        }
        
        if (!archivo.canRead()) {
            throw new ImportacionException("No se puede leer el archivo: " + rutaArchivo, "FILE_NOT_READABLE");
        }
        
        String extension = obtenerExtension(archivo.getName());
        
        try (FileInputStream fis = new FileInputStream(archivo)) {
            return importarDesdeStream(fis, extension);
        } catch (IOException e) {
            throw new ImportacionException(
                "Error al leer el archivo: " + rutaArchivo, 
                "IO_ERROR", 
                e.getMessage(), 
                e
            );
        }
    }
    
    /**
     * Método que importa cursos desde la entrada, dado el formato (extensión).
     * @param inputStream Flujo de datos de entrada que contiene la definición de cursos.
     * @param extension Extensión o formato del contenido (sin el punto).
     * @return {@link ResultadoImportacion} con la lista de cursos importados y el formato usado.
     * @throws ImportacionException si el formato no está soportado o la importación falla.
     */
    public ResultadoImportacion importarDesdeStream(InputStream inputStream, String extension) throws ImportacionException {
        Optional<ImportadorStrategy> importadorOpt = importadorFactory.obtenerImportador(extension);
        
        if (importadorOpt.isEmpty()) {
            throw new ImportacionException(
                "Formato no soportado: " + extension + ". Formatos soportados: " + 
                String.join(", ", importadorFactory.getExtensionesSuportadas()),
                "UNSUPPORTED_FORMAT"
            );
        }
        
        ImportadorStrategy importador = importadorOpt.get();
        List<Curso> cursos = importador.importar(inputStream);
        
        return new ResultadoImportacion(cursos, importador.getTipoFormato());
    }
    
    /**
     * Método que valida un curso antes de guardarlo en el sistema.
     * @param curso Instancia {@link CursoDTO} que contiene la información del curso recogida en la importación.
     * @return Instancia {@link ResultadoValidación} con el estado de la validación.
     */
    public ResultadoValidacion validarCurso(CursoDTO curso) {
        return ValidadorCurso.validar(curso);
    }
    
    /**
     * Método que obtiene los formatos soportados por el sistema.
     * @return Array de Strings con las extensiones válidas (sin el punto).
     */
    public String[] getFormatosSoportados() {
        return importadorFactory.getExtensionesSuportadas();
    }
    
    /**
     * Método que verifica si un formato está soportado.
     * @param extension Extensión a verificar.
     * @return {@code true} si el sistema lo soporta, {@code false} en caso contrario.
     */
    public boolean soportaFormato(String extension) {
        return importadorFactory.soportaExtension(extension);
    }
    
    /**
     * Método que extrae la extensión de un nombre de archivo.
     * @param nombreArchivo Nombre del archivo (con o sin ruta).
     * @return Extensión del archivo.
     */
    private String obtenerExtension(String nombreArchivo) {
        int ultimoPunto = nombreArchivo.lastIndexOf('.');
        if (ultimoPunto == -1 || ultimoPunto == nombreArchivo.length() - 1) {
            return "";
        }
        return nombreArchivo.substring(ultimoPunto + 1);
    }
    
    
    /**
     * Clase que representa el resultado de la importación de un curso. Contiene: <ul>
     * <li> Lista de cursos leídos. </li>
     * <li> Formato utilizado. </li>
     * <li> Cantidad de cursos importada.</li>
     * </ul>
     */
    public static class ResultadoImportacion {
        private final List<Curso> cursos;
        private final String formatoUtilizado;
        private final int cantidadImportada;
        
        public ResultadoImportacion(List<Curso> cursos, String formatoUtilizado) {
            this.cursos = cursos;
            this.formatoUtilizado = formatoUtilizado;
            this.cantidadImportada = cursos != null ? cursos.size() : 0;
        }
        
        public List<Curso> getCursos() { return cursos; }
        public String getFormatoUtilizado() { return formatoUtilizado; }
        public int getCantidadImportada() { return cantidadImportada; }
        
        public boolean fueExitoso() {
            return cursos != null && !cursos.isEmpty();
        }
    }
}
