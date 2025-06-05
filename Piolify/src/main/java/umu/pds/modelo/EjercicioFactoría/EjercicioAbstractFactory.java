package umu.pds.modelo.EjercicioFactoría;

import umu.pds.modelo.Ejercicio;
import umu.pds.modelo.TipoEjercicio;

public abstract class EjercicioAbstractFactory {
    
    /**
     * Crea un ejercicio vacío del tipo correspondiente
     */
    public abstract Ejercicio crearEjercicio();
    
    /**
     * Crea un ejercicio con contenido y respuesta
     */
    public abstract Ejercicio crearEjercicio(String contenido, String respuesta);
    
    /**
     * Obtiene el tipo de ejercicio que maneja esta factory
     */
    public abstract TipoEjercicio getTipoEjercicio();
    
    /**
     * Factory Method estático para obtener la factory correcta
     * Este es el único lugar donde existe un condicional
     */
    public static EjercicioAbstractFactory getFactory(TipoEjercicio tipo) {
        switch (tipo) {
            case FLASHCARD:
                return new FlashcardFactory();
            case OPCION_MULTIPLE:
                return new OpcionMultipleFactory();
            case COMPLETAR_HUECOS:
                return new RellenarHuecosFactory();
            default:
                throw new IllegalArgumentException("Tipo de ejercicio no soportado: " + tipo);
        }
    }
}
