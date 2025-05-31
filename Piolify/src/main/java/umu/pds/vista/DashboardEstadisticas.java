package umu.pds.vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.BoxLayout;

import umu.pds.modelo.Usuario;
import umu.pds.modelo.Estadisticas;
import umu.pds.modelo.Logro;
import umu.pds.modelo.Curso;

public class DashboardEstadisticas extends JPanel {

    private static final long serialVersionUID = 1L;
    private Color primaryColor = new Color(240, 230, 140); // Color amarillo suave para cabecera
    private Color panelColor = new Color(245, 245, 245);   // Color de fondo para los paneles
    private Color textColor = new Color(70, 70, 70);       // Color para texto principal
    private Color accentColor = new Color(76, 175, 80);    // Color verde para barras de progreso

    /**
     * Constructor del panel de estadísticas
     */
    public DashboardEstadisticas(Usuario usuario) {
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
        JPanel panelResumen = createPanelResumen("Resumen de Actividad");
        GridBagConstraints gbc_panelResumen = new GridBagConstraints();
        gbc_panelResumen.insets = new Insets(0, 0, 5, 5);
        gbc_panelResumen.fill = GridBagConstraints.BOTH;
        gbc_panelResumen.gridx = 1;
        gbc_panelResumen.gridy = 1;
        panelCentral.add(panelResumen, gbc_panelResumen);
        
        // Añadir datos de resumen
        addResumenData(panelResumen, usuario);
        
        // Panel de actividad semanal (arriba a la derecha)
        JPanel panelActividad = createPanelActividad("Actividad Semanal");
        GridBagConstraints gbc_panelActividad = new GridBagConstraints();
        gbc_panelActividad.insets = new Insets(0, 0, 5, 5);
        gbc_panelActividad.fill = GridBagConstraints.BOTH;
        gbc_panelActividad.gridx = 3;
        gbc_panelActividad.gridy = 1;
        panelCentral.add(panelActividad, gbc_panelActividad);
        
        // Añadir gráfico de actividad semanal
        addActividadSemanal(panelActividad, usuario);
        
        // Panel de logros (abajo a la izquierda)
        JPanel panelLogros = createPanelLogros("Logros");
        GridBagConstraints gbc_panelLogros = new GridBagConstraints();
        gbc_panelLogros.insets = new Insets(0, 0, 5, 5);
        gbc_panelLogros.fill = GridBagConstraints.BOTH;
        gbc_panelLogros.gridx = 1;
        gbc_panelLogros.gridy = 3;
        panelCentral.add(panelLogros, gbc_panelLogros);
        
        // Añadir logros
        addLogros(panelLogros, usuario);
        
        // Panel de progreso en cursos (abajo a la derecha)
        JPanel panelProgreso = createPanelProgreso("Progreso en Cursos");
        GridBagConstraints gbc_panelProgreso = new GridBagConstraints();
        gbc_panelProgreso.insets = new Insets(0, 0, 5, 5);
        gbc_panelProgreso.fill = GridBagConstraints.BOTH;
        gbc_panelProgreso.gridx = 3;
        gbc_panelProgreso.gridy = 3;
        panelCentral.add(panelProgreso, gbc_panelProgreso);
        
        // Añadir progreso de cursos
        addProgresoCursos(panelProgreso, usuario);
    }
    
    /**
     * Crea un panel con título para resumen
     */
    private JPanel createPanelResumen(String titulo) {
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
     * Crea un panel con título para actividad
     */
    private JPanel createPanelActividad(String titulo) {
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
     * Crea un panel con título para logros
     */
    private JPanel createPanelLogros(String titulo) {
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
     * Crea un panel con título para progreso
     */
    private JPanel createPanelProgreso(String titulo) {
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
    private void addResumenData(JPanel panel, Usuario usuario) {
        JPanel dataPanel = new JPanel();
        dataPanel.setOpaque(false);
        dataPanel.setLayout(new GridBagLayout());
        
        Estadisticas stats = usuario.getEstadisticas();
        String rachaActual = stats != null ? stats.getRachaDias() + " días" : "0 días";
        String mejorRacha = stats != null ? stats.getMejorRacha() + " días" : "0 días";
        String tiempoTotal = stats != null ? stats.getTiempoTotal() + " min" : "0 min";
        String ejerciciosCompletados = stats != null ? String.valueOf(stats.getTotalEjerciciosCompletados()) : "0";
        String precision = stats != null ? String.format("%.2f", stats.getPrecision()) + "%" : "0%";
        
        String[][] datos = {
            {"Racha actual:", rachaActual},
            {"Mejor racha:", mejorRacha},
            {"Tiempo total:", tiempoTotal},
            {"Ejercicios completados:", ejerciciosCompletados},
            {"Precisión:", precision}
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
    private void addActividadSemanal(JPanel panel, Usuario usuario) {
        // Si tienes datos reales de actividad semanal, ponlos aquí.
        // Por ahora, muestra un mensaje.
        JLabel lbl = new JLabel("Próximamente: actividad semanal real.");
        lbl.setFont(new Font("Arial", Font.ITALIC, 14));
        panel.add(lbl, BorderLayout.CENTER);
    }
    
    /**
     * Añade los logros obtenidos
     */
    private void addLogros(JPanel panel, Usuario usuario) {
        JPanel logrosPanel = new JPanel();
        logrosPanel.setOpaque(false);
        logrosPanel.setLayout(new BoxLayout(logrosPanel, BoxLayout.Y_AXIS));
        
        List<Logro> logros = usuario.getLogros();
        if (logros == null || logros.isEmpty()) {
            JLabel lbl = new JLabel("Aún no tienes logros.");
            lbl.setFont(new Font("Arial", Font.ITALIC, 14));
            logrosPanel.add(lbl);
        } else {
            for (Logro logro : logros) {
                JLabel lbl = new JLabel("• " + logro.getNombre() + ": " + logro.getDescripcion());
                lbl.setFont(new Font("Arial", Font.PLAIN, 14));
                logrosPanel.add(lbl);
            }
        }
        
        panel.add(logrosPanel, BorderLayout.CENTER);
    }
    
    /**
     * Añade el progreso de los cursos
     */
    private void addProgresoCursos(JPanel panel, Usuario usuario) {
        JPanel cursosPanel = new JPanel();
        cursosPanel.setOpaque(false);
        cursosPanel.setLayout(new BoxLayout(cursosPanel, BoxLayout.Y_AXIS));
        
        List<Curso> cursos = usuario.getBiblioteca();
        if (cursos == null || cursos.isEmpty()) {
            JLabel lbl = new JLabel("No tienes cursos en progreso.");
            lbl.setFont(new Font("Arial", Font.ITALIC, 14));
            cursosPanel.add(lbl);
        } else {
            for (Curso curso : cursos) {
                // Si tienes progreso real, cámbialo aquí. Por ahora, solo muestra el nombre.
                JLabel lbl = new JLabel("• " + curso.getTitulo());
                lbl.setFont(new Font("Arial", Font.PLAIN, 14));
                cursosPanel.add(lbl);
            }
        }
        
        panel.add(cursosPanel, BorderLayout.CENTER);
    }
}