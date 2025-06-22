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
import umu.pds.modelo.Bloque;
import umu.pds.modelo.Ejercicio;
import umu.pds.modelo.Estrategia;
import umu.pds.modelo.ProgresoBloque;
import umu.pds.modelo.Usuario;
import umu.pds.servicios.ServicioProgreso;
import umu.pds.controlador.ImportacionController;
import umu.pds.controlador.Piolify;
import umu.pds.vista.elementos.PioButton;
import umu.pds.vista.elementos.PioColores;

public class Biblioteca extends JPanel {
	private Usuario usuario;
	private Piolify controlador = Piolify.getUnicaInstancia();
	private ImportacionController importacionController = this.controlador.getImportacionController();
	private JPanel panelCentral;
	private JPanel panelListaCursos;
	private JPanel panelImportarCursos;
	private JTextArea descripcionAreaListaCursos;
	private Color panelColor = PioColores.GRIS_PANEL;
	private ServicioProgreso servicioProgreso;

	private static final long serialVersionUID = 1L;

	/**
	 * Create the panel.
	 */
	public Biblioteca(Usuario usuario) {
		this.usuario = usuario;
		this.servicioProgreso = new ServicioProgreso();
		
		setLayout(new BorderLayout());
		setBackground(PioColores.BLANCO);

		panelCentral = new JPanel();
		panelCentral.setBackground(PioColores.BLANCO);

		// Usamos GridBagLayout para organizar los paneles de estadísticas
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

		// Usamos BorderLayout para unificar estructura con el panel de importar
		panelListaCursos.setLayout(new BorderLayout(0, 15)); // 15px de espacio vertical

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

	private Estrategia mostrarSelectorEstrategia() {
		String[] opciones = this.importacionController.getTiposEstrategiasDefinidas().stream().map(t -> t.toString())
				.toArray(String[]::new);
		String seleccion = (String) JOptionPane.showInputDialog(this,
				"Selecciona la estrategia de aprendizaje para la sesión", "Seleccionar Estrategia",
				JOptionPane.INFORMATION_MESSAGE, null, opciones, opciones[0]);

		if (seleccion == null)
			return null;
		else
			return this.importacionController.getEstrategia(seleccion);
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
					// Actualizar usuario desde el controlador para obtener los cambios más
					// recientes
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
	        // Crear panel para los botones de cursos
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
	    boolean cursoCompletado = servicioProgreso.isCursoCompletado(usuario, curso);
	    
	    if (cursoCompletado) {
	        // Curso completado
	        manejarCursoCompletado(curso);
	    } else {
	        // Curso nuevo o en progreso, mostrar selector de bloques
	        mostrarSelectorBloques(curso);
	    }
	}

	/**
	 * Muestra el selector de bloques con información de progreso.
	 */
	private void mostrarSelectorBloques(Curso curso) {
	    List<Bloque> bloques = curso.getBloques();
	    
	    if (bloques == null || bloques.isEmpty()) {
	        JOptionPane.showMessageDialog(this,
	            "El curso \"" + curso.getTitulo() + "\" no tiene bloques de ejercicios.",
	            "Sin ejercicios", JOptionPane.WARNING_MESSAGE);
	        return;
	    }

	    // Crear opciones que incluyan información de progreso
	    String[] opcionesBloques = bloques.stream()
	        .map(bloque -> {
	            ProgresoBloque progreso = servicioProgreso.obtenerProgreso(usuario, bloque);
	            if (progreso == null) {
	                return bloque.getTitulo() + " (No iniciado)";
	            } else if (progreso.isCompletado()) {
	                return bloque.getTitulo() + " (✓ Completado)";
	            } else {
	                return bloque.getTitulo() + String.format(" (%.1f%% completado)", 
	                    progreso.getPorcentajeCompletado());
	            }
	        })
	        .toArray(String[]::new);

	    String mensaje = String.format(
	        "Curso: %s\nProgreso general: %.1f%%\n\nSelecciona el bloque que quieres estudiar:",
	        curso.getTitulo(),
	        servicioProgreso.calcularPorcentajeCurso(usuario, curso)
	    );

	    String seleccionBloque = (String) JOptionPane.showInputDialog(
	        this, mensaje, "Elegir Bloque",
	        JOptionPane.QUESTION_MESSAGE, null, opcionesBloques, opcionesBloques[0]
	    );

	    if (seleccionBloque == null) return;

	    // Encontrar el bloque seleccionado
	    int indiceSeleccionado = -1;
	    for (int i = 0; i < opcionesBloques.length; i++) {
	        if (opcionesBloques[i].equals(seleccionBloque)) {
	            indiceSeleccionado = i;
	            break;
	        }
	    }
	    
	    if (indiceSeleccionado >= 0) {
	        Bloque bloqueSeleccionado = bloques.get(indiceSeleccionado);
	        manejarSeleccionBloque(bloqueSeleccionado);
	    }
	}

	/**
	 * Maneja la selección de un bloque específico.
	 */
	private void manejarSeleccionBloque(Bloque bloque) {
	    ProgresoBloque progreso = servicioProgreso.obtenerProgreso(usuario, bloque);
	    
	    if (progreso == null) {
	        // Bloque nuevo
	        iniciarBloqueNuevo(bloque);
	    } else if (progreso.isCompletado()) {
	        // Bloque completado
	        manejarBloqueCompletado(bloque);
	    } else {
	        // Bloque en progreso
	        manejarBloqueEnProgreso(bloque, progreso);
	    }
	}

	/**
	 * Inicia un bloque que no se ha empezado nunca.
	 */
	private void iniciarBloqueNuevo(Bloque bloque) {
	    Estrategia estrategia = mostrarSelectorEstrategia();
	    if (estrategia == null) return;
	    
	    // Crear progreso del bloque
	    ProgresoBloque progresoBloque = servicioProgreso.iniciarProgreso(
	        usuario, bloque, estrategia.getTipoEstrategia()
	    );
	    
	    // Abrir ventana de ejercicios
	    abrirVentanaEjercicios(bloque, estrategia, progresoBloque);
	}

	/**
	 * Maneja un bloque que está completado.
	 */
	private void manejarBloqueCompletado(Bloque bloque) {
	    int respuesta = JOptionPane.showConfirmDialog(this,
	        "El bloque \"" + bloque.getTitulo() + "\" ya está completado.\n¿Deseas repetirlo?", 
	        "Bloque Completado", JOptionPane.YES_NO_OPTION);
	        
	    if (respuesta == JOptionPane.YES_OPTION) {
	        reiniciarBloque(bloque);
	    }
	}

	/**
	 * Maneja un bloque que está en progreso.
	 */
	private void manejarBloqueEnProgreso(Bloque bloque, ProgresoBloque progreso) {
	    String estrategiaActual = progreso.getEstrategiaUtilizada().toString();
	    
	    String mensaje = String.format(
	        "Bloque: \"%s\"\n" +
	        "Progreso: %.1f%% (%d/%d ejercicios)\n" +
	        "Estrategia actual: %s\n\n" +
	        "¿Qué deseas hacer?",
	        bloque.getTitulo(),
	        progreso.getPorcentajeCompletado(),
	        progreso.getEjerciciosCompletados(),
	        bloque.getEjercicios().size(),
	        estrategiaActual
	    );
	    
	    String[] opciones = {
	        "Continuar donde lo dejé",
	        "Cambiar estrategia y continuar",
	        "Reiniciar este bloque",
	        "Cancelar"
	    };
	    
	    int seleccion = JOptionPane.showOptionDialog(
	        this, mensaje, "Bloque en Progreso", 
	        JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, 
	        null, opciones, opciones[0]
	    );
	    
	    switch (seleccion) {
	        case 0: // Continuar donde lo dejé
	            continuarBloque(bloque, progreso);
	            break;
	            
	        case 1: // Cambiar estrategia y continuar
	            cambiarEstrategiaYContinuar(bloque, progreso);
	            break;
	            
	        case 2: // Reiniciar este bloque
	            reiniciarBloque(bloque);
	            break;
	            
	        case 3: // Cancelar
	        default:
	            return;
	    }
	}

	/**
	 * Continúa un bloque manteniendo la estrategia actual.
	 */
	private void continuarBloque(Bloque bloque, ProgresoBloque progreso) {
	    String estrategiaActual = progreso.getEstrategiaUtilizada().toString();
	    Estrategia estrategia = importacionController.getEstrategia(estrategiaActual);
	    
	    abrirVentanaEjercicios(bloque, estrategia, progreso);
	}

	/**
	 * Permite cambiar la estrategia antes de continuar.
	 */
	private void cambiarEstrategiaYContinuar(Bloque bloque, ProgresoBloque progreso) {
	    Estrategia nuevaEstrategia = mostrarSelectorEstrategia();
	    if (nuevaEstrategia == null) return;
	    
	    // Actualizar estrategia en el progreso
	    progreso.setEstrategiaUtilizada(nuevaEstrategia.getTipoEstrategia());
	    servicioProgreso.guardarProgreso(progreso);
	    
	    abrirVentanaEjercicios(bloque, nuevaEstrategia, progreso);
	}

	/**
	 * Reinicia un bloque desde el principio.
	 */
	private void reiniciarBloque(Bloque bloque) {
	    Estrategia estrategia = mostrarSelectorEstrategia();
	    if (estrategia == null) return;
	    
	    // Eliminar progreso anterior del bloque
	    servicioProgreso.eliminarProgreso(usuario, bloque);
	    
	    // Crear nuevo progreso
	    ProgresoBloque nuevoProgreso = servicioProgreso.iniciarProgreso(
	        usuario, bloque, estrategia.getTipoEstrategia()
	    );
	    
	    // Abrir ventana desde el principio
	    abrirVentanaEjercicios(bloque, estrategia, nuevoProgreso);
	}

	/**
	 * Abre la ventana de ejercicios para un bloque específico.
	 */
	private void abrirVentanaEjercicios(Bloque bloque, Estrategia estrategia, ProgresoBloque progreso) {
	    try {
	        if (bloque.getEjercicios() == null || bloque.getEjercicios().isEmpty()) {
	            JOptionPane.showMessageDialog(this,
	                "El bloque \"" + bloque.getTitulo() + "\" no tiene ejercicios.",
	                "Sin ejercicios", JOptionPane.WARNING_MESSAGE);
	            return;
	        }

	        List<Ejercicio> ejercicios = estrategia.ordenarEjercicios(bloque.getEjercicios());
	        
	        SwingUtilities.invokeLater(() -> {
	            PioEjerciciosConProgreso ventanaEjercicios = new PioEjerciciosConProgreso(
	                ejercicios, progreso, servicioProgreso
	            );
	            ventanaEjercicios.setVisible(true);
	        });
	        
	    } catch (Exception e) {
	        JOptionPane.showMessageDialog(this,
	            "Error al abrir los ejercicios: " + e.getMessage(),
	            "Error", JOptionPane.ERROR_MESSAGE);
	    }
	}

	/**
	 * Maneja un curso que está completado.
	 */
	private void manejarCursoCompletado(Curso curso) {
		int respuesta = JOptionPane.showConfirmDialog(this,
				"\"" + curso.getTitulo() + "\" ya está completado.\n¿Deseas repetirlo?", "Curso Completado",
				JOptionPane.YES_NO_OPTION);

		if (respuesta == JOptionPane.YES_OPTION) {
			// Eliminar progreso de todos los bloques del curso
			for (Bloque bloque : curso.getBloques()) {
				servicioProgreso.eliminarProgreso(usuario, bloque);
			}
			mostrarSelectorBloques(curso);
		}
	}

	/**
	 * Método para limpiar cursos duplicados basándose en el título
	 */
	private List<Curso> limpiarCursosDuplicados(List<Curso> cursos) {
		if (cursos == null || cursos.isEmpty()) {
			return cursos;
		}

		// Usar un Map para eliminar duplicados por título
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