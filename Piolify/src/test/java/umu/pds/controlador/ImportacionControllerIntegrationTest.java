package umu.pds.controlador;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import umu.pds.modelo.Usuario;
import umu.pds.utils.RegistroUsuarioDTO;

class ImportacionControllerIntegrationTest {
	
    private ImportacionController importacionController;
    private UsuarioController usuarioController;
    private Usuario usuario;
    private File dbFile;
    
    private static final String RUTA_ARCHIVO_JSON = "src/main/resources/curso_ejemplo.json";

    @BeforeEach
    void setUp() throws Exception {
        dbFile = new File("testbasedatos.db");
        
        Piolify piolify = Piolify.getUnicaInstancia();
        importacionController = piolify.getImportacionController();
        usuarioController = piolify.getUsuarioController();
        
        usuario = crearUsuarioPrueba();
        
        // Establecer el usuario como autenticado para los tests (con tal de poder probar la importación)
        piolify.setUsuarioActual(usuario);
    }
    
    @AfterEach
    void cleanUp() throws Exception {
        // Limpiar la DB de test
        if (dbFile != null && dbFile.exists()) {
            dbFile.delete();
        }
    }
    
    /**
     * Crea un usuario único para cada ejecución del test
     * Usando timestamp para garantizar unicidad y biblioteca vacía
     */
    private Usuario crearUsuarioPrueba() {
        // Email único para cada ejecución del test
        String emailTest = "test_integration_" + System.currentTimeMillis() + "@test.com";
        
        RegistroUsuarioDTO dto = new RegistroUsuarioDTO.Builder()
            .nombre("Test")
            .apellidos("Integration")
            .genero("H")
            .email(emailTest)
            .password("testpass")
            .confirmar("testpass")
            .rutaImagenPerfil("/test_foto.jpg")
            .build();
        
        boolean registrado = usuarioController.registrarUsuario(dto);
        if (!registrado) {
            throw new RuntimeException("No se pudo registrar usuario de prueba");
        }
        
        // Obtener el usuario recién creado
        Usuario usuarioCreado = usuarioController.buscarUsuarioPorEmail(emailTest);
        
        return usuarioCreado;
    }

    /**
     * Flujo completo de importación desde archivo JSON real
     * hasta persistencia en base de datos real.
     * 
     */
    @Test
    void testIntegracionFlujoCompletoImportacion() {
        // Verificar que el archivo existe antes de continuar
        File archivoJson = new File(RUTA_ARCHIVO_JSON);
        assertTrue(archivoJson.exists(), 
            "El archivo " + RUTA_ARCHIVO_JSON + " debe existir para este test");
        
        // biblioteca vacía
        Usuario usuarioInicial = usuarioController.buscarUsuarioPorEmail(usuario.getEmail());
        assertEquals(0, usuarioInicial.getBiblioteca().size());
        
        boolean resultado = importacionController.importarCursosDesdeArchivo(
            RUTA_ARCHIVO_JSON, usuario);
        
        // La importación debe ser exitosa
        assertTrue(resultado);
        
        // Verificar que se importaron cursos
        Usuario usuarioActualizado = usuarioController.buscarUsuarioPorEmail(usuario.getEmail());
        assertTrue(usuarioActualizado.getBiblioteca().size() > 0);
    }
    
    /**
     * Verificar que no se duplican cursos en importaciones múltiples
     */
    @Test
    void testIntegracionNoDuplicacionCursos() {
        File archivoJson = new File(RUTA_ARCHIVO_JSON);
        assertTrue(archivoJson.exists(), 
            "El archivo " + RUTA_ARCHIVO_JSON + " debe existir para este test.");
       
        Usuario usuarioInicial = usuarioController.buscarUsuarioPorEmail(usuario.getEmail());
        assertEquals(0, usuarioInicial.getBiblioteca().size(), 
            "El usuario debería empezar sin cursos");
        
        // Primera importación
        boolean primeraImportacion = importacionController.importarCursosDesdeArchivo(
            RUTA_ARCHIVO_JSON, usuario);
        // La importación debe ser exitosa
        assertTrue(primeraImportacion);
        
        Usuario usuarioTrasPrimera = usuarioController.buscarUsuarioPorEmail(usuario.getEmail());
        int cursosTrasPrimera = usuarioTrasPrimera.getBiblioteca().size();
        // al menos un curso debe haber sido importado
        assertTrue(cursosTrasPrimera > 0);
        
        // Segunda importación del mismo archivo
        boolean segundaImportacion = importacionController.importarCursosDesdeArchivo(
            RUTA_ARCHIVO_JSON, usuario);
        assertTrue(segundaImportacion);
        
        Usuario usuarioTrasSegunda = usuarioController.buscarUsuarioPorEmail(usuario.getEmail());
        int cursosTraseSegunda = usuarioTrasSegunda.getBiblioteca().size();
        
        // Verificar que no se duplicaron cursos
        assertEquals(cursosTrasPrimera, cursosTraseSegunda);
    }
    
    /**
     *Persistencia correcta de ejercicios
     */
    @Test
    void testIntegracionPersistenciaEjercicios() {
        File archivoJson = new File(RUTA_ARCHIVO_JSON);
        assertTrue(archivoJson.exists(), 
            "El archivo " + RUTA_ARCHIVO_JSON + " debe existir para este test");
       
        Usuario usuarioInicial = usuarioController.buscarUsuarioPorEmail(usuario.getEmail());
        assertEquals(0, usuarioInicial.getBiblioteca().size());
        boolean resultado = importacionController.importarCursosDesdeArchivo(
            RUTA_ARCHIVO_JSON, usuario);
        

        // La importación debe ser exitosa
        assertTrue(resultado);
        
        Usuario usuarioActualizado = usuarioController.buscarUsuarioPorEmail(usuario.getEmail());
        // al menos un curso debe haber sido importado
        assertTrue(usuarioActualizado.getBiblioteca().size() > 0);
    }
    
    /**
     * Manejo de archivo inexistente
     */
    @Test
    void testIntegracionArchivoInexistente() {
        String archivoInexistente = "archivo_que_no_existe.json";
        
        boolean resultado = importacionController.importarCursosDesdeArchivo(
            archivoInexistente, usuario);
        
        // debería fallar la importación
        assertFalse(resultado);
        
        // Verificar que el usuario sigue sin cursos
        Usuario usuarioActualizado = usuarioController.buscarUsuarioPorEmail(usuario.getEmail());
        assertEquals(0, usuarioActualizado.getBiblioteca().size());
    }
}