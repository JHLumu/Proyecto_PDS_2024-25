package umu.pds.servicios.importacion;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.HashSet;

/**
 * Fábrica encargada de la instanciación de implementaciones de {@link ImportadorStrategy}.
 */
public class ImportadorFactory {
    
    /**
     * Mapa que asocia extensiones de archivo a sus correspondientes estrategias de importación.
     */
    private final Map<String, ImportadorStrategy> importadores;
    
    /**
     * Mapeador de cursos para convertir entre DTO y entidad.
     */
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
     * Inicializa los importadores por defecto.
     * Este método se mantiene para preservar la funcionalidad existente,
     * pero ahora la fábrica puede ser extendida sin modificación.
     */
    private void inicializarImportadores() {
        // Registrar importadores disponibles
        registrarImportador(new ImportadorJSON(cursoMapper));
        registrarImportador(new ImportadorYAML(cursoMapper));
    }
    
    /**
     * Registra un nuevo importador en la fábrica.
     * Utiliza las extensiones definidas por el propio importador,
     * eliminando la necesidad de modificar esta clase para nuevos formatos.
     * @param importador Estrategia de importación a registrar.
     * @throws IllegalArgumentException Si el importador es nulo o no define extensiones.
     */
    public void registrarImportador(ImportadorStrategy importador) {
        if (importador == null) {
            throw new IllegalArgumentException("El importador no puede ser nulo");
        }
        
        String[] extensiones = importador.getExtensionesSuportadas();
        if (extensiones == null || extensiones.length == 0) {
            throw new IllegalArgumentException(
                "El importador " + importador.getClass().getSimpleName() + 
                " debe definir al menos una extensión soportada"
            );
        }
        
        // Registrar cada extensión con el importador correspondiente
        for (String extension : extensiones) {
            if (extension != null && !extension.trim().isEmpty()) {
                String extensionLimpia = extension.toLowerCase().trim();
                ImportadorStrategy existente = importadores.get(extensionLimpia);
                
                if (existente != null && !existente.equals(importador)) {
                    throw new IllegalStateException(
                        "La extensión '" + extensionLimpia + "' ya está registrada para el importador " + 
                        existente.getClass().getSimpleName()
                    );
                }
                
                importadores.put(extensionLimpia, importador);
            }
        }
    }
    
    /**
     * Desregistra un importador de la fábrica.
     * Útil para testing o para reemplazar importadores dinámicamente.
     * @param importador Importador a desregistrar.
     */
    public void desregistrarImportador(ImportadorStrategy importador) {
        if (importador == null) return;
        
        // Encontrar y remover todas las extensiones asociadas a este importador
        importadores.entrySet().removeIf(entry -> entry.getValue().equals(importador));
    }
    
    /**
     * Obtiene un importador basado en la extensión del archivo.
     * @param extension Extensión del archivo para el cual se desea obtener el importador.
     * @return Un {@link Optional} que contiene el importador si existe, o vacío si no se encuentra.
     */
    public Optional<ImportadorStrategy> obtenerImportador(String extension) {
        if (extension == null || extension.trim().isEmpty()) {
            return Optional.empty();
        }
        return Optional.ofNullable(importadores.get(extension.toLowerCase().trim()));
    }
    
    /**
     * Verifica si la fábrica soporta una extensión específica.
     * @param extension Extensión del archivo a verificar.
     * @return {@code true} si la extensión es soportada, {@code false} en caso contrario.
     */
    public boolean soportaExtension(String extension) {
        if (extension == null || extension.trim().isEmpty()) {
            return false;
        }
        return importadores.containsKey(extension.toLowerCase().trim());
    }
    
    /**
     * Obtiene todas las extensiones soportadas por los importadores registrados.
     * @return Array de extensiones soportadas, ordenadas alfabéticamente.
     */
    public String[] getExtensionesSuportadas() {
        Set<String> extensiones = new HashSet<>(importadores.keySet());
        return extensiones.stream()
                .sorted()
                .toArray(String[]::new);
    }
    
    /**
     * Obtiene información sobre todos los importadores registrados.
     * Útil para diagnóstico y debugging.
     * @return Mapa que asocia cada tipo de formato con sus extensiones soportadas.
     */
    public Map<String, String[]> getInformacionImportadores() {
        Map<String, String[]> info = new HashMap<>();
        Set<ImportadorStrategy> importadoresUnicos = new HashSet<>(importadores.values());
        
        for (ImportadorStrategy importador : importadoresUnicos) {
            info.put(importador.getTipoFormato(), importador.getExtensionesSuportadas());
        }
        
        return info;
    }
    
    /**
     * Verifica si hay algún importador registrado.
     * @return true si hay al menos un importador registrado, false en caso contrario.
     */
    public boolean tieneImportadores() {
        return !importadores.isEmpty();
    }
    
    /**
     * Obtiene el número total de extensiones soportadas.
     * @return Número de extensiones diferentes soportadas.
     */
    public int getNumeroExtensionesSuportadas() {
        return importadores.size();
    }
}