package umu.pds.controlador;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.awt.Component;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import umu.pds.modelo.Bloque;
import umu.pds.modelo.Ejercicio;
import umu.pds.modelo.Estrategia;
import umu.pds.modelo.ProgresoBloque;
import umu.pds.servicios.ServicioProgreso;
import umu.pds.vista.PioEjerciciosConProgreso;

public class ProgresoControllerTest {

    @Mock
    private Piolify mockControlador;
    @Mock
    private ServicioProgreso mockServicioProgreso;
    @Mock
    private Component mockParentComponent;
    @Mock
    private Bloque mockBloque;
    @Mock
    private Estrategia mockEstrategia;
    @Mock
    private ProgresoBloque mockProgresoBloque;

    @InjectMocks
    private ProgresoController progresoController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // Inicializa ProgresoController con los mocks
        progresoController = new ProgresoController(mockControlador);
        // Inyecta el mock de ServicioProgreso manualmente ya que no se hace a través del constructor por defecto.
        // Se asume que el servicioProgreso se inicializa directamente en la clase ProgresoController
        // o mediante un setter, si existiera. Para este test, lo simulamos para que el mock sea usado.
        try {
            java.lang.reflect.Field field = ProgresoController.class.getDeclaredField("servicioProgreso");
            field.setAccessible(true);
            field.set(progresoController, mockServicioProgreso);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testAbrirVentanaEjercicios_BloqueSinEjercicios() {
        // Simula que el bloque no tiene ejercicios
        when(mockBloque.getEjercicios()).thenReturn(Collections.emptyList());
        when(mockBloque.getTitulo()).thenReturn("Bloque de Prueba");

        try (MockedStatic<JOptionPane> mockedJOptionPane = Mockito.mockStatic(JOptionPane.class)) {
            progresoController.abrirVentanaEjercicios(mockBloque, mockEstrategia, mockProgresoBloque, mockParentComponent);

            // Verifica que se muestra un mensaje de advertencia
            mockedJOptionPane.verify(() -> JOptionPane.showMessageDialog(mockParentComponent,
                    "El bloque \"Bloque de Prueba\" no tiene ejercicios.",
                    "Sin ejercicios", JOptionPane.WARNING_MESSAGE));
            // Verifica que no se intenta ordenar ni abrir la ventana de ejercicios
            verify(mockEstrategia, never()).ordenarEjercicios(any());
            try (MockedStatic<SwingUtilities> mockedSwingUtilities = Mockito.mockStatic(SwingUtilities.class)) {
                mockedSwingUtilities.verify(() -> SwingUtilities.invokeLater(any(Runnable.class)), never());
            }
        }
    }

    @Test
    void testAbrirVentanaEjercicios_BloqueConEjercicios() {
        // Simula que el bloque tiene ejercicios
        List<Ejercicio> ejercicios = new ArrayList<>();
        ejercicios.add(mock(Ejercicio.class));
        ejercicios.add(mock(Ejercicio.class));

        when(mockBloque.getEjercicios()).thenReturn(ejercicios);
        when(mockEstrategia.ordenarEjercicios(ejercicios)).thenReturn(ejercicios); // La estrategia devuelve los mismos ejercicios ordenados

        try (MockedStatic<SwingUtilities> mockedSwingUtilities = Mockito.mockStatic(SwingUtilities.class)) {
            // Captura el Runnable pasado a invokeLater
            mockedSwingUtilities.when(() -> SwingUtilities.invokeLater(any(Runnable.class)))
                    .thenAnswer(invocation -> {
                        Runnable runnable = invocation.getArgument(0);
                        runnable.run(); // Ejecuta el Runnable inmediatamente para probar la lógica interna
                        return null;
                    });

            try (MockedStatic<PioEjerciciosConProgreso> mockedPioEjercicios = Mockito.mockStatic(PioEjerciciosConProgreso.class)) {
                // Simula el constructor de PioEjerciciosConProgreso
                PioEjerciciosConProgreso mockVentana = mock(PioEjerciciosConProgreso.class);
                mockedPioEjercicios.when(() -> new PioEjerciciosConProgreso(any(), any(), any()))
                        .thenReturn(mockVentana);

                progresoController.abrirVentanaEjercicios(mockBloque, mockEstrategia, mockProgresoBloque, mockParentComponent);

                // Verifica que se intentó ordenar los ejercicios
                verify(mockEstrategia).ordenarEjercicios(ejercicios);
                // Verifica que se llamó a invokeLater
                mockedSwingUtilities.verify(() -> SwingUtilities.invokeLater(any(Runnable.class)));
                // Verifica que la ventana se creó y se hizo visible
                mockedPioEjercicios.verify(() -> new PioEjerciciosConProgreso(ejercicios, mockProgresoBloque, mockServicioProgreso));
                verify(mockVentana).setVisible(true);
            }
        }
    }

    @Test
    void testAbrirVentanaEjercicios_ManejoExcepcion() {
        // Simula que ocurre una excepción al intentar ordenar los ejercicios
        when(mockBloque.getEjercicios()).thenReturn(new ArrayList<>()); // Bloque con ejercicios para que no salte la primera rama
        when(mockEstrategia.ordenarEjercicios(any())).thenThrow(new RuntimeException("Error simulado al ordenar"));

        try (MockedStatic<JOptionPane> mockedJOptionPane = Mockito.mockStatic(JOptionPane.class)) {
            assertDoesNotThrow(() -> progresoController.abrirVentanaEjercicios(mockBloque, mockEstrategia, mockProgresoBloque, mockParentComponent));

            // Verifica que se muestra un mensaje de error
            mockedJOptionPane.verify(() -> JOptionPane.showMessageDialog(mockParentComponent,
                    "Error al abrir los ejercicios: Error simulado al ordenar", "Error", JOptionPane.ERROR_MESSAGE));
            // Verifica que no se intenta abrir la ventana de ejercicios
            try (MockedStatic<SwingUtilities> mockedSwingUtilities = Mockito.mockStatic(SwingUtilities.class)) {
                mockedSwingUtilities.verify(() -> SwingUtilities.invokeLater(any(Runnable.class)), never());
            }
        }
    }
}