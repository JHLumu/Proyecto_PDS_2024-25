package umu.pds.vista;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import umu.pds.modelo.Curso;
import umu.pds.modelo.Usuario;
import umu.pds.controlador.Piolify;
import umu.pds.vista.elementos.PioButton;
import umu.pds.vista.elementos.PioColores;

public class Biblioteca extends JPanel {
	private Usuario usuario;
	private Piolify controlador = Piolify.getUnicaInstancia();
	private JPanel panelCentral;
	private JPanel panelListaCursos;
	private JPanel panelImportarCursos;
	private JTextArea descripcionAreaListaCursos;
	private Color panelColor = PioColores.GRIS_PANEL;

	private static final long serialVersionUID = 1L;

	/**
	 * Create the panel.
	 */
	public Biblioteca(Usuario usuario) {
		this.usuario = usuario;
		
		setLayout(new BorderLayout());
		setBackground(PioColores.BLANCO);

		panelCentral = new JPanel();
		panelCentral.setBackground(PioColores.BLANCO);

		GridBagLayout gbl_panelCentral = new GridBagLayout();
		gbl_panelCentral.columnWidths = new int[] { 20, 0, 20, 0, 20, 0 };
		gbl_panelCentral.rowHeights = new int[] { 20, 0, 20, 0, 20, 0 };
		gbl_panelCentral.columnWeights = new double[] { 0.0, 0.5, 0.0, 0.5, 0.0, Double.MIN_VALUE };
		gbl_panelCentral.rowWeights = new double[] { 0.0, 0.4, 0.0, 0.6, 0.0, Double.MIN_VALUE };
		panelCentral.setLayout(gbl_panelCentral);

		JScrollPane scrollPane = new JScrollPane(panelCentral);
		add(scrollPane, BorderLayout.CENTER);

		GridBagConstraints gbc_panelListaCursos = new GridBagConstraints();
		gbc_panelListaCursos.gridx = 1;
		gbc_panelListaCursos.gridy = 2;
		gbc_panelListaCursos.fill = GridBagConstraints.BOTH;
		gbc_panelListaCursos.weightx = 1.0;
		gbc_panelListaCursos.weighty = 1.0;
		panelListaCursos = createPanelListaCursos("Biblioteca de Cursos");
		panelCentral.add(panelListaCursos, gbc_panelListaCursos);

		GridBagConstraints gbc_panelImportarCursos = new GridBagConstraints();
		gbc_panelImportarCursos.gridx = 3;
		gbc_panelImportarCursos.gridy = 2;
		gbc_panelImportarCursos.fill = GridBagConstraints.BOTH;
		gbc_panelImportarCursos.weightx = 1.0;
		gbc_panelImportarCursos.weighty = 1.0;
		panelImportarCursos = createPanelImportarCursos("Importar Cursos");
		panelCentral.add(panelImportarCursos, gbc_panelImportarCursos);

		JButton btnImportar = new PioButton("Importar curso");
		btnImportar.setBackground(PioColores.MARRON_BUTTON);
		btnImportar.addActionListener(e -> importarCurso());
		panelImportarCursos.add(btnImportar, BorderLayout.SOUTH);

		refrescarCursos();
	}

	/**
	 * Crea un panel para la biblioteca interna de cursos (usando BorderLayout)
	 */
	private JPanel createPanelListaCursos(String titulo) {
		JPanel panelListaCursos = new JPanel();
		panelListaCursos.setBackground(panelColor);
		panelListaCursos.setBorder(BorderFactory.createCompoundBorder(
				new MatteBorder(1, 1, 1, 1, new Color(220, 220, 220)), new EmptyBorder(15, 15, 15, 15)));

		panelListaCursos.setLayout(new BorderLayout(0, 15)); 

		// Título
		JLabel lblTitulo = new JLabel(titulo);
		lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
		lblTitulo.setForeground(PioColores.GRIS_TEXT);
		panelListaCursos.add(lblTitulo, BorderLayout.NORTH);

		// Descripción por defecto
		descripcionAreaListaCursos = new JTextArea(
				"Aquí aparecerán todos los cursos disponibles para que explores, aprendas y sigas creciendo. "
						+ "Actualmente no hay cursos cargados, pero muy pronto tendrás acceso a contenidos variados "
						+ "y personalizados. ¡Mantente atento, lo bueno está por llegar!");
		descripcionAreaListaCursos.setLineWrap(true);
		descripcionAreaListaCursos.setWrapStyleWord(true);
		descripcionAreaListaCursos.setEditable(false);
		descripcionAreaListaCursos.setOpaque(false);
		descripcionAreaListaCursos.setFont(new Font("Arial", Font.PLAIN, 14));
		descripcionAreaListaCursos.setForeground(PioColores.NEGRO);
		descripcionAreaListaCursos.setBorder(null);

		return panelListaCursos;
	}

	/**
	 * Crea un panel para la biblioteca interna de cursos (importar cursos)
	 */
	private JPanel createPanelImportarCursos(String titulo) {
		JPanel panelImportarCursos = new JPanel();
		panelImportarCursos.setBackground(panelColor);
		panelImportarCursos.setBorder(BorderFactory.createCompoundBorder(
				new MatteBorder(1, 1, 1, 1, new Color(220, 220, 220)), new EmptyBorder(15, 15, 15, 15)));

		panelImportarCursos.setLayout(new BorderLayout(0, 15));

		// Título del panel
		JLabel lblTitulo = new JLabel(titulo);
		lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
		lblTitulo.setForeground(PioColores.GRIS_TEXT);
		panelImportarCursos.add(lblTitulo, BorderLayout.NORTH);

		JTextArea descripcionArea = new JTextArea(
				"Selecciona un archivo en formato .json para importar tus cursos a la plataforma.\n"
						+ "Asegúrate de que el archivo siga la estructura correcta para que la importación se realice sin problemas.");

		descripcionArea.setLineWrap(true);
		descripcionArea.setWrapStyleWord(true);
		descripcionArea.setEditable(false);
		descripcionArea.setOpaque(false);
		descripcionArea.setFont(new Font("Arial", Font.PLAIN, 14));
		descripcionArea.setForeground(PioColores.NEGRO);
		descripcionArea.setBorder(null);
		panelImportarCursos.add(descripcionArea, BorderLayout.CENTER);

		return panelImportarCursos;
	}

