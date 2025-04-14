package umu.pds.vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

import umu.pds.utils.Utils;
import umu.pds.vista.elementos.PioColores;

/**
 * Panel que muestra las estadísticas del usuario
 * 
 * NOTA: Este panel es un ejemplo de cómo se podría implementar una vista de estadísticas.
 * para que sea funcional, debemos usar las funciones del controlador.
 */
public class Estadisticas extends JPanel {

    private static final long serialVersionUID = 1L;
    private Color primaryColor = new Color(240, 230, 140); // Color amarillo suave para cabecera
    private Color panelColor = new Color(245, 245, 245);   // Color de fondo para los paneles
    private Color textColor = new Color(70, 70, 70);       // Color para texto principal
    private Color accentColor = new Color(76, 175, 80);    // Color verde para barras de progreso

    /**
     * Constructor del panel de estadísticas
     */
    public Estadisticas() {
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(5, 5, 5, 5));
        setLayout(new BorderLayout(0, 0));
        
        // Panel central con la información de estadísticas
        JPanel panelCentral = new JPanel();
        panelCentral.setBackground(Color.WHITE);
        add(panelCentral, BorderLayout.CENTER);
        
        // Usamos GridBagLayout para organizar los paneles de estadísticas
        GridBagLayout gbl_panelCentral = new GridBagLayout();
        gbl_panelCentral.columnWidths = new int[]{20, 0, 20, 0, 20, 0};
        gbl_panelCentral.rowHeights = new int[]{20, 0, 20, 0, 20, 0};
        gbl_panelCentral.columnWeights = new double[]{0.0, 0.5, 0.0, 0.5, 0.0, Double.MIN_VALUE};
        gbl_panelCentral.rowWeights = new double[]{0.0, 0.4, 0.0, 0.6, 0.0, Double.MIN_VALUE};
        panelCentral.setLayout(gbl_panelCentral);
        
        // Panel de resumen de actividad (arriba a la izquierda)
        JPanel panelResumen = createPanel("Resumen de Actividad");
        GridBagConstraints gbc_panelResumen = new GridBagConstraints();
        gbc_panelResumen.insets = new Insets(0, 0, 5, 5);
        gbc_panelResumen.fill = GridBagConstraints.BOTH;
        gbc_panelResumen.gridx = 1;
        gbc_panelResumen.gridy = 1;
        panelCentral.add(panelResumen, gbc_panelResumen);
        
        // Añadir datos de resumen
        addResumenData(panelResumen);
        
        // Panel de actividad semanal (arriba a la derecha)
        JPanel panelActividad = createPanel("Actividad Semanal");
        GridBagConstraints gbc_panelActividad = new GridBagConstraints();
        gbc_panelActividad.insets = new Insets(0, 0, 5, 5);
        gbc_panelActividad.fill = GridBagConstraints.BOTH;
        gbc_panelActividad.gridx = 3;
        gbc_panelActividad.gridy = 1;
        panelCentral.add(panelActividad, gbc_panelActividad);
        
        // Añadir gráfico de actividad semanal
        addActividadSemanal(panelActividad);
        
        // Panel de logros (abajo a la izquierda)
        JPanel panelLogros = createPanel("Logros");
        GridBagConstraints gbc_panelLogros = new GridBagConstraints();
        gbc_panelLogros.insets = new Insets(0, 0, 5, 5);
        gbc_panelLogros.fill = GridBagConstraints.BOTH;
        gbc_panelLogros.gridx = 1;
        gbc_panelLogros.gridy = 3;
        panelCentral.add(panelLogros, gbc_panelLogros);
        
        // Añadir logros
        addLogros(panelLogros);
        
        // Panel de progreso en cursos (abajo a la derecha)
        JPanel panelProgreso = createPanel("Progreso en Cursos");
        GridBagConstraints gbc_panelProgreso = new GridBagConstraints();
        gbc_panelProgreso.insets = new Insets(0, 0, 5, 5);
        gbc_panelProgreso.fill = GridBagConstraints.BOTH;
        gbc_panelProgreso.gridx = 3;
        gbc_panelProgreso.gridy = 3;
        panelCentral.add(panelProgreso, gbc_panelProgreso);
        
