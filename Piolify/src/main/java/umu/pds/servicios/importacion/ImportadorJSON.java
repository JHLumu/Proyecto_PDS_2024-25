package umu.pds.servicios.importacion;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import umu.pds.modelo.Curso;


import java.io.IOException;
import java.io.InputStream;
import java.util.List;


public class ImportadorJSON implements ImportadorStrategy {
    
    private final ObjectMapper objectMapper;
    private final CursoMapper cursoMapper;
    
    public ImportadorJSON(CursoMapper cursoMapper) {
        this.objectMapper = new ObjectMapper();
        // Configurar para ser más tolerante con JSON
        this.objectMapper.configure(com.fasterxml.jackson.core.JsonParser.Feature.ALLOW_COMMENTS, true);
        this.objectMapper.configure(com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        this.cursoMapper = cursoMapper;
    }
    
    @Override
    public List<Curso> importar(InputStream inputStream) throws ImportacionException {
        try {
            // Leer todo el contenido primero para poder reiniciar si es necesario
            byte[] contenido = inputStream.readAllBytes();
            
            List<CursoDTO> cursosDTO;
            
            try {
                // Intentar leer como array de cursos primero
                CollectionType listType = objectMapper.getTypeFactory()
                    .constructCollectionType(List.class, CursoDTO.class);
                cursosDTO = objectMapper.readValue(contenido, listType);
            } catch (Exception e) {
                // Si falla, intentar leer como un solo curso
                try {
                    CursoDTO cursoDTO = objectMapper.readValue(contenido, CursoDTO.class);
                    cursosDTO = List.of(cursoDTO);
                } catch (Exception e2) {
                    // Si ambos fallan, lanzar excepción más descriptiva
                    throw new ImportacionException(
                        "El archivo JSON no tiene el formato esperado. Debe ser un curso único o un array de cursos.",
                        "PARSE_ERROR",
                        "Error parseando como array: " + e.getMessage() + ". Error parseando como objeto único: " + e2.getMessage(),
                        e2
                    );
                }
            }
            
            return cursoMapper.convertirDesdeDTO(cursosDTO);
            
        } catch (IOException e) {
            throw new ImportacionException(
                "Error al leer el archivo JSON", 
                "IO_ERROR", 
                e.getMessage(), 
                e
            );
        } catch (ImportacionException e) {
            // Re-lanzar excepciones de importación
            throw e;
        } catch (Exception e) {
            throw new ImportacionException(
                "Error inesperado durante la importación JSON", 
                "UNEXPECTED_ERROR", 
                e.getMessage(), 
                e
            );
        }
    }
    
    @Override
    public boolean soportaFormato(String extension) {
        return "json".equalsIgnoreCase(extension);
    }
    
    @Override
    public String getTipoFormato() {
        return "JSON";
    }
}