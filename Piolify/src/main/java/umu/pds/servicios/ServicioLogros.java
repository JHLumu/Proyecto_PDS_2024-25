package umu.pds.servicios;

import java.util.ArrayList;
import java.util.List;

import umu.pds.controlador.Piolify;
import umu.pds.modelo.Estadisticas;
import umu.pds.modelo.TipoLogro;
import umu.pds.modelo.Usuario;


/**
 * Clase de apoyo para los controladores, utilizado para la gestión 
 * de los logros de los usuarios.
 */
public class ServicioLogros {
	

    /**
     * Método que recupera todos los logros definidos, con un estado asociado según el usuario.
     * @param usuario Instancia {@link Usuario} oara el cual se consultan el estado de los logros.
     * @return Lista de instancias {@link LogroConEstado}, con un estado asociado.
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
     * Método que desbloquea un logro específico para un usuario.
     * @param usuario Instancia {@link Usuario} oara el cual se desbloquea un logro.
     * @param tipo Constante {@link TipoLogro}. Logro definido en el sistema que se desbloquea. 
     */
    public void desbloquearLogro(Usuario usuario, TipoLogro tipo) {
    	System.out.println("Logro desbloqueado");
       Piolify.getUnicaInstancia().getUsuarioController().desbloquearLogro(usuario, tipo);
    }

    /**
     * Métoo que comprueba y desbloquea automáticamente todos los logros que el usuario haya conseguido
     * basándose en sus estadísticas actuales.
     * @param usuario Instancia {@link Usuario} para el cual se comprobarán los logros definidos en el sistema.
     * @return Lista de instancias {@link LogroConEstado} que han sido desbloqueados. 
     */
    public List<TipoLogro> comprobarYDesbloquearLogros(Usuario usuario) {
        List<TipoLogro> logrosDesbloqueados = new ArrayList<>();
        Estadisticas estadisticas = usuario.getEstadisticas();
        int cursosComenzados = usuario.getCursosComenzados();
        
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
     * Clase interna que representa un logro con su estado de desbloqueo.
     * Se utiliza para mostrar los logros y su estado en la interfaz de usuario.
     */
    public static class LogroConEstado {
        private final TipoLogro tipoLogro;
        private final boolean desbloqueado;

        /**
         * Constructor de la clase {@link LogroConEstado}. Asocia un logro definido en el sistema un estado.
         * @param tipoLogro Constante {@link TipoLogro}. Logro definido en el sistema que se desbloquea.
         * @param desbloqueado {@code true} si el logro está desbloqueado, {@code false} en caso contrario.
         */
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


