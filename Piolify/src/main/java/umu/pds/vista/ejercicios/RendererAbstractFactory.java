package umu.pds.vista.ejercicios;

import umu.pds.modelo.TipoEjercicio;

public abstract class RendererAbstractFactory {
    
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
    public static RendererAbstractFactory getFactory(TipoEjercicio tipo) {
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

