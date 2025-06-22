package umu.pds.servicios.importacion;

import java.util.ArrayList;
import java.util.List;

/**
 * Validador de cursos que verifica la validez de un CursoDTO.
 * Realiza comprobaciones sobre los campos obligatorios y estructura del curso.
 */
public class ValidadorCurso {
    
    /**
     * Resultado de la validación de un curso.
     * Contiene información sobre si el curso es válido, errores encontrados y advertencias.
     */
    public static class ResultadoValidacion {
        private final boolean valido;
        private final List<String> errores;
        private final List<String> advertencias;
        
        /**
         * Constructor para crear un resultado de validación.
         * @param valido Indica si el curso es válido.
         * @param errores Lista de errores encontrados durante la validación.
         * @param advertencias Lista de advertencias encontradas durante la validación.
         */
        public ResultadoValidacion(boolean valido, List<String> errores, List<String> advertencias) {
            this.valido = valido;
            this.errores = errores != null ? errores : new ArrayList<>();
            this.advertencias = advertencias != null ? advertencias : new ArrayList<>();
        }
        
        /**
         * Verifica si el curso es válido.
         * @return true si el curso es válido, false en caso contrario.
         */
        public boolean isValido() { return valido; }
        /**
         * Obtiene la lista de errores encontrados durante la validación.
         * @return Lista de errores.
         */
        public List<String> getErrores() { return errores; }
        /**
         * Obtiene la lista de advertencias encontradas durante la validación.
         * @return Lista de advertencias.
         */
        public List<String> getAdvertencias() { return advertencias; }
    }
    
    /**
     * Valida un CursoDTO comprobando campos obligatorios y estructura.
     * @param curso CursoDTO a validar.
     * @return ResultadoValidacion con información sobre la validez del curso, errores y advertencias.
     */
    public static ResultadoValidacion validar(CursoDTO curso) {
        List<String> errores = new ArrayList<>();
        List<String> advertencias = new ArrayList<>();
        
        // Validar campos obligatorios del curso
        if (curso.getTitulo() == null || curso.getTitulo().trim().isEmpty()) {
            errores.add("El título del curso es obligatorio");
        }
        
        if (curso.getDescripcion() == null || curso.getDescripcion().trim().isEmpty()) {
            advertencias.add("Se recomienda incluir una descripción del curso");
        }
        
        if (curso.getBloques() == null || curso.getBloques().isEmpty()) {
            errores.add("El curso debe tener al menos un bloque");
        } else {
            // Validar bloques
            for (int i = 0; i < curso.getBloques().size(); i++) {
                var bloque = curso.getBloques().get(i);
                if (bloque.getTitulo() == null || bloque.getTitulo().trim().isEmpty()) {
                    errores.add("El bloque " + (i + 1) + " debe tener título");
                }
                
                if (bloque.getEjercicios() == null || bloque.getEjercicios().isEmpty()) {
                    errores.add("El bloque '" + bloque.getTitulo() + "' debe tener al menos un ejercicio");
                } else {
                    // Validar ejercicios
                    for (int j = 0; j < bloque.getEjercicios().size(); j++) {
                        var ejercicio = bloque.getEjercicios().get(j);
                        if (ejercicio.getTipo() == null || ejercicio.getTipo().trim().isEmpty()) {
                            errores.add("El ejercicio " + (j + 1) + " del bloque '" + bloque.getTitulo() + "' debe tener tipo");
                        }
                        
                        if (ejercicio.getContenido() == null || ejercicio.getContenido().trim().isEmpty()) {
                            errores.add("El ejercicio " + (j + 1) + " del bloque '" + bloque.getTitulo() + "' debe tener contenido");
                        }
                        
                        if (ejercicio.getRespuesta() == null || ejercicio.getRespuesta().trim().isEmpty()) {
                            errores.add("El ejercicio " + (j + 1) + " del bloque '" + bloque.getTitulo() + "' debe tener respuesta");
                        }
                    }
                }
            }
        }
        
        boolean valido = errores.isEmpty();
        return new ResultadoValidacion(valido, errores, advertencias);
    }
}