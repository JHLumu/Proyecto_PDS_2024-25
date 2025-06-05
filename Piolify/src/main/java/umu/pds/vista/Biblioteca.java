package umu.pds.vista;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

import java.awt.*;
import java.io.File;
import java.util.List;
import umu.pds.modelo.Curso;
import umu.pds.modelo.Usuario;
import umu.pds.controlador.Piolify;
import umu.pds.servicios.CursoSerializer;
import umu.pds.vista.elementos.PioColores;

public class Biblioteca extends JPanel {
    private Usuario usuario;
    private Piolify controlador = Piolify.getUnicaInstancia();
    private JPanel panelCentral;
    private JPanel panelListaCursos;
    private JPanel panelImportarCursos;
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

        JButton btnImportar = new JButton("Importar curso");
        btnImportar.setBackground(PioColores.MARRON_BUTTON);
        btnImportar.addActionListener(e -> importarCurso());
        panelImportarCursos.add(btnImportar, BorderLayout.SOUTH);
        
        //refrescarCursos();
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

        // Descripción
        JTextArea descripcionArea = new JTextArea(
            "Aquí aparecerán todos los cursos disponibles para que explores, aprendas y sigas creciendo. "
          + "Actualmente no hay cursos cargados, pero muy pronto tendrás acceso a contenidos variados "
          + "y personalizados. ¡Mantente atento, lo bueno está por llegar!"
        );
        descripcionArea.setLineWrap(true);
        descripcionArea.setWrapStyleWord(true);
        descripcionArea.setEditable(false);
        descripcionArea.setOpaque(false);
        descripcionArea.setFont(new Font("Arial", Font.PLAIN, 14));
        descripcionArea.setForeground(PioColores.NEGRO);
        descripcionArea.setBorder(null);

        panelListaCursos.add(descripcionArea, BorderLayout.CENTER);

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

    private void refrescarCursos() {
    	panelListaCursos.removeAll();
        List<Curso> cursos = usuario.getBiblioteca();
        if (cursos.isEmpty()) {
        	panelListaCursos.add(new JLabel("No tienes cursos en tu biblioteca."));
        } else {
            for (Curso curso : cursos) {
                JButton btnCurso = new JButton(curso.getTitulo());
                btnCurso.setAlignmentX(Component.LEFT_ALIGNMENT);
                // Acción al pulsar el botón: abrir el curso (por implementar)
                btnCurso.addActionListener(e -> {
                    JOptionPane.showMessageDialog(this, "Abrir curso: " + curso.getTitulo());
                });
                panelListaCursos.add(btnCurso);
            }
        }
        panelListaCursos.revalidate();
        panelListaCursos.repaint();
    }

    private void importarCurso() {
        JFileChooser fileChooser = new JFileChooser();
        int resultado = fileChooser.showOpenDialog(this);
        if (resultado == JFileChooser.APPROVE_OPTION) {
            File archivo = fileChooser.getSelectedFile();
            try {
                Curso nuevoCurso = CursoSerializer.importarCurso(archivo.getAbsolutePath());
                usuario.getBiblioteca().add(nuevoCurso);
                controlador.getUsuarioController().modificarUsuario(usuario); // Guarda el usuario actualizado
                refrescarCursos();
                JOptionPane.showMessageDialog(this, "Curso importado correctamente.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al importar el curso: " + ex.getMessage());
            }
        }
    }
} 