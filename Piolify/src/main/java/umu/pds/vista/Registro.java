package umu.pds.vista;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

import umu.pds.controlador.Piolify;
import umu.pds.utils.RegistroUsuarioDTO;
import umu.pds.utils.ImageUtils;
import umu.pds.vista.elementos.PioButton;
import umu.pds.vista.elementos.PioColores;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.Image;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.GridBagConstraints;
import javax.swing.JTextField;
import java.awt.Insets;
import java.awt.Toolkit;

import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.Font;
import java.awt.Cursor;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.awt.event.ActionEvent;
import javax.swing.ButtonGroup;

public class Registro extends JFrame {

	private static final long serialVersionUID = 1L;
	JLabel lblNewLabel;
	private JPanel contentPane;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JPasswordField passwordField;
	private JPasswordField passwordField_1;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private JRadioButton rdbtnNewRadioButton;
	private JRadioButton rdbtnNewRadioButton_1;
	private String rutaImagenPefil = "/fotoUser.png";


	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Registro frame = new Registro();
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
	public Registro() {
		setTitle("Registro - Piolify");
		setIconImage(Toolkit.getDefaultToolkit().getImage(Registro.class.getResource("/mascota.png")));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 912, 634);
		contentPane = new JPanel();
		contentPane.setBorder(null);

		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		panel.setBackground(PioColores.AMARILLO_LABEL);
		contentPane.add(panel, BorderLayout.WEST);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]{0, 309, 0, 0};
		gbl_panel.rowHeights = new int[]{0, 0, 20, 0, 0, 0};
		gbl_panel.columnWeights = new double[]{1.0, 1.0, 1.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{1.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		panel.setLayout(gbl_panel);
		
		JLabel lblNewLabel_7 = new JLabel("Únete a nosotros");
		lblNewLabel_7.setForeground(PioColores.BLANCO);
		lblNewLabel_7.setFont(new Font("Arial", Font.BOLD, 20));
		GridBagConstraints gbc_lblNewLabel_7 = new GridBagConstraints();
		gbc_lblNewLabel_7.fill = GridBagConstraints.VERTICAL;
		gbc_lblNewLabel_7.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_7.gridx = 1;
		gbc_lblNewLabel_7.gridy = 0;
		panel.add(lblNewLabel_7, gbc_lblNewLabel_7);
		
		JLabel lblNewLabel_8 = new JLabel("Pulse para elegir una foto");
		lblNewLabel_8.setForeground(PioColores.GRIS_TEXT);
		lblNewLabel_8.setFont(new Font("Arial", Font.BOLD, 15));
		GridBagConstraints gbc_lblNewLabel_8 = new GridBagConstraints();
		gbc_lblNewLabel_8.fill = GridBagConstraints.VERTICAL;
		gbc_lblNewLabel_8.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_8.gridx = 1;
		gbc_lblNewLabel_8.gridy = 1;
		panel.add(lblNewLabel_8, gbc_lblNewLabel_8);
		
		lblNewLabel = new JLabel("");
		lblNewLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.fill = GridBagConstraints.VERTICAL;
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel.gridx = 1;
		gbc_lblNewLabel.gridy = 3;
		lblNewLabel.setIcon(new ImageIcon(getClass().getResource("/fotoUser.png")));
		panel.add(lblNewLabel, gbc_lblNewLabel);
		lblNewLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				seleccionarFotoPerfil();
			}
		});
		
		JPanel panel_1 = new JPanel();
		panel_1.setBackground(PioColores.BLANCO);
		contentPane.add(panel_1, BorderLayout.CENTER);
		GridBagLayout gbl_panel_1 = new GridBagLayout();
		gbl_panel_1.columnWidths = new int[]{0, 180, 180, 0, 0};
		gbl_panel_1.rowHeights = new int[]{0, 0, 0, 20, 0, 0, 20, 0, 0, 20, 0, 0, 20, 0, 0, 20, 0, 0, 0, 0, 0, 0};
		gbl_panel_1.columnWeights = new double[]{1.0, 1.0, 1.0, 1.0, Double.MIN_VALUE};
		gbl_panel_1.rowWeights = new double[]{1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 1.0, Double.MIN_VALUE};
		panel_1.setLayout(gbl_panel_1);
		
		JLabel lblNewLabel_1 = new JLabel("Nombre");
		GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
		gbc_lblNewLabel_1.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel_1.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_1.gridx = 1;
		gbc_lblNewLabel_1.gridy = 1;
		panel_1.add(lblNewLabel_1, gbc_lblNewLabel_1);
		
		textField = new JTextField();
		textField.setBorder(null);
		textField.setBackground(PioColores.GRIS_FIELDS);
		textField.setFont(new Font("Tahoma", Font.PLAIN, 15));
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.gridwidth = 2;
		gbc_textField.insets = new Insets(0, 0, 5, 5);
		gbc_textField.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField.gridx = 1;
		gbc_textField.gridy = 2;
		panel_1.add(textField, gbc_textField);
		textField.setColumns(10);
		
		JLabel lblNewLabel_2 = new JLabel("Apellidos");
		GridBagConstraints gbc_lblNewLabel_2 = new GridBagConstraints();
		gbc_lblNewLabel_2.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel_2.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_2.gridx = 1;
		gbc_lblNewLabel_2.gridy = 4;
		panel_1.add(lblNewLabel_2, gbc_lblNewLabel_2);
		
		textField_1 = new JTextField();
		textField_1.setFont(new Font("Tahoma", Font.PLAIN, 15));
		textField_1.setBackground(PioColores.GRIS_FIELDS);
		textField_1.setBorder(null);
		GridBagConstraints gbc_textField_1 = new GridBagConstraints();
		gbc_textField_1.gridwidth = 2;
		gbc_textField_1.insets = new Insets(0, 0, 5, 5);
		gbc_textField_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_1.gridx = 1;
		gbc_textField_1.gridy = 5;
		panel_1.add(textField_1, gbc_textField_1);
		textField_1.setColumns(10);
		
		JLabel lblNewLabel_3 = new JLabel("Género");
		GridBagConstraints gbc_lblNewLabel_3 = new GridBagConstraints();
		gbc_lblNewLabel_3.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel_3.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_3.gridx = 1;
		gbc_lblNewLabel_3.gridy = 7;
		panel_1.add(lblNewLabel_3, gbc_lblNewLabel_3);
		
		rdbtnNewRadioButton = new JRadioButton("Hombre");
		rdbtnNewRadioButton.setBackground(PioColores.BLANCO);
		buttonGroup.add(rdbtnNewRadioButton);
		GridBagConstraints gbc_rdbtnNewRadioButton = new GridBagConstraints();
		gbc_rdbtnNewRadioButton.fill = GridBagConstraints.VERTICAL;
		gbc_rdbtnNewRadioButton.insets = new Insets(0, 0, 5, 5);
		gbc_rdbtnNewRadioButton.gridx = 1;
		gbc_rdbtnNewRadioButton.gridy = 8;
		panel_1.add(rdbtnNewRadioButton, gbc_rdbtnNewRadioButton);
		
		rdbtnNewRadioButton_1 = new JRadioButton("Mujer");
		rdbtnNewRadioButton_1.setBackground(PioColores.BLANCO);
		buttonGroup.add(rdbtnNewRadioButton_1);
		GridBagConstraints gbc_rdbtnNewRadioButton_1 = new GridBagConstraints();
		gbc_rdbtnNewRadioButton_1.fill = GridBagConstraints.VERTICAL;
		gbc_rdbtnNewRadioButton_1.insets = new Insets(0, 0, 5, 5);
		gbc_rdbtnNewRadioButton_1.gridx = 2;
		gbc_rdbtnNewRadioButton_1.gridy = 8;
		panel_1.add(rdbtnNewRadioButton_1, gbc_rdbtnNewRadioButton_1);
		
		JLabel lblNewLabel_4 = new JLabel("Correo electrónico");
		GridBagConstraints gbc_lblNewLabel_4 = new GridBagConstraints();
		gbc_lblNewLabel_4.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel_4.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_4.gridx = 1;
		gbc_lblNewLabel_4.gridy = 10;
		panel_1.add(lblNewLabel_4, gbc_lblNewLabel_4);
		
		textField_2 = new JTextField();
		textField_2.setFont(new Font("Tahoma", Font.PLAIN, 15));
		textField_2.setBackground(PioColores.GRIS_FIELDS);
		textField_2.setBorder(null);
		GridBagConstraints gbc_textField_2 = new GridBagConstraints();
		gbc_textField_2.gridwidth = 2;
		gbc_textField_2.insets = new Insets(0, 0, 5, 5);
		gbc_textField_2.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_2.gridx = 1;
		gbc_textField_2.gridy = 11;
		panel_1.add(textField_2, gbc_textField_2);
		textField_2.setColumns(10);
		
		JLabel lblNewLabel_5 = new JLabel("Contraseña");
		GridBagConstraints gbc_lblNewLabel_5 = new GridBagConstraints();
		gbc_lblNewLabel_5.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel_5.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_5.gridx = 1;
		gbc_lblNewLabel_5.gridy = 13;
		panel_1.add(lblNewLabel_5, gbc_lblNewLabel_5);
		
		passwordField = new JPasswordField();
		passwordField.setBorder(null);
		passwordField.setBackground(PioColores.GRIS_FIELDS);
		passwordField.setFont(new Font("Tahoma", Font.PLAIN, 15));
		GridBagConstraints gbc_passwordField = new GridBagConstraints();
		gbc_passwordField.gridwidth = 2;
		gbc_passwordField.insets = new Insets(0, 0, 5, 5);
		gbc_passwordField.fill = GridBagConstraints.HORIZONTAL;
		gbc_passwordField.gridx = 1;
		gbc_passwordField.gridy = 14;
		panel_1.add(passwordField, gbc_passwordField);
		
		JLabel lblNewLabel_6 = new JLabel("Confirmar contraseña");
		GridBagConstraints gbc_lblNewLabel_6 = new GridBagConstraints();
		gbc_lblNewLabel_6.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel_6.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_6.gridx = 1;
		gbc_lblNewLabel_6.gridy = 16;
		panel_1.add(lblNewLabel_6, gbc_lblNewLabel_6);
		
		passwordField_1 = new JPasswordField();
		passwordField_1.setFont(new Font("Tahoma", Font.PLAIN, 15));
		passwordField_1.setBackground(PioColores.GRIS_FIELDS);
		passwordField_1.setBorder(null);
		GridBagConstraints gbc_passwordField_1 = new GridBagConstraints();
		gbc_passwordField_1.gridwidth = 2;
		gbc_passwordField_1.insets = new Insets(0, 0, 5, 5);
		gbc_passwordField_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_passwordField_1.gridx = 1;
		gbc_passwordField_1.gridy = 17;
		panel_1.add(passwordField_1, gbc_passwordField_1);
		
		JButton btnNewButton_1 = new PioButton("Cancelar");
		btnNewButton_1.setBackground(PioColores.MARRON_BUTTON);
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				Login login = new Login();
				login.getFrame().setVisible(true);
			}
		});

		GridBagConstraints gbc_btnNewButton_1 = new GridBagConstraints();
		gbc_btnNewButton_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnNewButton_1.insets = new Insets(0, 0, 5, 5);
		gbc_btnNewButton_1.gridx = 1;
		gbc_btnNewButton_1.gridy = 19;
		panel_1.add(btnNewButton_1, gbc_btnNewButton_1);
		
		JButton btnNewButton = new PioButton("Registrar");
		btnNewButton.setBackground(PioColores.VERDE_BUTTON);
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				registrarUsuario();
			}
		});
		
		GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
		gbc_btnNewButton.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnNewButton.insets = new Insets(0, 0, 5, 5);
		gbc_btnNewButton.gridx = 2;
		gbc_btnNewButton.gridy = 19;
		panel_1.add(btnNewButton, gbc_btnNewButton);
		
		// boton enter para registrar usuario
		getRootPane().setDefaultButton(btnNewButton);
	}
	
	
	private void seleccionarFotoPerfil() {
	    // Opciones para URL o archivo local
	    String[] opciones = { "Introducir enlace", "Seleccionar archivo" };
	    int seleccion = JOptionPane.showOptionDialog(contentPane, "Seleccione cómo desea cargar la imagen:",
	            "Cargar Imagen", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, opciones,
	            opciones[0]);

	    if (seleccion == 0) {
	        // Cargar imagen desde URL
	        String urlImagen = JOptionPane.showInputDialog(contentPane, "Introduzca el enlace de la imagen:",
	                "Cargar Imagen desde URL", JOptionPane.PLAIN_MESSAGE);

	        if (urlImagen != null && !urlImagen.isEmpty()) {
	            try {
	                BufferedImage imagen = ImageIO.read(new URL(urlImagen));
	                ImageIcon icono = new ImageIcon(imagen.getScaledInstance(100, 100, Image.SCALE_SMOOTH));
	                icono = ImageUtils.createCircularIcon(imagen, 100);

	                String descripcion = urlImagen;
	                if(descripcion.startsWith("file:")){
	                    descripcion = descripcion.substring(5);
	                }
	                icono.setDescription(descripcion);
	                lblNewLabel.setIcon(icono);
	                rutaImagenPefil = descripcion;
	            } catch (IOException ex) {
	                JOptionPane.showMessageDialog(contentPane, "No se pudo cargar la imagen desde el enlace.", "Error",
	                        JOptionPane.ERROR_MESSAGE);
	            }
	        }
	    } else if (seleccion == 1) {
	        JFileChooser fileChooser = new JFileChooser();
	        fileChooser.setFileFilter(new FileNameExtensionFilter("Imágenes", "jpg", "jpeg", "png", "gif"));
	        int resultado = fileChooser.showOpenDialog(contentPane);
	        if (resultado == JFileChooser.APPROVE_OPTION) {
	            try {
	                BufferedImage imagen = ImageIO.read(fileChooser.getSelectedFile());
	                ImageIcon icono = new ImageIcon(imagen.getScaledInstance(100, 100, Image.SCALE_SMOOTH));
	                icono = ImageUtils.createCircularIcon(imagen, 100);
	                String ruta = fileChooser.getSelectedFile().getAbsolutePath();
	                if(ruta.startsWith("file:")){
	                    ruta = ruta.substring(5);
	                }
	                icono.setDescription(ruta);
	                lblNewLabel.setIcon(icono);
	                rutaImagenPefil = ruta;
	            } catch (IOException ex) {
	                JOptionPane.showMessageDialog(contentPane, "No se pudo cargar la imagen desde el archivo.", "Error",
	                        JOptionPane.ERROR_MESSAGE);
	            }
	        }
	    }
	}
	
	private void registrarUsuario() {
	    String nombre = textField.getText().trim();
	    String apellidos = textField_1.getText().trim();
	    String email = textField_2.getText().trim();
	    String genero = "";
	    String password = new String(passwordField.getPassword());
	    String confirmar = new String(passwordField_1.getPassword());

	    
	    if (rdbtnNewRadioButton.isSelected()) {
	        genero = "Hombre";
	    } else if (rdbtnNewRadioButton_1.isSelected()) {
	        genero = "Mujer";
	    }
	    
	    // USAR EL CONTROLADOR PARA GUARDAR EN BD
	    try {
	        Piolify controlador = Piolify.getUnicaInstancia();
	        RegistroUsuarioDTO dto = new RegistroUsuarioDTO.Builder()
	        	    .nombre(nombre)
	        	    .apellidos(apellidos)
	        	    .genero(genero)
	        	    .email(email)
	        	    .password(password)
	        	    .confirmar(confirmar)
	        	    .rutaImagenPerfil(rutaImagenPefil)
	        	    .build();

	        boolean exito = controlador.getUsuarioController().registrarUsuario(dto);
	        
	        if (exito) {
	        	JOptionPane.showMessageDialog(this, "Registro exitoso. ¡Bienvenido/a a Piolify!", "Éxito", JOptionPane.INFORMATION_MESSAGE);
	            dispose();
	        }
	        
	    } catch (IllegalArgumentException e) {
	    	
	    	JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error de validación", JOptionPane.ERROR_MESSAGE);
	        
	    } catch (RuntimeException e) {
	        JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error de registro", JOptionPane.ERROR_MESSAGE);
	    }
	}
}
