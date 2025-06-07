package umu.pds.persistencia;

import java.util.List;

import umu.pds.modelo.Amistad;
import umu.pds.modelo.Usuario;

public interface AmistadDAO {
    
    void guardarAmistad(Amistad amistad);
    void actualizarAmistad(Amistad amistad);
    Amistad buscarPorId(Long id);
    List<Amistad> buscarSolicitudesPendientes(Usuario receptor);
    List<Amistad> buscarAmistades(Usuario usuario);
    boolean existeAmistad(Usuario usuario1, Usuario usuario2);
    void eliminarAmistad(Amistad amistad);
}