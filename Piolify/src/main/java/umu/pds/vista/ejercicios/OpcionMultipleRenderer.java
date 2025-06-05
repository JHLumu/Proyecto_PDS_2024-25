package umu.pds.vista.ejercicios;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;

import umu.pds.modelo.EjercicioOpcionMultiple;
import umu.pds.vista.elementos.PioColores;

public class OpcionMultipleRenderer implements EjercicioRenderer {
    private JLabel preguntaLabel;
    private JPanel opcionesPanel;
    private ButtonGroup opcionesGroup;
    private JButton validarButton;
    private JLabel resultadoLabel;
    private EjercicioOpcionMultiple ejercicio;
    @Override
    public void renderizar(JPanel containerPanel, Object ejercicioObj) {
        this.ejercicio = (EjercicioOpcionMultiple) ejercicioObj;
        containerPanel.setLayout(new BorderLayout());
        
        // Pregunta
        preguntaLabel = new JLabel("<html><div style='text-align: center;'>" + 
                                  ejercicio.getContenido() + "</div></html>", 
                                  SwingConstants.CENTER);
        preguntaLabel.setFont(new Font("Arial", Font.BOLD, 18));
        preguntaLabel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        containerPanel.add(preguntaLabel, BorderLayout.NORTH);

        // Opciones
        opcionesPanel = new JPanel(new GridLayout(0, 1, 5, 5));
        opcionesPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        opcionesPanel.setOpaque(false);
        opcionesGroup = new ButtonGroup();
        
        List<String> opciones = ejercicio.getOpciones();
        for (int i = 0; i < opciones.size(); i++) {
            JRadioButton radioButton = new JRadioButton(
                (char)('A' + i) + ") " + opciones.get(i));
            radioButton.setFont(new Font("Arial", Font.PLAIN, 14));
            radioButton.setOpaque(false);
            radioButton.setActionCommand(String.valueOf((char)('A' + i)));
            opcionesGroup.add(radioButton);
            opcionesPanel.add(radioButton);
        }
        
        containerPanel.add(opcionesPanel, BorderLayout.CENTER);

        // Panel inferior
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setOpaque(false);
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setOpaque(false);
        
        validarButton = new JButton("Validar");
        validarButton.setFont(new Font("Arial", Font.BOLD, 14));
        validarButton.setBackground(Color.GREEN);
        validarButton.setForeground(Color.WHITE);
        validarButton.setFocusPainted(false);
        buttonPanel.add(validarButton);
        
        resultadoLabel = new JLabel("", SwingConstants.CENTER);
        resultadoLabel.setFont(new Font("Arial", Font.BOLD, 14));
        
        bottomPanel.add(buttonPanel, BorderLayout.NORTH);
        bottomPanel.add(resultadoLabel, BorderLayout.CENTER);
        
        containerPanel.add(bottomPanel, BorderLayout.SOUTH);
        configurarEventos();
    }
    
    @Override
    public void configurarEventos() {
        validarButton.addActionListener(e -> {
            boolean esCorrecta = validarRespuesta();
            // El resultado ya se muestra en validarRespuesta()
        });
    }
    
    @Override
    public boolean validarRespuesta() {
        ButtonModel seleccionado = opcionesGroup.getSelection();
        if (seleccionado == null) {
            mostrarResultado(false, "Por favor, selecciona una opción");
            return false;
        }
        
        String respuestaUsuario = seleccionado.getActionCommand();
        boolean esCorrecta = ejercicio.validarRespuesta(respuestaUsuario);
        
        if (esCorrecta) {
            mostrarResultado(true, "¡Correcto!");
        } else {
            mostrarResultado(false, "Incorrecto. La respuesta correcta es: " + 
                           ejercicio.getRespuesta());
        }
        
        validarButton.setEnabled(false);
        return esCorrecta;
    }
    
    @Override
    public void mostrarResultado(boolean esCorrecta, String mensaje) {
        resultadoLabel.setText(mensaje);
        resultadoLabel.setForeground(esCorrecta ? Color.GREEN : PioColores.ROJO);
    }
}
