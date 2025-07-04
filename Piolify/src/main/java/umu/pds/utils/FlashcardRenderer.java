package umu.pds.utils;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


import javax.swing.JPanel;
import javax.swing.JTextPane;

import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import umu.pds.modelo.EjercicioFlashcard;



public class FlashcardRenderer implements EjercicioRenderer {
   
	private JTextPane contenido;
    private boolean mostrandoRespuesta = false;
    private EjercicioFlashcard ejercicio;
    private JPanel panelCentral;
    private StyledDocument doc; 
    private SimpleAttributeSet center;
    
    /* * Renderiza el ejercicio de tipo Flashcard.
     * 
     * @param containerPanel Panel contenedor donde se renderizará el ejercicio.
     * @param ejercicioObj Objeto del ejercicio a renderizar, debe ser de tipo EjercicioFlashcard.
     */
    @SuppressWarnings("serial")
	@Override
    public void renderizar(JPanel containerPanel, Object ejercicioObj) {
        this.ejercicio = (EjercicioFlashcard) ejercicioObj;
        containerPanel.setLayout(new BorderLayout());

        contenido = new JTextPane();
        contenido.setText(ejercicio.getContenido());
        
        doc = contenido.getStyledDocument();
        center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);	
        doc.setParagraphAttributes(0, doc.getLength(), center, false);
        
        contenido.setFont(new Font("Arial", Font.BOLD, 24));
        contenido.setCursor(new Cursor(Cursor.HAND_CURSOR));
        contenido.setEditable(false);
        contenido.setOpaque(false);
        contenido.setBorder(null);
 
        panelCentral = new JPanel(new GridBagLayout());
        panelCentral.setOpaque(false);
        
        GridBagConstraints gbcBordeSuperior = new GridBagConstraints();
        gbcBordeSuperior.gridx=0;
        gbcBordeSuperior.weightx=1.0;
        gbcBordeSuperior.gridy=0;
        gbcBordeSuperior.weighty=1.0;
        gbcBordeSuperior.fill = GridBagConstraints.BOTH;
        panelCentral.add(new JPanel(){{setOpaque(false);}}, gbcBordeSuperior);
        
        GridBagConstraints gbcContenido = new GridBagConstraints();
        gbcContenido.gridx=0;
        gbcContenido.weightx=1.0;
        gbcContenido.gridy=1;
        gbcContenido.weighty=0.0;
        gbcContenido.fill=GridBagConstraints.HORIZONTAL;
        gbcContenido.anchor = GridBagConstraints.CENTER;
        panelCentral.add(contenido, gbcContenido);
        
        GridBagConstraints gbcBordeInferior = new GridBagConstraints();	
        gbcBordeInferior.gridx=0;
        gbcBordeInferior.weightx=1.0;
        gbcBordeInferior.gridy=2;
        gbcBordeInferior.weighty=1.0;
        gbcBordeInferior.fill = GridBagConstraints.BOTH;
        panelCentral.add(new JPanel(){{setOpaque(false);}}, gbcBordeInferior);
        
        containerPanel.add(panelCentral, BorderLayout.CENTER);
        configurarEventos();
    }
    
    /**
     * Configura los eventos del componente.
     * En este caso, al hacer clic en la flashcard, se alterna entre mostrar el contenido y la respuesta.
     */
    @Override
    public void configurarEventos() {
        contenido.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                toggleRespuesta();
                doc.setParagraphAttributes(0, doc.getLength(), center, false);
                panelCentral.revalidate();
                panelCentral.repaint();
            }
        });
    }
    
    /**
     * Alterna entre mostrar el contenido de la flashcard y su respuesta.
     * Si se está mostrando la respuesta, cambia al contenido; si se está mostrando el contenido, cambia a la respuesta.
     */
    private void toggleRespuesta() {
        mostrandoRespuesta = !mostrandoRespuesta;
        contenido.setText(mostrandoRespuesta ? 
            ejercicio.getRespuesta() : ejercicio.getContenido());
    }

    /**
     * Valida la respuesta del ejercicio.
     * @return true si la respuesta es válida, false en caso contrario.
     */
    @Override
    public boolean validarRespuesta() {
        return true; // Las flashcards no se validan automáticamente
    }
    
    /**
     * Muestra el resultado de la validación.
     * En el caso de las flashcards, no es necesario mostrar un resultado ya que no se valida la respuesta.
     * @param esCorrecta Indica si la respuesta es correcta o no.
     * @param mensaje Mensaje a mostrar (no utilizado en este caso).
     */
    @Override
    public void mostrarResultado(boolean esCorrecta, String mensaje) {
        // No necesario para flashcards
    }
    
}