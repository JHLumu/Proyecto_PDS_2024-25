package umu.pds.servicios.importacion;

import umu.pds.modelo.Curso;
import umu.pds.modelo.Ejercicio;
import umu.pds.modelo.Bloque;
import umu.pds.servicios.ServicioImportacion;
import umu.pds.servicios.ServicioImportacion.ResultadoImportacion;
import umu.pds.vista.PioEjercicios;

import javax.swing.*;
import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.ArrayList;

public class SimulacroServicioImportacion {
    
    private final ServicioImportacion servicioImportacion;
    private JFrame ventanaPrincipal;
    private JTextArea areaResultados;
    private List<Curso> cursosImportados;
    
    public SimulacroServicioImportacion() {
        this.servicioImportacion = new ServicioImportacion();
        this.cursosImportados = new ArrayList<>();
        inicializarInterfazGrafica();
    }
    
    private void inicializarInterfazGrafica() {
        SwingUtilities.invokeLater(() -> {
            ventanaPrincipal = new JFrame("Test Servicio Importación - Piolify");
            ventanaPrincipal.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            ventanaPrincipal.setSize(800, 600);
            ventanaPrincipal.setLocationRelativeTo(null);
            
            // Panel principal
            JPanel panelPrincipal = new JPanel(new BorderLayout());
            
            // Panel de botones
            JPanel panelBotones = new JPanel(new FlowLayout());
            
            JButton btnImportarArchivo = new JButton("Importar desde Archivo");
            JButton btnImportarStream = new JButton("Importar desde Stream");
            JButton btnProbarFormatos = new JButton("Probar Formatos");
            JButton btnMostrarEjercicios = new JButton("Mostrar Ejercicios");
            JButton btnLimpiar = new JButton("Limpiar");
            
            // Configurar acciones
            btnImportarArchivo.addActionListener(e -> probarImportacionDesdeArchivo());
            btnImportarStream.addActionListener(e -> probarImportacionDesdeStream());
            btnProbarFormatos.addActionListener(e -> probarFormatos());
            btnMostrarEjercicios.addActionListener(e -> mostrarEjerciciosEnVentana());
            btnLimpiar.addActionListener(e -> limpiarResultados());
            
            // Inicialmente deshabilitar el botón de mostrar ejercicios
            btnMostrarEjercicios.setEnabled(false);
            
            panelBotones.add(btnImportarArchivo);
            panelBotones.add(btnImportarStream);
            panelBotones.add(btnProbarFormatos);
            panelBotones.add(btnMostrarEjercicios);
            panelBotones.add(btnLimpiar);
            
            // Área de resultados
            areaResultados = new JTextArea();
            areaResultados.setEditable(false);
            areaResultados.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
            JScrollPane scrollPane = new JScrollPane(areaResultados);
            scrollPane.setBorder(BorderFactory.createTitledBorder("Resultados de las Pruebas"));
            
            panelPrincipal.add(panelBotones, BorderLayout.NORTH);
            panelPrincipal.add(scrollPane, BorderLayout.CENTER);
            
            ventanaPrincipal.add(panelPrincipal);
            ventanaPrincipal.setVisible(true);
            
            // Mensaje inicial
            appendResultado("=== INTERFAZ DE PRUEBA DEL SERVICIO DE IMPORTACIÓN ===\n");
            appendResultado("Selecciona una opción para comenzar las pruebas.\n\n");
        });
    }
    
    public static void main(String[] args) {
        // Establecer Look and Feel del sistema
        
        new SimulacroServicioImportacion();
    }
    
    public void probarImportacionDesdeArchivo() {
        appendResultado("--- Prueba: Importación desde archivo ---\n");
        
        try {
            // Abrir selector de archivos
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                "Archivos JSON (*.json)", "json"));
            
            int resultado = fileChooser.showOpenDialog(ventanaPrincipal);
            
