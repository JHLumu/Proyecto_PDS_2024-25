package umu.pds.vista;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;

import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.CardLayout;

import javax.swing.JLabel;
import javax.swing.BoxLayout;

import java.awt.Component;

import javax.swing.Box;
import java.awt.Dimension;
import javax.swing.JButton;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.awt.Font;

import umu.pds.controlador.Piolify;
import umu.pds.modelo.Usuario;
import umu.pds.utils.ImageUtils;
import umu.pds.vista.elementos.PioButton;
import umu.pds.vista.elementos.PioColores;

/* * Clase que representa la ventana principal de la aplicación Piolify.
 * Contiene un panel de navegación y un panel central con CardLayout
 * para gestionar diferentes vistas (estadísticas, cursos, perfil, amigos).
 */
public class Principal extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private Usuario usuarioActual;
	private JLabel lblFotoPerfil;

	// Panel con CardLayout para gestionar las vistas
	private JPanel panelCentroCardLayout;
	private CardLayout cardLayout;
	
	// Constantes para identificar las tarjetas
	public static final String PANEL_ESTADISTICAS = "estadisticas";
	public static final String PANEL_CURSOS = "cursos";
	public static final String PANEL_PERFIL = "perfil";
	public static final String PANEL_AMIGOS = "amigos";
	
	// Variables de instancia para los paneles
	private DashboardEstadisticas panelEstadisticas;
	private Biblioteca panelBiblioteca;
	private PerfilUsuario panelPerfil;
	private Amigos panelAmigos;
	
	// Botones para mantener referencia y poder actualizar su estado
	private JButton btnCursos;
	private JButton btnEstadisticas;
	private JButton btnPerfil;
	private JButton btnAmistades;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Principal frame = new Principal();
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
	public Principal() {
		
		Piolify.getUnicaInstancia().añadirObservador(this::actualizarImagenPerfil);
		usuarioActual = Piolify.getUnicaInstancia().getUsuarioActual();
		
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
		
		// Crear los paneles como variables de instancia
		panelEstadisticas = new DashboardEstadisticas(usuarioActual);
		panelBiblioteca = new Biblioteca(usuarioActual);
		panelPerfil = new PerfilUsuario(usuarioActual);
		
		// Añadir los paneles al CardLayout
		panelCentroCardLayout.add(panelEstadisticas, PANEL_ESTADISTICAS);
		panelCentroCardLayout.add(panelBiblioteca, PANEL_CURSOS);
		panelCentroCardLayout.add(panelPerfil, PANEL_PERFIL);
		
		// Mostrar el panel de cursos por defecto (en lugar del panel principal)
		cardLayout.show(panelCentroCardLayout, PANEL_CURSOS);
		// Actualizar el estado visual del botón de cursos como activo
		actualizarBotonesActivos(btnCursos);
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
		lblNewLabel.setIcon(ImageUtils.escalarImagen("/mascotaSaluda.png", 50));
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
		btnCursos = new PioButton("Mis Cursos");
		btnCursos.setBackground(PioColores.MARRON_BUTTON);
		btnCursos.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				cardLayout.show(panelCentroCardLayout, PANEL_CURSOS);
				actualizarBotonesActivos(btnCursos);
			}
		});
		panelNorte.add(btnCursos);
		
		// Botón Estadísticas - MODIFICADO para usar refrescarEstadisticas()
		btnEstadisticas = new PioButton("Estadísticas");
		btnEstadisticas.setBackground(PioColores.MARRON_BUTTON);
		btnEstadisticas.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		        // Refrescar las estadísticas del panel existente
		        if (panelEstadisticas != null) {
		            panelEstadisticas.refrescarEstadisticas();
		        }
		        cardLayout.show(panelCentroCardLayout, PANEL_ESTADISTICAS);
		        actualizarBotonesActivos(btnEstadisticas);
		    }
		});
		panelNorte.add(btnEstadisticas);
		
		// Botón amigos
		btnAmistades = new PioButton("Amigos");
		btnAmistades.setBackground(PioColores.MARRON_BUTTON);
		panelNorte.add(btnAmistades);
		btnAmistades.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		        if (panelAmigos == null) {
		            panelAmigos = new Amigos();
		            panelCentroCardLayout.add(panelAmigos, PANEL_AMIGOS);
		        }
		        
		        cardLayout.show(panelCentroCardLayout, PANEL_AMIGOS);
		        actualizarBotonesActivos(btnAmistades);
		    }
		});

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
		btnCursos.setBackground(PioColores.MARRON_BUTTON);
		btnEstadisticas.setBackground(PioColores.MARRON_BUTTON);
		btnPerfil.setBackground(PioColores.MARRON_BUTTON);
		btnAmistades.setBackground(PioColores.MARRON_BUTTON);
		
		// Destacar botón activo
		botonActivo.setBackground(PioColores.MARRON_BUTTON.darker());
	}
	
	/**
	 * Actualiza la imagen de perfil en la barra de navegación
	 */
	private void actualizarImagenPerfil() {
	    Usuario usuario = Piolify.getUnicaInstancia().getUsuarioActual();
	    String ruta = usuario.getImagenPerfil();
	    Image imagen = ImageUtils.cargarImagen(ruta);

	    if (imagen != null) {
	        lblFotoPerfil.setIcon(ImageUtils.createCircularIcon(imagen, 50));
	    }
	}
	
	/**
	 * Método público para acceder al panel de biblioteca desde otras clases
	 */
	public Biblioteca getPanelBiblioteca() {
		return panelBiblioteca;
	}
	
	/**
	 * Método público para acceder al panel de estadísticas desde otras clases
	 */
	public DashboardEstadisticas getPanelEstadisticas() {
		return panelEstadisticas;
	}
	
	/**
	 * Método público para acceder al panel de perfil desde otras clases
	 */
	public PerfilUsuario getPanelPerfil() {
		return panelPerfil;
	}
	
	/**
	 * Método público para acceder al panel de amigos desde otras clases
	 */
	public Amigos getPanelAmigos() {
		return panelAmigos;
	}
	
	/**
	 * Método para cambiar programáticamente a un panel específico
	 */
	public void mostrarPanel(String nombrePanel) {
		cardLayout.show(panelCentroCardLayout, nombrePanel);
		
		// Actualizar el botón activo según el panel mostrado
		switch (nombrePanel) {
			case PANEL_CURSOS:
				actualizarBotonesActivos(btnCursos);
				break;
			case PANEL_ESTADISTICAS:
				if (panelEstadisticas != null) {
					panelEstadisticas.refrescarEstadisticas();
				}
				actualizarBotonesActivos(btnEstadisticas);
				break;
			case PANEL_PERFIL:
				actualizarBotonesActivos(btnPerfil);
				break;
			case PANEL_AMIGOS:
				actualizarBotonesActivos(btnAmistades);
				break;
		}
	}
	
    @Override
    public void dispose() {
        Piolify.getUnicaInstancia().borrarObservador(this::actualizarImagenPerfil);
        super.dispose();
    }
}