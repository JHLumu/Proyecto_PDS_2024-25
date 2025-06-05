package umu.pds.modelo.EjercicioFactoría;

import umu.pds.modelo.Ejercicio;
import umu.pds.modelo.EjercicioFlashcard;
import umu.pds.modelo.TipoEjercicio;

public class FlashcardFactory extends EjercicioAbstractFactory {
    
    @Override
    public Ejercicio crearEjercicio() {
        return new EjercicioFlashcard();
    }
    
    @Override
    public Ejercicio crearEjercicio(String contenido, String respuesta) {
        return new EjercicioFlashcard(contenido, respuesta);
    }
    
    @Override
    public TipoEjercicio getTipoEjercicio() {
        return TipoEjercicio.FLASHCARD;
    }
}