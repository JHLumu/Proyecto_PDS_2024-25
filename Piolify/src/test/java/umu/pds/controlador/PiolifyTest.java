package umu.pds.controlador;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import umu.pds.modelo.Usuario;

class PiolifyTest {

    @Mock
    private UsuarioController usuarioControllerMock;
    
    @Mock
    private ImportacionController importacionControllerMock;
    
    
    private Piolify piolify;
    
    @BeforeEach
    void setUp() {
        usuarioControllerMock = mock(UsuarioController.class);
        importacionControllerMock = mock(ImportacionController.class);
        piolify = new Piolify(usuarioControllerMock, importacionControllerMock);
    }
    
    @Test
    void testGetUnicaInstancia() {
        Piolify instancia1 = Piolify.getUnicaInstancia();
        Piolify instancia2 = Piolify.getUnicaInstancia();

        assertNotNull(instancia1);
        assertEquals(instancia1, instancia2);
    }
    
    
    @Test
    void testGetControllers() {
        assertEquals(usuarioControllerMock, piolify.getUsuarioController());
        assertEquals(importacionControllerMock, piolify.getImportacionController());
    }
    
    @Test
    void testPatronObserver() {
        Runnable observadorMock = mock(Runnable.class);
        
        piolify.añadirObservador(observadorMock);
        piolify.notificarCambiosUsuario();

        // verificar que se llama al observador una vez
        verify(observadorMock, times(1)).run();
        
        piolify.borrarObservador(observadorMock);
        piolify.notificarCambiosUsuario();
        
        // verificar que solo se llama una vez
        verify(observadorMock, times(1)).run();
    }
    
    @Test
    void testSetYGetUsuarioActual() {
        Usuario usuarioMock = mock(Usuario.class);
        piolify.setUsuarioActual(usuarioMock);
        assertEquals(usuarioMock, piolify.getUsuarioActual());
    }
    
    @Test
    void testGetUsuarioActualSinUsuario() {
        assertThrows(RuntimeException.class, piolify::getUsuarioActual);
    }
    
    @Test
    void testRegistroExitoso() {
        piolify.registroExitoso();
        
        // No podemos verificar directamente la ventana porque es UI,
        // pero podemos verificar que no lance excepciones
        assertTrue(true);
    }
    
    // el test de loginExitoso no furula por ser una ventana de UI xd

}