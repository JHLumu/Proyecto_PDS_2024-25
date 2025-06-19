package umu.pds.vista;

import java.awt.*;
import java.net.MalformedURLException;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import umu.pds.modelo.Estadisticas;
import umu.pds.modelo.Logro;
import umu.pds.modelo.Usuario;
import umu.pds.persistencia.AdaptadorUsuarioDAO;
import umu.pds.persistencia.FactoriaDAO;
import umu.pds.persistencia.JPAFactoriaDAO;
import umu.pds.persistencia.UsuarioDAO;
import umu.pds.utils.ImageUtils;
import umu.pds.utils.LogroListCellRenderer;
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
    private JList<Logro> listaLogros;
    private DefaultListModel<Logro> listModelLogros;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                PerfilAmigo frame = new PerfilAmigo();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public PerfilAmigo() {
        initComponents();
    }

    public PerfilAmigo(Usuario amigo) {
        FactoriaDAO factoria = JPAFactoriaDAO.getInstancia();
        UsuarioDAO usuarioDAO = factoria.getUsuarioDAO();
        if (usuarioDAO instanceof AdaptadorUsuarioDAO) {
            this.amigo = ((AdaptadorUsuarioDAO) usuarioDAO)
                .recuperarUsuarioPorEmailConLogrosYEstadisticas(amigo.getEmail());
        } else {
            try {
                this.amigo = usuarioDAO.recuperarUsuario(amigo.getId());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        initComponents();
        cargarDatosAmigo();
    }

    private void initComponents() {
        setTitle("Perfil de Amigo");
        setIconImage(Toolkit.getDefaultToolkit().getImage(PerfilAmigo.class.getResource("/mascota.png")));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 762, 638);
        setLocationRelativeTo(null);

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setBackground(PioColores.BLANCO);
        setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout(0, 0));

        // Panel superior con foto y datos básicos
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

        lblFoto = new JLabel("Foto");
        lblFoto.setHorizontalAlignment(SwingConstants.CENTER);
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

        // Panel central con estadísticas y logros
        JPanel panelCentral = new JPanel();
        panelCentral.setBackground(PioColores.BLANCO);
        contentPane.add(panelCentral, BorderLayout.CENTER);

        GridBagLayout gbl_panelCentral = new GridBagLayout();
        gbl_panelCentral.columnWidths = new int[]{0, 20, 0, 0};
        gbl_panelCentral.rowHeights = new int[]{0, 0};
        gbl_panelCentral.columnWeights = new double[]{0.5, 0.0, 0.5, Double.MIN_VALUE};
        gbl_panelCentral.rowWeights = new double[]{1.0, Double.MIN_VALUE};
        panelCentral.setLayout(gbl_panelCentral);

        // Panel de estadísticas con GridBagLayout
        JPanel panelEstadisticas = new JPanel();
        panelEstadisticas.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(PioColores.GRIS_TEXT),
            "Estadísticas",
            0, 0,
            new Font("Tahoma", Font.BOLD, 14),
            PioColores.GRIS_TEXT));
        panelEstadisticas.setBackground(PioColores.GRIS_PANEL);
        GridBagConstraints gbc_panelEstadisticas = new GridBagConstraints();
        gbc_panelEstadisticas.insets = new Insets(10, 10, 10, 5);
        gbc_panelEstadisticas.fill = GridBagConstraints.BOTH;
        gbc_panelEstadisticas.gridx = 0;
        gbc_panelEstadisticas.gridy = 0;
        panelCentral.add(panelEstadisticas, gbc_panelEstadisticas);

        panelEstadisticas.setLayout(new GridBagLayout());
        Font labelFont = new Font("Tahoma", Font.BOLD, 13);
        Font valueFont = new Font("Tahoma", Font.PLAIN, 13);

        // Racha actual
        GridBagConstraints gbcRachaTitulo = new GridBagConstraints();
        gbcRachaTitulo.insets = new Insets(5, 10, 5, 10);
        gbcRachaTitulo.anchor = GridBagConstraints.WEST;
        gbcRachaTitulo.gridx = 0;
        gbcRachaTitulo.gridy = 0;
        JLabel lblRachaTitulo = new JLabel("Racha actual:");
        lblRachaTitulo.setFont(labelFont);
        panelEstadisticas.add(lblRachaTitulo, gbcRachaTitulo);

        GridBagConstraints gbcRacha = new GridBagConstraints();
        gbcRacha.insets = new Insets(5, 10, 5, 10);
        gbcRacha.anchor = GridBagConstraints.WEST;
        gbcRacha.gridx = 1;
        gbcRacha.gridy = 0;
        lblRacha = new JLabel("0 días");
        lblRacha.setFont(valueFont);
        panelEstadisticas.add(lblRacha, gbcRacha);

        // Mejor racha
        GridBagConstraints gbcMejorRachaTitulo = new GridBagConstraints();
        gbcMejorRachaTitulo.insets = new Insets(5, 10, 5, 10);
        gbcMejorRachaTitulo.anchor = GridBagConstraints.WEST;
        gbcMejorRachaTitulo.gridx = 0;
        gbcMejorRachaTitulo.gridy = 1;
        JLabel lblMejorRachaTitulo = new JLabel("Mejor racha:");
        lblMejorRachaTitulo.setFont(labelFont);
        panelEstadisticas.add(lblMejorRachaTitulo, gbcMejorRachaTitulo);

        GridBagConstraints gbcMejorRacha = new GridBagConstraints();
        gbcMejorRacha.insets = new Insets(5, 10, 5, 10);
        gbcMejorRacha.anchor = GridBagConstraints.WEST;
        gbcMejorRacha.gridx = 1;
        gbcMejorRacha.gridy = 1;
        lblMejorRacha = new JLabel("0 días");
        lblMejorRacha.setFont(valueFont);
        panelEstadisticas.add(lblMejorRacha, gbcMejorRacha);

        // Tiempo total
        GridBagConstraints gbcTiempoTitulo = new GridBagConstraints();
        gbcTiempoTitulo.insets = new Insets(5, 10, 5, 10);
        gbcTiempoTitulo.anchor = GridBagConstraints.WEST;
        gbcTiempoTitulo.gridx = 0;
        gbcTiempoTitulo.gridy = 2;
        JLabel lblTiempoTitulo = new JLabel("Tiempo total:");
        lblTiempoTitulo.setFont(labelFont);
        panelEstadisticas.add(lblTiempoTitulo, gbcTiempoTitulo);

        GridBagConstraints gbcTiempo = new GridBagConstraints();
        gbcTiempo.insets = new Insets(5, 10, 5, 10);
        gbcTiempo.anchor = GridBagConstraints.WEST;
        gbcTiempo.gridx = 1;
        gbcTiempo.gridy = 2;
        lblTiempo = new JLabel("0 min 0 s");
        lblTiempo.setFont(valueFont);
        panelEstadisticas.add(lblTiempo, gbcTiempo);

        // Ejercicios
        GridBagConstraints gbcEjerciciosTitulo = new GridBagConstraints();
        gbcEjerciciosTitulo.insets = new Insets(5, 10, 5, 10);
        gbcEjerciciosTitulo.anchor = GridBagConstraints.WEST;
        gbcEjerciciosTitulo.gridx = 0;
        gbcEjerciciosTitulo.gridy = 3;
        JLabel lblEjerciciosTitulo = new JLabel("Ejercicios:");
        lblEjerciciosTitulo.setFont(labelFont);
        panelEstadisticas.add(lblEjerciciosTitulo, gbcEjerciciosTitulo);

        GridBagConstraints gbcEjercicios = new GridBagConstraints();
        gbcEjercicios.insets = new Insets(5, 10, 5, 10);
        gbcEjercicios.anchor = GridBagConstraints.WEST;
        gbcEjercicios.gridx = 1;
        gbcEjercicios.gridy = 3;
        lblEjercicios = new JLabel("0");
        lblEjercicios.setFont(valueFont);
        panelEstadisticas.add(lblEjercicios, gbcEjercicios);

        // Panel de logros
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

        listModelLogros = new DefaultListModel<>();
        listaLogros = new JList<>(listModelLogros);
        listaLogros.setCellRenderer(new LogroListCellRenderer());
        listaLogros.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaLogros.setBackground(PioColores.GRIS_PANEL);
        listaLogros.setOpaque(true);
        listaLogros.setVisibleRowCount(-1);

        JScrollPane scrollLogros = new JScrollPane(listaLogros);
        scrollLogros.setBorder(null);
        scrollLogros.setOpaque(false);
        scrollLogros.getViewport().setOpaque(false);
        panelLogrosContainer.add(scrollLogros, BorderLayout.CENTER);
    }

    private void cargarDatosAmigo() {
        if (amigo == null) return;

        setTitle("Perfil de " + amigo.getNombre());

        // Foto
        Image imagen = ImageUtils.cargarImagen(amigo.getImagenPerfil());
        if (imagen != null) {
            lblFoto.setIcon(ImageUtils.createCircularIcon(imagen, 100));
            lblFoto.setText("");
        }

        // Información básica
        lblNombre.setText(amigo.getNombre() + " " + amigo.getApellidos());
        lblEmail.setText(amigo.getEmail());
        lblGenero.setText("Género: " + (amigo.getGenero() != null ? amigo.getGenero() : "No especificado"));

        // Estadísticas
        Estadisticas stats = amigo.getEstadisticas();
        if (stats != null) {
            lblRacha.setText(stats.getRachaDias() + " días");
            lblMejorRacha.setText(stats.getMejorRacha() + " días");
            int totalSegundos = stats.getTiempoTotal();
            int minutos = totalSegundos / 60;
            int segundos = totalSegundos % 60;
            lblTiempo.setText(minutos + " min " + segundos + " s");
            lblEjercicios.setText(String.valueOf(stats.getTotalEjerciciosCompletados()));
        }

        try {
            umu.pds.controlador.Piolify.getUnicaInstancia().getUsuarioController().verificarYDesbloquearLogros(amigo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        cargarLogrosAmigo(amigo);
    }

    private void cargarLogrosAmigo(Usuario amigo) {
        listModelLogros.clear();
        List<Logro> logros = amigo.getLogros();

        if (logros == null || logros.isEmpty()) {
            listModelLogros.addElement(new Logro(null, null) {
                @Override
                public String getNombre() { return "Aún no tiene logros."; }
                @Override
                public String getDescripcion() { return ""; }
                @Override
                public String getImagen() { return null; }
            });
            listaLogros.setEnabled(false);
        } else {
            for (Logro logro : logros) {
                listModelLogros.addElement(logro);
            }
            listaLogros.setEnabled(true);
        }
    }
}