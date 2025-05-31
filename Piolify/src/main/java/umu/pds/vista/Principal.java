package umu.pds.vista;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.CardLayout;

import javax.swing.JLabel;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;

import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.Box;
import java.awt.Dimension;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.border.EtchedBorder;
import java.awt.Font;
import javax.swing.border.LineBorder;

import umu.pds.controlador.Piolify;
import umu.pds.modelo.Usuario;
import umu.pds.utils.Utils;
import umu.pds.vista.elementos.PioButton;
import umu.pds.vista.elementos.PioColores;

import javax.swing.border.SoftBevelBorder;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;

public class Principal extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private Piolify controlador;
	private Usuario usuarioActual;
	private JLabel lblFotoPerfil;

	// Panel con CardLayout para gestionar las vistas
	private JPanel panelCentroCardLayout;
	private CardLayout cardLayout;
	
	// Constantes para identificar las tarjetas
	public static final String PANEL_PRINCIPAL = "principal";
	public static final String PANEL_ESTADISTICAS = "estadisticas";
	public static final String PANEL_MIS_CURSOS = "miscursos";
	public static final String PANEL_CREAR_CURSO = "crearcurso";
	public static final String PANEL_PERFIL = "perfil";
	
	// Botones para mantener referencia y poder actualizar su estado
	private JButton btnMisCursos;
	private JButton btnCrearCurso;
	private JButton btnEstadisticas;
	private JButton btnPerfil;

	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Principal frame = new Principal(null, new Piolify());
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}


	/**
	 * Create the frame.
	 */
	public Principal(Usuario usuario, Piolify controlador) {
		
		this.controlador = controlador;
		this.usuarioActual = usuario;
		
		setTitle("Piolify");
		setIconImage(Toolkit.getDefaultToolkit().getImage(Principal.class.getResource("/mascota.png")));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 912, 634);
		contentPane = new JPanel();
		contentPane.setBorder(null);

		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		// Panel de navegación superior
		JPanel panelNorte = crearPanelNavegacion();
		contentPane.add(panelNorte, BorderLayout.NORTH);
		
		// Inicializar CardLayout y su panel contenedor
		cardLayout = new CardLayout();
		panelCentroCardLayout = new JPanel(cardLayout);
		contentPane.add(panelCentroCardLayout, BorderLayout.CENTER);
		
		// Añadir panel principal
		panelCentroCardLayout.add(crearPanelPrincipal(), PANEL_PRINCIPAL);
		
		// Añadir panel de estadísticas
		panelCentroCardLayout.add(new Estadisticas(), PANEL_ESTADISTICAS);
		
		// Aquí añadirías los demás paneles conforme los necesites
		// Por ahora, crearemos paneles temporales para las otras secciones
		JPanel panelMisCursos = new JPanel();
		panelMisCursos.setBackground(Color.WHITE);
		panelMisCursos.add(new JLabel("Contenido de Mis Cursos (Por implementar)"));
		panelCentroCardLayout.add(panelMisCursos, PANEL_MIS_CURSOS);
		
		JPanel panelCrearCurso = new JPanel();
		panelCrearCurso.setBackground(Color.WHITE);
		panelCrearCurso.add(new JLabel("Contenido de Crear Curso (Por implementar)"));
		panelCentroCardLayout.add(panelCrearCurso, PANEL_CREAR_CURSO);
		
		JPanel panelPerfil = new JPanel();
		panelPerfil.setBackground(Color.WHITE);
		panelPerfil.add(new JLabel("Contenido de Mi Perfil (Por implementar)"));
		panelCentroCardLayout.add(panelPerfil, PANEL_PERFIL);
		
		// Mostrar el panel principal por defecto
		cardLayout.show(panelCentroCardLayout, PANEL_PRINCIPAL);
	}
	
	/**
	 * Crea el panel de navegación superior
	 */
	private JPanel crearPanelNavegacion() {
		JPanel panelNorte = new JPanel();
		panelNorte.setBorder(new EmptyBorder(5, 5, 5, 5));
		panelNorte.setBackground(PioColores.AMARILLO_LABEL);
		panelNorte.setLayout(new BoxLayout(panelNorte, BoxLayout.X_AXIS));
		
		Component rigidArea_1 = Box.createRigidArea(new Dimension(20, 20));
		panelNorte.add(rigidArea_1);
		
		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setIcon(Utils.escalarImagen("/mascotaSaluda.png", 50));
		panelNorte.add(lblNewLabel);
		
		Component rigidArea = Box.createRigidArea(new Dimension(20, 20));
		panelNorte.add(rigidArea);
		
		JLabel lblNewLabel_1 = new JLabel("Piolify");
		lblNewLabel_1.setForeground(PioColores.GRIS_TEXT);
		lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 15));
		panelNorte.add(lblNewLabel_1);
		
		Component glue = Box.createGlue();
		panelNorte.add(glue);
		
		// Botón Mis Cursos
		btnMisCursos = new PioButton("Mis Cursos");
		btnMisCursos.setBackground(PioColores.MARRON_BUTTON);
		btnMisCursos.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				cardLayout.show(panelCentroCardLayout, PANEL_MIS_CURSOS);
				actualizarBotonesActivos(btnMisCursos);
			}
		});
		panelNorte.add(btnMisCursos);
		
		// Botón Crear Curso
		btnCrearCurso = new PioButton("Crear Curso");
		btnCrearCurso.setBackground(PioColores.MARRON_BUTTON);
		btnCrearCurso.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				cardLayout.show(panelCentroCardLayout, PANEL_CREAR_CURSO);
				actualizarBotonesActivos(btnCrearCurso);
			}
		});
		panelNorte.add(btnCrearCurso);
		
		// Botón Estadísticas
		btnEstadisticas = new PioButton("Estadísticas");
		btnEstadisticas.setBackground(PioColores.MARRON_BUTTON);
		btnEstadisticas.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				cardLayout.show(panelCentroCardLayout, PANEL_ESTADISTICAS);
				actualizarBotonesActivos(btnEstadisticas);
			}
		});
		panelNorte.add(btnEstadisticas);
		
		Component glue_1 = Box.createGlue();
		panelNorte.add(glue_1);
		
		// Botón Mi Perfil
		btnPerfil = new PioButton("Mi Perfil");
		btnPerfil.setBackground(PioColores.MARRON_BUTTON);
		btnPerfil.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				cardLayout.show(panelCentroCardLayout, PANEL_PERFIL);
				actualizarBotonesActivos(btnPerfil);
			}
		});
		panelNorte.add(btnPerfil);
		
		Component rigidArea_3 = Box.createRigidArea(new Dimension(20, 20));
		panelNorte.add(rigidArea_3);
		
		lblFotoPerfil = new JLabel("");
		actualizarImagenPerfil(); // Actualiza la imagen de perfil al iniciar
		//lblFotoPerfil.setIcon(Utils.escalarImagen(usuarioActual.getImagenPerfil(), 50));
		panelNorte.add(lblFotoPerfil);
		
		Component rigidArea_2 = Box.createRigidArea(new Dimension(20, 20));
		panelNorte.add(rigidArea_2);
		
		return panelNorte;
	}
	
	/**
	 * Actualiza el estado visual de los botones de navegación
	 * @param botonActivo el botón que debe aparecer como activo
	 */
	private void actualizarBotonesActivos(JButton botonActivo) {
		// Restablecer todos los botones
		btnMisCursos.setBackground(PioColores.MARRON_BUTTON);
		btnCrearCurso.setBackground(PioColores.MARRON_BUTTON);
		btnEstadisticas.setBackground(PioColores.MARRON_BUTTON);
		btnPerfil.setBackground(PioColores.MARRON_BUTTON);
		
		// Destacar botón activo (se puede definir un color MARRON_BUTTON_ACTIVO en PioColores)
		// Por ahora usaremos un color más oscuro
		botonActivo.setBackground(PioColores.MARRON_BUTTON.darker());
	}
	
	/**
	 * Crea el panel principal (home)
	 */
	private JPanel crearPanelPrincipal() {
		JPanel panelCentro = new JPanel();
		panelCentro.setBackground(PioColores.BLANCO);
		GridBagLayout gbl_panelCentro = new GridBagLayout();
		gbl_panelCentro.columnWidths = new int[]{30, 0, 30, 0};
		gbl_panelCentro.rowHeights = new int[]{30, 0, 30, 0};
		gbl_panelCentro.columnWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
		gbl_panelCentro.rowWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
		panelCentro.setLayout(gbl_panelCentro);
		
		JPanel panel = new JPanel();
		panel.setBorder(null);
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.insets = new Insets(0, 0, 5, 5);
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 1;
		gbc_panel.gridy = 1;
		panelCentro.add(panel, gbc_panel);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]{20, 0, 0};
		gbl_panel.rowHeights = new int[]{20, 0, 20, 0, 40, 0, 0, 0};
		gbl_panel.columnWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		panel.setLayout(gbl_panel);
		
		JLabel lblNewLabel_2 = new JLabel("Bienvenido a Piolify");
		lblNewLabel_2.setForeground(PioColores.GRIS_TEXT);
		lblNewLabel_2.setFont(new Font("Tahoma", Font.BOLD, 15));
		GridBagConstraints gbc_lblNewLabel_2 = new GridBagConstraints();
		gbc_lblNewLabel_2.fill = GridBagConstraints.VERTICAL;
		gbc_lblNewLabel_2.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel_2.insets = new Insets(0, 0, 5, 0);
		gbc_lblNewLabel_2.gridx = 1;
		gbc_lblNewLabel_2.gridy = 1;
		panel.add(lblNewLabel_2, gbc_lblNewLabel_2);
		
		JLabel lblNewLabel_4 = new JLabel("Continúa aprendiendo o explora nuevos cursos");
		GridBagConstraints gbc_lblNewLabel_4 = new GridBagConstraints();
		gbc_lblNewLabel_4.fill = GridBagConstraints.VERTICAL;
		gbc_lblNewLabel_4.insets = new Insets(0, 0, 5, 0);
		gbc_lblNewLabel_4.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel_4.gridx = 1;
		gbc_lblNewLabel_4.gridy = 3;
		panel.add(lblNewLabel_4, gbc_lblNewLabel_4);
		
		JLabel lblNewLabel_5 = new JLabel("Cursos en progreso");
		lblNewLabel_5.setFont(new Font("Tahoma", Font.BOLD, 13));
		GridBagConstraints gbc_lblNewLabel_5 = new GridBagConstraints();
		gbc_lblNewLabel_5.insets = new Insets(0, 0, 5, 0);
		gbc_lblNewLabel_5.fill = GridBagConstraints.VERTICAL;
		gbc_lblNewLabel_5.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel_5.gridx = 1;
		gbc_lblNewLabel_5.gridy = 5;
		panel.add(lblNewLabel_5, gbc_lblNewLabel_5);
		
		return panelCentro;
	}
	
	private void actualizarImagenPerfil() {
	    String ruta = usuarioActual.getImagenPerfil();
	    Image imagen = null;

	    try {
	        if (ruta == null || ruta.isEmpty()) {
	            imagen = new ImageIcon(getClass().getResource("/fotoUser.png")).getImage();
	        } else if (ruta.startsWith("http://") || ruta.startsWith("https://")) {
	            imagen = javax.imageio.ImageIO.read(new java.net.URL(ruta));
	        } else if (ruta.startsWith("/") && getClass().getResource(ruta) != null) {
	            imagen = new ImageIcon(getClass().getResource(ruta)).getImage();
	        } else {
	            imagen = new ImageIcon(ruta).getImage();
	        }
	    } catch (Exception e) {
	        imagen = new ImageIcon(getClass().getResource("/fotoUser.png")).getImage();
	    }

	    if (imagen != null) {
	        lblFotoPerfil.setIcon(Utils.createCircularIcon(imagen, 50));
	    }
	}
	

}