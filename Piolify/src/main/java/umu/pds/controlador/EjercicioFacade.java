package umu.pds.controlador;

import umu.pds.modelo.Ejercicio;
import umu.pds.modelo.TipoEjercicio;
import umu.pds.utils.EjercicioRenderer;
import umu.pds.vista.ejercicios.RendererFactory;

/**
 * Facade que coordina la creación de ejercicios y sus renderers
 * respetando la separación de capas MVVC
 */
public class EjercicioFacade {

    
    /**
     * Crea un renderer usando la factory apropiada (VISTA)
     */
    public static EjercicioRenderer crearRenderer(TipoEjercicio tipo) {
        RendererFactory factory = RendererFactory.getFactory(tipo);
        return factory.crearRenderer();
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