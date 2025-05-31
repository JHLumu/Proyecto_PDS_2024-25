package umu.pds.servicios;

import com.fasterxml.jackson.databind.ObjectMapper;

import umu.pds.modelo.Curso;

import java.io.File;

public class CursoSerializer {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static void exportarCurso(Curso curso, String ruta) throws Exception {
        mapper.writeValue(new File(ruta), curso);
    }

    public static Curso importarCurso(String ruta) throws Exception {
        return mapper.readValue(new File(ruta), Curso.class);
    }
}
