package umu.pds.vista;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Arrays;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import umu.pds.controlador.Piolify;
import umu.pds.modelo.*;
import umu.pds.servicios.ServicioEstadisticas;
import umu.pds.servicios.ServicioProgreso;
import umu.pds.utils.EjercicioRenderer;
import umu.pds.utils.EjercicioRendererFactory;
import umu.pds.utils.ImageUtils;
import umu.pds.vista.elementos.BaseRoundedFrame;
import umu.pds.vista.elementos.PioColores;

/**
 * Ventana de ejercicios simplificada que maneja progreso solo a nivel de bloque.
 * Permite al usuario guardar su estado y continuar en cualquier momento desde donde lo dejó.
 */
public class PioEjerciciosConProgreso extends BaseRoundedFrame {
    private static final long serialVersionUID = 1L;

    // Componentes de interfaz
    private List<EjercicioRenderer> renderers;
    private Ejercicio ejercicioActual;
    private List<Ejercicio> listaEjercicios;
    private int indiceActual; // Índice en la lista actual de ejercicios
    private CardLayout cardLayout;
    private JPanel panelEjercicios;
    private JPanel panelBotones;
    private JButton btnAnterior;
    private JButton btnValidar;
    private JButton btnSiguiente;
    private JButton btnGuardarSalir;
    private JLabel lblProgreso;

    // Servicios y progreso
    private ServicioEstadisticas servicioEstadisticas;
    private ServicioProgreso servicioProgreso;
    private ProgresoBloque progresoBloque;
    
    // Variables de sesión
    private SesionAprendizaje sesionActual;
    private Usuario usuarioActual;
    private int aciertos = 0;
    private int fallos = 0;

    /**
     * Constructor principal que recibe una lista de ejercicios y un progreso de bloque.
     */
    public PioEjerciciosConProgreso(List<Ejercicio> ejercicios, ProgresoBloque progresoBloque,
            ServicioProgreso servicioProgreso) {
        super("Ejercicios - " + progresoBloque.getBloque().getTitulo());

        if (ejercicios == null || ejercicios.isEmpty()) {
            throw new IllegalArgumentException("La lista de ejercicios no puede estar vacía");
        }

        this.listaEjercicios = ejercicios;
        this.progresoBloque = progresoBloque;
        this.servicioProgreso = servicioProgreso;
        this.indiceActual = progresoBloque.getIndiceEjercicioActual(); // Empezar donde se quedó

        initRuntimeComponents();
        prepararTodosLosEjercicios();
        iniciarSesionAprendizaje();
        mostrarEjercicioActual();

        configurarGuardadoAutomatico();
    }


    private void initRuntimeComponents() {
        setIconImage(Toolkit.getDefaultToolkit().getImage(Login.class.getResource("/mascota.png")));
        setFocusable(false);
        setBackgroundColor(PioColores.AMARILLO_LABEL);
        setCloseButtonColor(PioColores.ROJO);

        // Inicializar servicios
        this.servicioEstadisticas = new ServicioEstadisticas();
        this.usuarioActual = Piolify.getUnicaInstancia().getUsuarioActual();

        // Crear componentes de interfaz
        cardLayout = new CardLayout();
        panelEjercicios = new JPanel(cardLayout);
        panelEjercicios.setOpaque(false);

        panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBotones.setOpaque(false);

        // Crear botones
        btnAnterior = new JButton("◀ Anterior");
        btnValidar = new JButton("Validar");
        btnSiguiente = new JButton("Siguiente ▶");
        btnGuardarSalir = new JButton("💾 Guardar y Salir");

        lblProgreso = new JLabel();
        lblProgreso.setFont(new Font("Arial", Font.BOLD, 12));

        configurarEventos();

        // Agregar componentes al panel de botones
        panelBotones.add(btnGuardarSalir);
        panelBotones.add(btnAnterior);
        panelBotones.add(lblProgreso);
        panelBotones.add(btnValidar);
        panelBotones.add(btnSiguiente);

        contentPanel.add(panelEjercicios, BorderLayout.CENTER);
        contentPanel.add(panelBotones, BorderLayout.SOUTH);

        actualizarProgreso();
        actualizarEstadoBotones();
    }

    private void configurarEventos() {
        btnAnterior.addActionListener(e -> anteriorEjercicio());
        btnValidar.addActionListener(e -> validarRespuestaActual());
        btnSiguiente.addActionListener(e -> siguienteEjercicio());
        btnGuardarSalir.addActionListener(e -> guardarYSalir());
    }

