package umu.pds.vista;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.List;
import umu.pds.modelo.Curso;
import umu.pds.modelo.Usuario;
import umu.pds.controlador.Piolify;
import umu.pds.servicios.CursoSerializer;

public class Biblioteca extends JPanel {
    private Usuario usuario;
    private Piolify controlador = Piolify.getUnicaInstancia();
    private JPanel panelCursos;
    
	private static final long serialVersionUID = 1L;

	/**
	 * Create the panel.
	 */
    public Biblioteca(Usuario usuario) {
        this.usuario = usuario;
        setLayout(new BorderLayout());

        JLabel lblTitulo = new JLabel("Mis Cursos");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        add(lblTitulo, BorderLayout.NORTH);

        panelCursos = new JPanel();
        panelCursos.setLayout(new BoxLayout(panelCursos, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(panelCursos);
        add(scrollPane, BorderLayout.CENTER);

        JButton btnImportar = new JButton("Importar curso");
        btnImportar.addActionListener(e -> importarCurso());
        add(btnImportar, BorderLayout.SOUTH);

        refrescarCursos();
    }

    private void refrescarCursos() {
        panelCursos.removeAll();
        List<Curso> cursos = usuario.getBiblioteca();
        if (cursos.isEmpty()) {
            panelCursos.add(new JLabel("No tienes cursos en tu biblioteca."));
        } else {
            for (Curso curso : cursos) {
                JButton btnCurso = new JButton(curso.getTitulo());
                btnCurso.setAlignmentX(Component.LEFT_ALIGNMENT);
                // Acción al pulsar el botón: abrir el curso (por implementar)
                btnCurso.addActionListener(e -> {
                    JOptionPane.showMessageDialog(this, "Abrir curso: " + curso.getTitulo());
                });
                panelCursos.add(btnCurso);
            }
        }
        panelCursos.revalidate();
        panelCursos.repaint();
    }

    private void importarCurso() {
        JFileChooser fileChooser = new JFileChooser();
        int resultado = fileChooser.showOpenDialog(this);
        if (resultado == JFileChooser.APPROVE_OPTION) {
            File archivo = fileChooser.getSelectedFile();
            try {
                Curso nuevoCurso = CursoSerializer.importarCurso(archivo.getAbsolutePath());
                usuario.getBiblioteca().add(nuevoCurso);
                controlador.modificarUsuario(usuario); // Guarda el usuario actualizado
                refrescarCursos();
                JOptionPane.showMessageDialog(this, "Curso importado correctamente.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al importar el curso: " + ex.getMessage());
            }
        }
    }
} 