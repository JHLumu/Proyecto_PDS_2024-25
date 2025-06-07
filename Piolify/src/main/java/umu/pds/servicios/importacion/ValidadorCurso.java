package umu.pds.servicios.importacion;

import java.util.ArrayList;
import java.util.List;

public class ValidadorCurso {
    
    public static class ResultadoValidacion {
        private final boolean valido;
        private final List<String> errores;
        private final List<String> advertencias;
        
        public ResultadoValidacion(boolean valido, List<String> errores, List<String> advertencias) {
            this.valido = valido;
            this.errores = errores != null ? errores : new ArrayList<>();
            this.advertencias = advertencias != null ? advertencias : new ArrayList<>();
        }
        
        public boolean isValido() { return valido; }
        public List<String> getErrores() { return errores; }
        public List<String> getAdvertencias() { return advertencias; }
    }
    
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