            if (resultado == JFileChooser.APPROVE_OPTION) {
                String rutaArchivo = fileChooser.getSelectedFile().getAbsolutePath();
                appendResultado("Intentando importar: " + rutaArchivo + "\n");
                
                ResultadoImportacion resultadoImportacion = servicioImportacion.importarDesdeArchivo(rutaArchivo);
                
                if (resultadoImportacion.fueExitoso()) {
                    appendResultado("✓ Importación exitosa!\n");
                    appendResultado("  - Formato: " + resultadoImportacion.getFormatoUtilizado() + "\n");
                    appendResultado("  - Cursos importados: " + resultadoImportacion.getCantidadImportada() + "\n");
                    
                    // Guardar cursos importados
                    cursosImportados.clear();
                    cursosImportados.addAll(resultadoImportacion.getCursos());
                    
                    mostrarDetallesCursos(resultadoImportacion.getCursos());
                    habilitarBotonEjercicios();
                } else {
                    appendResultado("✗ La importación no produjo resultados\n");
                }
            } else {
                appendResultado("Importación cancelada por el usuario.\n");
            }
            
        } catch (ImportacionException e) {
            appendResultado("✗ Error de importación: " + e.getMessage() + "\n");
            if (e.getTipoError().equals("FILE_NOT_FOUND")) {
                appendResultado("  (El archivo no fue encontrado)\n");
            }
        } catch (Exception e) {
            appendResultado("✗ Error inesperado: " + e.getMessage() + "\n");
        }
        
        appendResultado("\n");
    }
    
    public void probarImportacionDesdeStream() {
        appendResultado("--- Prueba: Importación desde stream ---\n");
        
        // JSON de ejemplo embebido
        String jsonEjemplo = """
            {
                "titulo": "Curso de Programación Java - Test",
                "descripcion": "Aprende los fundamentos de Java con ejercicios interactivos",
                "dificultad": "Intermedio",
                "autor": "Profesor Test",
                "bloques": [
                    {
                        "titulo": "Introducción a Java",
                        "descripcion": "Conceptos básicos del lenguaje",
                        "orden": 1,
                        "ejercicios": [
                            {
                                "tipo": "OPCION_MULTIPLE",
                                "contenido": "¿Qué es Java?",
                                "respuesta": "Un lenguaje de programación",
                                "dificultad": 1,
                                "orden": 1,
                                "propiedadesEspecificas": {
                                    "opciones": [
                                        "Un lenguaje de programación",
                                        "Una bebida",
                                        "Un sistema operativo",
                                        "Un navegador web"
                                    ]
                                }
                            },
                            {
                                "tipo": "COMPLETAR_HUECOS",
                                "contenido": "Java es un lenguaje _____ y _____",
                                "respuesta": "orientado a objetos, multiplataforma",
                                "dificultad": 2,
                                "orden": 2
                            },
                            {
                                "tipo": "VERDADERO_FALSO",
                                "contenido": "Java es un lenguaje compilado e interpretado",
                                "respuesta": "verdadero",
                                "dificultad": 2,
                                "orden": 3
                            }
                        ]
                    },
                    {
                        "titulo": "Programación Orientada a Objetos",
                        "descripcion": "Conceptos de POO en Java",
                        "orden": 2,
                        "ejercicios": [
                            {
                                "tipo": "OPCION_MULTIPLE",
                                "contenido": "¿Cuál es un principio de la POO?",
                                "respuesta": "Encapsulación",
                                "dificultad": 2,
                                "orden": 1,
                                "propiedadesEspecificas": {
                                    "opciones": [
                                        "Encapsulación",
                                        "Recursión",
                                        "Iteración",
                                        "Compilación"
                                    ]
                                }
                            }
                        ]
                    }
                ]
            }
            """;
        
        try {
            InputStream stream = new ByteArrayInputStream(jsonEjemplo.getBytes(StandardCharsets.UTF_8));
            ResultadoImportacion resultado = servicioImportacion.importarDesdeStream(stream, "json");
            
            if (resultado.fueExitoso()) {
                appendResultado("✓ Importación desde stream exitosa!\n");
                appendResultado("  - Formato: " + resultado.getFormatoUtilizado() + "\n");
                appendResultado("  - Cursos importados: " + resultado.getCantidadImportada() + "\n");
                
                // Guardar cursos importados
                cursosImportados.clear();
                cursosImportados.addAll(resultado.getCursos());
                
                mostrarDetallesCursos(resultado.getCursos());
                habilitarBotonEjercicios();
            } else {
                appendResultado("✗ La importación no produjo resultados\n");
            }
            
        } catch (ImportacionException e) {
            appendResultado("✗ Error de importación: " + e.getMessage() + "\n");
            appendResultado("  - Tipo: " + e.getTipoError() + "\n");
            if (e.getDetalles() != null) {
                appendResultado("  - Detalles: " + e.getDetalles() + "\n");
            }
        } catch (Exception e) {
            appendResultado("✗ Error inesperado: " + e.getMessage() + "\n");
            e.printStackTrace();
        }
        
        appendResultado("\n");
    }
    
    public void probarFormatos() {
        appendResultado("--- Prueba: Formatos y validaciones ---\n");
        
        // Mostrar formatos soportados
        String[] formatos = servicioImportacion.getFormatosSoportados();
        appendResultado("Extensiones soportadas: " + String.join(", ", formatos) + "\n");
        
        // Probar algunos formatos específicos
        appendResultado("¿Soporta JSON? " + servicioImportacion.soportaFormato("json") + "\n");
        appendResultado("¿Soporta YAML? " + servicioImportacion.soportaFormato("yaml") + "\n");
        appendResultado("¿Soporta XML? " + servicioImportacion.soportaFormato("xml") + "\n");
        appendResultado("¿Soporta TXT? " + servicioImportacion.soportaFormato("txt") + "\n");
        
        // Probar formato no soportado
        appendResultado("\nProbando formato no soportado...\n");
        try {
            InputStream stream = new ByteArrayInputStream("test".getBytes());
            servicioImportacion.importarDesdeStream(stream, "txt");
            appendResultado("✗ Debería haber fallado con formato no soportado\n");
            
        } catch (ImportacionException e) {
            if (e.getTipoError().equals("UNSUPPORTED_FORMAT")) {
                appendResultado("✓ Correctamente rechazó formato no soportado\n");
                appendResultado("  - Error: " + e.getMessage() + "\n");
            } else {
                appendResultado("✗ Error inesperado: " + e.getMessage() + "\n");
            }
        } catch (Exception e) {
            appendResultado("✗ Error inesperado: " + e.getMessage() + "\n");
        }
        
        // Probar JSON inválido
        appendResultado("\nProbando JSON inválido...\n");
        String jsonInvalido = "{ invalid json }";
        
        try {
            InputStream stream = new ByteArrayInputStream(jsonInvalido.getBytes(StandardCharsets.UTF_8));
            servicioImportacion.importarDesdeStream(stream, "json");
            appendResultado("✗ Debería haber fallado con JSON inválido\n");
            
        } catch (ImportacionException e) {
            if (e.getTipoError().equals("PARSE_ERROR")) {
                appendResultado("✓ Correctamente detectó JSON inválido\n");
                appendResultado("  - Error: " + e.getMessage() + "\n");
            } else {
                appendResultado("✗ Error inesperado: " + e.getMessage() + "\n");
            }
        } catch (Exception e) {
            appendResultado("✗ Error inesperado: " + e.getMessage() + "\n");
        }
        
        appendResultado("\n");
    }
    
    private void mostrarEjerciciosEnVentana() {
        if (cursosImportados.isEmpty()) {
            JOptionPane.showMessageDialog(ventanaPrincipal, 
                "No hay cursos importados. Primero importa un curso.", 
                "Sin cursos", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Mostrar selector de curso si hay múltiples
        Curso cursoSeleccionado;
        if (cursosImportados.size() == 1) {
            cursoSeleccionado = cursosImportados.get(0);
        } else {
            // Crear lista de opciones
            String[] opciones = cursosImportados.stream()
                .map(curso -> curso.getTitulo())
                .toArray(String[]::new);
            
            String seleccion = (String) JOptionPane.showInputDialog(
                ventanaPrincipal,
                "Selecciona el curso:",
                "Seleccionar Curso",
                JOptionPane.QUESTION_MESSAGE,
                null,
                opciones,
                opciones[0]
            );
            
            if (seleccion == null) return; // Usuario canceló
            
            cursoSeleccionado = cursosImportados.stream()
                .filter(curso -> curso.getTitulo().equals(seleccion))
                .findFirst()
                .orElse(null);
        }
        
        if (cursoSeleccionado == null) return;
        
        // Mostrar selector de bloque
        List<Bloque> bloques = cursoSeleccionado.getBloques();
        if (bloques == null || bloques.isEmpty()) {
            JOptionPane.showMessageDialog(ventanaPrincipal, 
                "El curso no tiene bloques de ejercicios.", 
                "Sin ejercicios", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Bloque bloqueSeleccionado;
        if (bloques.size() == 1) {
            bloqueSeleccionado = bloques.get(0);
        } else {
            String[] opcionesBloques = bloques.stream()
                .map(bloque -> bloque.getTitulo())
                .toArray(String[]::new);
            
            String seleccionBloque = (String) JOptionPane.showInputDialog(
                ventanaPrincipal,
                "Selecciona el bloque de ejercicios:",
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
        
        if (bloqueSeleccionado == null || 
            bloqueSeleccionado.getEjercicios() == null || 
            bloqueSeleccionado.getEjercicios().isEmpty()) {
            JOptionPane.showMessageDialog(ventanaPrincipal, 
                "El bloque seleccionado no tiene ejercicios.", 
                "Sin ejercicios", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Abrir PioEjercicios con la lista de ejercicios
        try {
            List<Ejercicio> ejercicios = bloqueSeleccionado.getEjercicios();
            appendResultado("Abriendo ventana de ejercicios...\n");
            appendResultado("  - Curso: " + cursoSeleccionado.getTitulo() + "\n");
            appendResultado("  - Bloque: " + bloqueSeleccionado.getTitulo() + "\n");
            appendResultado("  - Ejercicios: " + ejercicios.size() + "\n\n");
            
            SwingUtilities.invokeLater(() -> {
                PioEjercicios ventanaEjercicios = new PioEjercicios(ejercicios);
                ventanaEjercicios.setVisible(true);
            });
            
        } catch (Exception e) {
            appendResultado("✗ Error al abrir ventana de ejercicios: " + e.getMessage() + "\n");
            JOptionPane.showMessageDialog(ventanaPrincipal, 
                "Error al abrir los ejercicios: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void mostrarDetallesCursos(List<Curso> cursos) {
        appendResultado("\n  === DETALLES DE CURSOS IMPORTADOS ===\n");
        
        for (int i = 0; i < cursos.size(); i++) {
            Curso curso = cursos.get(i);
            appendResultado("  Curso " + (i + 1) + ":\n");
            appendResultado("    - Título: " + curso.getTitulo() + "\n");
            appendResultado("    - Descripción: " + curso.getDescripcion() + "\n");
            appendResultado("    - Dificultad: " + curso.getDificultad() + "\n");
            
            if (curso.getBloques() != null) {
                appendResultado("    - Bloques: " + curso.getBloques().size() + "\n");
                
                for (int j = 0; j < curso.getBloques().size(); j++) {
                    var bloque = curso.getBloques().get(j);
                    appendResultado("      * Bloque " + (j + 1) + ": " + bloque.getTitulo() + "\n");
                    
                    if (bloque.getEjercicios() != null) {
                        appendResultado("        - Ejercicios: " + bloque.getEjercicios().size() + "\n");
                        
                        for (int k = 0; k < bloque.getEjercicios().size(); k++) {
                            var ejercicio = bloque.getEjercicios().get(k);
                            appendResultado("          + Ejercicio " + (k + 1) + ": " + 
                                         ejercicio.getClass().getSimpleName() + 
                                         " (" + ejercicio.getContenido() + ")\n");
                        }
                    }
                }
            }
            appendResultado("\n");
        }
    }
    
    private void appendResultado(String texto) {
        SwingUtilities.invokeLater(() -> {
            areaResultados.append(texto);
            areaResultados.setCaretPosition(areaResultados.getDocument().getLength());
        });
    }
    
    private void limpiarResultados() {
        SwingUtilities.invokeLater(() -> {
            areaResultados.setText("");
            cursosImportados.clear();
            deshabilitarBotonEjercicios();
        });
    }
    
    private void habilitarBotonEjercicios() {
        SwingUtilities.invokeLater(() -> {
            // Buscar el botón en el panel
            Container container = ventanaPrincipal.getContentPane();
            JPanel panelBotones = (JPanel) ((JPanel) container.getComponent(0)).getComponent(0);
            JButton btnMostrarEjercicios = (JButton) panelBotones.getComponent(3);
            btnMostrarEjercicios.setEnabled(true);
        });
    }
    
    private void deshabilitarBotonEjercicios() {
        SwingUtilities.invokeLater(() -> {
            Container container = ventanaPrincipal.getContentPane();
            JPanel panelBotones = (JPanel) ((JPanel) container.getComponent(0)).getComponent(0);
            JButton btnMostrarEjercicios = (JButton) panelBotones.getComponent(3);
            btnMostrarEjercicios.setEnabled(false);
        });
    }
}