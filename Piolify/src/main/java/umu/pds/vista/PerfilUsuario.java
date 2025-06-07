package umu.pds.vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import umu.pds.controlador.Piolify;
import umu.pds.modelo.Usuario;
import umu.pds.utils.ImageUtils;
import umu.pds.vista.elementos.PioButton;
import umu.pds.vista.elementos.PioColores;

import javax.swing.BoxLayout;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.Box;

public class PerfilUsuario extends JPanel {

	private static final long serialVersionUID = 1L;

	private Usuario usuario;
	private Piolify controlador = Piolify.getUnicaInstancia();
	private JLabel lblFotoPerfil;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JPasswordField passwordField;
	private JPasswordField passwordField_1;
	private JPasswordField passwordField_2;
	private JRadioButton rdbtnHombre;
	private JRadioButton rdbtnMujer;
	
	private final ButtonGroup buttonGroup = new ButtonGroup();

	private String nuevaRutaImagen;
	private boolean imagenCambiada = false;

	public PerfilUsuario(Usuario usuario) {
		this.usuario = usuario;
		this.nuevaRutaImagen = usuario.getImagenPerfil();

		initComponents();
		cargarDatosUsuario();
	}

	private void initComponents() {
		setBackground(Color.WHITE);
		setBorder(new EmptyBorder(5, 5, 5, 5));
		setLayout(new BorderLayout(0, 0));

		// Panel izquierdo para foto
		JPanel panelFoto = new JPanel();
		panelFoto.setBackground(new Color(255, 255, 255));
		add(panelFoto, BorderLayout.WEST);
		GridBagLayout gbl_panelFoto = new GridBagLayout();
		gbl_panelFoto.columnWidths = new int[] { 20, 150, 20, 0 };
		gbl_panelFoto.rowHeights = new int[] { 20, 150, 20, 50, 20, 0 };
		gbl_panelFoto.columnWeights = new double[] { 0.0, 1.0, 0.0, Double.MIN_VALUE };
		gbl_panelFoto.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
	    panelFoto.setBorder(BorderFactory.createCompoundBorder(
	            new LineBorder(new Color(220, 220, 220), 1, true),
	            new EmptyBorder(20, 15, 20, 15)
	        ));
	    panelFoto.setBackground(PioColores.MARRON_BUTTON);
		panelFoto.setLayout(gbl_panelFoto);

		lblFotoPerfil = new JLabel("");
		lblFotoPerfil.setBorder(new TitledBorder(
		        BorderFactory.createCompoundBorder(
		                new LineBorder(PioColores.BLANCO, 5, true),
		                new EmptyBorder(10, 15, 15, 15)
		            ),
		            "Foto de Perfil", 
		            TitledBorder.LEADING, 
		            TitledBorder.TOP,
		            new Font("Arial", Font.BOLD, 16),
		            PioColores.GRIS_TEXT
		        ));
		lblFotoPerfil.setPreferredSize(new Dimension(200, 200));
		lblFotoPerfil.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		lblFotoPerfil.setHorizontalAlignment(SwingConstants.CENTER);
		lblFotoPerfil.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				cambiarFotoPerfil();
			}
		});
		GridBagConstraints gbc_lblFotoPerfil = new GridBagConstraints();
		gbc_lblFotoPerfil.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblFotoPerfil.insets = new Insets(0, 0, 5, 5);
		gbc_lblFotoPerfil.gridx = 1;
		gbc_lblFotoPerfil.gridy = 1;
		panelFoto.add(lblFotoPerfil, gbc_lblFotoPerfil);

		JLabel lblClickFoto = new JLabel("<html><center>Haz clic en la imagen<br>para cambiarla</center></html>");
		lblClickFoto.setFont(new Font("Arial", Font.ITALIC, 12));
		lblClickFoto.setForeground(PioColores.GRIS_TEXT);
		lblClickFoto.setHorizontalAlignment(SwingConstants.CENTER);
		GridBagConstraints gbc_lblClickFoto = new GridBagConstraints();
		gbc_lblClickFoto.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblClickFoto.insets = new Insets(0, 0, 5, 5);
		gbc_lblClickFoto.gridx = 1;
		gbc_lblClickFoto.gridy = 3;
		panelFoto.add(lblClickFoto, gbc_lblClickFoto);

		// Panel central
		JPanel panel_1 = new JPanel();
		panel_1.setBackground(Color.WHITE);
		add(panel_1, BorderLayout.CENTER);
		GridBagLayout gbl_panel_1 = new GridBagLayout();
		gbl_panel_1.columnWidths = new int[] { 20, 0, 20, 0 };
		gbl_panel_1.rowHeights = new int[] { 20, 0, 20, 0, 20, 0, 20, 0 };
		gbl_panel_1.columnWeights = new double[] { 0.0, 1.0, 0.0, Double.MIN_VALUE };
		gbl_panel_1.rowWeights = new double[] { 0.0, 0.4, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		panel_1.setLayout(gbl_panel_1);

		// Panel información personal
	    JPanel panel_2 = new JPanel();
	    panel_2.setBorder(new TitledBorder(
	        BorderFactory.createCompoundBorder(
	            new LineBorder(new Color(200, 200, 200), 1, true),
	            new EmptyBorder(10, 15, 15, 15)
	        ),
	        "Información Personal", 
	        TitledBorder.LEADING, 
	        TitledBorder.TOP,
	        new Font("Arial", Font.BOLD, 16),
	        PioColores.GRIS_TEXT
	    ));
		panel_2.setBackground(PioColores.BLANCO);
		GridBagConstraints gbc_panel_2 = new GridBagConstraints();
		gbc_panel_2.insets = new Insets(0, 0, 5, 5);
		gbc_panel_2.fill = GridBagConstraints.BOTH;
		gbc_panel_2.gridx = 1;
		gbc_panel_2.gridy = 1;
		panel_1.add(panel_2, gbc_panel_2);
		GridBagLayout gbl_panel_2 = new GridBagLayout();
		gbl_panel_2.columnWidths = new int[] { 20, 100, 20, 200, 20, 0 };
		gbl_panel_2.rowHeights = new int[] { 0, 30, 10, 30, 10, 30, 10, 30, 10, 0 };
		gbl_panel_2.columnWeights = new double[] { 0.0, 0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE };
		gbl_panel_2.rowWeights = new double[] { 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE };
		panel_2.setLayout(gbl_panel_2);

		JLabel lblNewLabel_1 = new JLabel("Nombre:");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblNewLabel_1.setForeground(PioColores.GRIS_TEXT);
		GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
		gbc_lblNewLabel_1.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_1.gridx = 1;
		gbc_lblNewLabel_1.gridy = 1;
		panel_2.add(lblNewLabel_1, gbc_lblNewLabel_1);

		textField = new JTextField();
		textField.setEditable(false);
		textField.setForeground(Color.GRAY);
		textField.setFont(new Font("Tahoma", Font.PLAIN, 14));
		textField.setBackground(PioColores.GRIS_FIELDS);
		textField.setBorder(null);
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.insets = new Insets(0, 0, 5, 5);
		gbc_textField.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField.gridx = 3;
		gbc_textField.gridy = 1;
		panel_2.add(textField, gbc_textField);
		textField.setColumns(10);

		JLabel lblNewLabel_2 = new JLabel("Apellidos:");
		lblNewLabel_2.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblNewLabel_2.setForeground(PioColores.GRIS_TEXT);
		GridBagConstraints gbc_lblNewLabel_2 = new GridBagConstraints();
		gbc_lblNewLabel_2.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_2.gridx = 1;
		gbc_lblNewLabel_2.gridy = 3;
		panel_2.add(lblNewLabel_2, gbc_lblNewLabel_2);

		textField_1 = new JTextField();
		textField_1.setForeground(Color.GRAY);
		textField_1.setEditable(false);
		textField_1.setFont(new Font("Tahoma", Font.PLAIN, 14));
		textField_1.setBackground(PioColores.GRIS_FIELDS);
		textField_1.setBorder(null);
		GridBagConstraints gbc_textField_1 = new GridBagConstraints();
		gbc_textField_1.insets = new Insets(0, 0, 5, 5);
		gbc_textField_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_1.gridx = 3;
		gbc_textField_1.gridy = 3;
		panel_2.add(textField_1, gbc_textField_1);
		textField_1.setColumns(10);

		JLabel lblNewLabel_3 = new JLabel("Email:");
		lblNewLabel_3.setForeground(PioColores.GRIS_TEXT);
		lblNewLabel_3.setFont(new Font("Tahoma", Font.BOLD, 14));
		GridBagConstraints gbc_lblNewLabel_3 = new GridBagConstraints();
		gbc_lblNewLabel_3.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_3.gridx = 1;
		gbc_lblNewLabel_3.gridy = 5;
		panel_2.add(lblNewLabel_3, gbc_lblNewLabel_3);

		textField_2 = new JTextField();
		textField_2.setFont(new Font("Tahoma", Font.PLAIN, 14));
		textField_2.setBackground(PioColores.GRIS_FIELDS);
		textField_2.setBorder(null);
		textField_2.setEditable(false);
		textField_2.setForeground(Color.GRAY);
		GridBagConstraints gbc_textField_2 = new GridBagConstraints();
		gbc_textField_2.insets = new Insets(0, 0, 5, 5);
		gbc_textField_2.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_2.gridx = 3;
		gbc_textField_2.gridy = 5;
		panel_2.add(textField_2, gbc_textField_2);
		textField_2.setColumns(10);

		JLabel lblNewLabel_4 = new JLabel("Género:");
		lblNewLabel_4.setForeground(PioColores.GRIS_TEXT);
		lblNewLabel_4.setFont(new Font("Tahoma", Font.BOLD, 14));
		GridBagConstraints gbc_lblNewLabel_4 = new GridBagConstraints();
		gbc_lblNewLabel_4.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_4.gridx = 1;
		gbc_lblNewLabel_4.gridy = 7;
		panel_2.add(lblNewLabel_4, gbc_lblNewLabel_4);

		JPanel panelGenero = new JPanel();
		panelGenero.setBackground(Color.WHITE);
		GridBagConstraints gbc_panelGenero = new GridBagConstraints();
		gbc_panelGenero.anchor = GridBagConstraints.WEST;
		gbc_panelGenero.insets = new Insets(0, 0, 5, 5);
		gbc_panelGenero.gridx = 3;
		gbc_panelGenero.gridy = 7;
		panel_2.add(panelGenero, gbc_panelGenero);

		rdbtnHombre = new JRadioButton("Hombre");
		rdbtnHombre.setEnabled(false);
		rdbtnHombre.setBackground(Color.WHITE);
		rdbtnHombre.setFont(new Font("Tahoma", Font.PLAIN, 14));
		buttonGroup.add(rdbtnHombre);
		panelGenero.add(rdbtnHombre);

		rdbtnMujer = new JRadioButton("Mujer");
		rdbtnMujer.setEnabled(false);
		rdbtnMujer.setBackground(Color.WHITE);
		rdbtnMujer.setFont(new Font("Tahoma", Font.PLAIN, 14));
		buttonGroup.add(rdbtnMujer);
		panelGenero.add(rdbtnMujer);

		// Panel cambiar contraseña
		JPanel panel_3 = new JPanel();
		panel_3.setBorder(new TitledBorder(
		        BorderFactory.createCompoundBorder(
		                new LineBorder(new Color(200, 200, 200), 1, true),
		                new EmptyBorder(10, 15, 15, 15)
		            ),
		            "Cambiar Contraseña", 
		            TitledBorder.LEADING, 
		            TitledBorder.TOP,
		            new Font("Arial", Font.BOLD, 16),
		            PioColores.GRIS_TEXT
		        ));
		panel_3.setBackground(PioColores.BLANCO);
		GridBagConstraints gbc_panel_3 = new GridBagConstraints();
		gbc_panel_3.insets = new Insets(0, 0, 5, 5);
		gbc_panel_3.fill = GridBagConstraints.HORIZONTAL;
		gbc_panel_3.gridx = 1;
		gbc_panel_3.gridy = 3;
		panel_1.add(panel_3, gbc_panel_3);
		GridBagLayout gbl_panel_3 = new GridBagLayout();
		gbl_panel_3.columnWidths = new int[] { 20, 100, 20, 200, 20, 0 };
		gbl_panel_3.rowHeights = new int[] { 10, 30, 10, 30, 10, 30, 10, 0 };
		gbl_panel_3.columnWeights = new double[] { 0.0, 0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE };
		gbl_panel_3.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		panel_3.setLayout(gbl_panel_3);

		JLabel lblPassword1 = new JLabel("Contraseña actual:");
		lblPassword1.setForeground(PioColores.GRIS_TEXT);
		lblPassword1.setFont(new Font("Tahoma", Font.BOLD, 14));
		GridBagConstraints gbc_lblPassword1 = new GridBagConstraints();
		gbc_lblPassword1.insets = new Insets(0, 0, 5, 5);
		gbc_lblPassword1.gridx = 1;
		gbc_lblPassword1.gridy = 1;
		panel_3.add(lblPassword1, gbc_lblPassword1);

		passwordField = new JPasswordField();
		passwordField.setFont(new Font("Tahoma", Font.PLAIN, 14));
		passwordField.setBackground(PioColores.GRIS_FIELDS);
		passwordField.setBorder(null);
		GridBagConstraints gbc_passwordField = new GridBagConstraints();
		gbc_passwordField.insets = new Insets(0, 0, 5, 5);
		gbc_passwordField.fill = GridBagConstraints.HORIZONTAL;
		gbc_passwordField.gridx = 3;
		gbc_passwordField.gridy = 1;
		panel_3.add(passwordField, gbc_passwordField);

		JLabel lblPassword2 = new JLabel("Nueva contraseña:");
		lblPassword2.setForeground(PioColores.GRIS_TEXT);
		lblPassword2.setFont(new Font("Tahoma", Font.BOLD, 14));
		GridBagConstraints gbc_lblPassword2 = new GridBagConstraints();
		gbc_lblPassword2.insets = new Insets(0, 0, 5, 5);
		gbc_lblPassword2.gridx = 1;
		gbc_lblPassword2.gridy = 3;
		panel_3.add(lblPassword2, gbc_lblPassword2);

		passwordField_1 = new JPasswordField();
		passwordField_1.setFont(new Font("Tahoma", Font.PLAIN, 14));
		passwordField_1.setBackground(PioColores.GRIS_FIELDS);
		passwordField_1.setBorder(null);
		GridBagConstraints gbc_passwordField_1 = new GridBagConstraints();
		gbc_passwordField_1.insets = new Insets(0, 0, 5, 5);
		gbc_passwordField_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_passwordField_1.gridx = 3;
		gbc_passwordField_1.gridy = 3;
		panel_3.add(passwordField_1, gbc_passwordField_1);

		JLabel lblPassword3 = new JLabel("Confirmar contraseña:");
		lblPassword3.setForeground(PioColores.GRIS_TEXT);
		lblPassword3.setFont(new Font("Tahoma", Font.BOLD, 14));
		GridBagConstraints gbc_lblPassword3 = new GridBagConstraints();
		gbc_lblPassword3.insets = new Insets(0, 0, 5, 5);
		gbc_lblPassword3.gridx = 1;
		gbc_lblPassword3.gridy = 5;
		panel_3.add(lblPassword3, gbc_lblPassword3);

		passwordField_2 = new JPasswordField();
		passwordField_2.setFont(new Font("Tahoma", Font.PLAIN, 14));
		passwordField_2.setBackground(PioColores.GRIS_FIELDS);
		passwordField_2.setBorder(null);
		GridBagConstraints gbc_passwordField_2 = new GridBagConstraints();
		gbc_passwordField_2.insets = new Insets(0, 0, 5, 5);
		gbc_passwordField_2.fill = GridBagConstraints.HORIZONTAL;
		gbc_passwordField_2.gridx = 3;
		gbc_passwordField_2.gridy = 5;
		panel_3.add(passwordField_2, gbc_passwordField_2);

		// Panel botones
		JPanel panelBotones = new JPanel();
		panelBotones.setBackground(Color.WHITE);
		GridBagConstraints gbc_panelBotones = new GridBagConstraints();
		gbc_panelBotones.insets = new Insets(0, 0, 0, 5);
		gbc_panelBotones.gridx = 1;
		gbc_panelBotones.gridy = 5;
		panel_1.add(panelBotones, gbc_panelBotones);
		
				JButton btnCancelar = new PioButton("Cancelar");
				btnCancelar.setBackground(PioColores.MARRON_BUTTON);
				btnCancelar.setFont(new Font("Tahoma", Font.BOLD, 14));
				btnCancelar.setPreferredSize(new Dimension(120, 35));
				btnCancelar.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						cargarDatosUsuario();
						JOptionPane.showMessageDialog(PerfilUsuario.this, "Cambios cancelados");
					}
				});
				
						JButton btnGuardar = new PioButton("Guardar Cambios");
						btnGuardar.setText("Guardar");
						btnGuardar.setBackground(PioColores.VERDE_BUTTON);
						btnGuardar.setFont(new Font("Tahoma", Font.BOLD, 14));
						btnGuardar.setPreferredSize(new Dimension(150, 35));
						btnGuardar.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								guardarCambios();
							}
						});
						panelBotones.setLayout(new BoxLayout(panelBotones, BoxLayout.X_AXIS));
						panelBotones.add(btnGuardar);
				
				Component horizontalStrut = Box.createHorizontalStrut(20);
				panelBotones.add(horizontalStrut);
				panelBotones.add(btnCancelar);
	}

	private void cargarDatosUsuario() {
		if (usuario != null) {
			textField.setText(usuario.getNombre());
			textField_1.setText(usuario.getApellidos());
			textField_2.setText(usuario.getEmail());

			if ("Hombre".equals(usuario.getGenero())) {
				rdbtnHombre.setSelected(true);
			} else {
				rdbtnMujer.setSelected(true);
			}

			actualizarImagenPerfil();

			// Limpiar campos de contraseña
			passwordField.setText("");
			passwordField_1.setText("");
			passwordField_2.setText("");
		}
	}

	private void actualizarImagenPerfil() {
		String ruta = nuevaRutaImagen;
		Image imagen = ImageUtils.cargarImagen(ruta);

		if (imagen != null) {
			lblFotoPerfil.setIcon(ImageUtils.createCircularIcon(imagen, 140));
		}
	}

	private void cambiarFotoPerfil() {
		String[] opciones = { "Introducir enlace", "Seleccionar archivo" };
		int seleccion = JOptionPane.showOptionDialog(this, "Seleccione cómo desea cargar la imagen:", "Cargar Imagen",
				JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, opciones, opciones[0]);

		if (seleccion == 0) {
			// Cargar desde URL
			String urlImagen = JOptionPane.showInputDialog(this, "Introduzca el enlace de la imagen:",
					"Cargar Imagen desde URL", JOptionPane.PLAIN_MESSAGE);

			if (urlImagen != null && !urlImagen.isEmpty()) {
				
				Image img = ImageUtils.cargarImagen(urlImagen);
				if (img != null) {
					nuevaRutaImagen = urlImagen;
					imagenCambiada = true;
					actualizarImagenPerfil();
				} else {
					JOptionPane.showMessageDialog(this, "No se pudo cargar la imagen desde el enlace.", "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		} else if (seleccion == 1) {
			// Cargar desde archivo
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setFileFilter(new FileNameExtensionFilter("Imágenes", "jpg", "jpeg", "png", "gif"));
			int resultado = fileChooser.showOpenDialog(this);
			if (resultado == JFileChooser.APPROVE_OPTION) {
				File archivo = fileChooser.getSelectedFile();
				if (ImageUtils.esImagenValida(archivo.getAbsolutePath())) {
					nuevaRutaImagen = archivo.getAbsolutePath();
					imagenCambiada = true;
					actualizarImagenPerfil();
				} else {
					JOptionPane.showMessageDialog(this, "El archivo seleccionado no es una imagen válida.", "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		}
	}

	private void guardarCambios() {
		try {
			// Validar datos básicos
			if (textField.getText().trim().isEmpty() || textField_1.getText().trim().isEmpty()) {
				JOptionPane.showMessageDialog(this, "Nombre y apellidos son obligatorios", "Datos incompletos",
						JOptionPane.WARNING_MESSAGE);
				return;
			}

			// Validar contraseña si se quiere cambiar
			String passwordActual = new String(passwordField.getPassword());
			String passwordNueva = new String(passwordField_1.getPassword());
			String passwordConfirmar = new String(passwordField_2.getPassword());

			boolean cambiarPassword = !passwordActual.isEmpty() || !passwordNueva.isEmpty()
					|| !passwordConfirmar.isEmpty();

			if (cambiarPassword) {
				// Validar que todos los campos estén llenos si se quiere cambiar contraseña
				if (passwordActual.isEmpty() || passwordNueva.isEmpty() || passwordConfirmar.isEmpty()) {
					JOptionPane.showMessageDialog(this, "Para cambiar la contraseña debe llenar todos los campos",
							"Campos incompletos", JOptionPane.WARNING_MESSAGE);
					return;
				}

				// Validar contraseña actual
				if (!passwordActual.equals(usuario.getPassword())) {
					JOptionPane.showMessageDialog(this, "La contraseña actual es incorrecta", "Error de contraseña",
							JOptionPane.ERROR_MESSAGE);
					passwordField.setBackground(PioColores.ROJO_ERROR);
					return;
				} else {
					passwordField.setBackground(PioColores.GRIS_FIELDS);
				}

				// Validar que las nuevas contraseñas coincidan
				if (!passwordNueva.equals(passwordConfirmar)) {
					JOptionPane.showMessageDialog(this, "Las contraseñas nuevas no coinciden", "Error de contraseña",
							JOptionPane.ERROR_MESSAGE);
					passwordField_1.setBackground(PioColores.ROJO_ERROR);
					passwordField_2.setBackground(PioColores.ROJO_ERROR);
					return;
				} else {
					passwordField_1.setBackground(PioColores.GRIS_FIELDS);
					passwordField_2.setBackground(PioColores.GRIS_FIELDS);
				}

			}

			// Actualizar datos del usuario
			
			
			String nombre = textField.getText().trim();
			String apellidos = textField_1.getText().trim();
			String genero = rdbtnHombre.isSelected() ? "Hombre" : "Mujer";

			
			
			controlador.getUsuarioController().actualizarUsuario(usuario, nombre, apellidos, genero, nuevaRutaImagen, passwordNueva, imagenCambiada, cambiarPassword);

			// Guardar en base de datos
			controlador.getUsuarioController().modificarUsuario(usuario);
			
			
			JOptionPane.showMessageDialog(this, "Perfil actualizado correctamente", "Éxito",
					JOptionPane.INFORMATION_MESSAGE);

			// Limpiar campos de contraseña
			passwordField.setText("");
			passwordField_1.setText("");
			passwordField_2.setText("");
			imagenCambiada = false;

		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Error al guardar los cambios: " + e.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}
	
}