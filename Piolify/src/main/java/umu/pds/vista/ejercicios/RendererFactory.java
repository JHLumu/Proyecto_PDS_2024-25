package umu.pds.vista.ejercicios;

import umu.pds.modelo.TipoEjercicio;
import umu.pds.utils.EjercicioRenderer;

public abstract class RendererFactory {
    
    /**
     * Crea un renderer del tipo correspondiente
     */
    public abstract EjercicioRenderer crearRenderer();
    
    /**
     * Obtiene el tipo de ejercicio que renderiza esta factory
     */
    public abstract TipoEjercicio getTipoEjercicio();
    
    /**
     * Factory Method estático para obtener la factory correcta
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

