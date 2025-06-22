package umu.pds.controlador;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.awt.Component;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import umu.pds.modelo.Bloque;
import umu.pds.modelo.Curso;
import umu.pds.modelo.Ejercicio;
import umu.pds.modelo.Estrategia;
import umu.pds.modelo.ProgresoBloque;
import umu.pds.modelo.TipoEstrategia;
import umu.pds.modelo.Usuario;
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
    private Curso mockCurso;
    @Mock
    private Estrategia mockEstrategia;
    @Mock
    private ProgresoBloque mockProgresoBloque;
    @Mock
    private Usuario mockUsuario;
    @Mock
    private ImportacionController mockImportacionController;

    @InjectMocks
    private ProgresoController progresoController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        progresoController = new ProgresoController(mockControlador);
        
        // Inyecta el mock de ServicioProgreso manualmente
        try {
            java.lang.reflect.Field field = ProgresoController.class.getDeclaredField("servicioProgreso");
            field.setAccessible(true);
            field.set(progresoController, mockServicioProgreso);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        
        // Setup común
        when(mockControlador.getUsuarioActual()).thenReturn(mockUsuario);
        when(mockControlador.getImportacionController()).thenReturn(mockImportacionController);
    }

    // ========== Tests para iniciarOContinuarCurso ==========
    
    @Test
    void testIniciarOContinuarCurso_CursoCompletado() {
        when(mockServicioProgreso.isCursoCompletado(mockUsuario, mockCurso)).thenReturn(true);
        
        try (MockedStatic<JOptionPane> mockedJOptionPane = Mockito.mockStatic(JOptionPane.class)) {
            when(mockCurso.getTitulo()).thenReturn("Curso Test");
            mockedJOptionPane.when(() -> JOptionPane.showConfirmDialog(
                eq(mockParentComponent), anyString(), eq("Curso Completado"), eq(JOptionPane.YES_NO_OPTION)))
                .thenReturn(JOptionPane.NO_OPTION);
            
            progresoController.iniciarOContinuarCurso(mockCurso, mockParentComponent);
            
            verify(mockServicioProgreso).isCursoCompletado(mockUsuario, mockCurso);
            mockedJOptionPane.verify(() -> JOptionPane.showConfirmDialog(
                eq(mockParentComponent), anyString(), eq("Curso Completado"), eq(JOptionPane.YES_NO_OPTION)));
        }
    }
    
    @Test
    void testIniciarOContinuarCurso_CursoNoCompletado() {
        when(mockServicioProgreso.isCursoCompletado(mockUsuario, mockCurso)).thenReturn(false);
        when(mockCurso.getBloques()).thenReturn(Collections.emptyList());
        when(mockCurso.getTitulo()).thenReturn("Curso Test");
        
        try (MockedStatic<JOptionPane> mockedJOptionPane = Mockito.mockStatic(JOptionPane.class)) {
            progresoController.iniciarOContinuarCurso(mockCurso, mockParentComponent);
            
            verify(mockServicioProgreso).isCursoCompletado(mockUsuario, mockCurso);
            // Verifica que se intenta mostrar el selector de bloques (que mostrará mensaje de sin ejercicios)
            mockedJOptionPane.verify(() -> JOptionPane.showMessageDialog(
                eq(mockParentComponent), anyString(), eq("Sin ejercicios"), eq(JOptionPane.WARNING_MESSAGE)));
        }
    }

    // ========== Tests para mostrarSelectorBloques ==========
    
    @Test
    void testMostrarSelectorBloques_SinBloques() {
        when(mockCurso.getBloques()).thenReturn(Collections.emptyList());
        when(mockCurso.getTitulo()).thenReturn("Curso Sin Bloques");
        
        try (MockedStatic<JOptionPane> mockedJOptionPane = Mockito.mockStatic(JOptionPane.class)) {
            progresoController.mostrarSelectorBloques(mockCurso, mockParentComponent);
            
            mockedJOptionPane.verify(() -> JOptionPane.showMessageDialog(mockParentComponent,
                "El curso \"Curso Sin Bloques\" no tiene bloques de ejercicios.",
                "Sin ejercicios", JOptionPane.WARNING_MESSAGE));
        }
    }
    
    @Test
    void testMostrarSelectorBloques_ConBloques() {
        Bloque bloque1 = mock(Bloque.class);
        Bloque bloque2 = mock(Bloque.class);
        List<Bloque> bloques = Arrays.asList(bloque1, bloque2);
        
        when(mockCurso.getBloques()).thenReturn(bloques);
        when(mockCurso.getTitulo()).thenReturn("Curso Test");
        when(bloque1.getTitulo()).thenReturn("Bloque 1");
        when(bloque2.getTitulo()).thenReturn("Bloque 2");
        
        when(mockServicioProgreso.obtenerProgreso(mockUsuario, bloque1)).thenReturn(null);
        when(mockServicioProgreso.obtenerProgreso(mockUsuario, bloque2)).thenReturn(mockProgresoBloque);
        when(mockProgresoBloque.isCompletado()).thenReturn(true);
        when(mockServicioProgreso.calcularPorcentajeCurso(mockUsuario, mockCurso)).thenReturn(50.0);
        
        try (MockedStatic<JOptionPane> mockedJOptionPane = Mockito.mockStatic(JOptionPane.class)) {
            mockedJOptionPane.when(() -> JOptionPane.showInputDialog(
                eq(mockParentComponent), anyString(), eq("Elegir Bloque"), 
                eq(JOptionPane.QUESTION_MESSAGE), eq(null), any(String[].class), any(String.class)))
                .thenReturn(null); // Usuario cancela
            
            progresoController.mostrarSelectorBloques(mockCurso, mockParentComponent);
            
            mockedJOptionPane.verify(() -> JOptionPane.showInputDialog(
                eq(mockParentComponent), anyString(), eq("Elegir Bloque"), 
                eq(JOptionPane.QUESTION_MESSAGE), eq(null), any(String[].class), any(String.class)));
        }
    }

    // ========== Tests para manejarSeleccionBloque ==========
    
    @Test
    void testManejarSeleccionBloque_BloqueNuevo() {
        when(mockServicioProgreso.obtenerProgreso(mockUsuario, mockBloque)).thenReturn(null);
        when(mockImportacionController.getTiposEstrategiasDefinidas())
            .thenReturn(Arrays.asList(TipoEstrategia.ALEATORIA));
        when(mockImportacionController.getEstrategia("ALEATORIA")).thenReturn(mockEstrategia);
        when(mockEstrategia.getTipoEstrategia()).thenReturn(TipoEstrategia.ALEATORIA);
        
        try (MockedStatic<JOptionPane> mockedJOptionPane = Mockito.mockStatic(JOptionPane.class)) {
            mockedJOptionPane.when(() -> JOptionPane.showInputDialog(
                eq(mockParentComponent), anyString(), eq("Seleccionar Estrategia"),
                eq(JOptionPane.INFORMATION_MESSAGE), eq(null), any(String[].class), any(String.class)))
                .thenReturn("ALEATORIA");
            
            progresoController.manejarSeleccionBloque(mockBloque, mockParentComponent);
            
            verify(mockServicioProgreso).obtenerProgreso(mockUsuario, mockBloque);
            verify(mockServicioProgreso).iniciarProgreso(mockUsuario, mockBloque, TipoEstrategia.ALEATORIA);
        }
    }
    
    @Test
    void testManejarSeleccionBloque_BloqueCompletado() {
        when(mockServicioProgreso.obtenerProgreso(mockUsuario, mockBloque)).thenReturn(mockProgresoBloque);
        when(mockProgresoBloque.isCompletado()).thenReturn(true);
        when(mockBloque.getTitulo()).thenReturn("Bloque Test");
        
        try (MockedStatic<JOptionPane> mockedJOptionPane = Mockito.mockStatic(JOptionPane.class)) {
            mockedJOptionPane.when(() -> JOptionPane.showConfirmDialog(
                eq(mockParentComponent), anyString(), eq("Bloque Completado"), eq(JOptionPane.YES_NO_OPTION)))
                .thenReturn(JOptionPane.NO_OPTION);
            
            progresoController.manejarSeleccionBloque(mockBloque, mockParentComponent);
            
            verify(mockServicioProgreso).obtenerProgreso(mockUsuario, mockBloque);
            mockedJOptionPane.verify(() -> JOptionPane.showConfirmDialog(
                eq(mockParentComponent), anyString(), eq("Bloque Completado"), eq(JOptionPane.YES_NO_OPTION)));
        }
    }

    // ========== Tests para manejarBloqueEnProgreso ==========
    
    @Test
    void testManejarBloqueEnProgreso_ContinuarDondeLoDejoOpcion() {
        when(mockProgresoBloque.getEstrategiaUtilizada()).thenReturn(TipoEstrategia.ALEATORIA);
        when(mockProgresoBloque.getPorcentajeCompletado()).thenReturn(50.0);
        when(mockProgresoBloque.getEjerciciosCompletados()).thenReturn(5);
        when(mockBloque.getTitulo()).thenReturn("Bloque Test");
        when(mockBloque.getEjercicios()).thenReturn(Arrays.asList(mock(Ejercicio.class), mock(Ejercicio.class)));
        when(mockImportacionController.getEstrategia("Aleatoria")).thenReturn(mockEstrategia);
        
        try (MockedStatic<JOptionPane> mockedJOptionPane = Mockito.mockStatic(JOptionPane.class)) {
            mockedJOptionPane.when(() -> JOptionPane.showOptionDialog(
                eq(mockParentComponent), anyString(), eq("Bloque en Progreso"), 
                eq(JOptionPane.DEFAULT_OPTION), eq(JOptionPane.QUESTION_MESSAGE), 
                eq(null), any(String[].class), any(String.class)))
                .thenReturn(0); // Continuar donde lo dejé
            
            progresoController.manejarBloqueEnProgreso(mockBloque, mockProgresoBloque, mockParentComponent);
            
            verify(mockImportacionController).getEstrategia("Aleatoria");
        }
    }
    
    @Test
    void testManejarBloqueEnProgreso_CambiarEstrategiaOpcion() {
        when(mockProgresoBloque.getEstrategiaUtilizada()).thenReturn(TipoEstrategia.ALEATORIA);
        when(mockProgresoBloque.getPorcentajeCompletado()).thenReturn(30.0);
        when(mockProgresoBloque.getEjerciciosCompletados()).thenReturn(3);
        when(mockBloque.getTitulo()).thenReturn("Bloque Test");
        when(mockBloque.getEjercicios()).thenReturn(Arrays.asList(mock(Ejercicio.class)));
        when(mockImportacionController.getTiposEstrategiasDefinidas())
            .thenReturn(Arrays.asList(TipoEstrategia.SECUENCIAL));
        when(mockImportacionController.getEstrategia("SECUENCIAL")).thenReturn(mockEstrategia);
        when(mockEstrategia.getTipoEstrategia()).thenReturn(TipoEstrategia.SECUENCIAL);
        
        try (MockedStatic<JOptionPane> mockedJOptionPane = Mockito.mockStatic(JOptionPane.class)) {
            mockedJOptionPane.when(() -> JOptionPane.showOptionDialog(
                eq(mockParentComponent), anyString(), eq("Bloque en Progreso"), 
                eq(JOptionPane.DEFAULT_OPTION), eq(JOptionPane.QUESTION_MESSAGE), 
                eq(null), any(String[].class), any(String.class)))
                .thenReturn(1); // Cambiar estrategia y continuar
            
            mockedJOptionPane.when(() -> JOptionPane.showInputDialog(
                eq(mockParentComponent), anyString(), eq("Seleccionar Estrategia"),
                eq(JOptionPane.INFORMATION_MESSAGE), eq(null), any(String[].class), any(String.class)))
                .thenReturn("SECUENCIAL");
            
            progresoController.manejarBloqueEnProgreso(mockBloque, mockProgresoBloque, mockParentComponent);
            
            verify(mockProgresoBloque).setEstrategiaUtilizada(TipoEstrategia.SECUENCIAL);
            verify(mockServicioProgreso).guardarProgreso(mockProgresoBloque);
        }
    }

    // ========== Tests para iniciarBloqueNuevo ==========
    
    @Test
    void testIniciarBloqueNuevo_EstrategiaSeleccionada() {
        when(mockImportacionController.getTiposEstrategiasDefinidas())
            .thenReturn(Arrays.asList(TipoEstrategia.ALEATORIA));
        when(mockImportacionController.getEstrategia("ALEATORIA")).thenReturn(mockEstrategia);
        when(mockEstrategia.getTipoEstrategia()).thenReturn(TipoEstrategia.ALEATORIA);
        when(mockServicioProgreso.iniciarProgreso(mockUsuario, mockBloque, TipoEstrategia.ALEATORIA))
            .thenReturn(mockProgresoBloque);
        
        try (MockedStatic<JOptionPane> mockedJOptionPane = Mockito.mockStatic(JOptionPane.class)) {
            mockedJOptionPane.when(() -> JOptionPane.showInputDialog(
                eq(mockParentComponent), anyString(), eq("Seleccionar Estrategia"),
                eq(JOptionPane.INFORMATION_MESSAGE), eq(null), any(String[].class), any(String.class)))
                .thenReturn("ALEATORIA");
            
            progresoController.iniciarBloqueNuevo(mockBloque, mockParentComponent);
            
            verify(mockServicioProgreso).iniciarProgreso(mockUsuario, mockBloque, TipoEstrategia.ALEATORIA);
        }
    }
    
    @Test
    void testIniciarBloqueNuevo_EstrategiaCancelada() {
        when(mockImportacionController.getTiposEstrategiasDefinidas())
            .thenReturn(Arrays.asList(TipoEstrategia.ALEATORIA));
        
        try (MockedStatic<JOptionPane> mockedJOptionPane = Mockito.mockStatic(JOptionPane.class)) {
            mockedJOptionPane.when(() -> JOptionPane.showInputDialog(
                eq(mockParentComponent), anyString(), eq("Seleccionar Estrategia"),
                eq(JOptionPane.INFORMATION_MESSAGE), eq(null), any(String[].class), any(String.class)))
                .thenReturn(null); // Usuario cancela
            
            progresoController.iniciarBloqueNuevo(mockBloque, mockParentComponent);
            
            verify(mockServicioProgreso, never()).iniciarProgreso(any(), any(), any());
        }
    }

    // ========== Tests para continuarBloque ==========
    
    @Test
    void testContinuarBloque() {
        when(mockProgresoBloque.getEstrategiaUtilizada()).thenReturn(TipoEstrategia.SECUENCIAL);
        when(mockImportacionController.getEstrategia("Secuencial")).thenReturn(mockEstrategia);
        when(mockBloque.getEjercicios()).thenReturn(Arrays.asList(mock(Ejercicio.class)));
        when(mockEstrategia.ordenarEjercicios(any())).thenReturn(Arrays.asList(mock(Ejercicio.class)));
        
        try (MockedStatic<SwingUtilities> mockedSwingUtilities = Mockito.mockStatic(SwingUtilities.class);
             MockedStatic<JOptionPane> mockedJOptionPane = Mockito.mockStatic(JOptionPane.class);  // AGREGAR ESTA LÍNEA
             MockedConstruction<PioEjerciciosConProgreso> mockedConstruction = Mockito.mockConstruction(PioEjerciciosConProgreso.class)) {
            
            mockedSwingUtilities.when(() -> SwingUtilities.invokeLater(any(Runnable.class)))
                    .thenAnswer(invocation -> {
                        Runnable runnable = invocation.getArgument(0);
                        runnable.run();
                        return null;
                    });
            
            progresoController.continuarBloque(mockBloque, mockProgresoBloque, mockParentComponent);
            
            verify(mockImportacionController).getEstrategia("Secuencial");
            assertEquals(1, mockedConstruction.constructed().size());
        }
    }

    // ========== Tests para reiniciarBloque ==========
    
    @Test
    void testReiniciarBloque() {
        when(mockImportacionController.getTiposEstrategiasDefinidas())
            .thenReturn(Arrays.asList(TipoEstrategia.ALEATORIA));
        when(mockImportacionController.getEstrategia("ALEATORIA")).thenReturn(mockEstrategia);
        when(mockEstrategia.getTipoEstrategia()).thenReturn(TipoEstrategia.ALEATORIA);
        when(mockServicioProgreso.iniciarProgreso(mockUsuario, mockBloque, TipoEstrategia.ALEATORIA))
            .thenReturn(mockProgresoBloque);
        
        try (MockedStatic<JOptionPane> mockedJOptionPane = Mockito.mockStatic(JOptionPane.class)) {
            mockedJOptionPane.when(() -> JOptionPane.showInputDialog(
                eq(mockParentComponent), anyString(), eq("Seleccionar Estrategia"),
                eq(JOptionPane.INFORMATION_MESSAGE), eq(null), any(String[].class), any(String.class)))
                .thenReturn("ALEATORIA");
            
            progresoController.reiniciarBloque(mockBloque, mockParentComponent);
            
            verify(mockServicioProgreso).eliminarProgreso(mockUsuario, mockBloque);
            verify(mockServicioProgreso).iniciarProgreso(mockUsuario, mockBloque, TipoEstrategia.ALEATORIA);
        }
    }

    // ========== Tests para manejarBloqueCompletado ==========
    
    @Test
    void testManejarBloqueCompletado_RepetirSi() {
        when(mockBloque.getTitulo()).thenReturn("Bloque Completado");
        when(mockImportacionController.getTiposEstrategiasDefinidas())
            .thenReturn(Arrays.asList(TipoEstrategia.ALEATORIA));
        when(mockImportacionController.getEstrategia("ALEATORIA")).thenReturn(mockEstrategia);
        when(mockEstrategia.getTipoEstrategia()).thenReturn(TipoEstrategia.ALEATORIA);
        
        try (MockedStatic<JOptionPane> mockedJOptionPane = Mockito.mockStatic(JOptionPane.class)) {
            mockedJOptionPane.when(() -> JOptionPane.showConfirmDialog(
                eq(mockParentComponent), anyString(), eq("Bloque Completado"), eq(JOptionPane.YES_NO_OPTION)))
                .thenReturn(JOptionPane.YES_OPTION);
            
            mockedJOptionPane.when(() -> JOptionPane.showInputDialog(
                eq(mockParentComponent), anyString(), eq("Seleccionar Estrategia"),
                eq(JOptionPane.INFORMATION_MESSAGE), eq(null), any(String[].class), any(String.class)))
                .thenReturn("ALEATORIA");
            
            progresoController.manejarBloqueCompletado(mockBloque, mockParentComponent);
            
            verify(mockServicioProgreso).eliminarProgreso(mockUsuario, mockBloque);
        }
    }
    
    @Test
    void testManejarBloqueCompletado_RepetirNo() {
        when(mockBloque.getTitulo()).thenReturn("Bloque Completado");
        
        try (MockedStatic<JOptionPane> mockedJOptionPane = Mockito.mockStatic(JOptionPane.class)) {
            mockedJOptionPane.when(() -> JOptionPane.showConfirmDialog(
                eq(mockParentComponent), anyString(), eq("Bloque Completado"), eq(JOptionPane.YES_NO_OPTION)))
                .thenReturn(JOptionPane.NO_OPTION);
            
            progresoController.manejarBloqueCompletado(mockBloque, mockParentComponent);
            
            verify(mockServicioProgreso, never()).eliminarProgreso(any(), any());
        }
    }

    // ========== Tests para manejarCursoCompletado ==========
    
    @Test
    void testManejarCursoCompletado_RepetirSi() {
        when(mockCurso.getTitulo()).thenReturn("Curso Completado");
        List<Bloque> bloques = Arrays.asList(mockBloque, mock(Bloque.class));
        when(mockCurso.getBloques()).thenReturn(bloques);
        
        try (MockedStatic<JOptionPane> mockedJOptionPane = Mockito.mockStatic(JOptionPane.class)) {
            mockedJOptionPane.when(() -> JOptionPane.showConfirmDialog(
                eq(mockParentComponent), anyString(), eq("Curso Completado"), eq(JOptionPane.YES_NO_OPTION)))
                .thenReturn(JOptionPane.YES_OPTION);
            
            // Mock para el selector de bloques que se mostrará después
            mockedJOptionPane.when(() -> JOptionPane.showMessageDialog(
                eq(mockParentComponent), anyString(), eq("Sin ejercicios"), eq(JOptionPane.WARNING_MESSAGE)))
                .then(invocation -> null);
            
            progresoController.manejarCursoCompletado(mockCurso, mockParentComponent);
            
            verify(mockServicioProgreso, times(2)).eliminarProgreso(eq(mockUsuario), any(Bloque.class));
        }
    }

    // ========== Tests para mostrarSelectorEstrategia ==========
    
    @Test
    void testMostrarSelectorEstrategia_EstrategiaSeleccionada() {
        when(mockImportacionController.getTiposEstrategiasDefinidas())
            .thenReturn(Arrays.asList(TipoEstrategia.ALEATORIA, TipoEstrategia.SECUENCIAL));
        when(mockImportacionController.getEstrategia("ALEATORIA")).thenReturn(mockEstrategia);
        
        try (MockedStatic<JOptionPane> mockedJOptionPane = Mockito.mockStatic(JOptionPane.class)) {
            mockedJOptionPane.when(() -> JOptionPane.showInputDialog(
                eq(mockParentComponent), anyString(), eq("Seleccionar Estrategia"),
                eq(JOptionPane.INFORMATION_MESSAGE), eq(null), any(String[].class), any(String.class)))
                .thenReturn("ALEATORIA");
            
            Estrategia resultado = progresoController.mostrarSelectorEstrategia(mockParentComponent);
            
            assertEquals(mockEstrategia, resultado);
            verify(mockImportacionController).getEstrategia("ALEATORIA");
        }
    }
    
    @Test
    void testMostrarSelectorEstrategia_Cancelado() {
        when(mockImportacionController.getTiposEstrategiasDefinidas())
            .thenReturn(Arrays.asList(TipoEstrategia.ALEATORIA));
        
        try (MockedStatic<JOptionPane> mockedJOptionPane = Mockito.mockStatic(JOptionPane.class)) {
            mockedJOptionPane.when(() -> JOptionPane.showInputDialog(
                eq(mockParentComponent), anyString(), eq("Seleccionar Estrategia"),
                eq(JOptionPane.INFORMATION_MESSAGE), eq(null), any(String[].class), any(String.class)))
                .thenReturn(null);
            
            Estrategia resultado = progresoController.mostrarSelectorEstrategia(mockParentComponent);
            
            assertNull(resultado);
            verify(mockImportacionController, never()).getEstrategia(anyString());
        }
    }

    // ========== Tests existentes para abrirVentanaEjercicios ==========
    
    @Test
    void testAbrirVentanaEjercicios_BloqueSinEjercicios() {
        when(mockBloque.getEjercicios()).thenReturn(Collections.emptyList());
        when(mockBloque.getTitulo()).thenReturn("Bloque de Prueba");

        try (MockedStatic<JOptionPane> mockedJOptionPane = Mockito.mockStatic(JOptionPane.class)) {
            progresoController.abrirVentanaEjercicios(mockBloque, mockEstrategia, mockProgresoBloque, mockParentComponent);

            mockedJOptionPane.verify(() -> JOptionPane.showMessageDialog(mockParentComponent,
                    "El bloque \"Bloque de Prueba\" no tiene ejercicios.",
                    "Sin ejercicios", JOptionPane.WARNING_MESSAGE));
            verify(mockEstrategia, never()).ordenarEjercicios(any());
            try (MockedStatic<SwingUtilities> mockedSwingUtilities = Mockito.mockStatic(SwingUtilities.class)) {
                mockedSwingUtilities.verify(() -> SwingUtilities.invokeLater(any(Runnable.class)), never());
            }
        }
    }

    @Test
    void testAbrirVentanaEjercicios_BloqueConEjercicios() {
        List<Ejercicio> ejercicios = new ArrayList<>();
        ejercicios.add(mock(Ejercicio.class));
        ejercicios.add(mock(Ejercicio.class));

        when(mockBloque.getEjercicios()).thenReturn(ejercicios);
        when(mockEstrategia.ordenarEjercicios(ejercicios)).thenReturn(ejercicios);

        try (MockedStatic<SwingUtilities> mockedSwingUtilities = Mockito.mockStatic(SwingUtilities.class);
             MockedConstruction<PioEjerciciosConProgreso> mockedConstruction = Mockito.mockConstruction(PioEjerciciosConProgreso.class)) {
            
            mockedSwingUtilities.when(() -> SwingUtilities.invokeLater(any(Runnable.class)))
                    .thenAnswer(invocation -> {
                        Runnable runnable = invocation.getArgument(0);
                        runnable.run();
                        return null;
                    });

            progresoController.abrirVentanaEjercicios(mockBloque, mockEstrategia, mockProgresoBloque, mockParentComponent);

            verify(mockEstrategia).ordenarEjercicios(ejercicios);
            mockedSwingUtilities.verify(() -> SwingUtilities.invokeLater(any(Runnable.class)));
            
            List<PioEjerciciosConProgreso> constructed = mockedConstruction.constructed();
            assertEquals(1, constructed.size(), "Should have constructed exactly one PioEjerciciosConProgreso instance");
            
            PioEjerciciosConProgreso mockVentana = constructed.get(0);
            verify(mockVentana).setVisible(true);
        }
    }

    @Test
    void testAbrirVentanaEjercicios_ManejoExcepcion() {
        List<Ejercicio> ejercicios = new ArrayList<>();
        ejercicios.add(mock(Ejercicio.class));
        
        when(mockBloque.getEjercicios()).thenReturn(ejercicios);
        when(mockEstrategia.ordenarEjercicios(any())).thenThrow(new RuntimeException("Error simulado al ordenar"));

        try (MockedStatic<JOptionPane> mockedJOptionPane = Mockito.mockStatic(JOptionPane.class)) {
            assertDoesNotThrow(() -> progresoController.abrirVentanaEjercicios(mockBloque, mockEstrategia, mockProgresoBloque, mockParentComponent));

            mockedJOptionPane.verify(() -> JOptionPane.showMessageDialog(mockParentComponent,
                    "Error al abrir los ejercicios: Error simulado al ordenar", "Error", JOptionPane.ERROR_MESSAGE));
            try (MockedStatic<SwingUtilities> mockedSwingUtilities = Mockito.mockStatic(SwingUtilities.class)) {
                mockedSwingUtilities.verify(() -> SwingUtilities.invokeLater(any(Runnable.class)), never());
            }
        }
    }

    // ========== Tests adicionales para casos edge ==========
    
    @Test
    void testMostrarSelectorBloques_BloquesConDiferentesEstados() {
        // Crear bloques con diferentes estados de progreso
        Bloque bloqueNoIniciado = mock(Bloque.class);
        Bloque bloqueEnProgreso = mock(Bloque.class);
        Bloque bloqueCompletado = mock(Bloque.class);
        List<Bloque> bloques = Arrays.asList(bloqueNoIniciado, bloqueEnProgreso, bloqueCompletado);
        
        when(mockCurso.getBloques()).thenReturn(bloques);
        when(mockCurso.getTitulo()).thenReturn("Curso Mixto");
        when(bloqueNoIniciado.getTitulo()).thenReturn("Bloque No Iniciado");
        when(bloqueEnProgreso.getTitulo()).thenReturn("Bloque En Progreso");
        when(bloqueCompletado.getTitulo()).thenReturn("Bloque Completado");
        
        // Configurar progreso de cada bloque
        when(mockServicioProgreso.obtenerProgreso(mockUsuario, bloqueNoIniciado)).thenReturn(null);
        
        ProgresoBloque progresoEnProgreso = mock(ProgresoBloque.class);
        when(mockServicioProgreso.obtenerProgreso(mockUsuario, bloqueEnProgreso)).thenReturn(progresoEnProgreso);
        when(progresoEnProgreso.isCompletado()).thenReturn(false);
        when(progresoEnProgreso.getPorcentajeCompletado()).thenReturn(75.5);
        
        ProgresoBloque progresoCompletado = mock(ProgresoBloque.class);
        when(mockServicioProgreso.obtenerProgreso(mockUsuario, bloqueCompletado)).thenReturn(progresoCompletado);
        when(progresoCompletado.isCompletado()).thenReturn(true);
        
        when(mockServicioProgreso.calcularPorcentajeCurso(mockUsuario, mockCurso)).thenReturn(58.5);
        
        try (MockedStatic<JOptionPane> mockedJOptionPane = Mockito.mockStatic(JOptionPane.class)) {
            mockedJOptionPane.when(() -> JOptionPane.showInputDialog(
                eq(mockParentComponent), anyString(), eq("Elegir Bloque"), 
                eq(JOptionPane.QUESTION_MESSAGE), eq(null), any(String[].class), any(String.class)))
                .thenReturn("Bloque En Progreso (75.5% completado)");
            
            progresoController.mostrarSelectorBloques(mockCurso, mockParentComponent);
            
            // Verificar que se calculó el porcentaje del curso
            verify(mockServicioProgreso).calcularPorcentajeCurso(mockUsuario, mockCurso);
            
            // Verificar que se obtuvo el progreso de todos los bloques
            verify(mockServicioProgreso).obtenerProgreso(mockUsuario, bloqueNoIniciado);
            verify(mockServicioProgreso).obtenerProgreso(mockUsuario, bloqueEnProgreso);
            verify(mockServicioProgreso).obtenerProgreso(mockUsuario, bloqueCompletado);
        }
    }
    
    @Test
    void testManejarBloqueEnProgreso_ReiniciarOpcion() {
        when(mockProgresoBloque.getEstrategiaUtilizada()).thenReturn(TipoEstrategia.ALEATORIA);
        when(mockProgresoBloque.getPorcentajeCompletado()).thenReturn(25.0);
        when(mockProgresoBloque.getEjerciciosCompletados()).thenReturn(2);
        when(mockBloque.getTitulo()).thenReturn("Bloque Test");
        when(mockBloque.getEjercicios()).thenReturn(Arrays.asList(mock(Ejercicio.class)));
        when(mockImportacionController.getTiposEstrategiasDefinidas())
            .thenReturn(Arrays.asList(TipoEstrategia.SECUENCIAL));
        when(mockImportacionController.getEstrategia("SECUENCIAL")).thenReturn(mockEstrategia);
        when(mockEstrategia.getTipoEstrategia()).thenReturn(TipoEstrategia.SECUENCIAL);
        
        try (MockedStatic<JOptionPane> mockedJOptionPane = Mockito.mockStatic(JOptionPane.class)) {
            mockedJOptionPane.when(() -> JOptionPane.showOptionDialog(
                eq(mockParentComponent), anyString(), eq("Bloque en Progreso"), 
                eq(JOptionPane.DEFAULT_OPTION), eq(JOptionPane.QUESTION_MESSAGE), 
                eq(null), any(String[].class), any(String.class)))
                .thenReturn(2); // Reiniciar este bloque
            
            mockedJOptionPane.when(() -> JOptionPane.showInputDialog(
                eq(mockParentComponent), anyString(), eq("Seleccionar Estrategia"),
                eq(JOptionPane.INFORMATION_MESSAGE), eq(null), any(String[].class), any(String.class)))
                .thenReturn("SECUENCIAL");
            
            progresoController.manejarBloqueEnProgreso(mockBloque, mockProgresoBloque, mockParentComponent);
            
            verify(mockServicioProgreso).eliminarProgreso(mockUsuario, mockBloque);
            verify(mockServicioProgreso).iniciarProgreso(mockUsuario, mockBloque, TipoEstrategia.SECUENCIAL);
        }
    }
    
    @Test
    void testManejarBloqueEnProgreso_CancelarOpcion() {
        when(mockProgresoBloque.getEstrategiaUtilizada()).thenReturn(TipoEstrategia.ALEATORIA);
        when(mockProgresoBloque.getPorcentajeCompletado()).thenReturn(40.0);
        when(mockProgresoBloque.getEjerciciosCompletados()).thenReturn(4);
        when(mockBloque.getTitulo()).thenReturn("Bloque Test");
        when(mockBloque.getEjercicios()).thenReturn(Arrays.asList(mock(Ejercicio.class)));
        
        try (MockedStatic<JOptionPane> mockedJOptionPane = Mockito.mockStatic(JOptionPane.class)) {
            mockedJOptionPane.when(() -> JOptionPane.showOptionDialog(
                eq(mockParentComponent), anyString(), eq("Bloque en Progreso"), 
                eq(JOptionPane.DEFAULT_OPTION), eq(JOptionPane.QUESTION_MESSAGE), 
                eq(null), any(String[].class), any(String.class)))
                .thenReturn(3); // Cancelar
            
            progresoController.manejarBloqueEnProgreso(mockBloque, mockProgresoBloque, mockParentComponent);
            
            // Verificar que no se realizó ninguna acción
            verify(mockServicioProgreso, never()).eliminarProgreso(any(), any());
            verify(mockServicioProgreso, never()).iniciarProgreso(any(), any(), any());
            verify(mockServicioProgreso, never()).guardarProgreso(any());
        }
    }
    
    @Test
    void testCambiarEstrategiaYContinuar_EstrategiaCancelada() {
        when(mockImportacionController.getTiposEstrategiasDefinidas())
            .thenReturn(Arrays.asList(TipoEstrategia.ALEATORIA));
        
        try (MockedStatic<JOptionPane> mockedJOptionPane = Mockito.mockStatic(JOptionPane.class)) {
            mockedJOptionPane.when(() -> JOptionPane.showInputDialog(
                eq(mockParentComponent), anyString(), eq("Seleccionar Estrategia"),
                eq(JOptionPane.INFORMATION_MESSAGE), eq(null), any(String[].class), any(String.class)))
                .thenReturn(null); // Usuario cancela
            
            progresoController.cambiarEstrategiaYContinuar(mockBloque, mockProgresoBloque, mockParentComponent);
            
            // Verificar que no se guardó el progreso ni se abrió la ventana
            verify(mockProgresoBloque, never()).setEstrategiaUtilizada(any());
            verify(mockServicioProgreso, never()).guardarProgreso(any());
        }
    }
    
    @Test
    void testAbrirVentanaEjercicios_BloqueConEjerciciosNulos() {
        when(mockBloque.getEjercicios()).thenReturn(null);
        when(mockBloque.getTitulo()).thenReturn("Bloque Sin Ejercicios");

        try (MockedStatic<JOptionPane> mockedJOptionPane = Mockito.mockStatic(JOptionPane.class)) {
            progresoController.abrirVentanaEjercicios(mockBloque, mockEstrategia, mockProgresoBloque, mockParentComponent);

            mockedJOptionPane.verify(() -> JOptionPane.showMessageDialog(mockParentComponent,
                    "El bloque \"Bloque Sin Ejercicios\" no tiene ejercicios.",
                    "Sin ejercicios", JOptionPane.WARNING_MESSAGE));
            verify(mockEstrategia, never()).ordenarEjercicios(any());
        }
    }
    
    @Test
    void testManejarCursoCompletado_RepetirNo() {
        when(mockCurso.getTitulo()).thenReturn("Curso Completado");
        
        try (MockedStatic<JOptionPane> mockedJOptionPane = Mockito.mockStatic(JOptionPane.class)) {
            mockedJOptionPane.when(() -> JOptionPane.showConfirmDialog(
                eq(mockParentComponent), anyString(), eq("Curso Completado"), eq(JOptionPane.YES_NO_OPTION)))
                .thenReturn(JOptionPane.NO_OPTION);
            
            progresoController.manejarCursoCompletado(mockCurso, mockParentComponent);
            
            // Verificar que no se eliminó el progreso de ningún bloque
            verify(mockServicioProgreso, never()).eliminarProgreso(any(), any());
        }
    }
    
    @Test
    void testMostrarSelectorBloques_BloquesNulos() {
        when(mockCurso.getBloques()).thenReturn(null);
        when(mockCurso.getTitulo()).thenReturn("Curso Sin Bloques");
        
        try (MockedStatic<JOptionPane> mockedJOptionPane = Mockito.mockStatic(JOptionPane.class)) {
            progresoController.mostrarSelectorBloques(mockCurso, mockParentComponent);
            
            mockedJOptionPane.verify(() -> JOptionPane.showMessageDialog(mockParentComponent,
                "El curso \"Curso Sin Bloques\" no tiene bloques de ejercicios.",
                "Sin ejercicios", JOptionPane.WARNING_MESSAGE));
        }
    }
    
    @Test
    void testMostrarSelectorBloques_SeleccionValidaDeBloque() {
        Bloque bloque1 = mock(Bloque.class);
        Bloque bloque2 = mock(Bloque.class);
        List<Bloque> bloques = Arrays.asList(bloque1, bloque2);
        
        when(mockCurso.getBloques()).thenReturn(bloques);
        when(mockCurso.getTitulo()).thenReturn("Curso Test");
        when(bloque1.getTitulo()).thenReturn("Bloque 1");
        when(bloque2.getTitulo()).thenReturn("Bloque 2");
        
        when(mockServicioProgreso.obtenerProgreso(mockUsuario, bloque1)).thenReturn(null);
        when(mockServicioProgreso.obtenerProgreso(mockUsuario, bloque2)).thenReturn(null);
        when(mockServicioProgreso.calcularPorcentajeCurso(mockUsuario, mockCurso)).thenReturn(0.0);
        
        // Mock para el selector de estrategias que se llamará después
        when(mockImportacionController.getTiposEstrategiasDefinidas())
            .thenReturn(Arrays.asList(TipoEstrategia.ALEATORIA));
        when(mockImportacionController.getEstrategia("ALEATORIA")).thenReturn(mockEstrategia);
        when(mockEstrategia.getTipoEstrategia()).thenReturn(TipoEstrategia.ALEATORIA);
        
        try (MockedStatic<JOptionPane> mockedJOptionPane = Mockito.mockStatic(JOptionPane.class)) {
            mockedJOptionPane.when(() -> JOptionPane.showInputDialog(
                eq(mockParentComponent), anyString(), eq("Elegir Bloque"), 
                eq(JOptionPane.QUESTION_MESSAGE), eq(null), any(String[].class), any(String.class)))
                .thenReturn("Bloque 2 (No iniciado)");
            
            mockedJOptionPane.when(() -> JOptionPane.showInputDialog(
                eq(mockParentComponent), anyString(), eq("Seleccionar Estrategia"),
                eq(JOptionPane.INFORMATION_MESSAGE), eq(null), any(String[].class), any(String.class)))
                .thenReturn("ALEATORIA");
            
            progresoController.mostrarSelectorBloques(mockCurso, mockParentComponent);
            
            // Verificar que se intentó iniciar el bloque seleccionado
            verify(mockServicioProgreso).iniciarProgreso(mockUsuario, bloque2, TipoEstrategia.ALEATORIA);
        }
    }
    
    // ========== Test para verificar la integración completa ==========
    
    @Test
    void testIntegracionCompleta_FlujoBloqueNuevo() {
        // Configuración para un flujo completo de bloque nuevo
        List<Ejercicio> ejercicios = Arrays.asList(mock(Ejercicio.class), mock(Ejercicio.class));
        
        when(mockServicioProgreso.obtenerProgreso(mockUsuario, mockBloque)).thenReturn(null);
        when(mockImportacionController.getTiposEstrategiasDefinidas())
            .thenReturn(Arrays.asList(TipoEstrategia.ALEATORIA));
        when(mockImportacionController.getEstrategia("ALEATORIA")).thenReturn(mockEstrategia);
        when(mockEstrategia.getTipoEstrategia()).thenReturn(TipoEstrategia.ALEATORIA);
        when(mockServicioProgreso.iniciarProgreso(mockUsuario, mockBloque, TipoEstrategia.ALEATORIA))
            .thenReturn(mockProgresoBloque);
        when(mockBloque.getEjercicios()).thenReturn(ejercicios);
        when(mockEstrategia.ordenarEjercicios(ejercicios)).thenReturn(ejercicios);
        
        try (MockedStatic<JOptionPane> mockedJOptionPane = Mockito.mockStatic(JOptionPane.class);
        	     MockedStatic<SwingUtilities> mockedSwingUtilities = Mockito.mockStatic(SwingUtilities.class);
        	     MockedConstruction<PioEjerciciosConProgreso> mockedConstruction = Mockito.mockConstruction(PioEjerciciosConProgreso.class)) {
        	    
    	    // AGREGAR ESTA LÍNEA para evitar que se ejecuten JOptionPane reales
    	    mockedJOptionPane.when(() -> JOptionPane.showMessageDialog(any(), anyString(), anyString(), anyInt()))
    	        .thenAnswer(invocation -> null);
            mockedJOptionPane.when(() -> JOptionPane.showInputDialog(
                eq(mockParentComponent), anyString(), eq("Seleccionar Estrategia"),
                eq(JOptionPane.INFORMATION_MESSAGE), eq(null), any(String[].class), any(String.class)))
                .thenReturn("ALEATORIA");
            
            mockedSwingUtilities.when(() -> SwingUtilities.invokeLater(any(Runnable.class)))
                    .thenAnswer(invocation -> {
                        Runnable runnable = invocation.getArgument(0);
                        runnable.run();
                        return null;
                    });
            
            // Ejecutar el flujo completo
            progresoController.manejarSeleccionBloque(mockBloque, mockParentComponent);
            
            // Verificaciones del flujo completo
            verify(mockServicioProgreso).obtenerProgreso(mockUsuario, mockBloque);
            verify(mockServicioProgreso).iniciarProgreso(mockUsuario, mockBloque, TipoEstrategia.ALEATORIA);
            verify(mockEstrategia).ordenarEjercicios(ejercicios);
            assertEquals(1, mockedConstruction.constructed().size());
            
            PioEjerciciosConProgreso ventanaCreada = mockedConstruction.constructed().get(0);
            verify(ventanaCreada).setVisible(true);
        }
    }
}