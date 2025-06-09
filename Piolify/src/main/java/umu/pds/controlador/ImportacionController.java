package umu.pds.controlador;

import umu.pds.modelo.Curso;
import umu.pds.modelo.Estrategia;
import umu.pds.modelo.EstrategiaFactory;
import umu.pds.modelo.TipoEstrategia;
import umu.pds.modelo.Usuario;
import umu.pds.servicios.CursoService;
import umu.pds.servicios.ServicioImportacion;
import umu.pds.servicios.ServicioImportacion.ResultadoImportacion;
import umu.pds.servicios.importacion.ImportacionException;

import java.io.InputStream;
import java.util.List;
import java.util.Optional;
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
    
    /**
     * Constructor para inyección de dependencias.
     * @param servicioImportacion Servicio de importación
     * @param cursoService Servicio de gestión de cursos
     */
	public ImportacionController(ServicioImportacion servicioImportacion, CursoService cursoService) {
		this.servicioImportacion = servicioImportacion;
		this.cursoService = cursoService;
	}
	
	
    /**
     * Importa cursos desde un archivo y los agrega al sistema
     * @param rutaArchivo Ruta del archivo a importar
     * @param usuario Usuario que realiza la importación
     * @return true si la importación fue exitosa, false en caso contrario
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
     * Importa cursos desde un InputStream y los agrega al sistema
     * @param inputStream InputStream del archivo a importar
     * @param extension Extensión del archivo (como en nuestro caso, "json")
     * @param usuario Usuario que realiza la importación
     * @return true si la importación fue exitosa, false en caso contrario
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
     * Obtiene los formatos de archivo soportados para importación
     * @return Array de extensiones soportadas
     */
    public String[] getFormatosSoportados() {
        return servicioImportacion.getFormatosSoportados();
    }
    
    /**
     * Verifica si un formato de archivo es soportado
     * @param extension Extensión del archivo a verificar
     * @return true si el formato es soportado, false en caso contrario
     */
    public boolean soportaFormato(String extension) {
        return servicioImportacion.soportaFormato(extension);
    }
    
    /**
     * Procesa los cursos importados, asignando el usuario autor y validando los datos
     * @param cursos Lista de cursos importados
     * @param usuario Usuario que realiza la importación
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
     * Valida que el curso importado tenga los campos necesarios
     * @param curso Curso a validar
     * @throws IllegalArgumentException si el curso no es válido
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
     * Muestra un mensaje de error detallado en caso de una excepción de importación
     * @param e Excepción de importación
     */
    private void mostrarErrorImportacion(ImportacionException e) {
        String mensaje = "Error de importación: " + e.getMessage();
        
        if (e.getDetalles() != null) {
            mensaje += "\nDetalles: " + e.getDetalles();
        }
        System.err.println(mensaje);
    }
    
    public List<TipoEstrategia> getTiposEstrategiasDefinidas() {
    	return EstrategiaFactory.getEstrategiasDefinidas();
    }

    public Estrategia getEstrategia(String estrategia) {
    	return EstrategiaFactory.crearEstrategia(estrategia);
    }
}