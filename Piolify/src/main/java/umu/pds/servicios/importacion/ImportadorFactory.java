package umu.pds.servicios.importacion;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ImportadorFactory {
    
    private final Map<String, ImportadorStrategy> importadores;
    private final CursoMapper cursoMapper;
    
    public ImportadorFactory(CursoMapper cursoMapper) {
        this.cursoMapper = cursoMapper;
        this.importadores = new HashMap<>();
        inicializarImportadores();
    }
    
    private void inicializarImportadores() {
        // Registrar importadores disponibles
        registrarImportador(new ImportadorJSON(cursoMapper));
        registrarImportador(new ImportadorYAML(cursoMapper));
    }
    
    public void registrarImportador(ImportadorStrategy importador) {
        // Permitir múltiples extensiones por importador
        String[] extensiones = getExtensionesDeImportador(importador);
        for (String extension : extensiones) {
            importadores.put(extension.toLowerCase(), importador);
        }
    }
    
    private String[] getExtensionesDeImportador(ImportadorStrategy importador) {
        if (importador instanceof ImportadorJSON) {
            return new String[]{"json"};
        } else if (importador instanceof ImportadorYAML) {
            return new String[]{"yaml", "yml"};
        }
        return new String[]{};
    }
    
    public Optional<ImportadorStrategy> obtenerImportador(String extension) {
        return Optional.ofNullable(importadores.get(extension.toLowerCase()));
    }
    
    public boolean soportaExtension(String extension) {
        return importadores.containsKey(extension.toLowerCase());
    }
    
    public String[] getExtensionesSuportadas() {
        return importadores.keySet().toArray(new String[0]);
    }
}
