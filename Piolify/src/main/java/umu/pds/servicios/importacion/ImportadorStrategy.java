package umu.pds.servicios.importacion;

import umu.pds.modelo.Curso;
import java.io.InputStream;
import java.util.List;

public interface ImportadorStrategy {
    /**
     * Importa un curso desde un InputStream.
     *
     * @param inputStream InputStream que contiene los datos del curso a importar.
     * @return Lista de cursos importados.
     * @throws ImportacionException Si ocurre un error durante la importación.
     */
    List<Curso> importar(InputStream inputStream) throws ImportacionException;
    /**
     * Verifica si el importador soporta un formato específico basado en la extensión del archivo.
     *
     * @param extension Extensión del archivo a verificar.
     * @return true si el formato es soportado, false en caso contrario.
     */
    boolean soportaFormato(String extension);
    /**
     * Obtiene el tipo de formato que maneja el importador.
     *
     * @return Tipo de formato del importador, como "JSON", "YAML", etc.
     */
    String getTipoFormato();
}