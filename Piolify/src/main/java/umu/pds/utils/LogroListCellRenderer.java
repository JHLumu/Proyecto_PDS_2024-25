package umu.pds.utils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import umu.pds.modelo.Logro;

import java.awt.*;
import java.text.SimpleDateFormat;

public class LogroListCellRenderer extends JPanel implements ListCellRenderer<Logro> {
    
    private static final long serialVersionUID = 1L;
    private JLabel lblIcon;
    private JLabel lblNombre;
    private JLabel lblDescripcion;
    private JLabel lblFecha;
    private SimpleDateFormat dateFormat;
    
    /**
     * Constructor por defecto que inicializa el renderizador de celdas para la lista de logros.
     * Configura el diseño, los paneles internos y los componentes necesarios para mostrar
     * el icono, nombre, descripción y fecha del logro.
     */
    public LogroListCellRenderer() {
    	
        setLayout(new BorderLayout(15, 5)); 
        setBorder(new EmptyBorder(10, 15, 10, 15));
        dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        
        // Panel izquierdo para el icono
        JPanel panelIcon = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        panelIcon.setPreferredSize(new Dimension(70, 60)); 
        panelIcon.setMinimumSize(new Dimension(70, 60));
        panelIcon.setMaximumSize(new Dimension(70, 60));
        
        lblIcon = new JLabel("");
        lblIcon.setHorizontalAlignment(JLabel.CENTER);
        lblIcon.setVerticalAlignment(JLabel.CENTER);
        lblIcon.setPreferredSize(new Dimension(60, 60)); 
        panelIcon.add(lblIcon);
        
        // Panel central para nombre y descripción
        JPanel panelInfo = new JPanel();
        panelInfo.setLayout(new BoxLayout(panelInfo, BoxLayout.Y_AXIS));
        panelInfo.setOpaque(false);
        
        lblNombre = new JLabel();
        lblNombre.setFont(new Font("Arial", Font.BOLD, 16)); // Fuente más grande
        lblNombre.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        lblDescripcion = new JLabel();
        lblDescripcion.setFont(new Font("Arial", Font.PLAIN, 13)); // Fuente más grande
        lblDescripcion.setForeground(Color.GRAY);
        lblDescripcion.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        panelInfo.add(lblNombre);
        panelInfo.add(Box.createVerticalStrut(3));
        panelInfo.add(lblDescripcion);
        
        // Panel derecho para la fecha
        JPanel panelFecha = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        panelFecha.setOpaque(false);
        panelFecha.setPreferredSize(new Dimension(80, 60)); // Tamaño fijo
        
        lblFecha = new JLabel();
        lblFecha.setFont(new Font("Arial", Font.ITALIC, 11));
        lblFecha.setForeground(Color.GRAY);
        panelFecha.add(lblFecha);
        
        add(panelIcon, BorderLayout.WEST);
        add(panelInfo, BorderLayout.CENTER);
        add(panelFecha, BorderLayout.EAST);
    }
    
    /**
     * Método que se invoca para renderizar cada celda de la lista de logros.
     * Configura el contenido de los componentes según el logro proporcionado,
     * y ajusta los colores y bordes según el estado de selección y foco.
     *
     * @param list La lista que contiene los logros.
     * @param logro El logro a renderizar.
     * @param index El índice del logro en la lista.
     * @param isSelected Indica si la celda está seleccionada.
     * @param cellHasFocus Indica si la celda tiene el foco.
     * @return El componente configurado para mostrar el logro.
     */
    @Override
    public Component getListCellRendererComponent(JList<? extends Logro> list, 
            Logro logro, int index, boolean isSelected, boolean cellHasFocus) {
        
        // Configurar contenido
        lblNombre.setText(logro.getNombre());
        lblDescripcion.setText(logro.getDescripcion());
        
        ImageIcon iconoOriginal = ImageUtils.escalarImagen(logro.getImagen(), 60);
        if (iconoOriginal != null) {
            // Asegurar que el icono se ajuste perfectamente
            Image img = iconoOriginal.getImage();
            Image imgEscalada = img.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
            lblIcon.setIcon(new ImageIcon(imgEscalada));
        } else {
            // Icono por defecto si no se puede cargar
            lblIcon.setText("🏆");
            lblIcon.setFont(new Font("Arial", Font.PLAIN, 40));
        }
        
        if (logro.getFecha() != null) {
            lblFecha.setText(dateFormat.format(logro.getFecha()));
        } else {
            lblFecha.setText("");
        }
        
        // Configurar colores según selección
        if (isSelected) {
            setBackground(list.getSelectionBackground());
            lblNombre.setForeground(list.getSelectionForeground());
            setOpaque(true);
        } else {
            setBackground(list.getBackground());
            lblNombre.setForeground(list.getForeground());
            setOpaque(index % 2 == 0); // Alternar fondo para filas pares
            if (index % 2 == 0) {
                setBackground(new Color(248, 248, 248));
            }
        }
        
        // Borde de foco
        if (cellHasFocus) {
            setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(list.getSelectionBackground(), 1),
                new EmptyBorder(9, 14, 9, 14)
            ));
        } else {
            setBorder(new EmptyBorder(10, 15, 10, 15));
        }
        
        return this;
    }
}