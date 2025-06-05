package umu.pds.controlador;

import umu.pds.modelo.Ejercicio;
import umu.pds.modelo.TipoEjercicio;
import umu.pds.modelo.EjercicioFactoría.EjercicioAbstractFactory;
import umu.pds.vista.ejercicios.EjercicioRenderer;
import umu.pds.vista.ejercicios.RendererAbstractFactory;

/**
 * Facade que coordina la creación de ejercicios y sus renderers
 * respetando la separación de capas MVVC
 */
public class EjercicioFacade {
    
    /**
     * Crea un ejercicio usando la factory apropiada (MODELO)
     */
    public static Ejercicio crearEjercicio(TipoEjercicio tipo, String contenido, String respuesta) {
        EjercicioAbstractFactory factory = EjercicioAbstractFactory.getFactory(tipo);
        return factory.crearEjercicio(contenido, respuesta);
    }
    
    /**
     * Crea un ejercicio vacío usando la factory apropiada (MODELO)
     */
    public static Ejercicio crearEjercicio(TipoEjercicio tipo) {
        EjercicioAbstractFactory factory = EjercicioAbstractFactory.getFactory(tipo);
        return factory.crearEjercicio();
    }
    
    /**
     * Crea un renderer usando la factory apropiada (VISTA)
     */
    public static EjercicioRenderer crearRenderer(TipoEjercicio tipo) {
        RendererAbstractFactory factory = RendererAbstractFactory.getFactory(tipo);
        return factory.crearRenderer();
    }
    
    /**
     * Crea tanto el ejercicio como su renderer en una sola llamada
     */
    public static EjercicioCompleto crearEjercicioCompleto(TipoEjercicio tipo, 
                                                         String contenido, 
                                                         String respuesta) {
        Ejercicio ejercicio = crearEjercicio(tipo, contenido, respuesta);
        EjercicioRenderer renderer = crearRenderer(tipo);
        return new EjercicioCompleto(ejercicio, renderer);
    }
    
    /**
     * Clase interna para encapsular ejercicio + renderer
     */
    public static class EjercicioCompleto {
        private final Ejercicio ejercicio;
        private final EjercicioRenderer renderer;
        
        public EjercicioCompleto(Ejercicio ejercicio, EjercicioRenderer renderer) {
            this.ejercicio = ejercicio;
            this.renderer = renderer;
        }
        
        public Ejercicio getEjercicio() {
            return ejercicio;
        }
        
        public EjercicioRenderer getRenderer() {
            return renderer;
        }
    }
}