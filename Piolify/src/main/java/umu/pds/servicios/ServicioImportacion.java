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

public class ServicioImportacion {
    
    private final ImportadorFactory importadorFactory;
    private final CursoMapper cursoMapper;
    
    public ServicioImportacion() {
        this.cursoMapper = new CursoMapper();
        this.importadorFactory = new ImportadorFactory(cursoMapper);
    }
    
    // Constructor para inyección de dependencias
    public ServicioImportacion(CursoMapper cursoMapper, ImportadorFactory importadorFactory) {
        this.cursoMapper = cursoMapper;
        this.importadorFactory = importadorFactory;
    }
    
    /**
     * Importa cursos desde un archivo
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
     * Importa cursos desde un InputStream
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
     * Valida un curso antes de importarlo
     */
    public ResultadoValidacion validarCurso(CursoDTO curso) {
        return ValidadorCurso.validar(curso);
    }
    
    /**
     * Obtiene los formatos soportados
     */
    public String[] getFormatosSoportados() {
        return importadorFactory.getExtensionesSuportadas();
    }
    
    /**
     * Verifica si un formato está soportado
     */
    public boolean soportaFormato(String extension) {
        return importadorFactory.soportaExtension(extension);
    }
    
    private String obtenerExtension(String nombreArchivo) {
        int ultimoPunto = nombreArchivo.lastIndexOf('.');
        if (ultimoPunto == -1 || ultimoPunto == nombreArchivo.length() - 1) {
            return "";
        }
        return nombreArchivo.substring(ultimoPunto + 1);
    }
    
    // ============= CLASE RESULTADO =============
    
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
