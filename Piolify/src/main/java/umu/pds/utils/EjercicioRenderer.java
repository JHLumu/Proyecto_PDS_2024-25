package umu.pds.utils;

import javax.swing.JPanel;

/**
 * Interfaz para la renderización de ejercicios en la capa vsta.
 */
public interface EjercicioRenderer {
	
	/**
	 * Método para renderizar un ejercicio en un panel dado.
	 * 
	 * @param containerPanel Panel donde se renderizará el ejercicio.
	 * @param ejercicio      Objeto que representa el ejercicio a renderizar.
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
