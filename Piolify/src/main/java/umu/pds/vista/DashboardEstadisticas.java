package umu.pds.vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.BoxLayout;
import javax.swing.JButton;

import umu.pds.modelo.Usuario;
import umu.pds.vista.elementos.PioButton;
import umu.pds.vista.elementos.PioColores;
import umu.pds.servicios.ServicioEstadisticas;
import umu.pds.servicios.ServicioEstadisticas.EstadisticasCurso;
import umu.pds.controlador.Piolify;
import umu.pds.modelo.Bloque;
import umu.pds.modelo.Curso;
import umu.pds.modelo.Estadisticas;
import umu.pds.modelo.Logro;
import umu.pds.modelo.TipoLogro;

public class DashboardEstadisticas extends JPanel {

    private static final long serialVersionUID = 1L;
    private Usuario usuario;
    private ServicioEstadisticas servicioEstadisticas;
    
    // Referencias a los componentes que necesitamos actualizar
    private JPanel panelResumenData;
    private JPanel panelLogrosContainer;
    private JPanel panelProgresoCursos;
    
    /**
     * Constructor del panel de estadísticas
     */
    public DashboardEstadisticas(Usuario usuario) {
        this.usuario = usuario;
        this.servicioEstadisticas = new ServicioEstadisticas();
        
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(5, 5, 5, 5));
        setLayout(new BorderLayout(0, 0));
        
