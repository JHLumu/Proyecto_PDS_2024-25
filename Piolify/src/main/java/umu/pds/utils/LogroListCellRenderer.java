package umu.pds.utils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import umu.pds.modelo.Logro;

import java.awt.*;
import java.text.SimpleDateFormat;

public class LogroListCellRenderer extends JPanel implements ListCellRenderer<Logro> {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JLabel lblIcon;
    private JLabel lblNombre;
    private JLabel lblDescripcion;
    private JLabel lblFecha;
    private SimpleDateFormat dateFormat;
    
    public LogroListCellRenderer() {
        setLayout(new BorderLayout(10, 5));
        setBorder(new EmptyBorder(8, 12, 8, 12));
        dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        
        // Panel izquierdo para el icono
        JPanel panelIcon = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        lblIcon = new JLabel("");
        panelIcon.add(lblIcon);
        
        // Panel central para nombre y descripción
        JPanel panelInfo = new JPanel();
        panelInfo.setLayout(new BoxLayout(panelInfo, BoxLayout.Y_AXIS));
        
        lblNombre = new JLabel();
        lblNombre.setFont(new Font("Arial", Font.BOLD, 14));
        
        lblDescripcion = new JLabel();
        lblDescripcion.setFont(new Font("Arial", Font.PLAIN, 12));
        lblDescripcion.setForeground(Color.GRAY);
        
        panelInfo.add(lblNombre);
        panelInfo.add(Box.createVerticalStrut(2));
        panelInfo.add(lblDescripcion);
        
        // Panel derecho para la fecha
        JPanel panelFecha = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        lblFecha = new JLabel();
        lblFecha.setFont(new Font("Arial", Font.ITALIC, 11));
        lblFecha.setForeground(Color.GRAY);
        panelFecha.add(lblFecha);
        
        add(panelIcon, BorderLayout.WEST);
        add(panelInfo, BorderLayout.CENTER);
        add(panelFecha, BorderLayout.EAST);
    }
    
    @Override
    public Component getListCellRendererComponent(JList<? extends Logro> list, 
            Logro logro, int index, boolean isSelected, boolean cellHasFocus) {
        
        // Configurar contenido
        lblNombre.setText(logro.getNombre());
        lblDescripcion.setText(logro.getDescripcion());
        lblIcon.setIcon(ImageUtils.escalarImagen(logro.getImagen(), 50));
        
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
                new EmptyBorder(7, 11, 7, 11)
            ));
        } else {
            setBorder(new EmptyBorder(8, 12, 8, 12));
        }
        
        return this;
    }
}