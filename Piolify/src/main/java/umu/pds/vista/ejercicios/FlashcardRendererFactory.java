package umu.pds.vista.ejercicios;

import umu.pds.modelo.TipoEjercicio;
import umu.pds.utils.EjercicioRenderer;
import umu.pds.utils.FlashcardRenderer;

/* Clase que representa una fábrica de renderers para ejercicios de tipo Flashcard.
 */
public class FlashcardRendererFactory extends RendererFactory {
    
    /**
     * {@inheritDoc}
     */
    @Override
    public EjercicioRenderer crearRenderer() {
        return new FlashcardRenderer();
    }
    
    /**
     * {@inheritDoc}
     * 
     * @return {@code TipoEjercicio.FLASHCARD}
     */
    @Override
    public TipoEjercicio getTipoEjercicio() {
        return TipoEjercicio.FLASHCARD;
    }
}