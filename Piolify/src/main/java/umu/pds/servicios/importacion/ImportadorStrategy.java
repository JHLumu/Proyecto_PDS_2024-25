package umu.pds.servicios.importacion;

import umu.pds.modelo.Curso;
import java.io.InputStream;
import java.util.List;

public interface ImportadorStrategy {
    List<Curso> importar(InputStream inputStream) throws ImportacionException;
    boolean soportaFormato(String extension);
    String getTipoFormato();
}