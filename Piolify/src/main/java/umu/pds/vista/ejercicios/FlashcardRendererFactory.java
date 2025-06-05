package umu.pds.vista.ejercicios;

import umu.pds.modelo.TipoEjercicio;

public class FlashcardRendererFactory extends RendererAbstractFactory {
    
    @Override
    public EjercicioRenderer crearRenderer() {
        return new FlashcardRenderer();
    }
    
    @Override
    public TipoEjercicio getTipoEjercicio() {
        return TipoEjercicio.FLASHCARD;
    }
}