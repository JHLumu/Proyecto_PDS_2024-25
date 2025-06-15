package umu.pds.vista.ejercicios;

import umu.pds.modelo.TipoEjercicio;
import umu.pds.utils.EjercicioRenderer;
import umu.pds.utils.FlashcardRenderer;

/* * Clase que representa una fábrica de renderers para ejercicios de tipo Flashcard.
 * Extiende RendererFactory para crear instancias de FlashcardRenderer.
 */
public class FlashcardRendererFactory extends RendererFactory {
    
    /**
     * Constructor por defecto.
     * Inicializa la fábrica de renderers para ejercicios de tipo Flashcard.
     */
    @Override
    public EjercicioRenderer crearRenderer() {
        return new FlashcardRenderer();
    }
    
    /**
     * Método que devuelve el tipo de ejercicio asociado a este renderer.
     * 
     * @return TipoEjercicio.FLASHCARD
     */
    @Override
    public TipoEjercicio getTipoEjercicio() {
        return TipoEjercicio.FLASHCARD;
    }
}