package umu.pds.vista;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import java.util.List;

import umu.pds.controlador.Piolify;
import umu.pds.modelo.Amistad;
import umu.pds.modelo.Usuario;
import umu.pds.servicios.UsuarioService;
import umu.pds.utils.ImageUtils;
import umu.pds.vista.elementos.PioButton;
import umu.pds.vista.elementos.PioColores;

/**
 * Clase que representa la vista de amigos en la aplicación Piolify.
 * Permite ver amigos, solicitudes de amistad y buscar nuevos amigos.
 */
public class Amigos extends JPanel {
    private static final long serialVersionUID = 1L;
    
    private Usuario usuario;
    private final Piolify controlador;
    private final UsuarioService usuarioService;
    private JPanel panelListaAmigos;
    private JPanel panelSolicitudes;
    private JTextField txtBuscar;

    public Amigos() {
        this.controlador = Piolify.getUnicaInstancia();
        this.usuarioService = new UsuarioService();
        this.usuario = controlador.getUsuarioActual();
        
        initComponents();
        refrescarVista();
    }

    private void initComponents() {
        setSize(900, 600);
        setLayout(new BorderLayout());
        setBackground(PioColores.BLANCO);

        JPanel panelCentral = new JPanel(new GridBagLayout());
        panelCentral.setBackground(PioColores.BLANCO);

        // Lista de amigos
        GridBagConstraints gbc1 = new GridBagConstraints();
        gbc1.insets = new Insets(20, 20, 10, 10);
        gbc1.fill = GridBagConstraints.BOTH;
        gbc1.gridx = 0;
        gbc1.gridy = 0;
        gbc1.weightx = 0.5;
        gbc1.weighty = 0.6;
        gbc1.gridwidth = 1;
        panelListaAmigos = new JPanel(new BorderLayout(0, 15));
        panelListaAmigos.setBackground(PioColores.GRIS_PANEL);
        panelListaAmigos.setBorder(BorderFactory.createCompoundBorder(
            new MatteBorder(1, 1, 1, 1, new Color(220, 220, 220)),
            new EmptyBorder(15, 15, 15, 15)
        ));
        JLabel lblTituloAmigos = new JLabel("Mis Amigos");
        lblTituloAmigos.setFont(new Font("Arial", Font.BOLD, 16));
        lblTituloAmigos.setForeground(PioColores.GRIS_TEXT);
        panelListaAmigos.add(lblTituloAmigos, BorderLayout.NORTH);
        panelCentral.add(panelListaAmigos, gbc1);
        
        // Solicitudes
        GridBagConstraints gbc2 = new GridBagConstraints();
        gbc2.insets = new Insets(20, 10, 10, 20);
        gbc2.fill = GridBagConstraints.BOTH;
        gbc2.gridx = 1;
        gbc2.gridy = 0;
        gbc2.weightx = 0.5;
        gbc2.weighty = 0.6;
        gbc2.gridwidth = 1;
        panelSolicitudes = new JPanel(new BorderLayout(0, 15));
        panelSolicitudes.setBackground(PioColores.GRIS_PANEL);
        panelSolicitudes.setBorder(BorderFactory.createCompoundBorder(
            new MatteBorder(1, 1, 1, 1, new Color(220, 220, 220)),
            new EmptyBorder(15, 15, 15, 15)
        ));
        JLabel lblTituloSolicitudes = new JLabel("Solicitudes Recibidas");
        lblTituloSolicitudes.setFont(new Font("Arial", Font.BOLD, 16));
        lblTituloSolicitudes.setForeground(PioColores.GRIS_TEXT);
        panelSolicitudes.add(lblTituloSolicitudes, BorderLayout.NORTH);
        panelCentral.add(panelSolicitudes, gbc2);

        GridBagConstraints gbc3 = new GridBagConstraints();
        gbc3.insets = new Insets(10, 20, 20, 20);
        gbc3.fill = GridBagConstraints.HORIZONTAL;
        gbc3.gridx = 0;
        gbc3.gridy = 1;
        gbc3.gridwidth = 2;
        gbc3.weighty = 0.0;
        JPanel panelBuscarAmigos = new JPanel(new BorderLayout(0, 15));
        panelBuscarAmigos.setBackground(PioColores.GRIS_PANEL);
        panelBuscarAmigos.setBorder(BorderFactory.createCompoundBorder(
            new MatteBorder(1, 1, 1, 1, new Color(220, 220, 220)),
            new EmptyBorder(15, 15, 15, 15)
        ));
        
        JLabel lblTituloBuscar = new JLabel("Buscar Nuevos Amigos");
        lblTituloBuscar.setFont(new Font("Arial", Font.BOLD, 16));
        lblTituloBuscar.setForeground(PioColores.GRIS_TEXT);
        panelBuscarAmigos.add(lblTituloBuscar, BorderLayout.NORTH);

        // Buscar por email
        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelBusqueda.setOpaque(false);
        
        txtBuscar = new JTextField(20);
        txtBuscar.setFont(new Font("Arial", Font.PLAIN, 14));
        txtBuscar.setBackground(PioColores.GRIS_FIELDS);
        txtBuscar.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        JButton btnBuscar = new PioButton("Buscar por email");
        btnBuscar.setBackground(PioColores.MARRON_BUTTON);
        btnBuscar.addActionListener(e -> buscarUsuario());

        panelBusqueda.add(new JLabel("Email: "));
        panelBusqueda.add(txtBuscar);
        panelBusqueda.add(btnBuscar);
        panelBuscarAmigos.add(panelBusqueda, BorderLayout.CENTER);
        
        panelCentral.add(panelBuscarAmigos, gbc3);

        // Añadir scroll
        JScrollPane scrollPane = new JScrollPane(panelCentral);
        scrollPane.setBorder(null);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void refrescarVista() {
        SwingUtilities.invokeLater(() -> {
            Component[] components = panelListaAmigos.getComponents();
            for (Component comp : components) {
                if (comp instanceof JScrollPane) {
                    panelListaAmigos.remove(comp);
                    break;
                }
            }

            JPanel contenedorAmigos = new JPanel();
            contenedorAmigos.setLayout(new BoxLayout(contenedorAmigos, BoxLayout.Y_AXIS));
            contenedorAmigos.setOpaque(false);

            List<Usuario> amigos = usuarioService.obtenerAmigos(usuario);

            if (amigos.isEmpty()) {
                JLabel labelVacio = new JLabel("No tienes amigos aún");
                labelVacio.setFont(new Font("Arial", Font.ITALIC, 14));
                labelVacio.setForeground(PioColores.GRIS_TEXT);
                labelVacio.setAlignmentX(Component.CENTER_ALIGNMENT);
                contenedorAmigos.add(labelVacio);
            } else {
                for (Usuario amigo : amigos) {
                    // Panel para cada amigo
                    JPanel panelAmigo = new JPanel(new BorderLayout(10, 0));
                    panelAmigo.setOpaque(false);
                    panelAmigo.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(200, 200, 200)),
                        BorderFactory.createEmptyBorder(10, 10, 10, 10)
                    ));
                    panelAmigo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

                    // Foto
                    JLabel lblFoto = new JLabel();
                    Image imagen = ImageUtils.cargarImagen(amigo.getImagenPerfil());
                    if (imagen != null) {
                        lblFoto.setIcon(ImageUtils.createCircularIcon(imagen, 40));
                    }
                    JPanel panelFoto = new JPanel(new FlowLayout(FlowLayout.LEFT));
                    panelFoto.setOpaque(false);
                    panelFoto.add(lblFoto);
                    panelAmigo.add(panelFoto, BorderLayout.WEST);
                    
                    // Info
                    JPanel panelInfo = new JPanel(new BorderLayout());
                    panelInfo.setOpaque(false);
                    JLabel lblNombre = new JLabel(amigo.getNombre() + " " + amigo.getApellidos());
                    lblNombre.setFont(new Font("Arial", Font.BOLD, 14));
                    panelInfo.add(lblNombre, BorderLayout.NORTH);
                    JLabel lblEmail = new JLabel(amigo.getEmail());
                    lblEmail.setFont(new Font("Arial", Font.PLAIN, 12));
                    lblEmail.setForeground(PioColores.GRIS_TEXT);
                    panelInfo.add(lblEmail, BorderLayout.CENTER);
                    panelAmigo.add(panelInfo, BorderLayout.CENTER);
                    
                    // Botón ver perfil
                    JButton btnPerfil = new PioButton("Ver Perfil");
                    btnPerfil.setBackground(PioColores.MARRON_BUTTON);
                    btnPerfil.addActionListener(e -> new PerfilAmigo(amigo).setVisible(true));
                    panelAmigo.add(btnPerfil, BorderLayout.EAST);

                    contenedorAmigos.add(panelAmigo);
                    contenedorAmigos.add(Box.createVerticalStrut(5));
                }
            }

            JScrollPane scrollAmigos = new JScrollPane(contenedorAmigos);
            scrollAmigos.setBorder(null);
            scrollAmigos.setOpaque(false);
            scrollAmigos.getViewport().setOpaque(false);
            panelListaAmigos.add(scrollAmigos, BorderLayout.CENTER);

            
            // Buscar el contenedor y limpiarlo
            Component[] componentsSolicitudes = panelSolicitudes.getComponents();
            for (Component comp : componentsSolicitudes) {
                if (comp instanceof JScrollPane) {
                    panelSolicitudes.remove(comp);
                    break;
                }
            }

            JPanel contenedorSolicitudes = new JPanel();
            contenedorSolicitudes.setLayout(new BoxLayout(contenedorSolicitudes, BoxLayout.Y_AXIS));
            contenedorSolicitudes.setOpaque(false);

            List<Amistad> solicitudes = usuarioService.obtenerSolicitudesPendientes(usuario);

            if (solicitudes.isEmpty()) {
                JLabel labelVacioSol = new JLabel("No hay solicitudes pendientes");
                labelVacioSol.setFont(new Font("Arial", Font.ITALIC, 14));
                labelVacioSol.setForeground(PioColores.GRIS_TEXT);
                labelVacioSol.setAlignmentX(Component.CENTER_ALIGNMENT);
                contenedorSolicitudes.add(labelVacioSol);
            } else {
                for (Amistad solicitud : solicitudes) {
                    Usuario solicitante = solicitud.getUsuario1();
                    
                    // Panel para cada solicitud
                    JPanel panelSolicitud = new JPanel(new BorderLayout(10, 0));
                    panelSolicitud.setOpaque(false);
                    panelSolicitud.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(200, 200, 200)),
                        BorderFactory.createEmptyBorder(10, 10, 10, 10)
                    ));
                    panelSolicitud.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

