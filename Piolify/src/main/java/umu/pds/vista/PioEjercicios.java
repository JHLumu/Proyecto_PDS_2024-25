package umu.pds.vista;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import umu.pds.controlador.EjercicioFacade;
import umu.pds.modelo.Ejercicio;
import umu.pds.utils.EjercicioRenderer;
import umu.pds.vista.elementos.BaseRoundedFrame;
import umu.pds.vista.elementos.PioColores;

public class PioEjercicios extends BaseRoundedFrame {
    private static final long serialVersionUID = 1L;
    private List<EjercicioRenderer> renderers;
    private Ejercicio ejercicioActual;
    
    // Manejo de lista de ejercicios
    private List<Ejercicio> listaEjercicios;
    private int indiceActual;
    
    // Componentes de la interfaz con CardLayout
    private CardLayout cardLayout;
    private JPanel panelEjercicios; // Panel con CardLayout
    private JPanel panelBotones;
    private JButton btnAnterior;
    private JButton btnValidar;
    private JButton btnSiguiente;
    private JLabel lblProgreso;
    
    // Constructor para WindowBuilder
    public PioEjercicios() {
        super();
        initDesignModeComponents();
    }
    
    // Constructor que recibe un ejercicio ya creado
    public PioEjercicios(Ejercicio ejercicio) {
        super("Ejercicio - " + ejercicio.getTipo().name());
        
        // Crear lista con un solo ejercicio
        this.listaEjercicios = Arrays.asList(ejercicio);
        this.indiceActual = 0;
        
        initRuntimeComponents();
        prepararTodosLosEjercicios();
        mostrarEjercicioActual();
    }
    
    // NUEVO: Constructor que recibe una lista de ejercicios
    public PioEjercicios(List<Ejercicio> ejercicios) {
        super("Serie de Ejercicios");
        
        if (ejercicios == null || ejercicios.isEmpty()) {
            throw new IllegalArgumentException("La lista de ejercicios no puede estar vacía");
        }
        
        this.listaEjercicios = ejercicios;
        this.indiceActual = 0;
        
        initRuntimeComponents();
        prepararTodosLosEjercicios();
        mostrarEjercicioActual();
    }
    
