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
     * Crea un ejercicio del tipo especificado y su renderer asociado.
     * 
     * @param tipo Tipo de ejercicio a crear
     * @return Un objeto EjercicioCompleto que contiene el ejercicio y su renderer
     */
    public static EjercicioRenderer crearRenderer(TipoEjercicio tipo) {
        RendererFactory factory = RendererFactory.getFactory(tipo);
        return factory.crearRenderer();
    }

    /** clase interna que encapsula un ejercicio y su renderer */
    public static class EjercicioCompleto {
        private final Ejercicio ejercicio;
        private final EjercicioRenderer renderer;
        
        /**
         * Constructor que crea un ejercicio y su renderer asociado.
         * 
         * @param ejercicio El ejercicio a crear
         * @param renderer El renderer asociado al ejercicio
         */
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