        // Añadir progreso de cursos
        addProgresoCursos(panelProgreso);
    }
    
    /**
     * Crea un panel con título
     */
    private JPanel createPanel(String titulo) {
        JPanel panel = new JPanel();
        panel.setBackground(panelColor);
        panel.setBorder(BorderFactory.createCompoundBorder(
            new MatteBorder(1, 1, 1, 1, new Color(220, 220, 220)),
            new EmptyBorder(15, 15, 15, 15)
        ));
        
        panel.setLayout(new BorderLayout(0, 15));
        
        // Título del panel
        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        lblTitulo.setForeground(textColor);
        panel.add(lblTitulo, BorderLayout.NORTH);
        
        return panel;
    }
    
    /**
     * Añade los datos del resumen de actividad
     */
    private void addResumenData(JPanel panel) {
        JPanel dataPanel = new JPanel();
        dataPanel.setOpaque(false);
        dataPanel.setLayout(new GridBagLayout());
        
        // Datos a mostrar
        String[][] datos = {
            {"Racha actual:", "4 días"},
            {"Mejor racha:", "12 días"},
            {"Tiempo esta semana:", "3.5 horas"},
            {"Tiempo total:", "42.8 horas"},
            {"Cursos completados:", "2"}
        };
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(8, 5, 8, 5);
        
        // Añadir cada fila de datos
        for (int i = 0; i < datos.length; i++) {
            // Etiqueta
            JLabel lblEtiqueta = new JLabel(datos[i][0]);
            lblEtiqueta.setFont(new Font("Arial", Font.PLAIN, 14));
            gbc.gridx = 0;
            gbc.gridy = i;
            dataPanel.add(lblEtiqueta, gbc);
            
            // Valor
            JLabel lblValor = new JLabel(datos[i][1]);
            lblValor.setFont(new Font("Arial", Font.BOLD, 14));
            gbc.gridx = 1;
            dataPanel.add(lblValor, gbc);
        }
        
        panel.add(dataPanel, BorderLayout.CENTER);
    }
    
    /**
     * Añade el gráfico de actividad semanal
     */
    private void addActividadSemanal(JPanel panel) {
        JPanel chartPanel = new JPanel();
        chartPanel.setOpaque(false);
        chartPanel.setLayout(new GridBagLayout());
        
        // Nombres de días
        String[] dias = {"Lun", "Mar", "Mié", "Jue", "Vie", "Sáb", "Dom"};
        
        // Valores de actividad (minutos)
        int[] valores = {60, 80, 30, 50, 90, 100, 20};
        
        // Calcular valor máximo para escalar
        int maxValor = 0;
        for (int valor : valores) {
            if (valor > maxValor) maxValor = valor;
        }
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.insets = new Insets(0, 5, 0, 5);
        
        // Añadir las barras para cada día
        for (int i = 0; i < dias.length; i++) {
            JPanel barContainer = new JPanel();
            barContainer.setOpaque(false);
            barContainer.setLayout(new BorderLayout(0, 5));
            
            // Barra vertical
            JProgressBar bar = new JProgressBar(JProgressBar.VERTICAL, 0, maxValor);
            bar.setValue(valores[i]);
            bar.setStringPainted(false);
            bar.setPreferredSize(new Dimension(30, 150));
            bar.setForeground(accentColor);
            bar.setBackground(new Color(240, 240, 240));
            barContainer.add(bar, BorderLayout.CENTER);
            
            // Etiqueta del día
            JLabel lblDia = new JLabel(dias[i], SwingConstants.CENTER);
            lblDia.setFont(new Font("Arial", Font.PLAIN, 12));
            barContainer.add(lblDia, BorderLayout.SOUTH);
            
            gbc.gridx = i;
            chartPanel.add(barContainer, gbc);
        }
        
        // Añadir leyenda
        JPanel legendPanel = new JPanel();
        legendPanel.setOpaque(false);
        legendPanel.setLayout(new BorderLayout());
        
        JLabel lblLegend = new JLabel("Minutos de estudio por día");
        lblLegend.setFont(new Font("Arial", Font.ITALIC, 12));
        legendPanel.add(lblLegend, BorderLayout.WEST);
        
        panel.add(chartPanel, BorderLayout.CENTER);
        panel.add(legendPanel, BorderLayout.SOUTH);
    }
    
    /**
     * Añade los logros obtenidos
     */
    private void addLogros(JPanel panel) {
        JPanel logrosPanel = new JPanel();
        logrosPanel.setOpaque(false);
        logrosPanel.setLayout(new GridBagLayout());
        
        // Datos de logros
        Object[][] logros = {
            {new Color(255, 215, 0), "★", "Constante", "7 días seguidos estudiando"},
            {new Color(192, 192, 192), "✓", "Perfeccionista", "100% en un ejercicio"},
            {new Color(205, 127, 50), "♫", "Melómano", "Completar un curso musical"}
        };
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(10, 5, 10, 5);
        
        // Añadir cada logro
        for (int i = 0; i < logros.length; i++) {
            // Círculo con icono
            JPanel circlePanel = new JPanel();
            circlePanel.setOpaque(false);
            circlePanel.setPreferredSize(new Dimension(40, 40));
            circlePanel.setLayout(new BorderLayout());
            
            JLabel lblIcon = new JLabel((String)logros[i][1], SwingConstants.CENTER);
            lblIcon.setFont(new Font("Arial", Font.BOLD, 18));
            lblIcon.setForeground(Color.WHITE);
            lblIcon.setOpaque(true);
            lblIcon.setBackground((Color)logros[i][0]);
            
            // Hacer el label circular
            lblIcon.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.WHITE, 2),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
            ));
            
            circlePanel.add(lblIcon, BorderLayout.CENTER);
            
            gbc.gridx = 0;
            gbc.gridy = i;
            logrosPanel.add(circlePanel, gbc);
            
            // Panel de información
            JPanel infoPanel = new JPanel();
            infoPanel.setOpaque(false);
            infoPanel.setLayout(new GridBagLayout());
            
            // Título del logro
            JLabel lblTitulo = new JLabel((String)logros[i][2]);
            lblTitulo.setFont(new Font("Arial", Font.BOLD, 14));
            
            GridBagConstraints gbc2 = new GridBagConstraints();
            gbc2.gridx = 0;
            gbc2.gridy = 0;
            gbc2.anchor = GridBagConstraints.WEST;
            infoPanel.add(lblTitulo, gbc2);
            
            // Descripción del logro
            JLabel lblDesc = new JLabel((String)logros[i][3]);
            lblDesc.setFont(new Font("Arial", Font.PLAIN, 12));
            
            gbc2.gridy = 1;
            infoPanel.add(lblDesc, gbc2);
            
            gbc.gridx = 1;
            logrosPanel.add(infoPanel, gbc);
        }
        
        panel.add(logrosPanel, BorderLayout.CENTER);
    }
    
    /**
     * Añade el progreso de los cursos
     */
    private void addProgresoCursos(JPanel panel) {
        JPanel cursosPanel = new JPanel();
        cursosPanel.setOpaque(false);
        cursosPanel.setLayout(new GridBagLayout());
        
        // Datos de cursos
        Object[][] cursos = {
            {"Programación Java", 45, new Color(76, 175, 80)},
            {"Inglés B2", 68, new Color(33, 150, 243)},
            {"Teoría Musical", 22, new Color(255, 87, 34)}
        };
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(15, 0, 15, 0);
        gbc.weightx = 1.0;
        
        // Añadir cada curso
        for (int i = 0; i < cursos.length; i++) {
            // Panel para este curso
            JPanel cursoPanel = new JPanel();
            cursoPanel.setOpaque(false);
            cursoPanel.setLayout(new BorderLayout(0, 5));
            
            // Título del curso
            JLabel lblTitulo = new JLabel((String)cursos[i][0]);
            lblTitulo.setFont(new Font("Arial", Font.BOLD, 14));
            cursoPanel.add(lblTitulo, BorderLayout.NORTH);
            
            // Barra de progreso
            JProgressBar progressBar = new JProgressBar(0, 100);
            progressBar.setValue((int)cursos[i][1]);
            progressBar.setStringPainted(true);
            progressBar.setString(cursos[i][1] + "%");
            progressBar.setForeground((Color)cursos[i][2]);
            progressBar.setBackground(new Color(240, 240, 240));
            cursoPanel.add(progressBar, BorderLayout.CENTER);
            
            gbc.gridy = i;
            cursosPanel.add(cursoPanel, gbc);
        }
        
        panel.add(cursosPanel, BorderLayout.CENTER);
    }
}