package umu.pds.servicios.importacion;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ImportadorFactory {
    
    /* Mapa que asocia extensiones de archivo a sus correspondientes estrategias de importación. */
    private final Map<String, ImportadorStrategy> importadores;
    /* Mapeador de cursos para convertir entre DTO y entidad. */
    private final CursoMapper cursoMapper;
    
    /**
     * Constructor de la fábrica de importadores.
     * Inicializa el mapeador de cursos y registra los importadores disponibles.
     * @param cursoMapper Mapeador de cursos para convertir entre DTO y entidad.
     */
    public ImportadorFactory(CursoMapper cursoMapper) {
        this.cursoMapper = cursoMapper;
        this.importadores = new HashMap<>();
        inicializarImportadores();
    }
    
    /**
     * Constructor por defecto que inicializa el mapeador de cursos y registra los importadores disponibles.
     */
    private void inicializarImportadores() {
        // Registrar importadores disponibles
        registrarImportador(new ImportadorJSON(cursoMapper));
        registrarImportador(new ImportadorYAML(cursoMapper));
    }
    
    /**
     * Registra un nuevo importador en la fábrica.
     * Permite múltiples extensiones por importador, asegurando que cada extensión se asocie con su estrategia de importación.
     * @param importador Estrategia de importación a registrar.
     */
    public void registrarImportador(ImportadorStrategy importador) {
        // Permitir múltiples extensiones por importador
        String[] extensiones = getExtensionesDeImportador(importador);
        for (String extension : extensiones) {
            importadores.put(extension.toLowerCase(), importador);
        }
    }
    
    /**
     * Obtiene las extensiones soportadas por un importador específico.
     * @param importador Estrategia de importación para la cual se desean las extensiones.
     * @return Array de extensiones soportadas por el importador.
     */
    private String[] getExtensionesDeImportador(ImportadorStrategy importador) {
        if (importador instanceof ImportadorJSON) {
            return new String[]{"json"};
        } else if (importador instanceof ImportadorYAML) {
            return new String[]{"yaml", "yml"};
        }
        return new String[]{};
    }
    
    /**
     * Obtiene un importador basado en la extensión del archivo.
     * @param extension Extensión del archivo para el cual se desea obtener el importador.
     * @return Un {@link Optional} que contiene el importador si existe, o vacío si no se encuentra.
     */
    public Optional<ImportadorStrategy> obtenerImportador(String extension) {
        return Optional.ofNullable(importadores.get(extension.toLowerCase()));
    }
    
    /**
     * Verifica si la fábrica soporta una extensión específica.
     * @param extension Extensión del archivo a verificar.
     * @return {@code true} si la extensión es soportada, {@code false} en caso contrario.
     */
    public boolean soportaExtension(String extension) {
        return importadores.containsKey(extension.toLowerCase());
    }
    
    /**
     * Obtiene todas las extensiones soportadas por los importadores registrados.
     * @return Array de extensiones soportadas.
     */
    public String[] getExtensionesSuportadas() {
        return importadores.keySet().toArray(new String[0]);
    }
}