	private void importarCurso() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileFilter(new FileNameExtensionFilter("Archivos JSON (*.json)", "json"));

		int resultado = fileChooser.showOpenDialog(this);
		if (resultado == JFileChooser.APPROVE_OPTION) {
			File archivo = fileChooser.getSelectedFile();
			try {
				// Contar cursos antes de la importación
				int cursosAntes = usuario.getBiblioteca().size();

				boolean resultadoImportacion = controlador.getImportacionController()
						.importarCursosDesdeArchivo(archivo.getAbsolutePath(), usuario);

				if (!resultadoImportacion) {
					JOptionPane.showMessageDialog(this, "Error al importar el curso: No cumple con el formato esperado",
							"Error de Importación", JOptionPane.ERROR_MESSAGE);
				} else {
					//Obtener usuario actual desde el controlador
					this.usuario = controlador.getUsuarioActual();

					// Contar cursos después de la importación
					int cursosDepues = usuario.getBiblioteca().size();
					int cursosImportados = cursosDepues - cursosAntes;

					// Refrescar la vista inmediatamente
					refrescarCursos();

					String mensaje;
					if (cursosImportados > 0) {
						mensaje = cursosImportados
								+ " curso(s) importado(s) correctamente.\nYa puedes hacer clic sobre ellos para practicar los ejercicios.";
					} else {
						mensaje = "Los cursos del archivo ya estaban en tu biblioteca.\nNo se añadieron cursos duplicados.";
					}

					JOptionPane.showMessageDialog(this, mensaje, "Importación Completada",
							JOptionPane.INFORMATION_MESSAGE);
				}

			} catch (Exception ex) {
				JOptionPane.showMessageDialog(this, "Error al importar el curso: " + ex.getMessage(),
						"Error de Importación", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	public void refrescarCursos() {
	    // Remover todo del panel excepto el título
	    Component titulo = null;
	    for (Component comp : panelListaCursos.getComponents()) {
	        if (comp instanceof JLabel) {
	            titulo = comp;
	            break;
	        }
	    }

	    panelListaCursos.removeAll();

	    // Re-añadir el título
	    if (titulo != null) {
	        panelListaCursos.add(titulo, BorderLayout.NORTH);
	    }

	    // Limpiar duplicados antes de mostrar
	    List<Curso> cursos = limpiarCursosDuplicados(usuario.getBiblioteca());

	    if (cursos.isEmpty()) {
	        panelListaCursos.add(descripcionAreaListaCursos, BorderLayout.CENTER);
	    } else {

	        JPanel panelBotonesCursos = new JPanel();
	        panelBotonesCursos.setLayout(new BoxLayout(panelBotonesCursos, BoxLayout.Y_AXIS));
	        panelBotonesCursos.setOpaque(false);

	        for (Curso curso : cursos) {
	            PioButton btnCurso = new PioButton(curso.getTitulo());
	            btnCurso.setBackground(PioColores.MARRON_BUTTON);
	            btnCurso.setAlignmentX(Component.LEFT_ALIGNMENT);
	            btnCurso.setMaximumSize(new Dimension(Integer.MAX_VALUE, btnCurso.getPreferredSize().height));

	            btnCurso.addActionListener(e -> manejarClicCurso(curso));

	            panelBotonesCursos.add(btnCurso);
	            panelBotonesCursos.add(Box.createRigidArea(new Dimension(0, 5)));
	        }
	        panelListaCursos.add(panelBotonesCursos, BorderLayout.CENTER);
	    }

	    panelListaCursos.revalidate();
	    panelListaCursos.repaint();
	}

	/**
	 * Maneja el clic en un botón de curso.
	 */
	private void manejarClicCurso(Curso curso) {
	    controlador.getProgresoController().iniciarOContinuarCurso(curso, this);
	}


	/**
	 * Método para limpiar cursos duplicados basándose en el título
	 */
	private List<Curso> limpiarCursosDuplicados(List<Curso> cursos) {
		if (cursos == null || cursos.isEmpty()) {
			return cursos;
		}

		Map<String, Curso> cursosUnicos = new LinkedHashMap<>();

		for (Curso curso : cursos) {
			if (curso != null && curso.getTitulo() != null) {
				// Solo mantener el primer curso con cada título
				cursosUnicos.putIfAbsent(curso.getTitulo(), curso);
			}
		}

		List<Curso> cursosLimpios = new ArrayList<>(cursosUnicos.values());

		// Si había duplicados, actualizar la biblioteca del usuario
		if (cursosLimpios.size() != cursos.size()) {
			System.out.println(
					"Se encontraron " + (cursos.size() - cursosLimpios.size()) + " cursos duplicados. Limpiando...");
			usuario.setBiblioteca(cursosLimpios);

			// Guardar los cambios
			try {
				controlador.getUsuarioController().modificarUsuario(usuario);
				System.out.println("Biblioteca actualizada sin duplicados");
			} catch (Exception e) {
				System.err.println("Error al actualizar biblioteca: " + e.getMessage());
			}
		}

		return cursosLimpios;
	}
}