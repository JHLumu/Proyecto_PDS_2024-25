package umu.pds.vista.ejercicios;

import umu.pds.modelo.TipoEjercicio;
import umu.pds.utils.EjercicioRenderer;

public abstract class RendererFactory {
    
    /**
     * Método que crea un renderer del tipo correspondiente.
     */
    public abstract EjercicioRenderer crearRenderer();
    
    /**
     * Método que devuelve el tipo de ejercicio asociado a esta Factory.
     */
    public abstract TipoEjercicio getTipoEjercicio();
    
    /** Método estático para obtener la fábrica de renderers según el tipo de ejercicio.
     * 
     * @param tipo Tipo de ejercicio para el que se desea obtener la fábrica de renderers.
     * @return Una instancia de RendererFactory correspondiente al tipo de ejercicio.
     */
    public static RendererFactory getFactory(TipoEjercicio tipo) {
        switch (tipo) {
            case FLASHCARD:
                return new FlashcardRendererFactory();
            case OPCION_MULTIPLE:
                return new OpcionMultipleRendererFactory();
            case COMPLETAR_HUECOS:
                return new RellenarHuecosRendererFactory();
            default:
                throw new IllegalArgumentException("Renderer no soportado para tipo: " + tipo);
        }
    }
}

