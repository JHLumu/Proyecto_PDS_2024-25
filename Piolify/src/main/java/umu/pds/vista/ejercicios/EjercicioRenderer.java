package umu.pds.vista.ejercicios;

import javax.swing.JPanel;

public interface EjercicioRenderer {
    void renderizar(JPanel containerPanel, Object ejercicio);
    void configurarEventos();
    boolean validarRespuesta();
    void mostrarResultado(boolean esCorrecta, String mensaje);
}
