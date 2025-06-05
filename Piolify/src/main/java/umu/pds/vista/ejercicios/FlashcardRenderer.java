package umu.pds.vista.ejercicios;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import umu.pds.modelo.EjercicioFlashcard;

public class FlashcardRenderer implements EjercicioRenderer {
    private JLabel contenidoLabel;
    private boolean mostrandoRespuesta = false;
    private EjercicioFlashcard ejercicio;
    
    @Override
    public void renderizar(JPanel containerPanel, Object ejercicioObj) {
        this.ejercicio = (EjercicioFlashcard) ejercicioObj;
        containerPanel.setLayout(new BorderLayout());
        
        contenidoLabel = new JLabel(ejercicio.getContenido(), SwingConstants.CENTER);
        contenidoLabel.setFont(new Font("Arial", Font.BOLD, 24));
        contenidoLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        containerPanel.add(contenidoLabel, BorderLayout.CENTER);
        configurarEventos();
    }
    
    @Override
    public void configurarEventos() {
        contenidoLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                toggleRespuesta();
            }
        });
    }
    
    private void toggleRespuesta() {
        mostrandoRespuesta = !mostrandoRespuesta;
        contenidoLabel.setText(mostrandoRespuesta ? 
            ejercicio.getRespuesta() : ejercicio.getContenido());
    }

    @Override
    public boolean validarRespuesta() {
        return true; // Las flashcards no se validan automáticamente
    }
    
    @Override
    public void mostrarResultado(boolean esCorrecta, String mensaje) {
        // No necesario para flashcards
    }
    
}