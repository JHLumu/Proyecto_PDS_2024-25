package umu.pds.servicios.importacion;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import umu.pds.modelo.Curso;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class ImportadorYAML implements ImportadorStrategy {
    
    private final ObjectMapper yamlMapper;
    private final CursoMapper cursoMapper;
    
    public ImportadorYAML(CursoMapper cursoMapper) {
        this.yamlMapper = new ObjectMapper(new YAMLFactory());
        this.cursoMapper = cursoMapper;
    }
    
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
    
    @Override
    public boolean soportaFormato(String extension) {
        return "yaml".equalsIgnoreCase(extension) || "yml".equalsIgnoreCase(extension);
    }
    
    @Override
    public String getTipoFormato() {
        return "YAML";
    }
}
