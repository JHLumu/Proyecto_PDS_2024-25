package umu.pds.servicios.importacion;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import umu.pds.modelo.Curso;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Estrategia de importación que procesa la importación de cursos definidos en formato YAML.
 */
public class ImportadorYAML implements ImportadorStrategy {
    
    /** Extensiones de archivo soportadas por este importador. */
    private static final String[] EXTENSIONES_SOPORTADAS = {"yaml", "yml"};
    
    /** 
     * Manejador de excepciones personalizado para errores de importación.
     */
    private final ObjectMapper yamlMapper;
    /**
     * Mapeador de cursos para convertir entre DTO y entidad.
     */
    private final CursoMapper cursoMapper;
    
    /**
     * Constructor que inicializa el mapeador de objetos YAML y el mapeador de cursos.
     * Configura el mapeador para manejar adecuadamente los archivos YAML.
     * @param cursoMapper Mapeador de cursos para convertir entre DTO y entidad.
     */
    public ImportadorYAML(CursoMapper cursoMapper) {
        this.yamlMapper = new ObjectMapper(new YAMLFactory());
        this.cursoMapper = cursoMapper;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public List<Curso> importar(InputStream inputStream) throws ImportacionException {
        try {
            // Intentar leer como array de cursos primero
            CollectionType listType = yamlMapper.getTypeFactory()
                .constructCollectionType(List.class, CursoDTO.class);
            
            List<CursoDTO> cursosDTO;
            
            try {
                cursosDTO = yamlMapper.readValue(inputStream, listType);
            } catch (Exception e) {
                // Si falla, intentar leer como un solo curso
                inputStream.reset();
                CursoDTO cursoDTO = yamlMapper.readValue(inputStream, CursoDTO.class);
                cursosDTO = List.of(cursoDTO);
            }
            
            return cursoMapper.convertirDesdeDTO(cursosDTO);
            
        } catch (IOException e) {
            throw new ImportacionException(
                "Error al parsear el archivo YAML", 
                "PARSE_ERROR", 
                e.getMessage(), 
                e
            );
        } catch (Exception e) {
            throw new ImportacionException(
                "Error inesperado durante la importación YAML", 
                "UNEXPECTED_ERROR", 
                e.getMessage(), 
                e
            );
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean soportaFormato(String extension) {
        if (extension == null) return false;
        String ext = extension.toLowerCase().trim();
        for (String soportada : EXTENSIONES_SOPORTADAS) {
            if (soportada.equals(ext)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String getTipoFormato() {
        return "YAML";
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String[] getExtensionesSuportadas() {
        return EXTENSIONES_SOPORTADAS.clone(); // Devolver copia para evitar modificaciones
    }
}