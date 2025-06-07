package umu.pds.vista.ejercicios;

import umu.pds.modelo.TipoEjercicio;
import umu.pds.utils.EjercicioRenderer;
import umu.pds.utils.FlashcardRenderer;

public class FlashcardRendererFactory extends RendererFactory {
    
    @Override
    public EjercicioRenderer crearRenderer() {
        return new FlashcardRenderer();
    }
    
    @Override
    public TipoEjercicio getTipoEjercicio() {
        return TipoEjercicio.FLASHCARD;
    }
}