    private void configurarGuardadoAutomatico() {
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                guardarYSalir();
            }
        });
    }

    private void prepararTodosLosEjercicios() {
        renderers = Arrays.asList(new EjercicioRenderer[listaEjercicios.size()]);

        for (int i = 0; i < listaEjercicios.size(); i++) {
            Ejercicio ejercicio = listaEjercicios.get(i);

            EjercicioRenderer renderer = EjercicioRendererFactory.crearRenderer(ejercicio.getTipo());
            renderers.set(i, renderer);

            JPanel panelEjercicio = new JPanel(new BorderLayout());
            panelEjercicio.setOpaque(false);

            renderer.renderizar(panelEjercicio, ejercicio);

            String nombreCard = "ejercicio_" + i;
            panelEjercicios.add(panelEjercicio, nombreCard);
        }
    }

    private void iniciarSesionAprendizaje() {
        if (listaEjercicios != null && !listaEjercicios.isEmpty()) {
            Ejercicio primerEjercicio = listaEjercicios.get(0);
            Curso curso = primerEjercicio.getBloque().getCurso();

            sesionActual = servicioEstadisticas.iniciarSesion(usuarioActual, curso, primerEjercicio);
        }
    }

    private void actualizarProgreso() {
        int ejercicioActualNumero = indiceActual + 1;
        int totalEjerciciosEnBloque = listaEjercicios.size();
        double porcentajeBloque = progresoBloque.getPorcentajeCompletado();

        String estadisticas = String.format("Bloque: %s | Ejercicio %d de %d (%.1f%%) | Aciertos: %d | Fallos: %d",
                progresoBloque.getBloque().getTitulo(), ejercicioActualNumero, totalEjerciciosEnBloque,
                porcentajeBloque, aciertos, fallos);
        lblProgreso.setText(estadisticas);
    }

    private void mostrarEjercicioActual() {
        // Validar índice
        if (indiceActual < 0 || indiceActual >= listaEjercicios.size()) {
            indiceActual = Math.max(0, Math.min(indiceActual, listaEjercicios.size() - 1));
        }

        // Actualizar ejercicio actual
        ejercicioActual = listaEjercicios.get(indiceActual);

        // Mostrar la tarjeta correspondiente
        String nombreCard = "ejercicio_" + indiceActual;
        cardLayout.show(panelEjercicios, nombreCard);

        // Resetear estado de botones
        btnValidar.setEnabled(true);
        btnSiguiente.setEnabled(false);

        // Actualizar progreso y título
        actualizarProgreso();
        actualizarEstadoBotones();
        setTitle("Ejercicio " + (indiceActual + 1) + " - " + ejercicioActual.getTipo().name());

        // Refrescar la vista
        panelEjercicios.revalidate();
        panelEjercicios.repaint();

        pack();
        setLocationRelativeTo(null);
    }

    private void validarRespuestaActual() {
        EjercicioRenderer rendererActual = renderers.get(indiceActual);

        if (rendererActual != null) {
            boolean esCorrecta = rendererActual.validarRespuesta();

            // Registrar estadísticas
            if (esCorrecta) {
                registrarAcierto();
            } else {
                registrarFallo();
            }

            // Mostrar resultado
            mostrarResultadoValidacion(esCorrecta);

            if (esCorrecta) {
                btnValidar.setEnabled(false);

                if (hayMasEjercicios()) {
                    btnSiguiente.setEnabled(true);
                } else {
                    // Es el último ejercicio de este bloque
                    finalizarBloque();
                }
            }
        }
    }

    private void mostrarResultadoValidacion(boolean esCorrecta) {
        String mensaje = esCorrecta ? "¡Correcto!" : "Incorrecto. Inténtalo de nuevo.";
        String titulo = esCorrecta ? "Bien hecho" : "Resultado";

        ImageIcon icono = null;
        String imagePath = esCorrecta ? "/acierto.png" : "/fallo.png";
        try {
            icono = ImageUtils.escalarImagen(imagePath, 48);
        } catch (Exception e) {
            System.err.println("No se pudo cargar la imagen: " + imagePath);
        }

        if (icono != null) {
            JOptionPane.showMessageDialog(this, mensaje, titulo, JOptionPane.PLAIN_MESSAGE, icono);
        } else {
            int tipoMensaje = esCorrecta ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.WARNING_MESSAGE;
            JOptionPane.showMessageDialog(this, mensaje, titulo, tipoMensaje);
        }
    }

    private void siguienteEjercicio() {
        if (hayMasEjercicios()) {
            // Guardar progreso antes de avanzar
            guardarProgresoActual();

            indiceActual++;
            mostrarEjercicioActual();
        }
    }

    private void anteriorEjercicio() {
        if (indiceActual > 0) {
            indiceActual--;
            mostrarEjercicioActual();
        }
    }

    private void guardarProgresoActual() {
        try {
            // Actualizar índice en el bloque
            progresoBloque.setIndiceEjercicioActual(indiceActual);
            
            // Actualizar ejercicios completados (solo si hemos avanzado)
            progresoBloque.setEjerciciosCompletados(Math.max(indiceActual, progresoBloque.getEjerciciosCompletados()));
            
            // Guardar usando el servicio simplificado
            servicioProgreso.guardarProgreso(progresoBloque);

            System.out.println("Progreso del bloque guardado: " + indiceActual + "/" + listaEjercicios.size()
                    + " (" + String.format("%.1f", progresoBloque.getPorcentajeCompletado()) + "%)");

        } catch (Exception e) {
            System.err.println("Error al guardar progreso del bloque: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void guardarYSalir() {
        try {
            // Solo guardar si hemos completado al menos un ejercicio
            if (indiceActual >= 0) {
                guardarProgresoActual();
            }

            // Finalizar sesión si está activa
            if (sesionActual != null && !sesionActual.isCompletada()) {
                servicioEstadisticas.finalizarSesion(sesionActual);
            }

            JOptionPane.showMessageDialog(this,
                    "Progreso guardado correctamente.\nPuedes continuar el bloque en cualquier momento.",
                    "Progreso Guardado", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            System.err.println("Error al guardar y salir: " + e.getMessage());
            e.printStackTrace();

            JOptionPane.showMessageDialog(this, "Hubo un problema al guardar el progreso:\n" + e.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        } finally {
            dispose();
        }
    }

    private void finalizarBloque() {
        try {
            // Marcar el ejercicio actual como completado
            guardarProgresoActual();

            // Usar el servicio simplificado para avanzar
            servicioProgreso.avanzarEjercicio(progresoBloque);

            // Finalizar sesión
            finalizarSesion();

            // Verificar si el curso está completado
            verificarCursoCompletado();

            // Mostrar mensaje de bloque completado
            mostrarMensajeBloque();

        } catch (Exception e) {
            System.err.println("Error al finalizar bloque: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void mostrarMensajeBloque() {
        Curso curso = progresoBloque.getBloque().getCurso();
        boolean cursoCompletado = servicioProgreso.isCursoCompletado(usuarioActual, curso);
        
        String mensaje;
        if (cursoCompletado) {
            mensaje = String.format(
                "¡Felicidades! Has completado el bloque \"%s\" y con él TODO el curso \"%s\"!\n\n" +
                "¡Excelente trabajo! 🎉",
                progresoBloque.getBloque().getTitulo(),
                curso.getTitulo()
            );
        } else {
            Bloque siguienteBloque = servicioProgreso.obtenerSiguienteBloqueRecomendado(usuarioActual, curso);
            if (siguienteBloque != null) {
                mensaje = String.format(
                    "¡Bloque \"%s\" completado! 🎉\n\n" +
                    "Progreso del curso: %.1f%%\n\n" +
                    "Siguiente bloque recomendado: \"%s\"\n" +
                    "Puedes continuar desde la biblioteca.",
                    progresoBloque.getBloque().getTitulo(),
                    servicioProgreso.calcularPorcentajeCurso(usuarioActual, curso),
                    siguienteBloque.getTitulo()
                );
            } else {
                mensaje = String.format(
                    "¡Bloque \"%s\" completado! 🎉\n\n" +
                    "Progreso del curso: %.1f%%",
                    progresoBloque.getBloque().getTitulo(),
                    servicioProgreso.calcularPorcentajeCurso(usuarioActual, curso)
                );
            }
        }

        JOptionPane.showMessageDialog(this, mensaje, "¡Bloque Completado!", JOptionPane.INFORMATION_MESSAGE);

        // Cambiar botón siguiente a finalizar
        btnSiguiente.setText("Finalizar");
        btnSiguiente.setEnabled(true);
        btnSiguiente.removeActionListener(btnSiguiente.getActionListeners()[0]);
        btnSiguiente.addActionListener(e -> dispose());
    }

    private void verificarCursoCompletado() {
        Curso curso = progresoBloque.getBloque().getCurso();
        
        if (servicioProgreso.isCursoCompletado(usuarioActual, curso)) {
            System.out.println("¡Curso completado: " + curso.getTitulo() + "!");
        }
    }

    private void registrarAcierto() {
        aciertos++;
        if (sesionActual != null) {
            servicioEstadisticas.registrarAcierto(sesionActual);
        }
    }

    private void registrarFallo() {
        fallos++;
        if (sesionActual != null) {
            servicioEstadisticas.registrarFallo(sesionActual);
        }
    }

    private void finalizarSesion() {
        if (sesionActual != null) {
            servicioEstadisticas.finalizarSesion(sesionActual);
            mostrarResumenSesion();
        }
    }

    private void mostrarResumenSesion() {
        if (sesionActual == null) return;

        StringBuilder resumen = new StringBuilder();
        resumen.append("¡Sesión completada!\n\n");

        // Resumen de la sesión actual
        resumen.append("Estadísticas de la sesión:\n");
        resumen.append("• Bloque: ").append(progresoBloque.getBloque().getTitulo()).append("\n");
        resumen.append("• Ejercicios completados: ").append(sesionActual.getEjerciciosCompletados()).append("\n");
        resumen.append("• Aciertos: ").append(sesionActual.getAciertos()).append("\n");
        resumen.append("• Fallos: ").append(sesionActual.getFallos()).append("\n");
        resumen.append("• Precisión: ").append(String.format("%.1f%%", sesionActual.getPorcentajeAciertos())).append("\n");

        if (sesionActual.getTiempoTotal() > 0) {
            int minutos = sesionActual.getTiempoTotal() / 60;
            int segundos = sesionActual.getTiempoTotal() % 60;
            resumen.append("• Tiempo total: ").append(minutos).append("m ").append(segundos).append("s\n");
        }

        // Estadísticas del progreso del bloque
        resumen.append("\nProgreso del bloque:\n");
        resumen.append("• Completado: ").append(String.format("%.1f%%", progresoBloque.getPorcentajeCompletado())).append("\n");
        
        // Estadísticas del curso
        Curso curso = progresoBloque.getBloque().getCurso();
        double progresoCurso = servicioProgreso.calcularPorcentajeCurso(usuarioActual, curso);
        resumen.append("• Progreso del curso \"").append(curso.getTitulo()).append("\": ").append(String.format("%.1f%%", progresoCurso));

        JOptionPane.showMessageDialog(this, resumen.toString(), "Resumen de Sesión", JOptionPane.INFORMATION_MESSAGE);
    }

    private void actualizarEstadoBotones() {
        btnAnterior.setEnabled(indiceActual > 0);
        btnValidar.setEnabled(true);

        if (indiceActual >= listaEjercicios.size() - 1) {
            btnSiguiente.setEnabled(false);
        }
    }

    private boolean hayMasEjercicios() {
        return indiceActual < listaEjercicios.size() - 1;
    }

    // ==========================================
    // MÉTODOS PÚBLICOS PARA COMPATIBILIDAD
    // ==========================================

    public boolean validarRespuesta() {
        EjercicioRenderer rendererActual = renderers.get(indiceActual);
        if (rendererActual != null) {
            return rendererActual.validarRespuesta();
        }
        return false;
    }

    public int getIndiceActual() {
        return indiceActual;
    }

    public int getTotalEjercicios() {
        return listaEjercicios.size();
    }

    public Ejercicio getEjercicioActual() {
        return ejercicioActual;
    }

    public ProgresoBloque getProgresoBloque() {
        return progresoBloque;
    }

    public SesionAprendizaje getSesionActual() {
        return sesionActual;
    }

    public String getEstadisticasActuales() {
        return String.format("Bloque: %s | Aciertos: %d | Fallos: %d | Ejercicio: %d/%d | Progreso: %.1f%%",
                progresoBloque.getBloque().getTitulo(), aciertos, fallos, indiceActual + 1, listaEjercicios.size(),
                progresoBloque.getPorcentajeCompletado());
    }

    @Override
    public void dispose() {
        // Asegurar guardado antes de cerrar
        if (sesionActual != null && !sesionActual.isCompletada()) {
            try {
                if (indiceActual >= 0) {
                    guardarProgresoActual();
                }
                servicioEstadisticas.finalizarSesion(sesionActual);
            } catch (Exception e) {
                System.err.println("Error al guardar progreso en dispose: " + e.getMessage());
            }
        }
        super.dispose();
    }
}