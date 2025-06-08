package umu.pds.persistencia;

import java.util.List;
import umu.pds.modelo.Curso;
import umu.pds.modelo.SesionAprendizaje;
import umu.pds.modelo.Usuario;

public interface SesionAprendizajeDAO {
    void guardarSesion(SesionAprendizaje sesion);
    void actualizarSesion(SesionAprendizaje sesion);
    SesionAprendizaje buscarPorId(Long id);
    List<SesionAprendizaje> buscarSesionesPorUsuario(Usuario usuario);
    List<SesionAprendizaje> buscarSesionesPorUsuarioYCurso(Usuario usuario, Curso curso);
    List<SesionAprendizaje> buscarSesionesCompletadas(Usuario usuario);
    void eliminarSesion(SesionAprendizaje sesion);
}
