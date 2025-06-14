package umu.pds.vista;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

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
import umu.pds.modelo.Usuario;
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
       
     // Usamos GridBagLayout para organizar los paneles de estadísticas
        GridBagLayout gbl_panelCentral = new GridBagLayout();
        gbl_panelCentral.columnWidths = new int[]{20, 0, 20, 0, 20, 0};
        gbl_panelCentral.rowHeights = new int[]{20, 0, 20, 0, 20, 0};
        gbl_panelCentral.columnWeights = new double[]{0.0, 0.5, 0.0, 0.5, 0.0, Double.MIN_VALUE};
        gbl_panelCentral.rowWeights = new double[]{0.0, 0.4, 0.0, 0.6, 0.0, Double.MIN_VALUE};
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
            new MatteBorder(1, 1, 1, 1, new Color(220, 220, 220)),
            new EmptyBorder(15, 15, 15, 15)
        ));

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
          + "y personalizados. ¡Mantente atento, lo bueno está por llegar!"
        );
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
            new MatteBorder(1, 1, 1, 1, new Color(220, 220, 220)),
            new EmptyBorder(15, 15, 15, 15)
        ));

        panelImportarCursos.setLayout(new BorderLayout(0, 15));

        // Título del panel
        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        lblTitulo.setForeground(PioColores.GRIS_TEXT);
        panelImportarCursos.add(lblTitulo, BorderLayout.NORTH);
           
        JTextArea descripcionArea = new JTextArea( "Selecciona un archivo en formato .json para importar tus cursos a la plataforma.\n" +
                "Asegúrate de que el archivo siga la estructura correcta para que la importación se realice sin problemas.");
        
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
    	String[] opciones = this.importacionController.getTiposEstrategiasDefinidas().stream().map(t -> t.toString()).toArray(String[]::new);
    	String seleccion = (String) JOptionPane.showInputDialog(
    			this,
    			"Selecciona la estrategia de aprendizaje para la sesión",
    			"Seleccionar Estrategia",
    			JOptionPane.INFORMATION_MESSAGE,
    			null,
    			opciones,
    			opciones[0]
    			);
    	
    	if (seleccion == null) return null;
    	else return this.importacionController.getEstrategia(seleccion);
    }
    
    /**
     * Muestra el selector de bloques para un curso, similar al simulacro
     */
    private void mostrarSelectorBloques(Curso curso, Estrategia estrategia) {
        List<Bloque> bloques = curso.getBloques();
        Bloque bloqueSeleccionado;
        
        //El curso no tiene bloques
        if (bloques == null || bloques.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "El curso \"" + curso.getTitulo() + "\" no tiene bloques de ejercicios.", 
                "Sin ejercicios", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        //El curso solo tiene un bloque: Se abre directamente
        if (bloques.size() == 1) {
            bloqueSeleccionado = bloques.get(0);
        } 
        
        //El curso tiene varios bloques: Se muestra una ventana para elegir el bloque
        else {
        	
            // Crear lista de opciones
            String[] opcionesBloques = bloques.stream().map(bloque -> bloque.getTitulo()).toArray(String[]::new);
            
            String seleccionBloque = (String) JOptionPane.showInputDialog(
                this,
                "Selecciona el bloque de ejercicios para el curso \"" + curso.getTitulo() + "\":",
                "Seleccionar Bloque",
                JOptionPane.QUESTION_MESSAGE,
                null,
                opcionesBloques,
                opcionesBloques[0]
            );
            
            if (seleccionBloque == null) return; // Usuario canceló
            
            bloqueSeleccionado = bloques.stream()
                .filter(bloque -> bloque.getTitulo().equals(seleccionBloque))
                .findFirst()
                .orElse(null);
        }
        
        //Validación del bloque
        //TODO: Es correcto que esto esté en una clase de la capa vista?
        if (bloqueSeleccionado == null || 
            bloqueSeleccionado.getEjercicios() == null || 
            bloqueSeleccionado.getEjercicios().isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "El bloque seleccionado no tiene ejercicios.", 
                "Sin ejercicios", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Abrir PioEjercicios con la lista de ejercicios
        try {
        	
            List<Ejercicio> ejercicios = estrategia.ordenarEjercicios(bloqueSeleccionado.getEjercicios());
            SwingUtilities.invokeLater(() -> {
                PioEjercicios ventanaEjercicios = new PioEjercicios(ejercicios);
                ventanaEjercicios.setVisible(true);
            });
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Error al abrir los ejercicios: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void importarCurso() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
            "Archivos JSON (*.json)", "json"));
        
        int resultado = fileChooser.showOpenDialog(this);
        if (resultado == JFileChooser.APPROVE_OPTION) {
            File archivo = fileChooser.getSelectedFile();
            try {
                // Contar cursos antes de la importación
                int cursosAntes = usuario.getBiblioteca().size();
                
            	boolean resultadoImportacion = controlador.getImportacionController().importarCursosDesdeArchivo(archivo.getAbsolutePath(), usuario);
                
            	if (!resultadoImportacion) {
            	    JOptionPane.showMessageDialog(this, 
            	        "Error al importar el curso: No cumple con el formato esperado", 
            	        "Error de Importación", 
            	        JOptionPane.ERROR_MESSAGE);
            	} else {
            	    // Actualizar usuario desde el controlador para obtener los cambios más recientes
            	    this.usuario = controlador.getUsuarioActual();
            	    
            	    // Contar cursos después de la importación
            	    int cursosDepues = usuario.getBiblioteca().size();
            	    int cursosImportados = cursosDepues - cursosAntes;
            	    
            	    // Refrescar la vista inmediatamente
            		refrescarCursos();
            		
            		String mensaje;
            		if (cursosImportados > 0) {
            		    mensaje = cursosImportados + " curso(s) importado(s) correctamente.\nYa puedes hacer clic sobre ellos para practicar los ejercicios.";
            		} else {
            		    mensaje = "Los cursos del archivo ya estaban en tu biblioteca.\nNo se añadieron cursos duplicados.";
            		}
            		
                    JOptionPane.showMessageDialog(this, 
                        mensaje, 
                        "Importación Completada", 
                        JOptionPane.INFORMATION_MESSAGE);
            	}
                
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, 
                    "Error al importar el curso: " + ex.getMessage(),
                    "Error de Importación", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void refrescarCursos() {
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
        
        // NUEVO: Limpiar duplicados antes de mostrar
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
                
                // Acción al pulsar el botón: mostrar selector de bloques
                btnCurso.addActionListener(e -> {
                	Estrategia estrategia = mostrarSelectorEstrategia();
                	if (estrategia != null) mostrarSelectorBloques(curso, estrategia);
                	});
                
                panelBotonesCursos.add(btnCurso);
                panelBotonesCursos.add(Box.createRigidArea(new Dimension(0, 5))); // Espacio entre botones
            }
            
            panelListaCursos.add(panelBotonesCursos, BorderLayout.CENTER);
        }
        
        panelListaCursos.revalidate();
        panelListaCursos.repaint();
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
            System.out.println("Se encontraron " + (cursos.size() - cursosLimpios.size()) + " cursos duplicados. Limpiando...");
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