    private void initDesignModeComponents() {
        JLabel label = new JLabel("Vista de diseño - Ejercicio Universal", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 16));
        contentPanel.add(label, BorderLayout.CENTER);
    }
    
    private void initRuntimeComponents() {
        setSize(700, 500);
        setLocationRelativeTo(null);
        setBackgroundColor(PioColores.AMARILLO_LABEL);
        setCloseButtonColor(PioColores.ROJO);
        
        // Crear CardLayout y panel principal
        cardLayout = new CardLayout();
        panelEjercicios = new JPanel(cardLayout);
        panelEjercicios.setOpaque(false);
        
        // Crear panel de botones
        panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBotones.setOpaque(false);
        
        // Crear botones
        btnAnterior = new JButton("◀ Anterior");
        btnValidar = new JButton("Validar");
        btnSiguiente = new JButton("Siguiente ▶");
        
        // Crear label de progreso
        lblProgreso = new JLabel();
        lblProgreso.setFont(new Font("Arial", Font.BOLD, 12));
        
        // Configurar acciones de botones
        configurarEventos();
        
        // Agregar componentes
        panelBotones.add(btnAnterior);
        panelBotones.add(lblProgreso);
        panelBotones.add(btnValidar);
        panelBotones.add(btnSiguiente);
        
        contentPanel.add(panelEjercicios, BorderLayout.CENTER);
        contentPanel.add(panelBotones, BorderLayout.SOUTH);
        
        // Actualizar progreso
        actualizarProgreso();
        actualizarEstadoBotones();
    }
    
    private void prepararTodosLosEjercicios() {
        // Crear todos los paneles de ejercicios y agregarlos al CardLayout
        renderers = Arrays.asList(new EjercicioRenderer[listaEjercicios.size()]);
        
        for (int i = 0; i < listaEjercicios.size(); i++) {
            Ejercicio ejercicio = listaEjercicios.get(i);
            
            // Crear renderer para este ejercicio
            EjercicioRenderer renderer = EjercicioFacade.crearRenderer(ejercicio.getTipo());
            renderers.set(i, renderer);
            
            // Crear panel para este ejercicio
            JPanel panelEjercicio = new JPanel(new BorderLayout());
            panelEjercicio.setOpaque(false);
            
            // Renderizar el ejercicio en su panel
            renderer.renderizar(panelEjercicio, ejercicio);
            
            // Agregar al CardLayout con un nombre único
            String nombreCard = "ejercicio_" + i;
            panelEjercicios.add(panelEjercicio, nombreCard);
        }
    }
    
    private void configurarEventos() {
    	btnAnterior.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                anteriorEjercicio();
            }
        });
    	
        btnValidar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                validarRespuestaActual();
            }
        });
        
        btnSiguiente.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                siguienteEjercicio();
            }
        });
    }
    
    private void mostrarEjercicioActual() {
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
    }
    
    private void validarRespuestaActual() {
        EjercicioRenderer rendererActual = renderers.get(indiceActual);
        
        if (rendererActual != null) {
            boolean esCorrecta = rendererActual.validarRespuesta();
            
            // Mostrar resultado
            String mensaje = esCorrecta ? "¡Correcto!" : "Incorrecto. Inténtalo de nuevo.";
            String titulo = esCorrecta ? "Bien hecho" : "Resultado";
            int tipoMensaje = esCorrecta ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.WARNING_MESSAGE;
            
            JOptionPane.showMessageDialog(this, mensaje, titulo, tipoMensaje);
            
            // Si es correcto, habilitar botón siguiente
            if (esCorrecta) {
                btnValidar.setEnabled(false);
                
                if (hayMasEjercicios()) {
                    btnSiguiente.setEnabled(true);
                } else {
                    // Es el último ejercicio
                    JOptionPane.showMessageDialog(this, 
                        "¡Felicidades! Has completado todos los ejercicios.", 
                        "Serie completada", 
                        JOptionPane.INFORMATION_MESSAGE);
                    btnSiguiente.setText("Finalizar");
                    btnSiguiente.setEnabled(true);
                }
            }
        }
    }
    
    private void actualizarEstadoBotones() {
        // El botón anterior se habilita si no estamos en el primer ejercicio
        btnAnterior.setEnabled(indiceActual > 0);
        
        // El botón siguiente se habilita si no estamos en el último ejercicio
        btnSiguiente.setEnabled(indiceActual < listaEjercicios.size() - 1);
        
        // El botón solución siempre está habilitado
        btnValidar.setEnabled(true);
    }
    
    private void anteriorEjercicio() {
        if (indiceActual > 0) {
            indiceActual--;
            mostrarEjercicioActual();
        }
    }
    
    private void siguienteEjercicio() {
        if (hayMasEjercicios()) {
            indiceActual++;
            mostrarEjercicioActual();
        } else {
            // Finalizar la serie
            dispose();
        }
    }
    
    private boolean hayMasEjercicios() {
        return indiceActual < listaEjercicios.size() - 1;
    }
    
    private void actualizarProgreso() {
        lblProgreso.setText(String.format("Ejercicio %d de %d", 
            indiceActual + 1, listaEjercicios.size()));
    }
    
    // Método público para validación (mantenido por compatibilidad)
    public boolean validarRespuesta() {
        EjercicioRenderer rendererActual = renderers.get(indiceActual);
        if (rendererActual != null) {
            return rendererActual.validarRespuesta();
        }
        return false;
    }
    
    // Métodos públicos para acceso a la información
    public int getIndiceActual() {
        return indiceActual;
    }
    
    public int getTotalEjercicios() {
        return listaEjercicios.size();
    }
    
    public Ejercicio getEjercicioActual() {
        return ejercicioActual;
    }
    
    // Método para navegar a un ejercicio específico (funcionalidad adicional)
    public void irAEjercicio(int indice) {
        if (indice >= 0 && indice < listaEjercicios.size()) {
            indiceActual = indice;
            mostrarEjercicioActual();
        }
    }
    
}