package umu.pds.servicios;

import com.fasterxml.jackson.databind.ObjectMapper;

import umu.pds.modelo.Curso;

import java.io.File;

/**
 * Clase de apoyo usada por los controladores de la aplicación
 * para la importación y exportación de cursos.
 */
public class CursoSerializer {

    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * Método encargado de exportar un curso almacenado en la aplicación.
     * @param curso Curso a exportar.
     * @param ruta Ruta donde se guardará el curso exportado en formato JSON.
     * @throws Exception En caso de que ocurra un error durante la exportación, se lanzará una excepción.
     */
    public static void exportarCurso(Curso curso, String ruta) throws Exception {
        mapper.writeValue(new File(ruta), curso);
    }

    /**
     * 
     * @param ruta Ruta del archivo JSON.
     * @return Curso que se acaba de importar.
     * @throws Exception En caso de que ocurra un error durante la importación, se lanzará una excepción.
     */
    public static Curso importarCurso(String ruta) throws Exception {
        return mapper.readValue(new File(ruta), Curso.class);
    }
}
