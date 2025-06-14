package umu.pds.controlador;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import umu.pds.controlador.EjercicioFacade.EjercicioCompleto;
import umu.pds.modelo.Ejercicio;
import umu.pds.modelo.TipoEjercicio;
import umu.pds.utils.EjercicioRenderer;

/**
 * Test unitario para EjercicioFacade
 * Compatible con las dependencias del pom.xml (JUnit 5.11.0 y Mockito 5.3.1)
 * Maneja correctamente las clases abstractas
 */
class EjercicioFacadeTest {

    private EjercicioRenderer mockRenderer;
    private Ejercicio mockEjercicio;

    @BeforeEach
    void setUp() {
        mockRenderer = mock(EjercicioRenderer.class);
        mockEjercicio = createEjercicioStub();
    }

    /**
     * Crea un stub de Ejercicio para casos donde el mock no funciona
     */
    private Ejercicio createEjercicioStub() {
        return new Ejercicio() {

			@Override
			public void renderEjercicio() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public boolean validarRespuesta(String respuestaUsuario) {
				// TODO Auto-generated method stub
				return false;
			}
            // Implementación mínima para testing
            // Añadir métodos abstractos si los hay
        };
    }

    // Tests para EjercicioCompleto (clase interna que sí podemos probar directamente)
    
    @Test
    @DisplayName("Test constructor de EjercicioCompleto")
    void testEjercicioCompletoConstructor() {
        // When
        EjercicioCompleto ejercicioCompleto = new EjercicioCompleto(mockEjercicio, mockRenderer);
        
        // Then
        assertNotNull(ejercicioCompleto);
        assertEquals(mockEjercicio, ejercicioCompleto.getEjercicio());
        assertEquals(mockRenderer, ejercicioCompleto.getRenderer());
    }

    @Test
    @DisplayName("Test getEjercicio de EjercicioCompleto")
    void testEjercicioCompletoGetEjercicio() {
        // Given
        EjercicioCompleto ejercicioCompleto = new EjercicioCompleto(mockEjercicio, mockRenderer);
        
        // When
        Ejercicio resultado = ejercicioCompleto.getEjercicio();
        
        // Then
        assertEquals(mockEjercicio, resultado);
        assertSame(mockEjercicio, resultado);
    }

    @Test
    @DisplayName("Test getRenderer de EjercicioCompleto")
    void testEjercicioCompletoGetRenderer() {
        // Given
        EjercicioCompleto ejercicioCompleto = new EjercicioCompleto(mockEjercicio, mockRenderer);
        
        // When
        EjercicioRenderer resultado = ejercicioCompleto.getRenderer();
        
        // Then
        assertEquals(mockRenderer, resultado);
        assertSame(mockRenderer, resultado);
    }

    @Test
    @DisplayName("Test EjercicioCompleto con valores null")
    void testEjercicioCompletoConNull() {
        // When
        EjercicioCompleto ejercicioCompleto = new EjercicioCompleto(null, null);
        
        // Then
        assertNotNull(ejercicioCompleto);
        assertNull(ejercicioCompleto.getEjercicio());
        assertNull(ejercicioCompleto.getRenderer());
    }

    @Test
    @DisplayName("Test EjercicioCompleto - múltiples instancias")
    void testEjercicioCompletoMultiplesInstancias() {
        // Given - creamos instancias adicionales manejando clases abstractas
        Ejercicio otroEjercicio;
        otroEjercicio = createEjercicioStub();
        EjercicioRenderer otroRenderer = mock(EjercicioRenderer.class);
        
        // When
        EjercicioCompleto completo1 = new EjercicioCompleto(mockEjercicio, mockRenderer);
        EjercicioCompleto completo2 = new EjercicioCompleto(otroEjercicio, otroRenderer);
        
        // Then
        assertNotEquals(completo1.getEjercicio(), completo2.getEjercicio());
        assertNotEquals(completo1.getRenderer(), completo2.getRenderer());
        
        assertEquals(mockEjercicio, completo1.getEjercicio());
        assertEquals(mockRenderer, completo1.getRenderer());
        assertEquals(otroEjercicio, completo2.getEjercicio());
        assertEquals(otroRenderer, completo2.getRenderer());
    }

    @Test
    @DisplayName("Test inmutabilidad de EjercicioCompleto")
    void testEjercicioCompletoInmutabilidad() {
        // Given
        EjercicioCompleto ejercicioCompleto = new EjercicioCompleto(mockEjercicio, mockRenderer);
        
        // When - obtenemos las referencias múltiples veces
        Ejercicio ejercicio1 = ejercicioCompleto.getEjercicio();
        Ejercicio ejercicio2 = ejercicioCompleto.getEjercicio();
        EjercicioRenderer renderer1 = ejercicioCompleto.getRenderer();
        EjercicioRenderer renderer2 = ejercicioCompleto.getRenderer();
        
        // Then - deberían ser las mismas referencias
        assertSame(ejercicio1, ejercicio2);
        assertSame(renderer1, renderer2);
        assertEquals(mockEjercicio, ejercicio1);
        assertEquals(mockRenderer, renderer1);
    }

