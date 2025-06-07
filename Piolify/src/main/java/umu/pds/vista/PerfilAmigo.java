package umu.pds.vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import umu.pds.modelo.Estadisticas;
import umu.pds.modelo.Logro;
import umu.pds.modelo.Usuario;
import umu.pds.utils.ImageUtils;
import umu.pds.vista.elementos.PioColores;

public class PerfilAmigo extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private Usuario amigo;
    private JLabel lblFoto;
    private JLabel lblNombre;
    private JLabel lblEmail;
    private JLabel lblGenero;
    private JLabel lblRacha;
    private JLabel lblMejorRacha;
    private JLabel lblTiempo;
    private JLabel lblEjercicios;
    private JPanel panelLogros;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    PerfilAmigo frame = new PerfilAmigo();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Constructor para WindowBuilder (sin parámetros)
     */
    public PerfilAmigo() {
        initComponents();
    }
    
    /**
     * Constructor para usar con un amigo real
     */
    public PerfilAmigo(Usuario amigo) {
        this.amigo = amigo;
        initComponents();
        cargarDatosAmigo();
    }

    /**
     * Create the frame.
     */
    private void initComponents() {
        setTitle("Perfil de Amigo");
        setIconImage(Toolkit.getDefaultToolkit().getImage(PerfilAmigo.class.getResource("/mascota.png")));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 600, 500);
        setLocationRelativeTo(null);
        
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setBackground(PioColores.BLANCO);
        setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout(0, 0));

        JPanel panelSuperior = new JPanel();
        panelSuperior.setBackground(PioColores.AMARILLO_LABEL);
        panelSuperior.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        contentPane.add(panelSuperior, BorderLayout.NORTH);
        
        GridBagLayout gbl_panelSuperior = new GridBagLayout();
        gbl_panelSuperior.columnWidths = new int[]{120, 20, 0, 0};
        gbl_panelSuperior.rowHeights = new int[]{120, 0};
        gbl_panelSuperior.columnWeights = new double[]{0.0, 0.0, 1.0, Double.MIN_VALUE};
        gbl_panelSuperior.rowWeights = new double[]{0.0, Double.MIN_VALUE};
        panelSuperior.setLayout(gbl_panelSuperior);

        lblFoto = new JLabel("");
        lblFoto.setHorizontalAlignment(SwingConstants.CENTER);
        lblFoto.setText("Foto");
        GridBagConstraints gbc_lblFoto = new GridBagConstraints();
        gbc_lblFoto.insets = new Insets(0, 0, 0, 5);
        gbc_lblFoto.gridx = 0;
        gbc_lblFoto.gridy = 0;
        panelSuperior.add(lblFoto, gbc_lblFoto);

        JPanel panelInfo = new JPanel();
        panelInfo.setOpaque(false);
        GridBagConstraints gbc_panelInfo = new GridBagConstraints();
        gbc_panelInfo.fill = GridBagConstraints.BOTH;
        gbc_panelInfo.gridx = 2;
        gbc_panelInfo.gridy = 0;
        panelSuperior.add(panelInfo, gbc_panelInfo);
        panelInfo.setLayout(new BoxLayout(panelInfo, BoxLayout.Y_AXIS));

        lblNombre = new JLabel("");
        lblNombre.setFont(new Font("Arial", Font.BOLD, 18));
        lblNombre.setForeground(PioColores.GRIS_TEXT);
        panelInfo.add(lblNombre);
        
        panelInfo.add(Box.createVerticalStrut(10));

        lblEmail = new JLabel("");
        lblEmail.setFont(new Font("Arial", Font.PLAIN, 14));
        lblEmail.setForeground(PioColores.GRIS_TEXT);
        panelInfo.add(lblEmail);

        lblGenero = new JLabel("");
        lblGenero.setFont(new Font("Arial", Font.PLAIN, 14));
        lblGenero.setForeground(PioColores.GRIS_TEXT);
        panelInfo.add(lblGenero);

        JPanel panelCentral = new JPanel();
        panelCentral.setBackground(PioColores.BLANCO);
        contentPane.add(panelCentral, BorderLayout.CENTER);
        
        GridBagLayout gbl_panelCentral = new GridBagLayout();
        gbl_panelCentral.columnWidths = new int[]{0, 20, 0, 0};
        gbl_panelCentral.rowHeights = new int[]{0, 0};
        gbl_panelCentral.columnWeights = new double[]{0.5, 0.0, 0.5, Double.MIN_VALUE};
        gbl_panelCentral.rowWeights = new double[]{1.0, Double.MIN_VALUE};
        panelCentral.setLayout(gbl_panelCentral);

        JPanel panelEstadisticas = new JPanel();
        panelEstadisticas.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(PioColores.GRIS_TEXT), 
            "Estadísticas", 
            0, 0, 
            new Font("Arial", Font.BOLD, 14), 
            PioColores.GRIS_TEXT));
        panelEstadisticas.setBackground(PioColores.GRIS_PANEL);
        GridBagConstraints gbc_panelEstadisticas = new GridBagConstraints();
        gbc_panelEstadisticas.insets = new Insets(10, 10, 10, 5);
        gbc_panelEstadisticas.fill = GridBagConstraints.BOTH;
        gbc_panelEstadisticas.gridx = 0;
        gbc_panelEstadisticas.gridy = 0;
        panelCentral.add(panelEstadisticas, gbc_panelEstadisticas);
        panelEstadisticas.setLayout(new BoxLayout(panelEstadisticas, BoxLayout.Y_AXIS));

        lblRacha = new JLabel("Racha actual: 0 días");
        lblRacha.setFont(new Font("Arial", Font.PLAIN, 12));
        panelEstadisticas.add(lblRacha);
        
        panelEstadisticas.add(Box.createVerticalStrut(5));

        lblMejorRacha = new JLabel("Mejor racha: 0 días");
        lblMejorRacha.setFont(new Font("Arial", Font.PLAIN, 12));
        panelEstadisticas.add(lblMejorRacha);
        
        panelEstadisticas.add(Box.createVerticalStrut(5));

        lblTiempo = new JLabel("Tiempo total: 0 min");
        lblTiempo.setFont(new Font("Arial", Font.PLAIN, 12));
        panelEstadisticas.add(lblTiempo);
        
        panelEstadisticas.add(Box.createVerticalStrut(5));

        lblEjercicios = new JLabel("Ejercicios: 0");
        lblEjercicios.setFont(new Font("Arial", Font.PLAIN, 12));
        panelEstadisticas.add(lblEjercicios);

        JPanel panelLogrosContainer = new JPanel();
        panelLogrosContainer.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(PioColores.GRIS_TEXT), 
            "Logros", 
            0, 0, 
            new Font("Arial", Font.BOLD, 14), 
            PioColores.GRIS_TEXT));
        panelLogrosContainer.setBackground(PioColores.GRIS_PANEL);
        GridBagConstraints gbc_panelLogrosContainer = new GridBagConstraints();
        gbc_panelLogrosContainer.insets = new Insets(10, 5, 10, 10);
        gbc_panelLogrosContainer.fill = GridBagConstraints.BOTH;
        gbc_panelLogrosContainer.gridx = 2;
        gbc_panelLogrosContainer.gridy = 0;
        panelCentral.add(panelLogrosContainer, gbc_panelLogrosContainer);
        panelLogrosContainer.setLayout(new BorderLayout(0, 0));

        panelLogros = new JPanel();
        panelLogros.setOpaque(false);
        panelLogros.setLayout(new BoxLayout(panelLogros, BoxLayout.Y_AXIS));

        JScrollPane scrollLogros = new JScrollPane(panelLogros);
        scrollLogros.setBorder(null);
        scrollLogros.setOpaque(false);
        scrollLogros.getViewport().setOpaque(false);
        panelLogrosContainer.add(scrollLogros, BorderLayout.CENTER);
    }

    private void cargarDatosAmigo() {
        if (amigo == null) return;

        // título
        setTitle("Perfil de " + amigo.getNombre());

        //foto
        Image imagen = ImageUtils.cargarImagen(amigo.getImagenPerfil());
        if (imagen != null) {
            lblFoto.setIcon(ImageUtils.createCircularIcon(imagen, 100));
            lblFoto.setText("");
        }

        // información básica
        lblNombre.setText(amigo.getNombre() + " " + amigo.getApellidos());
        lblEmail.setText(amigo.getEmail());
        lblGenero.setText("Género: " + (amigo.getGenero() != null ? amigo.getGenero() : "No especificado"));

        //estadísticas
        Estadisticas stats = amigo.getEstadisticas();
        if (stats != null) {
            lblRacha.setText("Racha actual: " + stats.getRachaDias() + " días");
            lblMejorRacha.setText("Mejor racha: " + stats.getMejorRacha() + " días");
            lblTiempo.setText("Tiempo total: " + stats.getTiempoTotal() + " min");
            lblEjercicios.setText("Ejercicios: " + stats.getTotalEjerciciosCompletados());
        }

        // logros
        panelLogros.removeAll();
        
        if (amigo.getLogros() != null && !amigo.getLogros().isEmpty()) {
            for (Logro logro : amigo.getLogros()) {
                JLabel lblLogro = new JLabel("🏆 " + logro.getNombre());
                lblLogro.setFont(new Font("Arial", Font.PLAIN, 12));
                lblLogro.setToolTipText(logro.getDescripcion());
                panelLogros.add(lblLogro);
                panelLogros.add(Box.createVerticalStrut(3));
            }
        } else {
            JLabel lblSinLogros = new JLabel("No tiene logros aún");
            lblSinLogros.setFont(new Font("Arial", Font.ITALIC, 12));
            lblSinLogros.setForeground(PioColores.GRIS_TEXT);
            panelLogros.add(lblSinLogros);
        }

        panelLogros.revalidate();
        panelLogros.repaint();
    }
}