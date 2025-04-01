package umu.pds.vista;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.BoxLayout;
import java.awt.Component;
import javax.swing.Box;
import java.awt.Dimension;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Toolkit;

import javax.swing.border.EtchedBorder;
import java.awt.Font;
import javax.swing.border.LineBorder;

import umu.pds.utils.Utils;
import umu.pds.vista.elementos.PioButton;
import umu.pds.vista.elementos.PioColores;

import javax.swing.border.SoftBevelBorder;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;

public class Principal extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

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
		setTitle("Piolify");
		setIconImage(Toolkit.getDefaultToolkit().getImage(Principal.class.getResource("/mascota.png")));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 912, 634);
		contentPane = new JPanel();
		contentPane.setBorder(null);

		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JPanel panelNorte = new JPanel();
		panelNorte.setBorder(new EmptyBorder(5, 5, 5, 5));
		panelNorte.setBackground(PioColores.AMARILLO_LABEL);
		contentPane.add(panelNorte, BorderLayout.NORTH);
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
		
		JButton btnNewButton = new PioButton("Mis Cursos");
		btnNewButton.setBackground(PioColores.MARRON_BUTTON);
		panelNorte.add(btnNewButton);
		
		JButton btnNewButton_1 = new PioButton("Crear Curso");
		btnNewButton_1.setBackground(PioColores.MARRON_BUTTON);
		panelNorte.add(btnNewButton_1);
		
		JButton btnNewButton_2 = new PioButton("Estadísticas");
		btnNewButton_2.setBackground(PioColores.MARRON_BUTTON);
		panelNorte.add(btnNewButton_2);
		
		Component glue_1 = Box.createGlue();
		panelNorte.add(glue_1);
		
		JButton btnNewButton_3 = new PioButton("Mi Perfil");
		btnNewButton_3.setBackground(PioColores.MARRON_BUTTON);
		panelNorte.add(btnNewButton_3);
		
		Component rigidArea_3 = Box.createRigidArea(new Dimension(20, 20));
		panelNorte.add(rigidArea_3);
		
		JLabel lblNewLabel_3 = new JLabel("");
		lblNewLabel_3.setIcon(Utils.escalarImagen("/fotoUser.png", 50));
		panelNorte.add(lblNewLabel_3);
		
		Component rigidArea_2 = Box.createRigidArea(new Dimension(20, 20));
		panelNorte.add(rigidArea_2);
		
		JPanel panelCentro = new JPanel();
		panelCentro.setBackground(PioColores.BLANCO);
		contentPane.add(panelCentro, BorderLayout.CENTER);
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
	}

}
