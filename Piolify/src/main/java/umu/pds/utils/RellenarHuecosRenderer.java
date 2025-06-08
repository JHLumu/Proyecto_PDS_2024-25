package umu.pds.utils;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import umu.pds.modelo.EjercicioRellenarHuecos;
import umu.pds.vista.elementos.PioColores;

public class RellenarHuecosRenderer implements EjercicioRenderer {
    private JPanel contenidoPanel;
    private List<JTextField> camposHuecos;
    private JButton solucionButton;
    private JLabel resultadoLabel;
    private EjercicioRellenarHuecos ejercicio;
    private JPanel containerPanel;
    
    @Override
    public void renderizar(JPanel containerPanel, Object ejercicioObj) {
        this.ejercicio = (EjercicioRellenarHuecos) ejercicioObj;
        this.containerPanel = containerPanel;
        containerPanel.setLayout(new BorderLayout());
        
        camposHuecos = new ArrayList<>();
        
        // Panel principal con scroll
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);
        
        JPanel containerPanelInterno = new JPanel(new BorderLayout());
        containerPanelInterno.setOpaque(false);
        
        contenidoPanel = new JPanel();
        contenidoPanel.setLayout(new BoxLayout(contenidoPanel, BoxLayout.Y_AXIS));
        contenidoPanel.setOpaque(false);
        contenidoPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        crearContenidoConHuecos();
        
        containerPanelInterno.add(contenidoPanel, BorderLayout.NORTH);
        scrollPane.setViewportView(containerPanelInterno);
        containerPanel.add(scrollPane, BorderLayout.CENTER);

        // Panel inferior
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setOpaque(false);
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setOpaque(false);
        
        solucionButton = new JButton("Solución");
        solucionButton.setFont(new Font("Arial", Font.BOLD, 14));
        solucionButton.setBackground(Color.BLUE);
        solucionButton.setForeground(Color.WHITE);
        solucionButton.setFocusPainted(false);
        buttonPanel.add(solucionButton);
        
        resultadoLabel = new JLabel("", SwingConstants.CENTER);
        resultadoLabel.setFont(new Font("Arial", Font.BOLD, 14));
        
        bottomPanel.add(buttonPanel, BorderLayout.NORTH);
        bottomPanel.add(resultadoLabel, BorderLayout.CENTER);
        
        containerPanel.add(bottomPanel, BorderLayout.SOUTH);
        configurarEventos();
    }
    
    private void crearContenidoConHuecos() {
        // Implementación similar a la original
        String contenido = ejercicio.getContenido();
        Pattern pattern = Pattern.compile("(\\{[^}]*\\}|\\[[^\\]]*\\]|_{3,})");
        Matcher matcher = pattern.matcher(contenido);
        
        JPanel lineaPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        lineaPanel.setOpaque(false);
        
        int ultimaPosicion = 0;
        Font fuente = new Font("Arial", Font.PLAIN, 16);
        
        while (matcher.find()) {
            String textoAntes = contenido.substring(ultimaPosicion, matcher.start());
            if (!textoAntes.isEmpty()) {
                agregarTextoEnLineas(lineaPanel, textoAntes, fuente);
            }
            
            JTextField campoHueco = new JTextField(10);
            campoHueco.setFont(fuente);
            campoHueco.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(PioColores.GRIS_TEXT, 2),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
            ));
            camposHuecos.add(campoHueco);
            lineaPanel.add(campoHueco);
            
            ultimaPosicion = matcher.end();
        }
        
        if (ultimaPosicion < contenido.length()) {
            String textoRestante = contenido.substring(ultimaPosicion);
            agregarTextoEnLineas(lineaPanel, textoRestante, fuente);
        }
        
        contenidoPanel.add(lineaPanel);
    }
    
    private void agregarTextoEnLineas(JPanel panel, String texto, Font fuente) {
        String[] palabras = texto.split("\\s+");
        for (String palabra : palabras) {
            if (!palabra.trim().isEmpty()) {
                JLabel label = new JLabel(palabra + " ");
                label.setFont(fuente);
                panel.add(label);
            }
        }
    }
    
    @Override
    public void configurarEventos() {
        solucionButton.addActionListener(e -> {
            mostrarSolucion();
        });
    }
    
    @Override
    public boolean validarRespuesta() {
        // Implementación de validación similar a la original
        if (camposHuecos.isEmpty()) {
            mostrarResultado(false, "No hay huecos para completar");
            return false;
        }
        
        for (JTextField campo : camposHuecos) {
            if (campo.getText().trim().isEmpty()) {
                mostrarResultado(false, "Por favor, completa todos los huecos");
                return false;
            }
        }
        
        String[] respuestasCorrectas = ejercicio.getRespuesta().split("\\|");
        int camposCorrectos = 0;
        
        for (int i = 0; i < camposHuecos.size(); i++) {
            JTextField campo = camposHuecos.get(i);
            String respuestaUsuario = campo.getText().trim();
            
            boolean esCorrecto = false;
            if (i < respuestasCorrectas.length) {
                esCorrecto = respuestaUsuario.equalsIgnoreCase(respuestasCorrectas[i].trim());
            }
            
            if (esCorrecto) {
                campo.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Color.GREEN, 2),
                    BorderFactory.createEmptyBorder(5, 5, 5, 5)
                ));
                camposCorrectos++;
            } else {
                campo.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(PioColores.ROJO, 2),
                    BorderFactory.createEmptyBorder(5, 5, 5, 5)
                ));
            }
            
            campo.setEditable(false);
        }
        
        boolean todasCorrectas = camposCorrectos == camposHuecos.size();
        if (todasCorrectas) {
            mostrarResultado(true, "¡Correcto! Todas las respuestas son correctas.");
        } else {
            mostrarResultado(false, String.format("Incorrecto. %d de %d respuestas correctas.", 
                           camposCorrectos, camposHuecos.size()));
        }
        
        return todasCorrectas;
    }
    
    private void mostrarSolucion() {
        if (camposHuecos.isEmpty()) {
            mostrarResultado(true, "No hay huecos para mostrar");
            return;
        }
        
        String[] respuestasCorrectas = ejercicio.getRespuesta().split("\\|");
        
        // Rellenar los campos con las respuestas correctas
        for (int i = 0; i < camposHuecos.size(); i++) {
            JTextField campo = camposHuecos.get(i);
            if (i < respuestasCorrectas.length) {
                campo.setText(respuestasCorrectas[i].trim());
            }
            campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.BLUE, 2),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
            ));
            campo.setEditable(false);
        }
        
        mostrarResultado(true, "Solución mostrada");
        solucionButton.setEnabled(false);
    }
    
    @Override
    public void mostrarResultado(boolean esCorrecta, String mensaje) {
        resultadoLabel.setText(mensaje);
        resultadoLabel.setForeground(Color.BLUE);
    }
}