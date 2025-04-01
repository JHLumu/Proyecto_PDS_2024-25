package umu.pds.vista;

import umu.pds.modelo.EjercicioFlashcard;
import umu.pds.vista.elementos.BaseRoundedFrame;
import umu.pds.vista.elementos.PioColores;

import java.awt.BorderLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class PioFlashCard extends BaseRoundedFrame {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JLabel contenidoLabel;
    private boolean mostrandoRespuesta = false;
    private EjercicioFlashcard flashcard;

    // Constructor para WindowBuilder
    public PioFlashCard() {
        super();
        initDesignModeComponents();
    }

    // Constructor normal
    public PioFlashCard(EjercicioFlashcard flashcard) {
        super("Flashcard");
        this.flashcard = flashcard;
        initRuntimeComponents();
    }

    private void initDesignModeComponents() {
        // Componentes visibles en WindowBuilder
        contenidoLabel = new JLabel("Pregunta de ejemplo", SwingConstants.CENTER);
        contenidoLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        contentPanel.add(contenidoLabel, BorderLayout.CENTER);
    }

    private void initRuntimeComponents() {
        setSize(400, 300);
        setLocationRelativeTo(null);
        
        // Configuración específica de ejecución
        setBackgroundColor(PioColores.AMARILLO_LABEL);
        // setTitleBarColor(PioColores.AMARILLO_LABEL);
        setCloseButtonColor(PioColores.ROJO);
        
        contenidoLabel = new JLabel(flashcard.getContenido(), SwingConstants.CENTER);
        contenidoLabel.setFont(new Font("Arial", Font.BOLD, 24));
        contenidoLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        contenidoLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                toggleRespuesta();
            }
        });

        contentPanel.add(contenidoLabel, BorderLayout.CENTER);
    }

    private void toggleRespuesta() {
        mostrandoRespuesta = !mostrandoRespuesta;
        contenidoLabel.setText(mostrandoRespuesta ? 
            flashcard.getRespuesta() : flashcard.getContenido());
    }

    public static void main(String[] args) {
        EjercicioFlashcard fc = new EjercicioFlashcard("¿Capital de Francia?", "París");
        SwingUtilities.invokeLater(() -> {
            PioFlashCard frame = new PioFlashCard(fc);
            frame.setVisible(true);
        });
    }
}