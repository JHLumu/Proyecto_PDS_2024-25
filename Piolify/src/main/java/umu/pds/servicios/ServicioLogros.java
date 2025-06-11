package umu.pds.servicios;

import java.util.ArrayList;
import java.util.List;

import umu.pds.modelo.Estadisticas;
import umu.pds.modelo.Logro;
import umu.pds.modelo.TipoLogro;
import umu.pds.modelo.Usuario;

public class ServicioLogros {
	    
    public ServicioLogros() {
    }
    
    /**
     * Obtiene todos los logros con su estado actual (desbloqueado/bloqueado)
     */
    public List<LogroConEstado> obtenerLogrosConEstado(Usuario usuario) {
        List<LogroConEstado> logrosConEstado = new ArrayList<>();

        // Usar directamente el enum como repositorio
        for (TipoLogro tipoLogro : TipoLogro.values()) {
            boolean desbloqueado = usuario.tieneLogroDesbloqueado(tipoLogro);
            logrosConEstado.add(new LogroConEstado(tipoLogro, desbloqueado));
        }

        return logrosConEstado;
    }

    /**
     * Desbloquea un logro específico para un usuario
     */
    public void desbloquearLogro(Usuario usuario, TipoLogro tipo) {
        usuario.desbloquearLogro(tipo);
    }

    /**
     * Comprueba y desbloquea automáticamente todos los logros que el usuario haya conseguido
     * basándose en sus estadísticas actuales
     */
    public List<TipoLogro> comprobarYDesbloquearLogros(Usuario usuario) {
        List<TipoLogro> logrosDesbloqueados = new ArrayList<>();
        Estadisticas estadisticas = usuario.getEstadisticas();
        int cursosComenzados = usuario.getCursosComenzados(); // Asumiendo que existe este método
        
        // Comprobar todos los tipos de logros
        for (TipoLogro tipoLogro : TipoLogro.values()) {
            // Solo procesar si el usuario no tiene ya este logro
            if (!usuario.tieneLogroDesbloqueado(tipoLogro)) {
                // Verificar si se cumple la condición para este logro
                if (tipoLogro.seCumpleCondicion(estadisticas, cursosComenzados)) {
                    usuario.desbloquearLogro(tipoLogro);
                    logrosDesbloqueados.add(tipoLogro);
                }
            }
        }
        
        return logrosDesbloqueados;
    }

    /**
     * Comprueba si un logro específico se puede desbloquear para un usuario
     */
    public boolean puedeDesbloquearLogro(Usuario usuario, TipoLogro tipo) {
        if (usuario.tieneLogroDesbloqueado(tipo)) {
            return false; // Ya está desbloqueado
        }
        
        Estadisticas estadisticas = usuario.getEstadisticas();
        int cursosComenzados = usuario.getCursosComenzados();
        
        return tipo.seCumpleCondicion(estadisticas, cursosComenzados);
    }
    
    /**
     * Clase interna que representa un logro con su estado
     */
    public static class LogroConEstado {
        private final TipoLogro tipoLogro;
        private final boolean desbloqueado;

        public LogroConEstado(TipoLogro tipoLogro, boolean desbloqueado) {
            this.tipoLogro = tipoLogro;
            this.desbloqueado = desbloqueado;
        }

        public TipoLogro getTipoLogro() {
            return tipoLogro;
        }

        public String getNombre() {
            return tipoLogro.getNombre();
        }

        public String getDescripcion() {
            return tipoLogro.getDescripcion();
        }

        public int getCondicion() {
            return tipoLogro.getCondicion();
        }

        public boolean isDesbloqueado() {
            return desbloqueado;
        }
    }
}


