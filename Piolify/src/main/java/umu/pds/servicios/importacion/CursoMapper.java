package umu.pds.servicios.importacion;

import umu.pds.modelo.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase encargada de la conversión entre objetos DTO a entidades del dominio para cursos, ejercicios y bloques.
 */
public class CursoMapper {
    
    /**
     * Convierte una lista de CursoDTO a una lista de Curso.
     * @param cursosDTO Lista de CursoDTO a convertir.
     * @return Lista de Curso convertida.
     * @throws ImportacionException Si ocurre un error durante la conversión.
     */
    public List<Curso> convertirDesdeDTO(List<CursoDTO> cursosDTO) throws ImportacionException {
        List<Curso> cursos = new ArrayList<>();
        
        for (CursoDTO cursoDTO : cursosDTO) {
            cursos.add(convertirDesdeDTO(cursoDTO));
        }
        
        return cursos;
    }
    
    /**
     * Convierte un CursoDTO a un Curso.
     * @param cursoDTO CursoDTO a convertir.
     * @return Curso convertido.
     * @throws ImportacionException Si ocurre un error durante la conversión.
     */
    public Curso convertirDesdeDTO(CursoDTO cursoDTO) throws ImportacionException {
        if (cursoDTO == null) {
            throw new ImportacionException("CursoDTO no puede ser null", "VALIDATION_ERROR");
        }
        
        Curso curso = new Curso();
        curso.setTitulo(cursoDTO.getTitulo());
        curso.setDescripcion(cursoDTO.getDescripcion());
        curso.setDificultad(cursoDTO.getDificultad());
        
        if (cursoDTO.getBloques() != null) {
            List<Bloque> bloques = new ArrayList<>();
            for (BloqueDTO bloqueDTO : cursoDTO.getBloques()) {
                Bloque bloque = convertirBloqueDesdeDTO(bloqueDTO, curso);
                bloques.add(bloque);
            }
            curso.setBloques(bloques);
        }
        
        return curso;
    }
    
    /**
     * Convierte un BloqueDTO a un Bloque, asociándolo al Curso proporcionado.
     * @param bloqueDTO BloqueDTO a convertir.
     * @param curso Curso al que se asociará el bloque.
     * @return Bloque convertido.
     * @throws ImportacionException Si ocurre un error durante la conversión.
     */
    private Bloque convertirBloqueDesdeDTO(BloqueDTO bloqueDTO, Curso curso) throws ImportacionException {
        if (bloqueDTO == null) {
            throw new ImportacionException("BloqueDTO no puede ser null", "VALIDATION_ERROR");
        }
        
        Bloque bloque = new Bloque();
        bloque.setTitulo(bloqueDTO.getTitulo());
        bloque.setDescripcion(bloqueDTO.getDescripcion());
        bloque.setCurso(curso); 
        
        if (bloqueDTO.getEjercicios() != null) {
            List<Ejercicio> ejercicios = new ArrayList<>();
            for (EjercicioDTO ejercicioDTO : bloqueDTO.getEjercicios()) {
                Ejercicio ejercicio = convertirEjercicioDesdeDTO(ejercicioDTO, bloque);
                ejercicios.add(ejercicio);
            }
            bloque.setEjercicios(ejercicios);
        }
        
        return bloque;
    }
    
    /**
     * Convierte un EjercicioDTO a un Ejercicio, asociándolo al Bloque proporcionado.
     * @param ejercicioDTO EjercicioDTO a convertir.
     * @param bloque Bloque al que se asociará el ejercicio.
     * @return Ejercicio convertido.
     * @throws ImportacionException Si ocurre un error durante la conversión.
     */
    private Ejercicio convertirEjercicioDesdeDTO(EjercicioDTO ejercicioDTO, Bloque bloque) throws ImportacionException {
        if (ejercicioDTO == null) {
            throw new ImportacionException("EjercicioDTO no puede ser null", "VALIDATION_ERROR");
        }
        
        String tipo = ejercicioDTO.getTipo().toUpperCase();
        Ejercicio ejercicio;
        
        switch (tipo) {
            case "OPCION_MULTIPLE":
                ejercicio = crearEjercicioOpcionMultiple(ejercicioDTO);
                break;
            case "COMPLETAR_HUECOS":
                ejercicio = crearEjercicioRellenarHuecos(ejercicioDTO);
                break;
            case "FLASHCARD":
                ejercicio = crearEjercicioFlashcard(ejercicioDTO);
                break;
            default:
                throw new ImportacionException(
                    "Tipo de ejercicio no reconocido: " + tipo, 
                    "UNSUPPORTED_EXERCISE_TYPE"
                );
        }
        
        // Establecer propiedades comunes
        ejercicio.setContenido(ejercicioDTO.getContenido());
        ejercicio.setRespuesta(ejercicioDTO.getRespuesta());
        ejercicio.setDificultad(ejercicioDTO.getDificultad());
        ejercicio.setBloque(bloque);
        
        return ejercicio;
    }
    
    /**
     * Crea un EjercicioOpcionMultiple a partir de un EjercicioDTO.
     * @param dto EjercicioDTO con las propiedades específicas.
     * @return Un EjercicioOpcionMultiple con las opciones extraídas del DTO.
     * @throws ImportacionException Si las opciones no son válidas.
     */
    private EjercicioOpcionMultiple crearEjercicioOpcionMultiple(EjercicioDTO dto) throws ImportacionException {
        EjercicioOpcionMultiple ejercicio = new EjercicioOpcionMultiple();
        // Extraer opciones de las propiedades específicas
        
            Object opciones = dto.getOpciones();
            if (opciones != null && opciones instanceof List) {
                @SuppressWarnings("unchecked")
                List<String> listaOpciones = (List<String>) opciones;
                ejercicio.setOpciones(listaOpciones);
            } else {
                throw new ImportacionException(
                    "Las opciones del ejercicio de opción múltiple deben ser una lista", 
                    "VALIDATION_ERROR"
                );
            }
        
        return ejercicio;
    }
    
    /**
     * Crea un EjercicioRellenarHuecos a partir de un EjercicioDTO.
     * @param dto EjercicioDTO con las propiedades específicas.
     * @return Un EjercicioRellenarHuecos con las respuestas extraídas del DTO.
     */
    private EjercicioRellenarHuecos crearEjercicioRellenarHuecos(EjercicioDTO dto) {
        return new EjercicioRellenarHuecos();
    }
    
    /**
     * Crea un EjercicioFlashcard a partir de un EjercicioDTO.
     * @param dto EjercicioDTO con las propiedades específicas.
     * @return Un EjercicioFlashcard con las propiedades necesarias.
     */
    private EjercicioFlashcard crearEjercicioFlashcard(EjercicioDTO dto) {
        return new EjercicioFlashcard();
    }
    
   
}