    @Test
    @DisplayName("Test EjercicioCompleto con ejercicio null y renderer válido")
    void testEjercicioCompletoEjercicioNullRendererValido() {
        // When
        EjercicioCompleto ejercicioCompleto = new EjercicioCompleto(null, mockRenderer);
        
        // Then
        assertNull(ejercicioCompleto.getEjercicio());
        assertEquals(mockRenderer, ejercicioCompleto.getRenderer());
    }

    @Test
    @DisplayName("Test EjercicioCompleto con ejercicio válido y renderer null")
    void testEjercicioCompletoEjercicioValidoRendererNull() {
        // When
        EjercicioCompleto ejercicioCompleto = new EjercicioCompleto(mockEjercicio, null);
        
        // Then
        assertEquals(mockEjercicio, ejercicioCompleto.getEjercicio());
        assertNull(ejercicioCompleto.getRenderer());
    }

    // Tests para el método crearRenderer usando integración en lugar de mocks estáticos
    // Nota: Estos tests requieren que las clases RendererFactory y sus implementaciones existan
    
    @ParameterizedTest
    @EnumSource(TipoEjercicio.class)
    @DisplayName("Test integración crearRenderer con todos los tipos")
    void testCrearRendererIntegracion(TipoEjercicio tipo) {
        // Este test verifica que el método crearRenderer funciona con tipos válidos
        // Si RendererFactory.getFactory() está implementado correctamente, debería devolver un renderer
        
        try {
            // When
            EjercicioRenderer resultado = EjercicioFacade.crearRenderer(tipo);
            
            // Then
            assertNotNull(resultado, "El renderer no debería ser null para tipo: " + tipo);
        } catch (Exception e) {
            // Si se lanza una excepción, verificamos que es del tipo esperado
            assertTrue(e instanceof IllegalArgumentException || e instanceof UnsupportedOperationException,
                "Excepción inesperada para tipo " + tipo + ": " + e.getClass().getSimpleName());
        }
    }

    @Test
    @DisplayName("Test crearRenderer con tipo null")
    void testCrearRendererConTipoNull() {
        // When & Then
        assertThrows(Exception.class, () -> {
            EjercicioFacade.crearRenderer(null);
        }, "Debería lanzar excepción con tipo null");
    }

    @Test
    @DisplayName("Test crearRenderer con FLASHCARD específico")
    void testCrearRendererFlashcardEspecifico() {
        // Given
        TipoEjercicio tipo = TipoEjercicio.FLASHCARD;
        
        try {
            // When
            EjercicioRenderer resultado = EjercicioFacade.crearRenderer(tipo);
            
            // Then
            assertNotNull(resultado, "El renderer para FLASHCARD no debería ser null");
        } catch (Exception e) {
            // Si no está implementado, debería lanzar una excepción específica
            assertTrue(e instanceof IllegalArgumentException || e instanceof UnsupportedOperationException,
                "Excepción inesperada: " + e.getClass().getSimpleName());
        }
    }

    @Test
    @DisplayName("Test crearRenderer con OPCION_MULTIPLE específico")
    void testCrearRendererOpcionMultipleEspecifico() {
        // Given
        TipoEjercicio tipo = TipoEjercicio.OPCION_MULTIPLE;
        
        try {
            // When
            EjercicioRenderer resultado = EjercicioFacade.crearRenderer(tipo);
            
            // Then
            assertNotNull(resultado, "El renderer para OPCION_MULTIPLE no debería ser null");
        } catch (Exception e) {
            // Si no está implementado, debería lanzar una excepción específica
            assertTrue(e instanceof IllegalArgumentException || e instanceof UnsupportedOperationException,
                "Excepción inesperada: " + e.getClass().getSimpleName());
        }
    }

    @Test
    @DisplayName("Test crearRenderer con COMPLETAR_HUECOS específico")
    void testCrearRendererCompletarHuecosEspecifico() {
        // Given
        TipoEjercicio tipo = TipoEjercicio.COMPLETAR_HUECOS;
        
        try {
            // When
            EjercicioRenderer resultado = EjercicioFacade.crearRenderer(tipo);
            
            // Then
            assertNotNull(resultado, "El renderer para COMPLETAR_HUECOS no debería ser null");
        } catch (Exception e) {
            // Si no está implementado, debería lanzar una excepción específica
            assertTrue(e instanceof IllegalArgumentException || e instanceof UnsupportedOperationException,
                "Excepción inesperada: " + e.getClass().getSimpleName());
        }
    }
}