                    // Foto
                    JLabel lblFotoSol = new JLabel();
                    Image imagenSol = ImageUtils.cargarImagen(solicitante.getImagenPerfil());
                    if (imagenSol != null) {
                        lblFotoSol.setIcon(ImageUtils.createCircularIcon(imagenSol, 40));
                    }
                    JPanel panelFotoSol = new JPanel(new FlowLayout(FlowLayout.LEFT));
                    panelFotoSol.setOpaque(false);
                    panelFotoSol.add(lblFotoSol);
                    panelSolicitud.add(panelFotoSol, BorderLayout.WEST);
                    
                    // Info
                    JPanel panelInfoSol = new JPanel(new BorderLayout());
                    panelInfoSol.setOpaque(false);
                    JLabel lblNombreSol = new JLabel(solicitante.getNombre() + " " + solicitante.getApellidos());
                    lblNombreSol.setFont(new Font("Arial", Font.BOLD, 14));
                    panelInfoSol.add(lblNombreSol, BorderLayout.NORTH);
                    JLabel lblEmailSol = new JLabel(solicitante.getEmail());
                    lblEmailSol.setFont(new Font("Arial", Font.PLAIN, 12));
                    lblEmailSol.setForeground(PioColores.GRIS_TEXT);
                    panelInfoSol.add(lblEmailSol, BorderLayout.CENTER);
                    panelSolicitud.add(panelInfoSol, BorderLayout.CENTER);
                    
