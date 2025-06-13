package umu.pds.utils;

import javax.swing.JPanel;

/**
 * Interfaz para la renderización de ejercicios en la capa vsta.
 */
public interface EjercicioRenderer {
	
	/**
	 * Método que renderiza un ejercicio en un panel.
	 * @param containerPanel Instancia {@link JPanel} donde se mostrará el ejercicio.
	 * @param ejercicio Instancia {@link Ejercicio} a renderizar.
	 */
    void renderizar(JPanel containerPanel, Object ejercicio);
    
    /**
     * Método para configuración de eventos para la interacción entre el usuario y el ejercicio.
     */
    void configurarEventos();
    
    /**
     * Método que valida una respuesta del usuario a un ejercicio.
     * @return {@code true} si la respuesta es correcta, {@code false} en caso contrario. 
     */
    boolean validarRespuesta();
    
    /**
     * Método que muestra el resultado de la validación de la respuesta al usuario.
     * @param esCorrecta {@code true} si la respuesta es correcta, {@code false} en caso contrario. 
     * @param mensaje texto a mostrar al usuario.
     */
    void mostrarResultado(boolean esCorrecta, String mensaje);
}