        initComponents();
        cargarDatos();
    }
    
    private void initComponents() {
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
        JPanel panelResumen = new JPanel();
        panelResumen.setBackground(PioColores.GRIS_PANEL);
        panelResumen.setBorder(BorderFactory.createCompoundBorder(
            new MatteBorder(1, 1, 1, 1, new Color(220, 220, 220)),
            new EmptyBorder(15, 15, 15, 15)
        ));
        panelResumen.setLayout(new BorderLayout(0, 15));
        
        // Título del panel resumen
        JLabel lblTituloResumen = new JLabel("Resumen General");
        lblTituloResumen.setFont(new Font("Arial", Font.BOLD, 16));
        lblTituloResumen.setForeground(PioColores.GRIS_TEXT);
        panelResumen.add(lblTituloResumen, BorderLayout.NORTH);
        
        // Panel de datos del resumen (se actualizará dinámicamente)
        panelResumenData = new JPanel();
        panelResumenData.setOpaque(false);
        panelResumen.add(panelResumenData, BorderLayout.CENTER);
        
        GridBagConstraints gbc_panelResumen = new GridBagConstraints();
        gbc_panelResumen.insets = new Insets(0, 0, 5, 5);
        gbc_panelResumen.fill = GridBagConstraints.BOTH;
        gbc_panelResumen.gridx = 1;
        gbc_panelResumen.gridy = 1;
        panelCentral.add(panelResumen, gbc_panelResumen);
        
        // Panel de logros (arriba a la derecha)
        JPanel panelLogros = new JPanel();
        panelLogros.setBackground(PioColores.GRIS_PANEL);
        panelLogros.setBorder(BorderFactory.createCompoundBorder(
            new MatteBorder(1, 1, 1, 1, new Color(220, 220, 220)),
            new EmptyBorder(15, 15, 15, 15)
        ));
        panelLogros.setLayout(new BorderLayout(0, 15));
        
        // Título del panel logros
        JLabel lblTituloLogros = new JLabel("Logros Obtenidos");
        lblTituloLogros.setFont(new Font("Arial", Font.BOLD, 16));
        lblTituloLogros.setForeground(PioColores.GRIS_TEXT);
        panelLogros.add(lblTituloLogros, BorderLayout.NORTH);
        
        // Contenedor de logros (se actualizará dinámicamente)
        panelLogrosContainer = new JPanel();
        panelLogrosContainer.setOpaque(false);
        panelLogros.add(panelLogrosContainer, BorderLayout.CENTER);
        
        GridBagConstraints gbc_panelLogros = new GridBagConstraints();
        gbc_panelLogros.insets = new Insets(0, 0, 5, 5);
        gbc_panelLogros.fill = GridBagConstraints.BOTH;
        gbc_panelLogros.gridx = 3;
        gbc_panelLogros.gridy = 1;
        panelCentral.add(panelLogros, gbc_panelLogros);
        
        // Panel de progreso en cursos (abajo, spanning dos columnas)
        JPanel panelProgreso = new JPanel();
        panelProgreso.setBackground(PioColores.GRIS_PANEL);
        panelProgreso.setBorder(BorderFactory.createCompoundBorder(
            new MatteBorder(1, 1, 1, 1, new Color(220, 220, 220)),
            new EmptyBorder(15, 15, 15, 15)
        ));
        panelProgreso.setLayout(new BorderLayout(0, 15));
        
        // Título del panel progreso
        JLabel lblTituloProgreso = new JLabel("Progreso por Cursos");
        lblTituloProgreso.setFont(new Font("Arial", Font.BOLD, 16));
        lblTituloProgreso.setForeground(PioColores.GRIS_TEXT);
        panelProgreso.add(lblTituloProgreso, BorderLayout.NORTH);
        
        // Contenedor de progreso de cursos (se actualizará dinámicamente)
        panelProgresoCursos = new JPanel();
        panelProgresoCursos.setOpaque(false);
        panelProgreso.add(panelProgresoCursos, BorderLayout.CENTER);
        
        GridBagConstraints gbc_panelProgreso = new GridBagConstraints();
        gbc_panelProgreso.insets = new Insets(0, 0, 5, 5);
        gbc_panelProgreso.fill = GridBagConstraints.BOTH;
        gbc_panelProgreso.gridx = 1;
        gbc_panelProgreso.gridy = 3;
        gbc_panelProgreso.gridwidth = 3; // Spanning dos columnas
        panelCentral.add(panelProgreso, gbc_panelProgreso);
    }
    
    /**
     * Carga todos los datos actualizados
     */
    private void cargarDatos() {
        // Obtener usuario actualizado desde el controlador
        try {
            this.usuario = Piolify.getUnicaInstancia().getUsuarioActual();
            
            // Actualizar estadísticas desde la base de datos
            servicioEstadisticas.actualizarEstadisticasUsuario(usuario);
            
            // Recargar usuario actualizado después de actualizar estadísticas
            this.usuario = Piolify.getUnicaInstancia().getUsuarioActual();
            
            // Cargar datos en los paneles
            cargarResumenData();
            cargarLogros();
            cargarProgresoCursos();
            
        } catch (Exception e) {
            System.err.println("Error al cargar datos de estadísticas: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Carga los datos del resumen de actividad
     */
    private void cargarResumenData() {
        panelResumenData.removeAll();
        panelResumenData.setLayout(new GridBagLayout());
        
        Estadisticas stats = usuario.getEstadisticas();
        String rachaActual = stats != null ? stats.getRachaDias() + " días" : "0 días";
        String mejorRacha = stats != null ? stats.getMejorRacha() + " días" : "0 días";
        
        // El tiempo total ya está en segundos en la base de datos
        String tiempoTotal;
        if (stats != null) {
            int tiempoTotalSegundos = stats.getTiempoTotal(); // Ya está en segundos
            int minutos = tiempoTotalSegundos / 60;
            int segundos = tiempoTotalSegundos % 60;
            tiempoTotal = minutos + "m " + segundos + "s";
        } else {
            tiempoTotal = "0m 0s";
        }
        
        String ejerciciosCompletados = stats != null ? String.valueOf(stats.getTotalEjerciciosCompletados()) : "0";
        String precision = stats != null ? String.format("%.1f%%", stats.getPrecision()) : "0%";
        
        String[][] datos = {
            {"Racha actual:", rachaActual},
            {"Mejor racha:", mejorRacha},
            {"Tiempo total:", tiempoTotal},
            {"Ejercicios completados:", ejerciciosCompletados},
            {"Precisión promedio:", precision}
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
            panelResumenData.add(lblEtiqueta, gbc);
            
            // Valor
            JLabel lblValor = new JLabel(datos[i][1]);
            lblValor.setFont(new Font("Arial", Font.BOLD, 14));
            lblValor.setForeground(PioColores.MARRON_BUTTON.darker());
            gbc.gridx = 1;
            gbc.insets = new Insets(8, 15, 8, 5);
            panelResumenData.add(lblValor, gbc);
            gbc.insets = new Insets(8, 5, 8, 5); // Reset insets
        }
        
        panelResumenData.revalidate();
        panelResumenData.repaint();
    }
    
    /**
     * Carga los logros obtenidos
     */
    private void cargarLogros() {
        panelLogrosContainer.removeAll();
        panelLogrosContainer.setLayout(new BoxLayout(panelLogrosContainer, BoxLayout.Y_AXIS));
        
        usuario.desbloquearLogro(TipoLogro.PRIMER_CURSO);
        List<Logro> logros = usuario.getLogros();
        if (logros == null || logros.isEmpty()) {
            JLabel lbl = new JLabel("Aún no tienes logros.");
            lbl.setFont(new Font("Arial", Font.ITALIC, 14));
            lbl.setForeground(PioColores.GRIS_TEXT);
            panelLogrosContainer.add(lbl);
        } else {
            for (Logro logro : logros.subList(0, Math.min(logros.size(), 5))) { // Mostrar máximo 5
                JLabel lbl = new JLabel("🏆 " + logro.getNombre());
                lbl.setFont(new Font("Arial", Font.PLAIN, 14));
                lbl.setToolTipText(logro.getDescripcion());
                panelLogrosContainer.add(lbl);
            }
            
            if (logros.size() > 5) {
                JLabel lblMas = new JLabel("+" + (logros.size() - 5) + " logros más...");
                lblMas.setFont(new Font("Arial", Font.ITALIC, 12));
                lblMas.setForeground(PioColores.GRIS_TEXT);
                panelLogrosContainer.add(lblMas);
            }
        }
        
        panelLogrosContainer.revalidate();
        panelLogrosContainer.repaint();
    }
    
    /**
     * Carga el progreso de los cursos
     */
    private void cargarProgresoCursos() {
        panelProgresoCursos.removeAll();
        panelProgresoCursos.setLayout(new BorderLayout());
        
        JPanel cursosContainer = new JPanel();
        cursosContainer.setOpaque(false);
        cursosContainer.setLayout(new BoxLayout(cursosContainer, BoxLayout.Y_AXIS));
        
        try {
            List<EstadisticasCurso> estadisticasCursos = servicioEstadisticas.obtenerEstadisticasTodosCursos(usuario);
            
            if (estadisticasCursos.isEmpty()) {
                JLabel lbl = new JLabel("No has comenzado ningún curso aún.");
                lbl.setFont(new Font("Arial", Font.ITALIC, 14));
                lbl.setForeground(PioColores.GRIS_TEXT);
                cursosContainer.add(lbl);
            } else {
                for (EstadisticasCurso estadistica : estadisticasCursos) {
                    JPanel cursoPanel = crearPanelCurso(estadistica);
                    cursosContainer.add(cursoPanel);
                }
            }
        } catch (Exception e) {
            System.err.println("Error al cargar estadísticas de cursos: " + e.getMessage());
            JLabel lblError = new JLabel("Error al cargar estadísticas de cursos");
            lblError.setFont(new Font("Arial", Font.ITALIC, 14));
            lblError.setForeground(Color.RED);
            cursosContainer.add(lblError);
        }
        
        JScrollPane scrollPane = new JScrollPane(cursosContainer);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        
        panelProgresoCursos.add(scrollPane, BorderLayout.CENTER);
        panelProgresoCursos.revalidate();
        panelProgresoCursos.repaint();
    }
    
    /**
     * Crea un panel individual para mostrar estadísticas de un curso
     */
    private JPanel crearPanelCurso(EstadisticasCurso estadistica) {
        JPanel cursoPanel = new JPanel(new BorderLayout());
        cursoPanel.setOpaque(false);
        cursoPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        cursoPanel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
        // Hacer el panel clickeable para ver detalles
        cursoPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                mostrarDetallesCurso(estadistica);
            }
            
            @Override
            public void mouseEntered(MouseEvent e) {
                cursoPanel.setOpaque(true);
                cursoPanel.setBackground(new Color(240, 240, 240));
                cursoPanel.repaint();
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                cursoPanel.setOpaque(false);
                cursoPanel.repaint();
            }
        });
        
        // Información principal del curso
        JPanel infoPanel = new JPanel(new GridBagLayout());
        infoPanel.setOpaque(false);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(2, 0, 2, 15);
        
        // Título del curso
        JLabel lblTitulo = new JLabel(estadistica.getCurso().getTitulo());
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        infoPanel.add(lblTitulo, gbc);
        
        gbc.gridwidth = 1;
        gbc.gridy = 1;
        
        // Progreso
        gbc.gridx = 0;
        JLabel lblProgreso = new JLabel("Progreso:");
        lblProgreso.setFont(new Font("Arial", Font.PLAIN, 12));
        infoPanel.add(lblProgreso, gbc);
        
        gbc.gridx = 1;
        JLabel lblProgresoValor = new JLabel(String.format("%.1f%%", estadistica.getPorcentajeCompletado()));
        lblProgresoValor.setFont(new Font("Arial", Font.BOLD, 12));
        lblProgresoValor.setForeground(PioColores.VERDE_BUTTON.darker());
        infoPanel.add(lblProgresoValor, gbc);
        
        // Tiempo dedicado (necesitamos recalcular con segundos)
        gbc.gridx = 0; gbc.gridy = 2;
        JLabel lblTiempo = new JLabel("Tiempo:");
        lblTiempo.setFont(new Font("Arial", Font.PLAIN, 12));
        infoPanel.add(lblTiempo, gbc);
        
        gbc.gridx = 1;
        // Necesitamos obtener el tiempo total en segundos para mostrar m y s
        int tiempoTotalSegundos = estadistica.getTiempoTotalSegundos();
        int minutos = tiempoTotalSegundos / 60;
        int segundos = tiempoTotalSegundos % 60;
        String tiempoTexto = minutos + "m " + segundos + "s";
        JLabel lblTiempoValor = new JLabel(tiempoTexto);
        lblTiempoValor.setFont(new Font("Arial", Font.BOLD, 12));
        infoPanel.add(lblTiempoValor, gbc);
        
        // Precisión
        gbc.gridx = 0; gbc.gridy = 3;
        JLabel lblPrecision = new JLabel("Precisión:");
        lblPrecision.setFont(new Font("Arial", Font.PLAIN, 12));
        infoPanel.add(lblPrecision, gbc);
        
        gbc.gridx = 1;
        JLabel lblPrecisionValor = new JLabel(String.format("%.1f%%", estadistica.getPrecision()));
        lblPrecisionValor.setFont(new Font("Arial", Font.BOLD, 12));
        Color colorPrecision = estadistica.getPrecision() >= 70 ? 
            PioColores.VERDE_BUTTON.darker() : 
            estadistica.getPrecision() >= 50 ? 
                Color.ORANGE.darker() : 
                PioColores.ROJO.darker();
        lblPrecisionValor.setForeground(colorPrecision);
        infoPanel.add(lblPrecisionValor, gbc);
        
        cursoPanel.add(infoPanel, BorderLayout.CENTER);
        
        // Indicador de que es clickeable
        JLabel lblClick = new JLabel("Click para ver detalles >");
        lblClick.setFont(new Font("Arial", Font.ITALIC, 10));
        lblClick.setForeground(PioColores.GRIS_TEXT);
        cursoPanel.add(lblClick, BorderLayout.EAST);
        
        return cursoPanel;
    }
    
    /**
     * Método mejorado para mostrar detalles completos del curso
     */
    private void mostrarDetallesCurso(EstadisticasCurso estadistica) {
        // Crear un panel personalizado para mostrar más detalles
        JPanel panelDetalle = new JPanel(new BorderLayout());
        panelDetalle.setPreferredSize(new Dimension(500, 400));
        
        // Información del curso
        JPanel panelInfo = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Título del curso
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        JLabel lblTitulo = new JLabel(estadistica.getCurso().getTitulo());
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        panelInfo.add(lblTitulo, gbc);
        
        gbc.gridwidth = 1;
        
        // Estadísticas detalladas con tiempo en segundos
        int tiempoTotalSegundos = estadistica.getTiempoTotalSegundos();
        int minutos = tiempoTotalSegundos / 60;
        int segundos = tiempoTotalSegundos % 60;
        String tiempoTexto = minutos + " minutos " + segundos + " segundos";
        
        String[][] datos = {
            {"Progreso:", String.format("%.1f%% completado", estadistica.getPorcentajeCompletado())},
            {"Ejercicios realizados:", String.valueOf(estadistica.getEjerciciosCompletados())},
            {"Tiempo dedicado:", tiempoTexto},
            {"Precisión:", String.format("%.1f%%", estadistica.getPrecision())},
            {"Dificultad:", estadistica.getCurso().getDificultad()},
            {"Autor:", estadistica.getCurso().getAutor() != null ? estadistica.getCurso().getAutor() : "Desconocido"}
        };
        
        for (int i = 0; i < datos.length; i++) {
            gbc.gridx = 0; gbc.gridy = i + 1;
            JLabel lblEtiqueta = new JLabel(datos[i][0]);
            lblEtiqueta.setFont(new Font("Arial", Font.BOLD, 14));
            panelInfo.add(lblEtiqueta, gbc);
            
            gbc.gridx = 1;
            JLabel lblValor = new JLabel(datos[i][1]);
            lblValor.setFont(new Font("Arial", Font.PLAIN, 14));
            panelInfo.add(lblValor, gbc);
        }
        
        panelDetalle.add(panelInfo, BorderLayout.NORTH);
        
        // Descripción del curso
        if (estadistica.getCurso().getDescripcion() != null) {
            JTextArea txtDescripcion = new JTextArea(estadistica.getCurso().getDescripcion());
            txtDescripcion.setLineWrap(true);
            txtDescripcion.setWrapStyleWord(true);
            txtDescripcion.setEditable(false);
            txtDescripcion.setOpaque(false);
            txtDescripcion.setBorder(BorderFactory.createTitledBorder("Descripción"));
            
            JScrollPane scrollDesc = new JScrollPane(txtDescripcion);
            scrollDesc.setPreferredSize(new Dimension(450, 100));
            panelDetalle.add(scrollDesc, BorderLayout.CENTER);
        }
        
        // Botón para continuar estudiando
        JPanel panelBotones = new JPanel(new FlowLayout());
        JButton btnContinuar = new PioButton("Continuar Estudiando");
        btnContinuar.setBackground(PioColores.VERDE_BUTTON);
        btnContinuar.addActionListener(e -> {
            // Abrir el curso para continuar estudiando
            abrirCursoParaEstudiar(estadistica.getCurso());
            SwingUtilities.getWindowAncestor(panelDetalle).dispose();
        });
        panelBotones.add(btnContinuar);
        panelDetalle.add(panelBotones, BorderLayout.SOUTH);
        
        // Mostrar en diálogo
        JOptionPane.showMessageDialog(
            this,
            panelDetalle,
            "Estadísticas de " + estadistica.getCurso().getTitulo(),
            JOptionPane.PLAIN_MESSAGE
        );
    }

    /**
     * Abre un curso para continuar estudiando
     */
    private void abrirCursoParaEstudiar(Curso curso) {
        try {
            // Verificar que el curso tenga bloques y ejercicios
            if (curso.getBloques() == null || curso.getBloques().isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "Este curso no tiene ejercicios disponibles.",
                    "Sin contenido",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Tomar el primer bloque con ejercicios
            Bloque bloqueConEjercicios = null;
            for (Bloque bloque : curso.getBloques()) {
                if (bloque.getEjercicios() != null && !bloque.getEjercicios().isEmpty()) {
                    bloqueConEjercicios = bloque;
                    break;
                }
            }
            
            if (bloqueConEjercicios == null) {
                JOptionPane.showMessageDialog(this,
                    "Este curso no tiene ejercicios disponibles.",
                    "Sin ejercicios",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            final Bloque bloqueParaAbrir = bloqueConEjercicios;
            
            // Abrir PioEjercicios
            SwingUtilities.invokeLater(() -> {
                PioEjercicios ventanaEjercicios = new PioEjercicios(bloqueParaAbrir.getEjercicios());
                ventanaEjercicios.setVisible(true);
            });
            
        } catch (Exception ex) {
            System.err.println("Error al abrir ejercicios: " + ex.getMessage());
            JOptionPane.showMessageDialog(this,
                "Error al abrir los ejercicios: " + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Método público para refrescar las estadísticas
     */
    public void refrescarEstadisticas() {
        // Recargar todos los datos
        cargarDatos();
    }
}