                    // Botones
                    JPanel panelBotones = new JPanel(new FlowLayout());
                    panelBotones.setOpaque(false);
                    
                    JButton btnAceptar = new PioButton("Aceptar");
                    btnAceptar.setBackground(PioColores.VERDE_BUTTON);
                    btnAceptar.addActionListener(e -> procesarSolicitud(solicitud, true));

                    JButton btnRechazar = new PioButton("Rechazar");
                    btnRechazar.setBackground(PioColores.ROJO_ERROR);
                    btnRechazar.addActionListener(e -> procesarSolicitud(solicitud, false));

                    panelBotones.add(btnAceptar);
                    panelBotones.add(btnRechazar);
                    panelSolicitud.add(panelBotones, BorderLayout.EAST);

                    contenedorSolicitudes.add(panelSolicitud);
                    contenedorSolicitudes.add(Box.createVerticalStrut(5));
                }
            }

            JScrollPane scrollSolicitudes = new JScrollPane(contenedorSolicitudes);
            scrollSolicitudes.setBorder(null);
            scrollSolicitudes.setOpaque(false);
            scrollSolicitudes.getViewport().setOpaque(false);
            panelSolicitudes.add(scrollSolicitudes, BorderLayout.CENTER);

            panelListaAmigos.revalidate();
            panelListaAmigos.repaint();
            panelSolicitudes.revalidate();
            panelSolicitudes.repaint();
        });
    }

    private void procesarSolicitud(Amistad solicitud, boolean aceptar) {
        try {
            boolean exito = usuarioService.procesarSolicitudAmistad(solicitud.getId(), usuario, aceptar);

            if (exito) {
                String mensaje = aceptar ? "Solicitud aceptada!" : "Solicitud rechazada!";
                JOptionPane.showMessageDialog(this, mensaje);
                refrescarVista();
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Error al procesar la solicitud",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            System.err.println("Error al procesar solicitud: " + ex.getMessage());
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                "Error al procesar la solicitud: " + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buscarUsuario() {
        String email = txtBuscar.getText().trim();
        
        if (email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, introduce un email");
            return;
        }


        try {
            Usuario encontrado = usuarioService.obtenerUsuarioPorEmail(email);
            if (encontrado == null) {
                JOptionPane.showMessageDialog(this, "Usuario no encontrado");
                return;
            }
            if (encontrado.getId().equals(usuario.getId())) {
                JOptionPane.showMessageDialog(this, "No puedes enviarte una solicitud a ti mismo");
                return;
            }
            int respuesta = JOptionPane.showConfirmDialog(this,
                "¿Enviar solicitud de amistad a " + encontrado.getNombre() + " " + encontrado.getApellidos() + "?",
                "Confirmar",
                JOptionPane.YES_NO_OPTION);

            if (respuesta == JOptionPane.YES_OPTION) {    
                boolean exito = usuarioService.enviarSolicitudAmistad(usuario, encontrado);
                if (exito) {
                    JOptionPane.showMessageDialog(this, "Solicitud enviada!");
                    txtBuscar.setText("");
                } else {
                    JOptionPane.showMessageDialog(this, "No se pudo enviar la solicitud. Puede que ya exista una relación.");
                }
            }
        } catch (Exception e) {
            System.err.println("Error al buscar usuario: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }
}