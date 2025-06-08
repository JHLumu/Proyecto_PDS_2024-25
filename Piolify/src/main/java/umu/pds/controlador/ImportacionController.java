package umu.pds.controlador;

import umu.pds.modelo.Curso;
import umu.pds.modelo.Usuario;
import umu.pds.servicios.CursoService;
import umu.pds.servicios.ServicioImportacion;
import umu.pds.servicios.ServicioImportacion.ResultadoImportacion;
import umu.pds.servicios.importacion.ImportacionException;

import java.io.InputStream;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ImportacionController {
    
    private static final Logger logger = Logger.getLogger(ImportacionController.class.getName());
    
    private final ServicioImportacion servicioImportacion;
    private final CursoService cursoService;
    
    public ImportacionController() {
        this.servicioImportacion = new ServicioImportacion();
        this.cursoService = new CursoService();
    }
    
    // constructor para testing
	public ImportacionController(ServicioImportacion servicioImportacion, CursoService cursoService) {
		this.servicioImportacion = servicioImportacion;
		this.cursoService = cursoService;
	}
	
	
    /**
     * Importa cursos desde un archivo y los agrega al sistema
     */
    public boolean importarCursosDesdeArchivo(String rutaArchivo, Usuario usuario) {
        try {
            logger.info("Iniciando importación desde archivo: " + rutaArchivo);
            
            ResultadoImportacion resultado = servicioImportacion.importarDesdeArchivo(rutaArchivo);
            
            if (resultado.fueExitoso()) {
                // Aquí podrías agregar lógica para persistir los cursos
                // Por ejemplo, llamar a un DAO o repositorio
                logger.info("Importación exitosa: " + resultado.getCantidadImportada() + 
                           " cursos importados usando formato " + resultado.getFormatoUtilizado());
                
                // Procesar cursos importados
                procesarCursosImportados(resultado.getCursos(), usuario);
                return true;
            } else {
                logger.warning("La importación no produjo cursos válidos");
                return false;
            }
            
        } catch (ImportacionException e) {
            logger.log(Level.SEVERE, "Error durante la importación: " + e.getMessage(), e);
            mostrarErrorImportacion(e);
            return false;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error inesperado durante la importación", e);
            return false;
        }
    }
    
    /**
     * Importa cursos desde un InputStream
     */
    public boolean importarCursosDesdeStream(InputStream inputStream, String extension, Usuario usuario) {
        try {
            logger.info("Iniciando importación desde stream con extensión: " + extension);
            
            ResultadoImportacion resultado = servicioImportacion.importarDesdeStream(inputStream, extension);
            
            if (resultado.fueExitoso()) {
                logger.info("Importación exitosa: " + resultado.getCantidadImportada() + 
                           " cursos importados usando formato " + resultado.getFormatoUtilizado());
                
                procesarCursosImportados(resultado.getCursos(), usuario);
                return true;
            } else {
                logger.warning("La importación no produjo cursos válidos");
                return false;
            }
            
        } catch (ImportacionException e) {
            logger.log(Level.SEVERE, "Error durante la importación: " + e.getMessage(), e);
            mostrarErrorImportacion(e);
            return false;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error inesperado durante la importación", e);
            return false;
        }
    }
    
    /**
     * Obtiene los formatos de archivo soportados
     */
    public String[] getFormatosSoportados() {
        return servicioImportacion.getFormatosSoportados();
    }
    
    /**
     * Verifica si un formato está soportado
     */
    public boolean soportaFormato(String extension) {
        return servicioImportacion.soportaFormato(extension);
    }
    
    /**
     * Procesa los cursos importados (aquí puedes agregar lógica de negocio)
     */
    private void procesarCursosImportados(List<Curso> cursos, Usuario usuario) {
        // Establecer el usuario autor como el usuario actual
        // Usuario usuarioActual = aplicacionPrincipal.getUsuarioActual();
        
        for (Curso curso : cursos) {
            // Asignar autor si no está establecido
            // if (curso.getAutor() == null) {
            //     curso.setAutor(usuarioActual);
            // }
            
            // Validaciones adicionales
            validarCursoImportado(curso);
            cursoService.guardarCurso(curso);
            usuario.getBiblioteca().add(curso);
            Piolify.getUnicaInstancia().getUsuarioController().modificarUsuario(usuario);
            logger.info("Curso procesado: " + curso.getTitulo());
        }
        
        // Notificar cambios si es necesario
        // aplicacionPrincipal.notificarCambiosUsuario();
    }
    
    /**
     * Validación adicional de cursos importados
     */
    private void validarCursoImportado(Curso curso) {
        if (curso.getTitulo() == null || curso.getTitulo().trim().isEmpty()) {
            throw new IllegalArgumentException("El curso debe tener un título válido");
        }
        
        if (curso.getBloques() == null || curso.getBloques().isEmpty()) {
            throw new IllegalArgumentException("El curso debe tener al menos un bloque");
        }
        
        // Validar bloques y ejercicios
        curso.getBloques().forEach(bloque -> {
            if (bloque.getEjercicios() == null || bloque.getEjercicios().isEmpty()) {
                throw new IllegalArgumentException("El bloque '" + bloque.getTitulo() + "' debe tener ejercicios");
            }
        });
    }
    
    /**
     * Maneja y muestra errores de importación
     */
    private void mostrarErrorImportacion(ImportacionException e) {
        String mensaje = "Error de importación: " + e.getMessage();
        
        if (e.getDetalles() != null) {
            mensaje += "\nDetalles: " + e.getDetalles();
        }
        
        // Aquí podrías mostrar un diálogo de error en la vista
        System.err.println(mensaje);
    }
}